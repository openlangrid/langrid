package jp.go.nict.langrid.management.web.view.component.panel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class MultipleFileUploaderPanel extends FormComponentPanel<File>{
	/**
	 * 
	 * 
	 */
	public MultipleFileUploaderPanel(String componentId, Boolean isDefaultRemote, String fileExtension, boolean aIsRequired){
		super(componentId, new Model<File>());
		extension = fileExtension;
		isRemote = isDefaultRemote;
		this.isRequired = aIsRequired;
		rewriteContainer = new WebMarkupContainer("rewriteContainer");
		rewriteContainer.setOutputMarkupId(true);
		add(rewriteContainer);
		repeater = new RepeatingView("repeater");
		rewriteContainer.add(repeater);
		RepeatingWrapContainer rwc = new RepeatingWrapContainer(repeater.newChildId());
		repeater.add(rwc);
		AjaxSubmitLink addLink = new AjaxSubmitLink("addLink")
		{
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form){
				RepeatingWrapContainer rwc = new RepeatingWrapContainer(repeater.newChildId());
				repeater.add(rwc);
				Iterator<RepeatingWrapContainer> ite = (Iterator<RepeatingWrapContainer>)repeater.iterator();
				boolean isVisible = repeater.size() > 1;
				while(ite.hasNext()){
					ite.next().setVisibleControlLink(isVisible);
				}
				target.addComponent(rewriteContainer);
			}

			private static final long serialVersionUID = 1L;
		};
		addLink.setDefaultFormProcessing(false);
		add(addLink);
	}
	
	/**
	 * 
	 * 
	 */
//	public Map<String, InputStream> getInputStreams()
	public Map<String, byte[]> getFileMap()
	throws ResourceStreamNotFoundException, IOException
	{
		Map<String, byte[]> map = new HashMap<String, byte[]>();
		Iterator<RepeatingWrapContainer> ite = (Iterator<RepeatingWrapContainer>)repeater.iterator();
		while(ite.hasNext()){
			RepeatingWrapContainer rwc = ite.next();
			FileUploaderPanel panel = rwc.getUploader();
			map.putAll(panel.getFileMap());
		}
		return map;
	}
	
	public List<FileUploaderPanel> getValidateComponent(){
		List<FileUploaderPanel> list = new ArrayList<FileUploaderPanel>();
		Iterator ite = repeater.iterator();
		while(ite.hasNext()){
			RepeatingWrapContainer rwc = (RepeatingWrapContainer)ite.next();
			list.add(rwc.getUploader());
		}
		return list;
	}
	
	private class RepeatingWrapContainer extends WebMarkupContainer{
		private static final long serialVersionUID = 1L;
		public RepeatingWrapContainer(String componentId){
			super(componentId);
			add(uploader = new FileUploaderPanel("uploader", isRemote, extension, isRequired));
			add(deleteLink = new AjaxSubmitLink("deleteLink")
			{
				@Override
				public void onSubmit(AjaxRequestTarget target, Form form){
					if(repeater.size() == 1){
						return;
					}
					repeater.remove(getParent());
					Iterator<RepeatingWrapContainer> ite = (Iterator<RepeatingWrapContainer>)repeater.iterator();
					boolean isVisible = repeater.size() > 1;
					while(ite.hasNext()){
						ite.next().setVisibleControlLink(isVisible);
					}
					target.addComponent(rewriteContainer);
				}
				
				private static final long serialVersionUID = 1L;
			});
			deleteLink.setDefaultFormProcessing(false);
			deleteLink.setOutputMarkupId(true);
			deleteLink.setOutputMarkupPlaceholderTag(true);
			deleteLink.setVisible(false);
		}
		
		public FileUploaderPanel getUploader(){
			return uploader;
		}
		
		public void setVisibleControlLink(boolean isVisible){
			deleteLink.setVisible(isVisible);
		}
		
		private FileUploaderPanel uploader;
		private AjaxSubmitLink deleteLink;
	}
	
	protected boolean isRemote;
	protected WebMarkupContainer rewriteContainer;
	private boolean isRequired;
	private RepeatingView repeater;
	private String extension;
	private static final long serialVersionUID = 1L;
}
