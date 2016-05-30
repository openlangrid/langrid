package jp.go.nict.langrid.management.web.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.management.web.model.InternalLanguageModel;
import jp.go.nict.langrid.management.web.model.enumeration.LanguagePathType;
import jp.go.nict.langrid.management.web.model.enumeration.MetaAttribute;
import jp.go.nict.langrid.management.web.model.languagepath.LanguagePathModel;

public class LanguagePathUtil {
   public static Set<LanguagePathType> getPathTypeSet(String typeId, Set<String> attributeList){
      Set<LanguagePathType> set = new HashSet<LanguagePathType>();
      if(typeId.equals("BackTranslation")){
         set.add(LanguagePathType.BACKTRANSLATION);
      }
      for(String attrId : attributeList){
         if(attrId.equals("supportedLanguagePairs_AnyCombination") ){
            set.add(LanguagePathType.PAIR);
            set.add(LanguagePathType.COMBINATION);
         }else if(attrId.equals("supportedLanguagePairs_PairList") ){
            set.add(LanguagePathType.PAIR);
         }else if(attrId.equals("supportedLanguagePaths_AnyCombination") ){
            set.add(LanguagePathType.MULTI);
            set.add(LanguagePathType.COMBINATION);
         }else if(attrId.equals("supportedLanguagePaths_PathList") ){
            set.add(LanguagePathType.MULTI);
         }else if(attrId.equals("supportedLanguages")
            || attrId.equals("supportedVoiceTypes")
            ||attrId.equals("supportedAudioTypes") )
         {
            set.add(LanguagePathType.SINGLE);
         }
      }
      if(set.size() == 0){
         set.add(LanguagePathType.UNKNOWN);
      }
      return set;
   }
   
   public static LanguagePathType getPathType(String typeId, String attribute) {
      Set<String> set = new HashSet<String>();
      set.add(attribute);
      return getPathTypeSet(typeId, set).iterator().next();
   }
   
   public static String makeMargedLanguagePathCodeString(Collection<LanguagePath[]> pathList, MetaAttribute metaAttribute, Locale locale) {
      StringBuilder sb = new StringBuilder();
      if(metaAttribute.equals(MetaAttribute.SUPPORTEDLANGUAGEPAIRS_PAIRLIST)){
         for(LanguagePath[] paths : pathList){
            sb.append("(");
            if(2 < paths[0].getPath().length){
               Language[] languages = paths[0].getPath();
               for(int i = 0; i < languages.length; i++){
                  sb.append(languages[i].getCode());
                  if(i + 2 < languages.length){
                     sb.append("-");
                  }else if(i + 1 < languages.length){
                     sb.append("->");
                  }
               }
            }else if(2 == paths[0].getPath().length) {
               Language[] languages = paths[0].getPath();
               sb.append(languages[0].getCode());
               if(paths.length < 2){
                  sb.append("->");
               }else{
                  sb.append("<->");
               }
               sb.append(languages[1]);
            }
            sb.append(")");
         }
      }else if(metaAttribute.equals(MetaAttribute.SUPPORTEDLANGUAGEPATHS_PATHLIST)){
         int i = 1;
         for(LanguagePath[] paths : pathList){
            sb.append("(");
            for(LanguagePath path : paths){
               for(Language l : path.getPath()){
                  sb.append(l.getCode());
                  if(i < path.getPath().length - 1){
                     sb.append("-");
                  }else if(i < path.getPath().length){
                     sb.append("->");
                  }
                  i++;
               }
               sb.append(")");
            }
         }
      }else if((metaAttribute.equals(MetaAttribute.SUPPORTEDLANGUAGEPAIRS_ANYCOMBINATION)
         || metaAttribute.equals(MetaAttribute.SUPPORTEDLANGUAGEPATHS_ANYCOMBINATION)))
      {
         List<Language> list = new ArrayList<Language>();
         for(LanguagePath[] paths : pathList){
            sb.append("(");
            for(LanguagePath path : paths){
               for(Language l : path.getPath()){
                  if(list.contains(l)){
                     continue;
                  }else{
                     list.add(l);
                  }
               }
            }
            int i = 1;
            for(Language l : list){
               sb.append(l.getCode());
               if(i < list.size()){
                  sb.append(", ");
               }
               i++;
            }
            sb.append(")");
         }
      }else{
         int i = 1;
         int j = 1;
         for(LanguagePath[] paths : pathList){
            sb.append("(");
            for(LanguagePath path : paths){
               for(Language l : path.getPath()){
                  sb.append(l.getCode());
                  if(j < path.getPath().length){
                     sb.append(", ");
                  }
                  j++;
               }
               if(i < paths.length){
                  sb.append(", ");
               }
               j = 1;
               i++;
            }
            sb.append(")");
         }
      }
      return sb.toString();
   }
   
   
   public static String makeLanguagePathString(LanguagePath[] paths, MetaAttribute metaAttribute, Locale locale)
   throws InvalidLanguageTagException
   {
      StringBuilder sb = new StringBuilder();
      if(metaAttribute.equals(MetaAttribute.SUPPORTEDLANGUAGEPAIRS_PAIRLIST)){
         if(2 < paths[0].getPath().length){
            Language[] languages = paths[0].getPath();
            for(int i = 0; i < languages.length; i++){
               sb.append((InternalLanguageModel.getDisplayString(languages[i].getCode(), locale)));
               if(i + 2 < languages.length){
                  sb.append("-");
               }else if(i + 1 < languages.length){
                  sb.append("->");
               }
            }
         }else if(2 == paths[0].getPath().length) {
            Language[] languages = paths[0].getPath();
            sb.append((InternalLanguageModel.getDisplayString(languages[0].getCode(), locale)));
            if(paths.length < 2){
               sb.append("->");
            }else{
               sb.append("<->");
            }
            sb.append((InternalLanguageModel.getDisplayString(languages[1].getCode(), locale)));
         }
      }else if(metaAttribute.equals(MetaAttribute.SUPPORTEDLANGUAGEPATHS_PATHLIST)){
         int i = 1;
         for(LanguagePath path : paths){
            for(Language l : path.getPath()){
               sb.append(InternalLanguageModel.getDisplayString(l.getCode(), locale));
               if(i < path.getPath().length - 1){
                  sb.append("-");
               }else if(i < path.getPath().length){
                  sb.append("->");
               }
               i++;
            }
         }
      }else if((metaAttribute.equals(MetaAttribute.SUPPORTEDLANGUAGEPAIRS_ANYCOMBINATION)
         || metaAttribute.equals(MetaAttribute.SUPPORTEDLANGUAGEPATHS_ANYCOMBINATION)))
      {
         List<Language> list = new ArrayList<Language>();
         for(LanguagePath path : paths){
            for(Language l : path.getPath()){
               if(list.contains(l)){
                  continue;
               }else{
                  list.add(l);
               }
            }
         }
         int i = 1;
         for(Language l : list){
            sb.append(InternalLanguageModel.getDisplayString(l.getCode(), locale));
            if(i < list.size()){
               sb.append(", ");
            }
            i++;
         }
      }else{
         int i = 1;
         int j = 1;
         for(LanguagePath path : paths){
            for(Language l : path.getPath()){
               sb.append(InternalLanguageModel.getDisplayString(l.getCode(), locale));
               if(j < path.getPath().length){
                  sb.append(", ");
               }
               j++;
            }
            if(i < paths.length){
               sb.append(", ");
            }
            j = 1;
            i++;
         }
      }
      return sb.toString();
   }
   
   public static LanguagePathModel buildLanguagePathMap(String pathValue, Locale locale)
   throws InvalidLanguageTagException 
   {
      LanguagePathModel model = new LanguagePathModel();
      String[] values = pathValue.split("[)],\\s*[(]");
      for(String path : values){
         path = path.replaceAll("[)]$", "");
         path = path.replaceAll("^[(]", "");
         if (path.contains("<->")) {
            LanguagePath lPath = makePairValue(path, locale);
            model.addPath("supportedLanguagePairs_PairList", lPath);
            model.addPath("supportedLanguagePairs_PairList", lPath.reverse());
         } else if (path.contains("->")) {
            LanguagePath lp = makeSimplexValue(path, locale);
            if (lp.getPath().length < 3) {
               model.addPath("supportedLanguagePairs_PairList", lp);
            } else {
               model.addPath("supportedLanguagePaths_PathList", lp);
            }
         } else {
            if (InternalLanguageModel.containName(path, locale)) {
               model.addPath("supportedLanguages"
                  , new LanguagePath(InternalLanguageModel.getByName(path, locale)));
            } else if (InternalLanguageModel.containCode(path)) {
               model.addPath("supportedLanguages"
                  , new LanguagePath(new Language(path)));
            }
         }
      }
      return model;
   }

   /**
    * テキスト入力から両方向の言語パスを生成する</br>
    * (en<->ja),(ja<->Korean),(zh-CN<->zh-Hans-CN)の形式が対象
    * 
    * @param pairString
    * @return
    * @throws InvalidLanguageTagException 
    */
   private static LanguagePath makePairValue(String pairString, Locale locale)
   throws InvalidLanguageTagException
   {
      Language[] pair = new Language[2];
      int i = 0;
      for(String p : pairString.split("<->")){
         if(InternalLanguageModel.containName(p, locale)){
            pair[i++] = InternalLanguageModel.getByName(p, locale);
         }else if(InternalLanguageModel.containCode(p)){
            pair[i++] = new Language(p);
         }
      }
      return new LanguagePath(pair);
   }

   /** 
    * テキスト入力から片方向の言語パスを生成する
    * 
    * (en-ja-Chinese-zh-Hans-CN->Chinese (China))</br>
    * (ja->English)</br>
    * (ko->en)等の形式が対象
    * 
    * @param pairString
    * @return
    * @throws InvalidLanguageTagException 
    */
   private static LanguagePath makeSimplexValue(String pairString, Locale locale)
   throws InvalidLanguageTagException
   {
      String[] tmp = pairString.split("-");
      Language[] path = new Language[tmp.length];
      for(int i = 0; i < tmp.length; i++){         
         tmp[i] = tmp[i].replace(">", "");
            if(InternalLanguageModel.containName(tmp[i], locale)) {
               path[i] = InternalLanguageModel.getByName(tmp[i], locale);
            }else if(InternalLanguageModel.containCode(tmp[i])){
               path[i] = new Language(tmp[i]);
            }
      }
      return new LanguagePath(path);
   }

   /**
    * 
    * 
    * @throws InvalidLanguageTagException 不正な言語コードが使用された
    */
   public static LanguagePathModel convertToLanguagePairModel(
      LanguagePath[] origin, String pairAttributeId, String combinationAttributeId)
   throws InvalidLanguageTagException
   {
      // Make path
      List<LanguagePath> rest = new ArrayList<LanguagePath>();

      Pairs pairs = new Pairs();
      for(LanguagePath p : origin){
         if(p.getPath().length != 2){
            rest.add(p);
            continue;
         }
         pairs.add(p.getPath()[0], p.getPath()[1]);
      }
      rest.addAll(pairs.removeUnidirectionalPairs());

      Pools pools = new Pools();
      for(Map.Entry<Language, Set<Language>> e : pairs.getPairs().entrySet()){
         Language src = e.getKey();
         for(Language dst : e.getValue()){
            pools.add(src, dst, pairs);
         }
      }
      Pair<List<Language>, List<List<Language>>> paths = pools.makeDistinctPaths();        
      
      // Set path model
      List<List<Language>> temp = new ArrayList<List<Language>>();
      LanguagePathModel model = new LanguagePathModel();
      temp.add(paths.getFirst());
      temp.addAll(paths.getSecond());
      for(List<Language> e : temp){
         if(e.size() != 0){
            if(e.size() > 2){
//               result.addCombination(e);
               
               LanguagePath path = new LanguagePath(e.toArray(new Language[]{}));
               model.addPath(combinationAttributeId, path);
            }else{
               LanguagePath path = new LanguagePath(e.toArray(new Language[]{}));
               model.addPath(pairAttributeId, path);
               model.addPath(pairAttributeId, path.reverse());
//               result.addBidirectional(e);
            }
         }
      }

      for(LanguagePath uni : rest){
         List<Language> set = new ArrayList<Language>();
         for(Language st : uni.getPath()){
            set.add(st);
         }
         LanguagePath path = new LanguagePath(set.toArray(new Language[]{}));
         model.addPath(pairAttributeId, path);
//         result.addUnidirectional(set);
      }
      return model;
   }
   
   private static class Pairs{
      public void add(Language src, Language dst){
         Set<Language> targets = pairs.get(src);
         if(targets == null){
            targets = new HashSet<Language>();
            pairs.put(src, targets);
         }
         targets.add(dst);
      }

      public boolean hasPair(Language src, Language dst){
         Set<Language> p = pairs.get(src);
         if(p == null) return false;
         return p.contains(dst);
      }

      public Collection<Language> listTargets(Language src){
         Set<Language> p = pairs.get(src);
         if(p == null) return Collections.EMPTY_LIST;
         return p;
      }

      public Map<Language, Set<Language>> getPairs(){
         return pairs;
      }

      public List<LanguagePath> removeUnidirectionalPairs() throws InvalidLanguageTagException{
         List<LanguagePath> rest = new ArrayList<LanguagePath>();
         Iterator<Map.Entry<Language, Set<Language>>> it= pairs.entrySet().iterator();
         while(it.hasNext()){
            Map.Entry<Language, Set<Language>> p = it.next();
            Language src = p.getKey();
            Iterator<Language> dstIt = p.getValue().iterator();
            while(dstIt.hasNext()){
               Language dst = dstIt.next();
               if(
//                   dst.equals(InternalLanguageModel.getWildcard().getCode()) && src.equals(InternalLanguageModel.getWildcard().getCode())
//                   ||
                     !hasPair(dst, src)){
                  // unidirectional pair
                  rest.add(new LanguagePath(src, dst));
                  dstIt.remove();
               }
            }
            if(p.getValue().size() == 0){
               it.remove();
            }
         }
         return rest;
      }

      private Map<Language, Set<Language>> pairs = new HashMap<Language, Set<Language>>();
   }

   private static class Pools{
      public Collection<List<Language>> getPools(){
         return pools;
      }

      public void add(Language src, Language dst, Pairs pairs){
         List<List<Language>> srcPools = getPools(src);
         boolean member = false;
         for(List<Language> pool : srcPools){
            member = true;
            if(pool.contains(dst)){
               break;
            }
            for(Language t : pool){
               if(!pairs.hasPair(t, dst)){
                  member = false;
                  break;
               }
            }
            if(member){
               pool.add(dst);
               getPools(dst).add(pool);
               break;
            }
         }
         if(!member){
            List<Language> pool = new ArrayList<Language>();
            pool.add(src);
            pool.add(dst);
            srcPools.add(pool);
            getPools(dst).add(pool);
            pools.add(pool);
         }
      }

      public Pair<List<Language>, List<List<Language>>> makeDistinctPaths(){
         // longest pool, pool[](重複除去済み)を返す
         int longestIndex = 0;
         int length = 0;
         for(int i = 0; i < pools.size(); i++){
            if(length < pools.get(i).size()){
               longestIndex = i;
               length = pools.get(i).size();
            }
         }
         List<Language> longest = new LinkedList<Language>();
         if(pools.size() != 0){
            longest = pools.get(longestIndex);
         }

         // 重複を省いてプールを再構成する。
         Pairs pairs = new Pairs();
         for(Language s : longest){
            for(Language t : longest){
               
               if(!(s.getCode().equals(InternalLanguageModel.getWildcard().getCode())
                  && t.getCode().equals(InternalLanguageModel.getWildcard().getCode()))
                     && s.equals(t))
               {
                  continue;
               }
               pairs.add(s, t);
            }
         }
         List<List<Language>> srcPools = new ArrayList<List<Language>>();
         for(int i = 0; i < pools.size(); i++){
            if(i == longestIndex) continue;
            srcPools.add(new ArrayList<Language>(pools.get(i)));
         }
         List<List<Language>> retPools = new ArrayList<List<Language>>();
         for(int i = 0; i < srcPools.size(); i++){
            List<Language> pool = srcPools.get(i);
            Pair<Language, Language> found = null;
            for(Language s : pool){
               for(Language t: pool){
                  if(!(s.getCode().equals(InternalLanguageModel.getWildcard().getCode())
                     && t.getCode().equals(InternalLanguageModel.getWildcard().getCode()))
                        && s.equals(t))
                  {
                     continue;
                  }
                  if(pairs.hasPair(s, t)){
                     found = Pair.create(s, t);
                     break;
                  }
               }
               if(found != null) break;
            }
            if(found != null){
               // 重複があれば分解
               pool.remove(found.getFirst());
               if(pool.size() > 1){
                  srcPools.add(pool);
                  for(Language t : pool){
                     if(t.equals(found.getSecond())) continue;
                     List<Language> newPool = new ArrayList<Language>();
                     newPool.add(found.getFirst());
                     newPool.add(t);
                     srcPools.add(newPool);
                  }
               }
            } else{
               retPools.add(pool);
               for(Language s : pool){
                  for(Language t: pool){
                     if(!(s.getCode().equals(InternalLanguageModel.getWildcard().getCode())
                        && t.getCode().equals(InternalLanguageModel.getWildcard().getCode()))
                           && s.equals(t))
                     {
                        continue;
                     }
                     pairs.add(s, t);
                  }
               }
            }
         }
         return new Pair<List<Language>, List<List<Language>>>(longest, retPools);
      }

      private List<List<Language>> getPools(Language lang){
         List<List<Language>> ret = langToPools.get(lang);
         if(ret == null){
            ret = new ArrayList<List<Language>>();
            langToPools.put(lang, ret);
         }
         return ret;
      }

      private List<List<Language>> pools = new ArrayList<List<Language>>();
//    private List<Set<String>> pools = new ArrayList<Set<String>>();
      private Map<Language, List<List<Language>>> langToPools = new HashMap<Language, List<List<Language>>>();
   }
}
