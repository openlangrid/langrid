package jp.go.nict.langrid.management.web.view.page.language.resource;

import jp.go.nict.langrid.management.web.view.page.language.LanguageDomainViewPage;

public abstract class ResourceViewPage extends LanguageDomainViewPage{
//	protected Component getLanguagePathComponent(String componentId, ResourceModel resource)
//	throws InvalidLanguageTagException
//	{
//	   String typeId = resource.getResourceType().getResourceTypeId();
//	   if(typeId.equals("Translator") || typeId.equals("BilingualDictionary")
//   	   || typeId.equals("ParallelText") || typeId.equals("MultilingualDictionary"))
//	   {
////		if(resource.getResourceType().equals(String.valueOf(ResourceTypeEnum.TRANSLATOR.ordinal()))
////				|| resource.getResourceType().equals(String.valueOf(ResourceTypeEnum.BILINGUALDICTIONARY.ordinal()))
////				|| resource.getResourceType().equals(String.valueOf(ResourceTypeEnum.PARALLELTEXT.ordinal()))
////				|| resource.getResourceType().equals(
////						String.valueOf(ResourceTypeEnum.BILINGUALDICTIONARYHEADWORDSEXTRACTION.ordinal()))
////				||resource.getResourceType().equals(String.valueOf(ResourceTypeEnum.MULTILINGUALDICTIONARY.ordinal())))
////		{
//			return new MultidirectionalLanguagePathPanel(componentId, resource.getSupportedLanguages());
//		}
//		
//		String language = LanguagePathUtil.encodeToSimplifiedExpressionByName(
//				resource.getSupportedLanguages(), getLocale());
//		Set<String> codeSet = new HashSet<String>();
//		for(jp.go.nict.langrid.language.LanguagePath path : resource.getSupportedLanguages()){
//			for(Language code : path.getPath()){
//				codeSet.add(code.getCode());
//			}
//		}
//		return getPathComponent(componentId, language, codeSet);
//	}
}
