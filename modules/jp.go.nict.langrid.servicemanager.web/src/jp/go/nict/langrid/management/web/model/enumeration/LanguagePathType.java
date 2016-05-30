package jp.go.nict.langrid.management.web.model.enumeration;

import java.util.ArrayList;
import java.util.List;

public enum LanguagePathType {
   SINGLE("Single")
   , PAIR("Pair")
   , MULTI("Multi")
   , COMBINATION("Combination")
   , BACKTRANSLATION("BackTranslation")
   , UNKNOWN("Unknown")
   ;
   
   private LanguagePathType(String displayName) {
      name = displayName;
   }
   
   public String getName(){
      return name;
   }
   
   public static List<LanguagePathType> getWithoutUnknown(){
      List<LanguagePathType> list = new ArrayList<LanguagePathType>();
      for(LanguagePathType type : values()){
         if( ! type.equals(LanguagePathType.UNKNOWN)){
            list.add(type);
         }
      }
      return list;
   }
   
   private String name;
}
