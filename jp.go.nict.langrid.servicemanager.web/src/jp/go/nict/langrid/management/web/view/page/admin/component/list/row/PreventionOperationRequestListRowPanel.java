package jp.go.nict.langrid.management.web.view.page.admin.component.list.row;

import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.OperationRequestModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.link.ConfirmLink;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class PreventionOperationRequestListRowPanel extends Panel{
	/**
	 * 
	 * 
	 * @throws ServiceManagerException
	 */
	public PreventionOperationRequestListRowPanel(
		String gridId, String componentId, OperationRequestModel operation, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		add(new Label("date", DateUtil.formatYMDWithSlash(
				operation.getCreatedDateTime().getTime())));
		Label label = new Label("operation", operation.getContents());
		label.setEscapeModelStrings(false);
		add(label);
		
		add(new ConfirmLink<OperationRequestModel>("confirm", new Model<OperationRequestModel>(operation)
		   , MessageManager.getMessage("LanguageGridOperator.request.message.delete.alert.Confirm", getLocale())) 
		{
		   @Override
		   public void onClick() {
		      try {
		         OperationRequestModel model = getModelObject();
               ServiceFactory.getInstance().getOperationService(model.getGridId(), model.getGridId(), "").delete(getModelObject());
//               info(MessageManager.getMessage("LanguageGridOperator.request.message.delete.Result", getLocale()));
            } catch(ServiceManagerException e) {
               error(MessageManager.getMessage("LanguageGridOperator.request.message.delete.Error", getLocale()));
               LogWriter.writeError("Operater", e, getPage().getClass());
            }
		   }
		});
	}

	private static final long serialVersionUID = 1L;
}
