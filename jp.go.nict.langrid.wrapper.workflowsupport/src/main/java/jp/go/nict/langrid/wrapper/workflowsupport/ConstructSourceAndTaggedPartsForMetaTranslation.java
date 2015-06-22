package jp.go.nict.langrid.wrapper.workflowsupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.wrapper.workflowsupport.util.StringUtil;

public class ConstructSourceAndTaggedPartsForMetaTranslation {
	public ConstructSPForMTResult constructSPForMT(String sourceLang, String source){
		List<TaggedPart> parts = new ArrayList<TaggedPart>();
		StringBuffer text = new StringBuffer();
		Matcher m = Pattern.compile("(< *[a-zA-Z]+[a-zA-Z0-9-]*>)|(<\\/[a-zA-Z]+[a-zA-Z0-9-]* *>)").matcher(source);
		Stack<String> waitingTags = new Stack<String>();
		int primary = 0;
		while(m.find()){
			String openTag = m.group(1);
			if(openTag == null){
				m.appendReplacement(text, m.group(2));
				continue;
			}
			Language lang = null;
			try{
				lang = new Language(openTag.substring(1, openTag.length() - 1).trim());
			} catch(InvalidLanguageTagException e){
			}
			if(lang == null){
				m.appendReplacement(text, m.group(2));
				continue;
			}
			m.appendReplacement(text, "");
			waitingTags.push("</" + openTag.substring(1));
			StringBuffer localBuf = new StringBuffer();
			while(m.find()){
				String ot = m.group(1);
				String ct = m.group(2);
				if(ot != null){
					m.appendReplacement(localBuf, ot);
					waitingTags.push("</" + ot.substring(1));
					continue;
				}
				if(ct != null && waitingTags.peek().equalsIgnoreCase(ct)){
					waitingTags.pop();
					if(waitingTags.size() == 0){
						m.appendReplacement(localBuf, "");
						String org = localBuf.toString();
						String code = StringUtil.generateCode(org, primary++);
						text.append(code);
						TaggedPart p = new TaggedPart();
						p.setCode(code);
						p.setLang(lang.getCode());
						p.setText(org);
						parts.add(p);
						localBuf = new StringBuffer();
						break;
					} else{
						m.appendReplacement(localBuf, ct);
						continue;
					}
				}
				m.appendReplacement(localBuf, m.group(0));
			}
		}
		m.appendTail(text);
		ConstructSPForMTResult r = new ConstructSPForMTResult();
		r.setText(text.toString());
		r.setParts(parts.toArray(new TaggedPart[]{}));
		return r;
	}
}
