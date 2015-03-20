package jp.go.nict.langrid.management.web.model.enumeration;

public enum MetaAttribute {
   SUPPORTEDLANGUAGEPAIRS_ANYCOMBINATION("supportedLanguagePairs_AnyCombination")
   , SUPPORTEDLANGUAGEPAIRS_PAIRLIST("supportedLanguagePairs_PairList")
   , SUPPORTEDLANGUAGEPATHS_ANYCOMBINATION("supportedLanguagePaths_AnyCombination")
   , SUPPORTEDLANGUAGEPATHS_PATHLIST("supportedLanguagePaths_PathList")
   , SUPPORTEDLANGUAGES("supportedLanguages")
   , SUPPORTEDVOICETYPES("supportedVoiceTypes")
   , SUPPORTEDAUDIOTYPES("supportedAudioTypes")
   , OTHER("Other");
   
   public String getName(){
      return name;
   }
   
   public static boolean containName(String name){
      for(MetaAttribute ma : values()){
         if(ma.name().equals(name)){
            return true;
         }
      }
      return false;
   }
   
   private MetaAttribute(String name) {
      this.name = name;
   }
   
   private String name;
}
