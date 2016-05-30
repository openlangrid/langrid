package jp.go.nict.langrid.management.web.view.page.language.service;

import jp.go.nict.langrid.management.web.view.page.language.LanguageDomainViewPage;

public abstract class ServiceViewPage extends LanguageDomainViewPage {
//	protected Component getLanguagePathComponent(String componentId, ServiceModel model)
//	throws InvalidLanguageTagException
//	{
//		if(model.getServiceType().equals(ServiceType.TRANSLATION.name())
//				|| model.getServiceType().equals(ServiceType.TRANSLATIONWITHTEMPORALDICTIONARY.name())
//				|| model.getServiceType().equals(ServiceType.BILINGUALDICTIONARY.name())
//				|| model.getServiceType().equals(ServiceType.BILINGUALDICTIONARYHEADWORDSEXTRACTION.name())
//				|| model.getServiceType().equals(ServiceType.BILINGUALDICTIONARYWITHLONGESTMATCHSEARCH.name())
//				|| model.getServiceType().equals(ServiceType.EDITABLEBILINGUALDICTIONARY.name())
//				|| model.getServiceType().equals(ServiceType.PARALLELTEXT.name())
//				|| model.getServiceType().equals(ServiceType.PARALLELTEXTWITHEMBEDDEDMETADATA.name())
//				|| model.getServiceType().equals(ServiceType.PARALLELTEXTWITHEXTERNALMETADATA.name())
//				|| model.getServiceType().equals(ServiceType.METADATAFORPARALLELTEXT.name()))
//		{
//			return new MultidirectionalLanguagePathPanel(componentId, model.getSupportedLanguages());
//		}
//		
//		String language = LanguagePathUtil.encodeToSimplifiedExpressionByName(
////			LanguageUtil.encodeLanguagePathToStringByName(
//				model.getSupportedLanguages(), getLocale());
//		Set<String> codeSet = new HashSet<String>();
//		for(LanguagePath path : model.getSupportedLanguages()){
//			for(Language code : path.getPath()){
//				codeSet.add(code.getCode());
//			}
//		}
//		return getPathComponent(componentId, language, codeSet);
//	}
}
