package jp.go.nict.langrid.management.web.view.page.admin.federation.source;

import jp.go.nict.langrid.management.web.view.page.admin.OperationRequestPage;

import org.apache.wicket.Page;

public class FederalOperationRequestResultPage extends OperationOfConnectionRequestResultPage {
	public FederalOperationRequestResultPage(
	   String sourceGridId, String targetGridId, String targetGridUserId, boolean isReject, String requestUrl)
	{
	   super(sourceGridId, targetGridId, targetGridUserId, isReject, requestUrl);
	}
	
	@Override
	protected Page getBackPage(){
	   return new OperationRequestPage();
	}
}
