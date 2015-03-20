package jp.go.nict.langrid.management.web.view.page.language.component.modal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.management.web.model.enumeration.LanguagePathDirection;
import jp.go.nict.langrid.management.web.model.enumeration.LanguagePathType;

public class PathModel implements Serializable {
   public PathModel(
      LanguagePathType pathType, LanguagePathDirection direction, Language[] languages, Locale languageLocale)
   {
      locale = languageLocale;
      this.languages = languages;
      this.type = pathType;
      this.direction = direction;
   }
   
   public List<LanguagePath> getLanguagePathList() {
      if(direction.equals(LanguagePathDirection.SIMPLEX)){
         return makeSinglePath();
      }
      if(type.equals(LanguagePathType.PAIR)){
         if(direction.equals(LanguagePathDirection.BOTH)){
            return makeReversiblePath();
         }else if(direction.equals(LanguagePathDirection.COMBINATION)){
            return makeCombinationPath();
         }
      }else if(type.equals(LanguagePathType.MULTI)){
         if(direction.equals(LanguagePathDirection.BOTH)){
            return makeReversiblePath();
         }else if(direction.equals(LanguagePathDirection.COMBINATION)){
            return makeCombinationPath();
         }
      }
      // Unknown
      return makeSinglePath();
   }
   
   public LanguagePathType getType(){
      return type;
   }
   
   public LanguagePathDirection getDirection(){
      return direction;
   }
   
   private List<LanguagePath> makeCombinationPath() {
      List<LanguagePath> list = new ArrayList<LanguagePath>();
      for(int headIndex = 0; headIndex < (languages.length - 1); headIndex++){
         for(int tailIndex = headIndex + 1;  tailIndex < languages.length; tailIndex++){
            LanguagePath lp = new LanguagePath(languages[headIndex], languages[tailIndex]);
            list.add(lp);
            list.add(lp.reverse());
         }
         headIndex++;
      }
      return list;
   }
   
   private List<LanguagePath> makeReversiblePath() {
      List<LanguagePath> list = new ArrayList<LanguagePath>();
      LanguagePath lp = new LanguagePath(languages);
      list.add(lp);
      list.add(lp.reverse());
      return list;
   }
   
   private List<LanguagePath> makeSinglePath() {
      List<LanguagePath> list = new ArrayList<LanguagePath>();
      LanguagePath lp = new LanguagePath(languages);
      list.add(lp);
      return list;
   }
   
   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      int i = 1;
      for(Language l : languages) {
         sb.append(l.getLocalizedName(locale).replaceAll("\\S\\(", " \\("));
         if(i < languages.length){
            if((type.equals(LanguagePathType.PAIR)
               || type.equals(LanguagePathType.MULTI))
               && ! direction.equals(LanguagePathDirection.COMBINATION))
            {
               sb.append(" ");
               sb.append(direction.getCode());
               sb.append(" ");
            }else{
               sb.append(", ");
            }
         }
         i++;
      }
      return sb.toString();
   }
   
   private Language[] languages;
   private LanguagePathType type;
   private LanguagePathDirection direction;
   private Locale locale;
}
