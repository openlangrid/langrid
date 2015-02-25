package jp.go.nict.langrid.management.web.view.page.language.service;

import java.io.Serializable;
import java.util.Calendar;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.management.web.model.IndividualExecutionInformationModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.model.service.ServiceInformationService;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.utility.StringUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.text.RequiredFromDateTextField;
import jp.go.nict.langrid.management.web.view.component.text.RequiredToDateTextField;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.LabelIconPanel;
import org.apache.wicket.markup.html.tree.LabelTree;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.WildcardListModel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class YourServiceCallsPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public YourServiceCallsPage() {
		Calendar start = Calendar.getInstance();
		Calendar end = CalendarUtil.createEndingOfDay(Calendar.getInstance());
		start.add(Calendar.MONTH, -1);
		start.set(Calendar.DAY_OF_MONTH, 1);
		start = CalendarUtil.createBeginningOfDay(start);
		buildPage(0, start, end);
	}

	public YourServiceCallsPage(int pageIndex, Calendar start, Calendar end) {
		buildPage(pageIndex, start, end);
	}

	private void buildPage(int index, Calendar start, Calendar end) {
		startDateField = new RequiredFromDateTextField(start.getTime());
		endDateField = new RequiredToDateTextField(startDateField, end.getTime());
		Form durationForm = new Form("form");
		durationForm.add(new IndicatingAjaxButton("set", durationForm) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				Calendar start = Calendar.getInstance();
				Calendar end = Calendar.getInstance();
				start.setTime(startDateField.getModelObject());
				end.setTime(endDateField.getModelObject());
				end = CalendarUtil.createEndingOfDay(end);
				try {
					makeTreeView(0, start, end, rewritable);
					target.addComponent(rewritable);
					target.addComponent(getFeedbackPanel());
				} catch(ServiceManagerException e) {
					doErrorProcess(e);
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.addComponent(getFeedbackPanel());
			}

			private static final long serialVersionUID = 1L;
		});
		durationForm.add(new Label("timezone", "(" + DateUtil.defaultTimeZone() + ")"));
		durationForm.add(endDateField);
		durationForm.add(startDateField);
		add(durationForm);
		try {
			add(rewritable = new WebMarkupContainer("rewriteWrapper"));
			rewritable.setOutputMarkupId(true);
			makeTreeView(index, start, end, rewritable);
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}

	private void makeTreeView(int index, Calendar start, Calendar end,
		WebMarkupContainer rewritable)
	throws ServiceManagerException
	{
		String userId = getSessionUserId();
		String gridId = getSelfGridId();
		ServiceInformationService service = ServiceFactory.getInstance()
			.getServiceInformationService(
				gridId, gridId, userId);
		service.setScopeParameter("", gridId, userId);
		LangridList<IndividualExecutionInformationModel> list = service.getVerboseList(
			index * pagingCount, pagingCount, userId, "", start, end
			, new MatchingCondition[]{new MatchingCondition("callNest", 0)}
			, new Order[]{});
		ListView<IndividualExecutionInformationModel> lv = new ListView<IndividualExecutionInformationModel>(
			"list", new WildcardListModel<IndividualExecutionInformationModel>(list))
		{
			@Override
			protected void populateItem(ListItem<IndividualExecutionInformationModel> item) {
				item.add(makeTree(item.getModelObject()));
			}

			private static final long serialVersionUID = 1L;
		};
		rewritable.addOrReplace(lv);
		boolean isFixed = (list.getTotalCount() - 1) < ((index + 1) * pagingCount);
		Link previewLink = createLink("topPreviewLink", index - 1, start, end);
		previewLink.add(new Label("topPreview", "< Preview").setRenderBodyOnly(true));
		previewLink.setEnabled(0 <= index - 1);
		rewritable.addOrReplace(previewLink);
		Link nextLink = createLink("topNextLink", index + 1, start, end);
		nextLink.add(new Label("topNext", "Next >").setRenderBodyOnly(true));
		nextLink.setEnabled(!isFixed);
		rewritable.addOrReplace(nextLink);
		Link underPreviewLink = createLink("underPreviewLink", index - 1, start, end);
		underPreviewLink.add(new Label("underPreview", "< Preview")
			.setRenderBodyOnly(true));
		underPreviewLink.setEnabled(0 <= index - 1);
		rewritable.addOrReplace(underPreviewLink);
		Link underNextLink = createLink("underNextLink", index + 1, start, end);
		underNextLink.add(new Label("underNext", "Next >").setRenderBodyOnly(true));
		underNextLink.setEnabled(!isFixed);
		rewritable.addOrReplace(underNextLink);
		previewLink.setVisible(list.size() != 0);
		nextLink.setVisible(list.size() != 0);
		underPreviewLink.setVisible(list.size() != 0);
		underNextLink.setVisible(list.size() != 0);
	}

	private LabelTree makeTree(IndividualExecutionInformationModel model) {
		CallTreeModel ctm = new CallTreeModel();
		ctm.setServiceId(model.getServiceId());
		ctm.setServiceName(StringUtil.nullandStringTohyphen(model.getServiceName()));
		ctm.setResponseTime(String.valueOf(model.getResponseTimeMillis()));
		ctm.setFault(model.getFaultCode());
		ctm.setFaultString(model.getFaultString());
		ctm.setProtocol(model.getProtocol());
		ctm.setResponseByte(String.valueOf(model.getResponseBytes()));
		ctm.setLicense(model.getLicense());
		ctm.setCopyright(model.getCopyright());
		String headerTable = DateUtil
			.formatYMDHMSWithSlash(model.getDateTime().getTime())
			+ "&nbsp;-&nbsp;" + ctm.getServiceId();
		CallTreeNode root = new CallTreeNode(
			headerTable
			,
			(model.getResponseCode() < 600 && model.getResponseCode() >= 400)
				|| (
				model.getFaultCode() != null && !model.getFaultCode().equals("null") && !model
					.getFaultCode().equals("")
				)
			);
		root.add(new CallTreeNode(getDescriptionString(ctm)));
		String callTree = model.getCallTree();
		if(callTree != null && !callTree.equals("")) {
			buildChildrenTree(root, model.getCallTree());
		}
		return new ServiceCallTree("calltree", new DefaultTreeModel(root));
	}

	private Link createLink(String linkId, final int index, final Calendar start,
		final Calendar end) {
		return new Link(linkId) {
			@Override
			public void onClick() {
				setResponsePage(new YourServiceCallsPage(index, start, end));
			}
		};
	}

	private void buildChildrenTree(CallTreeNode parent, String callTree) {
		JSONArray ja = JSONArray.fromObject(callTree);
		for(int i = 0; i < ja.size(); i++) {
			JSONObject jo = JSONObject.fromObject(ja.get(i));
			CallTreeModel ctm = new CallTreeModel();
			ctm.setServiceId(jo.getString("serviceId"));
			if(jo.getString("serviceName") != null
				&& !jo.getString("serviceName").equals("")
				&& !jo.getString("serviceName").equals("null"))
			{
				ctm.setServiceName(jo.getString("serviceName"));
			}
			ctm.setInvocationName(jo.getString("invocationName"));
			ctm.setResponseTime(String.valueOf(jo.getInt("responseTimeMillis")));
			ctm.setFault(jo.getString("faultCode"));
			ctm.setFaultString(jo.getString("faultString"));
			ctm.setCopyright(jo.getString("serviceCopyright"));
			ctm.setLicense(jo.getString("serviceLicense"));
			CallTreeNode own = new CallTreeNode(
				"<div class=\"calltree-header-break\">" + ctm.getServiceId() + "</div>"
				, ctm.getFault() != null && !ctm.getFault().equals("null")
					&& !ctm.getFault().equals(""));
			own.add(new CallTreeNode(getDescriptionString(ctm)));
			parent.add(own);
			String ownCallTree = jo.getString("children");
			if(ownCallTree != null && !ownCallTree.equals("")) {
				buildChildrenTree(own, ownCallTree);
			}
		}
	}
	
	private String getDescriptionString(CallTreeModel cal) {
		String table = "<div class=\"calltree-description\"><table>";
		table += "<tr><th style=\"white-space:pre;\"><div>" +
			MessageManager.getMessage("Common.label.service.Name", getLocale()) +
			":</div></th><td><div>"
			+ StringUtil.nullandStringTohyphen(cal.getServiceName()) + "</div></td></tr>";
		table += "<tr><th style=\"white-space:pre;\"><div>" +
			MessageManager.getMessage("Common.label.Copyright", getLocale()) +
			":</div></th><td><div>"
			+ StringUtil.nullandStringTohyphen(cal.getCopyright()) + "</div></td></tr>";
		table += "<tr><th style=\"white-space:pre;\"><div>" +
			MessageManager.getMessage("Common.label.License", getLocale()) +
			":</div></th><td><div>" + StringUtil.nullandStringTohyphen(cal.getLicense())
			+ "</div></td></tr>";
		if(cal.getInvocationName() != null && !cal.getInvocationName().equals("")
			&& !cal.getInvocationName().equals("null"))
		{
			table += "<tr><th style=\"white-space:pre;\"><div>"
				+
				MessageManager.getMessage("ServiceProfile.label.service.name.Invocation",
					getLocale()) +
				":</div></th><td><div>"
				+ StringUtil.nullandStringTohyphen(cal.getInvocationName())
				+ "</div></td></tr>";
		}
		if(cal.getProtocol() != null && !cal.getProtocol().equals("")
			&& !cal.getProtocol().equals("null"))
		{
			table += "<tr><th style=\"white-space:pre;\"><div>" +
				MessageManager.getMessage("Common.label.Protocol", getLocale()) +
				":</div></th><td><div>"
				+ StringUtil.nullandStringTohyphen(cal.getProtocol())
				+ "</div></td></tr>";
		}
		table += "<tr><th style=\"white-space:pre;\"><div>" +
			MessageManager.getMessage("Common.label.response.Time", getLocale()) +
			":</div></th><td><div>"
			+ StringUtil.nullandStringTohyphen(cal.getResponseTime())
			+ " ms</div></td></tr>";
		if(cal.getResponseByte() != null && !cal.getResponseByte().equals("")
			&& !cal.getResponseByte().equals("null"))
		{
			table += "<tr><th style=\"white-space:pre;\"><div>" +
				MessageManager.getMessage("Common.label.response.Byte", getLocale()) +
				":</div></th><td><div>"
				+ StringUtil.nullandStringTohyphen(cal.getResponseByte())
				+ " byte</div></td></tr>";
		}
		if(cal.getFault() != null && !cal.getFault().equals("")
			&& !cal.getFault().equals("null"))
		{
			table += "<tr><th style=\"white-space:pre;\"><div>" +
				MessageManager.getMessage("Common.label.fault.Code", getLocale()) +
				":</div></th><td><div>"
				+ StringUtil.nullandStringTohyphen(cal.getFault()) + "</div></td></tr>";
		}
		if(cal.getFaultString() != null && !cal.getFaultString().equals("")
			&& !cal.getFaultString().equals("null"))
		{
			table += "<tr><th style=\"white-space:pre;\"><div>" +
				MessageManager.getMessage("Common.label.fault.Message", getLocale()) +
				":</div></th><td><div>"
				+ StringUtil.nullandStringTohyphen(cal.getFaultString())
				+ "</div></td></tr>";
		}
		table += "</table></div>";
		return table;
	}
	
	private class ServiceCallTree extends LabelTree {
		public ServiceCallTree(String id, DefaultTreeModel model) {
			super(id, model);
		}

		@Override
		protected Component newNodeComponent(String id, IModel<Object> model) {
			return new LabelIconPanel(id, model, this) {
				@Override
				protected Component newContentComponent(
					String componentId, BaseTree tree, IModel<Object> model)
				{
					return new Label(componentId, model).setEscapeModelStrings(false);
				}

				@Override
				protected Component newImageComponent(
					String componentId, final BaseTree tree, final IModel<Object> model)
				{
					return new Image(componentId) {
						@Override
						protected void onComponentTag(ComponentTag tag) {
							TreeModel t = (TreeModel)tree.getDefaultModelObject();
							CallTreeNode dModel = (CallTreeNode)model.getObject();
							if(dModel.isLeaf()) {
								tag.put("src", "images/common/treeLeaf.jpg");
							} else if(dModel.isFault()) {
								tag.put("src", "images/common/failed.jpg");
							} else {
								tag.put("src", "images/common/success.jpg");
							}
						}
					};
				}

				private static final long serialVersionUID = 1L;
			};
		}
	}

	private class CallTreeNode extends DefaultMutableTreeNode{
	   public CallTreeNode(String message, boolean isFault){
	      super(message);
	      this.isFault = isFault;
	   }
	   public CallTreeNode(String message){this(message, false);}
	   public boolean isFault(){return isFault;}
	   private boolean isFault;
	}
	
	private class CallTreeModel implements Serializable {
		public CallTreeModel() {}
		public String getServiceId() {return serviceId;}
		public void setServiceId(String serviceId) {this.serviceId = serviceId;}
		public String getServiceName() {return serviceName;}
		public void setServiceName(String serviceName) {this.serviceName = serviceName;}
		public String getInvocationName() {return invocationName;}
		public void setInvocationName(String invocationName) {this.invocationName = invocationName;}
		public String getResponseTime() {return responseTime;}
		public void setResponseTime(String responseTime) {this.responseTime = responseTime;}
		public String getFault() {return fault;}
		public void setFault(String fault) {this.fault = fault;}
		public String getFaultString() {return faultString;}
		public void setFaultString(String faultString) {this.faultString = faultString;}
		public String getProtocol() {return protocol;}
		public void setProtocol(String protocol) {this.protocol = protocol;}
		public String getResponseByte() {return responseByte;}
		public void setResponseByte(String responseByte) {this.responseByte = responseByte;}
		public String getCopyright() {return copyright;}
		public void setCopyright(String copyright) {this.copyright = copyright;}
		public String getLicense() {return license;}
		public void setLicense(String license) {this.license = license;}
		@Override
		public boolean equals(Object obj) {return EqualsBuilder.reflectionEquals(this, obj);}
		@Override
		public String toString() {return ToStringBuilder.reflectionToString(this);}
		@Override
		public int hashCode() {return HashCodeBuilder.reflectionHashCode(this);}

		private String serviceId;
		private String serviceName;
		private String invocationName;
		private String responseTime;
		private String fault;
		private String faultString;
		private String protocol;
		private String responseByte;
		private String copyright;
		private String license;
		private static final long serialVersionUID = 1L;
	}
	
	private RequiredToDateTextField endDateField;
	private RequiredFromDateTextField startDateField;
	private WebMarkupContainer rewritable;
	private int pagingCount = 50;
}
