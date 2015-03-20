package jp.go.nict.langrid.management.web.view.page.language.service.admin.component.form.panel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.utility.FileUtil;
import jp.go.nict.langrid.management.web.utility.ZipUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.management.web.view.component.text.URLField;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.resource.UrlResourceStream;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 528 $
 */
public class FileMultipleUploadPanel extends Panel {
	/**
	 * 
	 * 
	 */
	public FileMultipleUploadPanel(String gridId, String componentId, List<String> list, boolean hasUrlField) {
		super(componentId);
		hasUrl = hasUrlField;
		add(form = new UploadForm());
	}

	/**
	 * 
	 * 
	 */
	public FileMultipleUploadPanel(String gridId, String componentId, boolean hasUrlField) {
		super(componentId, new Model());
		hasUrl = hasUrlField;
		add(form = new UploadForm());
	}

	public byte[] getZipFileInstance() throws ServiceManagerException {
		try {
			return ZipUtil.makeZipBinary(form.getFileMap(), FILENAMESEPERATOR);
		} catch(IOException e){
			throw new ServiceManagerException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public byte[] getJarFileInstance() throws ServiceManagerException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JarOutputStream jos = new JarOutputStream(baos);
			try {
				Map<String, byte[]> map = form.getFileMap();
				for(String fileNameKey : map.keySet()){
					String fileName = fileNameKey.split(FILENAMESEPERATOR)[0];
					JarEntry ze = new JarEntry(fileName);
					jos.putNextEntry(ze);
					jos.write(map.get(fileNameKey));
					jos.closeEntry();
				}
				return baos.toByteArray();
			} catch(IOException e) {
				throw new ServiceManagerException(e);
			} finally {
				jos.close();
			}
		} catch(IOException e){
			throw new ServiceManagerException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public boolean isEmpty() {
		return 0 == form.getFileMap().size();
	}

	private String gridId;
	private UploadForm form;
	private static final String FILENAMESEPERATOR = "##";
	private boolean hasUrl;

	private Form getForm() {
		Form form = new Form("fileForm");
		form.setMultiPart(true);
		form.setMaxSize(Bytes.bytes(Long.valueOf(MessageUtil.LIMIT_WSDL_FILE_SIZE)));

		UploadForm uf = new UploadForm();

		return form;
	}

	private class UploadForm extends Form {
		public UploadForm() {
			super("fileForm");
			setMultiPart(true);
			setMaxSize(Bytes.bytes(Long.valueOf(MessageUtil.LIMIT_WSDL_FILE_SIZE)));
			container = new WebMarkupContainer("uploads-container");
			container.setOutputMarkupId(true);
			add(container);
			container.add(fileField = new FileUploadField("file", new Model<FileUpload>()));
			fileField.add(new AjaxFormSubmitBehavior("onchange"){
				protected void onSubmit(AjaxRequestTarget target) {
					FileUpload fu = fileField.getFileUpload();
					if(fu != null) {
						try {
							String fileName = fu.getClientFileName() + FILENAMESEPERATOR + fileCount;
							FileListContainer lc = new FileListContainer(rv.newChildId(), fileName){
								public void deleteComponent(String fileName, MarkupContainer parent, AjaxRequestTarget target) {
									map.remove(fileName);
									rv.remove(parent);
									target.addComponent(container);
								};
							};
							map.put(fileName, StreamUtil.readAsBytes(fu.getInputStream()));
							rv.add(lc);
							fileCount++;
						} catch(IOException e) {
							FeedbackPanel fp = ((ServiceManagerPage)getPage()).getFeedbackPanel();
							fp.error("file upload is not complete.");
							target.addComponent(fp);
						}
					}
					target.addComponent(container);
				}
				@Override protected void onError(AjaxRequestTarget target) {warn("file upload is failed.");}
			});

			container.add(urlField = new URLField("url", new Model<String>()));
			urlField.setVisible(hasUrl);
			Button b = new Button("fetch");
			b.add(new AjaxFormSubmitBehavior("onclick") {
				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					String fileUrl = urlField.getModelObject();
					if(fileUrl == null || fileUrl.equals("")) {
						FeedbackPanel fp = ((ServiceManagerPage)getPage()).getFeedbackPanel();
						fp.error("URL is empty or invalid.");
						target.addComponent(fp);
						return;
					}
					try {
						UrlResourceStream urlStream = new UrlResourceStream(new URL(fileUrl));
						String fileName = FileUtil.getFileNameWithoutPath(fileUrl) + FILENAMESEPERATOR + fileCount;
						FileListContainer lc = new FileListContainer(rv.newChildId(), fileName){
							public void deleteComponent(String fileName, MarkupContainer parent, AjaxRequestTarget target) {
								map.remove(fileName);
								rv.remove(parent);
								target.addComponent(container);
							};
						};

						byte[] remoteFile = StreamUtil.readAsBytes(urlStream.getInputStream());
						map.put(fileName, remoteFile);
						rv.add(lc);
						fileCount++;
						urlField.setModelObject("");
						target.addComponent(container);
					} catch (ResourceStreamNotFoundException e) {
						FeedbackPanel fp = ((ServiceManagerPage)getPage()).getFeedbackPanel();
						fp.error("remote file is not found.");
						target.addComponent(fp);
					} catch (MalformedURLException e) {
						FeedbackPanel fp = ((ServiceManagerPage)getPage()).getFeedbackPanel();
						fp.error("Invalid URL.");
						target.addComponent(fp);
					} catch (IOException e) {
						FeedbackPanel fp = ((ServiceManagerPage)getPage()).getFeedbackPanel();
						fp.error("remote file is not found.");
						target.addComponent(fp);
					}
				}
				@Override
				protected void onError(AjaxRequestTarget target) {getPage().error("remote file is not found.");}
			});
			container.add(b);

			container.add(rv = new RepeatingView("uploads"));
		}

		public Map<String, byte[]> getFileMap() {
			return map;
		}

		private WebMarkupContainer container;
		private RepeatingView rv;
		private FileUploadField fileField;
		private URLField urlField;
		private int fileCount = 0;
		private Map<String, byte[]> map = new LinkedHashMap<String, byte[]>();
	}

	private class FileListContainer extends WebMarkupContainer {
		public FileListContainer(String componentId) {
			super(componentId);
		}

		public FileListContainer(String componentId, String fileName) {
			super(componentId);
			addComponent(fileName);
		}

		public void addFile(String fileName){
			addComponent(fileName);
		}

		public void deleteComponent(String fileName, MarkupContainer parent, AjaxRequestTarget target){
			// noop
		}

		private void addComponent(String name){
			String[] ss = name.split(FILENAMESEPERATOR);
			String fName = ss[0];
			add(new Label("fileName", fName));
			add(new AjaxLink<String>("deleteFile", new Model<String>(name)) {
				@Override
				public void onClick(AjaxRequestTarget target) {
					deleteComponent(getModelObject(), getParent(), target);
				}
			});
		}
	}
}
