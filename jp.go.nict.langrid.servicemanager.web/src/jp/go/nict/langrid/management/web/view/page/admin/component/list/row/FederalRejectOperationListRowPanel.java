package jp.go.nict.langrid.management.web.view.page.admin.component.list.row;

import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.dao.entity.OperationType;
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
public class FederalRejectOperationListRowPanel extends Panel{
	/**
	 * 
	 * 
	 * @throws ServiceManagerException
	 */
	public FederalRejectOperationListRowPanel(
		String gridId, String componentId, OperationRequestModel operation, String uniqueId)
	throws ServiceManagerException
	{
		super(componentId);
		sourceGridId = operation.getTargetId();
		targetGridId = gridId;
		add(new Label("date", DateUtil.formatYMDWithSlash(
				operation.getCreatedDateTime().getTime())));
		add(new Label("gridId", operation.getTargetId()));
		Map<String, String> map = new HashMap<String, String>();
		map.put("gridId", operation.getTargetId());
		Label label = new Label("operation"
		   , MessageManager.getMessage("operation.request.federation.Reject", map)
		);
		label.setEscapeModelStrings(false);
		add(label);

		add(new ConfirmLink<OperationRequestModel>("operate", new Model<OperationRequestModel>(operation)
		   , MessageManager.getMessage("LanguageGridOperator.request.message.delete.alert.Confirm", getLocale()))
		{
			@Override
			public void onClick() {
			   try {
               ServiceFactory.getInstance().getOperationService(targetGridId, targetGridId, "").deleteByTargetId(
                  getModelObject().getTargetId(), OperationType.FEDERATIONREJECT);
            } catch(ServiceManagerException e) {
               error(MessageManager.getMessage("LanguageGridOperator.request.message.delete.Error", getLocale()));
               LogWriter.writeError("Operater", e, getPage().getClass());
            }
			}
		});
	}

	private String sourceGridId;
	private String targetGridId;
	private static final long serialVersionUID = 1L;
}
