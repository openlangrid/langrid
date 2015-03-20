/*
 * $Id: GlobPosMapper.java 265 2010-10-03 10:25:32Z t-nakaguchi $
 *
 * This is a program to wrap language resources as Web services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 2.1 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.wrapper.common.util.posmap;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Logger;

import jp.go.nict.langrid.service_1_2.typed.PartOfSpeech;

import org.apache.oro.text.GlobCompiler;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 265 $
 */
public class GlobPosMapper implements PosMapper{
	/**
	 * 
	 * 
	 */
	public GlobPosMapper(InputStream posmapStream)
		throws IOException
	{
		PatternCompiler compiler = new GlobCompiler();
		Properties p = new LinkedHashMapProperties();
		p.load(posmapStream);
		for(Entry<Object, Object> e : p.entrySet()){
			String expression = e.getKey().toString();
			String pos = e.getValue().toString();
			if(pos.length() == 0) continue;
			PartOfSpeech posv = PartOfSpeech.valueOfExpression(pos);
			if(posv == null){
				logger.warning("unknown part of speech type: " + pos);
				continue;
			}

			if(!contains(expression, "?*[{\\")){
				tagToPos.put(e.getKey().toString(), posv);
				continue;
			}
			Pattern pattern = null;
			try{
				pattern = compiler.compile(expression);
			} catch(MalformedPatternException ex){
				logger.warning("malformed glob pattern: " + expression);
				continue;
			}

			char startChar = expression.charAt(0);
			if(specialChars.contains(startChar)){
				startWithSpecialCharPatterns.add(pattern);
			} else{
				List<Pattern> patterns = charToPatterns.get(startChar);
				if(patterns == null){
					patterns = new ArrayList<Pattern>();
					charToPatterns.put(startChar, patterns);
				}
				patterns.add(pattern);
			}
			patternToPos.put(pattern, posv);
		}
	}

	/**
	 * 
	 * 
	 */
	public PartOfSpeech get(String value){
		PartOfSpeech ret = tagToPos.get(value);
		if(ret != null) return ret;

		PatternMatcherInput input = new PatternMatcherInput(value);
		char startChar = value.charAt(0);
		List<Pattern> patterns = charToPatterns.get(startChar);
		if(patterns != null){
			for(Pattern p : patterns){
				if(matcher.matches(input, p)){
					return patternToPos.get(p);
				}
			}
		}
		for(Pattern p : startWithSpecialCharPatterns){
			if(matcher.matches(input, p)){
				return patternToPos.get(p);
			}
		}
		return PartOfSpeech.unknown;
	}

	private static class LinkedHashMapProperties extends Properties{
		@Override
		public Object get(Object key){
			return maps.get(key);
		}

		@Override
		public Object put(Object key, Object value){
			return maps.put(key, value);
		}

		@Override
		public Set<Entry<Object, Object>> entrySet(){
			return maps.entrySet();
		}

		@Override
		public Enumeration<Object> keys(){
			final Iterator<Object> keys = maps.keySet().iterator();
			return new Enumeration<Object>(){
				public boolean hasMoreElements() {
					return keys.hasNext();
				}
				public Object nextElement() {
					return keys.next();
				}
			};
		}

		private Map<Object, Object> maps = new LinkedHashMap<Object, Object>();

		private static final long serialVersionUID = -1303658243716232027L;
	}

	private static boolean contains(String source, String chars){
		char[] cs = chars.toCharArray();
		for(char s : source.toCharArray()){
			for(char c : cs){
				if(s == c) return true;
			}
		}
		return false;
	}

	private Map<String, PartOfSpeech> tagToPos
		= new HashMap<String, PartOfSpeech>();
	private Map<Character, List<Pattern>> charToPatterns
		= new HashMap<Character, List<Pattern>>();
	private List<Pattern> startWithSpecialCharPatterns
		= new ArrayList<Pattern>();
	private Map<Pattern, PartOfSpeech> patternToPos
		= new HashMap<Pattern, PartOfSpeech>();
	private Perl5Matcher matcher = new Perl5Matcher();

	private static Set<Character> specialChars = new HashSet<Character>(
			Arrays.asList(new Character[]{
					'*', '?', '[', '\\'
			}));
	
	private static Logger logger = Logger.getLogger(GlobPosMapper.class.getName());
}
