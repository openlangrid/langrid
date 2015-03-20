package jp.go.nict.langrid.management.web.utility;

import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.dao.entity.ScheduleActionType;
import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class NewsFactory{
	public static NewsModel createServiceAddNews(ServiceModel service) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("name", service.getServiceName());
		return NewsFactory.createLanguageServiceNews(
				service.getInstanceType(), "Registered"
				, false, true, false, param);
	}
	
	public static NewsModel createServiceReplaceNews(ServiceModel service){
		Map<String, String> param = new HashMap<String, String>();
		param.put("name", service.getServiceName());
		return NewsFactory.createLanguageServiceNews(
				service.getInstanceType(), "Replaced"
				, false, true, false, param);
	}
	
	
	/**
	 * 
	 * 
	 */
	public static NewsModel createLanguageResourceNews(
			String operationName, boolean isReserve, boolean isDone, Map<String, String> paramMap)
	{
		StringBuffer messageKey = new StringBuffer();
		messageKey.append("news.resource.language.");
		return createNews(messageKey, operationName, isReserve, isDone, false, paramMap);
	}
	
	/**
	 * 
	 * 
	 */
	public static NewsModel createLanguageServiceNews(
			InstanceType type, String operationName, boolean isReserve
			, boolean isDone, boolean hasRelated, Map<String, String> paramMap)
	{
		StringBuffer messageKey = new StringBuffer();
		messageKey.append("news.service.");
		if(type.equals(InstanceType.EXTERNAL)) {
			messageKey.append("atomic.");
		}else if(type.equals(InstanceType.BPEL)) {
			messageKey.append("composite.");
		}else {
			messageKey.append("all.");			
		}
		return createNews(messageKey, operationName, isReserve, isDone, hasRelated, paramMap);
	}
	
	/**
	 * 
	 * 
	 */
	public static NewsModel createServiceScheduleNews(InstanceType targetType
			, ScheduleActionType actionType, boolean isCancel, boolean isRelated
			, Map<String, String> paramMap)
	{
		String operationName = "";
		if(actionType.equals(ScheduleActionType.SUSPENSION)){
			operationName = "suspend";
		}else if(actionType.equals(ScheduleActionType.UNREGISTRATION)){
			operationName = "unregistration";
		}
		return createLanguageServiceNews(targetType, operationName, !isCancel, false
				, isRelated, paramMap);
	}

	public static NewsModel createResourceScheduleNews(ScheduleActionType actionType
			, boolean isCancel, boolean isRelated, Map<String, String> paramMap)
	{
		String operationName = "";
		if(actionType.equals(ScheduleActionType.SUSPENSION)){
			operationName = "suspend";
		}else if(actionType.equals(ScheduleActionType.UNREGISTRATION)){
			operationName = "unregistration";
		}
		return createLanguageResourceNews(operationName, !isCancel, false, paramMap);
	}

	private static NewsModel createNews(StringBuffer messageKey, String operationName, boolean isReserve
			, boolean isDone, boolean hasRelated, Map<String, String> paramMap)
	{
		String operateType = "";
		if(!isDone){
			if(isReserve){
				operateType = ".Reserve";
			}else{
				operateType = ".Cancel";
			}
		}
		if(hasRelated){
			operationName = operationName.toLowerCase();
			operateType = operateType.toLowerCase();
			operateType = operateType.concat(".ByResource");
		}
		messageKey.append(operationName);
		messageKey.append(operateType);
		NewsModel news = new NewsModel();
		news.setContents(MessageManager.getMessage(messageKey.toString(), paramMap));
		return news;
	}
}
