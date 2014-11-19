package jp.go.nict.langrid.wrapper.ws_1_2.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExceptionWord {
	public static String encodeExceptionWord(String content) {
		for (int i = 0; i < replace.size() ; i++) {
			String[] flatArray = ExceptionWord.flatten(exceptionWords.get(replace.get(i).get("name")));
			if (flatArray.length == 0) {
				continue;
			}
			
			for (int j = 0; j < flatArray.length; j ++) {
				flatArray[j] = flatArray[j].toLowerCase();
			}
			Arrays.sort(flatArray);
			
			Pattern p = Pattern.compile("(\\s+)|(<[^>]*>)");
			Matcher m = p.matcher(content);
			if(!m.find()) { 
				continue;
			}
			
			ArrayList<HashMap<String, Integer>> headTailLists = new ArrayList<HashMap<String, Integer>>();
			HashMap<String, Integer> aMap;
			int head = 0;
			do {
				aMap = new HashMap<String, Integer>();
				
				aMap.put("head", head);
				aMap.put("tail", m.start());
				
				headTailLists.add(aMap);
				head = m.end();
			} while (m.find());
			
			aMap = new HashMap<String, Integer>();
			aMap.put("head", head);
			aMap.put("tail", content.length());
			headTailLists.add(aMap);
			
			Collections.reverse(headTailLists);
			for (HashMap<String, Integer> headTail : headTailLists) {
				String substr = content.substring(headTail.get("head"), headTail.get("tail"));
				if (Arrays.binarySearch(flatArray, substr.toLowerCase()) >= 0) {
					String contentHead = content.substring(0, headTail.get("head"));
					String contentTail = content.substring(headTail.get("tail"));
					substr = substr.replace(replace.get(i).get("symbol"), replace.get(i).get("rule"));
					content = contentHead + substr + contentTail;
				}
			}
		}

		return content;
	}
	
	public static String encodeInvalidSeparatorWithLanguage(String text, String lang) {
		text = encode(text, ExceptionWord.patternForEncode);
		
		if (lang.equals("ja") || lang.equals("zh") || lang.equals("ko") || lang.startsWith("zh-")) {
			for (HashMap<String, String> aMap : replace) {
				text = text.replace(aMap.get("symbol") + "」", aMap.get("rule") + "」");
			}
		}
		
		if (lang.equals("en") || lang.equals("de") 
				|| lang.equals("es") || lang.equals("pt")
				|| lang.equals("fr") || lang.equals("it")
				|| lang.equals("ko")) {
			String[] separators = ExceptionWord.getSeparators(lang).toArray((new String[]{}));
			Arrays.sort(separators);
			for (HashMap<String, String> aMap : replace) {
				if (Arrays.binarySearch(separators, aMap.get("symbol")) >= 0) {
					text = text.replace(aMap.get("symbol") + "\"", aMap.get("rule") + "\"");
				}
			}
		}

		Pattern p = Pattern.compile("([.!?>]|^)\\s+(\\d+\\.?)+\\.");
		Matcher m = p.matcher(text);
		while(m.find()) {
			text = text.substring(0, m.end() -1) + "[[#dot]]" + text.substring(m.end()); 
		}

		return text;
	}
	
	public static String encode(String text, String pattern) {
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(text);
		
		while(m.find()) {
			String substr = m.group();
			int pos = text.indexOf(substr);
			String textHead = text.substring(0, pos);
			String textTail = text.substring(pos + substr.length());
			for (HashMap<String, String> aMap : replace) {
				substr = substr.replace(aMap.get("symbol"), aMap.get("rule"));
			}
			text = textHead + substr + textTail;
		}
		return text;
	}
	
	public static String decode(String text) {
		for(HashMap<String, String> aMap : replace) {
			text = text.replace(aMap.get("rule"), aMap.get("symbol"));
		}
		return text;
	}
	
	public static ArrayList<String> getSeparators(String lang) {
		ArrayList<String> aList = separatorArrays.get(lang);
		if (aList != null) {
			return aList;
		} else if (lang.startsWith("zh-")){
			return separatorArrays.get("zh");
		} else {
			return separatorArrays.get("en");
		}
	}
	
	public static String[] flatten(ArrayList<ArrayList<String>> arrays) {
		ArrayList<String> ret = new ArrayList<String>();
		if (arrays == null) return ret.toArray(new String[]{});
	
		for (ArrayList<String> aArray : arrays ) {
			ret.addAll(aArray);
		}
		return ret.toArray(new String[]{});
	}
	
	
	private static ArrayList<HashMap<String, String>> replace = new ArrayList<HashMap<String, String>>();
	private static HashMap<String, ArrayList<ArrayList<String>>> exceptionWords = new HashMap<String, ArrayList<ArrayList<String>>>();
	private static HashMap<String, ArrayList<String>> separatorArrays  = new HashMap<String, ArrayList<String>>();
	private static String patternForEncode;
	
	static {
		patternForEncode = "[.!?][-_~*.!?()a-zA-Z0-9;\\/:@&=+$,%#]*[-_~*()a-zA-Z0-9;\\/:@&=+$,%#]";
		
		separatorArrays.put("bg", new ArrayList<String>(Arrays.asList(".", "?", "!")));
		separatorArrays.put("de", new ArrayList<String>(Arrays.asList(".", "?", "!")));
		separatorArrays.put("en", new ArrayList<String>(Arrays.asList(".", "?", "!")));
		separatorArrays.put("es", new ArrayList<String>(Arrays.asList(".", "?", "!")));
		separatorArrays.put("fr", new ArrayList<String>(Arrays.asList(".", "?", "!")));
		separatorArrays.put("it", new ArrayList<String>(Arrays.asList(".", "?", "!")));
		separatorArrays.put("ja", new ArrayList<String>(Arrays.asList("。","．",".","？","！","?","!")));
		separatorArrays.put("ko", new ArrayList<String>(Arrays.asList(".", "?", "!")));
		separatorArrays.put("pt", new ArrayList<String>(Arrays.asList(".", "?", "!")));
		separatorArrays.put("zh", new ArrayList<String>(Arrays.asList("。","？","?","!","！","．",".")));		
		
		ArrayList<ArrayList<String>> aArray;
		aArray = new ArrayList<ArrayList<String>>();
		/*a:*/ aArray.add(new ArrayList<String>(Arrays.asList("a.","Abb.","accel.","ahd.","Al.","Anm.","Anon.","approx.","Apr.","Apt.","art.","Aug.","av.","Ave.")));
		/*b:*/ aArray.add(new ArrayList<String>(Arrays.asList("b.","Bd.","Bde.","bld.","bldg.","Blvd.","bzw.")));
		/*c:*/ aArray.add(new ArrayList<String>(Arrays.asList("c.","ca.","cf.","chap.","chaps.","cho.","Co.","col.","col Ped.","Corp.","corp.","cresc.")));
		/*d:*/ aArray.add(new ArrayList<String>(Arrays.asList("d.","Dec.","decresc.","ders.","dept.","dimin.","do.","Dr.")));
		/*e:*/ aArray.add(new ArrayList<String>(Arrays.asList("e.","ea.","ed.","eds.","enc.","env.","etc.","exp.","ex.")));
		/*f:*/ aArray.add(new ArrayList<String>(Arrays.asList("f.","Feb.","ff.","fig.","Fl.","figs.","fol.","Fri.")));
		/*g:*/ aArray.add(new ArrayList<String>(Arrays.asList("g.","Gl.","govt.")));
		/*h:*/ aArray.add(new ArrayList<String>(Arrays.asList("h.","Hg.","hg.","Hgg.","Hrsg.","hmhge.","hrsg.")));
		/*i:*/ aArray.add(new ArrayList<String>(Arrays.asList("i.","ib.","ibid.","id.","Inc.","inc.","inv.")));
		/*j:*/ aArray.add(new ArrayList<String>(Arrays.asList("j.","Jan.","Jg.","Jul.","Jun.","Jr.")));
		/*k:*/ aArray.add(new ArrayList<String>(Arrays.asList("k.")));
		/*l:*/ aArray.add(new ArrayList<String>(Arrays.asList("l.","ll.","Ln.","Ltd.","ltd.","lib.")));
		/*m:*/ aArray.add(new ArrayList<String>(Arrays.asList("m.","Mar.","mdse.","Messers.","mhd.","mo.","Mon.","Mr.","Mrs.","Ms.")));
		/*n:*/ aArray.add(new ArrayList<String>(Arrays.asList("n.","nd.","nhd.","Nm.","nn.","no.","nos.","Nov.","Nr.")));
		/*o:*/ aArray.add(new ArrayList<String>(Arrays.asList("o.","Oct.","od.","op.cit.")));
		/*p:*/ aArray.add(new ArrayList<String>(Arrays.asList("p.","par.","pars.","Ph.D.","pl.","p.m.","pmk.","po.","policli.","pp.","Prof.","pseud.")));
		/*q:*/ aArray.add(new ArrayList<String>(Arrays.asList("q.","qtr.")));
		/*r:*/ aArray.add(new ArrayList<String>(Arrays.asList("r.","rall.","Rd.","Re.","rec.","REG.","Ret.","rinforz.","rinfz.","rit.","ritard.","Rm.","Rp.")));
		/*s:*/ aArray.add(new ArrayList<String>(Arrays.asList("s.","Sat.","sec.","Seg.","Sep.","Sept.","SFOR.","Sig.","smorz.","Sp.","spp.","so.","St.","st.","Sun.","Syn.")));
		/*t:*/ aArray.add(new ArrayList<String>(Arrays.asList("t.","Taf.","T.B.","T.H.I.","Thu.","t.i.d.","trans.","transl.","Tue")));
		/*u:*/ aArray.add(new ArrayList<String>(Arrays.asList("u.","ut.","UVs.")));
		/*v:*/ aArray.add(new ArrayList<String>(Arrays.asList("v.","vgl.","viz.","Vol.","vol.","volz.","vs.","ver.")));
		/*w:*/ aArray.add(new ArrayList<String>(Arrays.asList("w.","WC.","Wed.","wk.","wks.")));
		/*x:*/ aArray.add(new ArrayList<String>(Arrays.asList("x.")));
		/*y:*/ aArray.add(new ArrayList<String>(Arrays.asList("y.")));
		/*z:*/ aArray.add(new ArrayList<String>(Arrays.asList("z.")));
		/*other:*/ aArray.add(new ArrayList<String>(Arrays.asList("übers.")));	
		exceptionWords.put("dot", aArray);
		
		aArray = new ArrayList<ArrayList<String>>();
		aArray.add(new ArrayList<String>(new ArrayList<String>()));
		exceptionWords.put("question", aArray);
		exceptionWords.put("exlamation", aArray);
		exceptionWords.put("kuten", aArray);
		exceptionWords.put("mbdot", aArray);
		exceptionWords.put("mbexclamation", aArray);
		exceptionWords.put("mbquestion", aArray);

		HashMap<String, String> aMap;
		aMap = new HashMap<String, String>();
		aMap.put("name", "dot");
		aMap.put("symbol", ".");
		aMap.put("rule", "[[#dot]]");
		replace.add(aMap);
		aMap = new HashMap<String, String>();
		aMap.put("name", "question");
		aMap.put("symbol", "?");
		aMap.put("rule", "[[#question]]");
		replace.add(aMap);
		aMap = new HashMap<String, String>();
		aMap.put("name", "exclamation");
		aMap.put("symbol", "!");
		aMap.put("rule", "[[#exclamation]]");
		replace.add(aMap);
		aMap = new HashMap<String, String>();
		aMap.put("name", "kuten");
		aMap.put("symbol", "。");
		aMap.put("rule", "[[#kuten]]");
		replace.add(aMap);
		aMap = new HashMap<String, String>();
		aMap.put("name", "mbdot");
		aMap.put("symbol", "．");
		aMap.put("rule", "[[#mbdot]]");
		replace.add(aMap);
		aMap = new HashMap<String, String>();
		aMap.put("name", "mbexclamation");
		aMap.put("symbol", "！");
		aMap.put("rule", "[[#mbexclamation]]");
		replace.add(aMap);
		aMap = new HashMap<String, String>();
		aMap.put("name", "mbquestion");
		aMap.put("symbol", "？");
		aMap.put("rule", "[[#mbquestion]]");
		replace.add(aMap);
		
	}
}
