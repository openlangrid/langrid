package jp.go.nict.langrid.management.web.model.languagepath;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.management.web.model.enumeration.MetaAttribute;

import org.apache.commons.lang.ArrayUtils;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class LanguagePathModel implements Serializable {
	/**
	 * 
	 * 
	 */
	public void addPath(String key, LanguagePath path) {
		LanguagePath[] marged = null;
		int i = 0;
		if(pathMap.containsKey(key)) {
			LanguagePath[] orig = pathMap.get(key);
			marged = new LanguagePath[orig.length + 1];
			for(LanguagePath lp : orig) {
				marged[i++] = lp;
			}
		}
		if(marged == null) {
			marged = new LanguagePath[1];
		}
		marged[i] = path;
		pathMap.put(key, marged);
	}

	/**
	 * 
	 * 
	 */
	public void addPathArray(String key, LanguagePath[] paths) {
		LanguagePath[] marged = null;
		int i = 0;
		if(pathMap.containsKey(key)) {
			LanguagePath[] orig = pathMap.get(key);
			marged = new LanguagePath[paths.length + orig.length];
			for(LanguagePath lp : orig) {
				marged[i++] = lp;
			}
		}
		if(marged == null) {
			marged = new LanguagePath[paths.length];
		}
		for(LanguagePath lp : paths) {
			marged[i++] = lp;
		}
		pathMap.put(key, marged);
	}

	/**
	 * 
	 * 
	 */
	public void replacePath(String key, LanguagePath[] paths) {
		pathMap.put(key, paths);
	}

	/**
	 * 
	 * 
	 */
	public LanguagePath[] getPath(String key) {
		return pathMap.get(key);
	}

	/**
	 * 
	 * 
	 */
	public Collection<LanguagePath[]> getCollectedPath(String key) {
		if(pathMap.get(key) == null) {
			return null;
		}
		List<LanguagePath> list = new ArrayList<LanguagePath>();
		List<LanguagePath[]> result = new ArrayList<LanguagePath[]>();
		for(LanguagePath path : pathMap.get(key)) {
			if(list.contains(path.reverse())) {
				list.remove(path.reverse());
				result.add(new LanguagePath[]{path.reverse(), path});
			} else {
				list.add(path);
			}
		}
		for(LanguagePath path : list) {
			result.add(new LanguagePath[]{path});
		}
		return result;
	}

	/**
	 * 
	 * 
	 */
	public LanguagePath[] getAllPath() {
		LanguagePath[] paths = new LanguagePath[]{};
		for(String key : pathMap.keySet()) {
			LanguagePath[] path = pathMap.get(key);
			if(key.equals("supportedLanguagePairs_AnyCombination")
				|| key.equals("supportedLanguagePaths_AnyCombination")) {
				for(LanguagePath p : pathMap.get(key)) {
					List<LanguagePath> list = new ArrayList<LanguagePath>();
					for(int i = 0; i < p.getPath().length - 1; i++) {
						for(int j = i + 1; j < p.getPath().length; j++) {
							list.add(new LanguagePath(p.getPath()[i], p.getPath()[j]));
							list.add(new LanguagePath(p.getPath()[j], p.getPath()[i]));
						}
					}
					path = list.toArray(new LanguagePath[]{});
				}
			}
			paths = (LanguagePath[])ArrayUtils.addAll(paths, path);
		}
		return paths;
	}

	/**
	 * 
	 * 
	 */
	public Set<String> getAllKeySet() {
		Set<String> all = new LinkedHashSet<String>();
		all.addAll(pathMap.keySet());
		all.addAll(otherMap.keySet());
		return all;
	}

	/**
	 * 
	 * 
	 */
	public Set<String> getKeySet() {
		return pathMap.keySet();
	}

	/**
	 * 
	 * 
	 */
	public LanguagePath[] getCombinationPathArray() {
		LanguagePath[] combinationPaths = pathMap
			.get(MetaAttribute.SUPPORTEDLANGUAGEPAIRS_ANYCOMBINATION.getName());
		if(combinationPaths == null) {
			combinationPaths = pathMap
				.get(MetaAttribute.SUPPORTEDLANGUAGEPATHS_ANYCOMBINATION.getName());
		} else {
			LanguagePath[] pathsCombination = pathMap
				.get(MetaAttribute.SUPPORTEDLANGUAGEPATHS_ANYCOMBINATION.getName());
			if(pathsCombination != null) {
				combinationPaths = ArrayUtil.append(combinationPaths, pathsCombination);
			}
		}
		return combinationPaths;
	}

	/**
	 * 
	 * 
	 */
	public Collection<LanguagePath[]> getDirectionalPathArray() {
		return getCollectedPath(MetaAttribute.SUPPORTEDLANGUAGEPAIRS_PAIRLIST.getName());
	}

	/**
	 * 
	 * 
	 */
	public LanguagePath[] getSinglePathArray() {
		LanguagePath[] languages = pathMap
			.get(MetaAttribute.SUPPORTEDLANGUAGES.getName());
		if(languages == null) {
			languages = pathMap.get(MetaAttribute.SUPPORTEDAUDIOTYPES.getName());
		} else {
			LanguagePath[] pathsDirectional = pathMap
				.get(MetaAttribute.SUPPORTEDAUDIOTYPES.getName());
			if(pathsDirectional != null) {
				languages = ArrayUtil.append(languages, pathsDirectional);
			}
		}
		if(languages == null) {
			languages = pathMap.get(MetaAttribute.SUPPORTEDVOICETYPES.getName());
		} else {
			LanguagePath[] pathsDirectional = pathMap
				.get(MetaAttribute.SUPPORTEDVOICETYPES.getName());
			if(pathsDirectional != null) {
				languages = ArrayUtil.append(languages, pathsDirectional);
			}
		}
		return languages;
	}

	/**
	 * 
	 * 
	 */
	public LanguagePath[] getMultihopPathArray() {
		return pathMap.get(MetaAttribute.SUPPORTEDLANGUAGEPATHS_PATHLIST.getName());
	}

	/**
	 * 
	 * 
	 */
	public void setOtherLanguage(String key, String value) {
		otherMap.put(key, value);
	}

	/**
	 * 
	 * 
	 */
	public String getOtherLanguage(String key) {
		return otherMap.get(key);
	}

	/**
	 * 
	 * 
	 */
	public Collection<String> getOtherLanguages() {
		return otherMap.values();
	}

	/**
	 * 
	 * 
	 */
	public boolean isEmpty() {
		return pathMap.isEmpty() && otherMap.isEmpty();
	}

	private Map<String, LanguagePath[]> pathMap = new LinkedHashMap<String, LanguagePath[]>();
	private Map<String, String> otherMap = new LinkedHashMap<String, String>();
	
	private static final long serialVersionUID = -1163162412461148856L;	
}
