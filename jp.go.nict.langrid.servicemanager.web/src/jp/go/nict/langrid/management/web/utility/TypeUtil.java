package jp.go.nict.langrid.management.web.utility;

import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.management.web.model.ResourceTypeModel;
import jp.go.nict.langrid.management.web.model.ServiceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.model.service.ServiceModelUtil;

public class TypeUtil {
   public static ServiceTypeModel getServiceTypeByResourceType(
      String gridId, String domainId, ResourceTypeModel model)
   {
      if(model.getResourceTypeId().equals("Other")){
         return ServiceModelUtil.makeOtherServiceTypeModel();
      }
      try{
         return ServiceFactory.getInstance().getServiceTypeService(gridId).get(
            domainId, relatedMap.get(model.getResourceTypeId()));
      } catch(ServiceManagerException e){
         return ServiceModelUtil.makeOtherServiceTypeModel();
      }
   }
   
   private static Map<String, String> relatedMap = new HashMap<String, String>(){{
      put("BilingualDictionary" ,"BilingualDictionary");
      put("ConceptDictionary" ,"ConceptDictionary");
      put("DependencyParser" ,"DependencyParser");
      put("DialogCorpus" ,"AdjacencyPair");
      put("LanguageIdentification" ,"LanguageIdentification");
      put("MorphologicalAnalyzer" ,"MorphologicalAnalysis");
      put("MultilingualDictionary" ,"BilingualDictionary");
      put("ParallelText" ,"ParallelText");
      put("Paraphraser" ,"Paraphrase");
      put("PictogramDictionary" ,"PictogramDictionary");
      put("SimilarityCalculator" ,"SimilarityCalculation");
      put("TextToSpeech" ,"TextToSpeech");
      put("Translator" ,"Translation");
   }};
}
