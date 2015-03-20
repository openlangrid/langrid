package jp.go.nict.langrid.management.web.view.page.admin.federation.source;

import jp.go.nict.langrid.management.web.view.page.admin.OperationRequestPage;

import org.apache.wicket.Page;

public class FederalOperationRequestPage extends OperationOfConnectionRequestPage {
	public FederalOperationRequestPage(String sourceGridId, String targetGridId) {
	   super(sourceGridId, targetGridId);
	}
	
	protected Page getCancelPage(){
	   return new OperationRequestPage();
	}
	
	@Override
	protected Page getResultPage(String sourceGridId, String targetGridId, String targetGridUserId, boolean isReject, String requestUrl) {
	   return new FederalOperationRequestResultPage(sourceGridId, targetGridId, targetGridUserId, isReject, requestUrl);
	}
}
