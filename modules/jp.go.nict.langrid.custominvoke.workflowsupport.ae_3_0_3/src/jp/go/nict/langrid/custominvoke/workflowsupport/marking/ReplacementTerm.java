/*
 * $Id: ReplacementTerm.java 260 2010-10-03 09:49:40Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
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

package jp.go.nict.langrid.custominvoke.workflowsupport.marking;

import jp.go.nict.langrid.custominvoke.workflowsupport.analysis.HangulAnalysis;
import jp.go.nict.langrid.custominvoke.workflowsupport.util.StringUtil;

/**
 * 形態素配列より、中間コードに置き換え文章を作成する
 * ハイライト対応版
 * @author koyama
 * @version 1.0
 */
public class ReplacementTerm extends jp.go.nict.langrid.custominvoke.workflowsupport.ReplacementTerm {
		
	/**
	 * 中間コードから文章に戻します。
	 * @param sourceLang 言語コード
	 * @param source 文章
	 * @param searchWords 対象語
	 * @param replacementWords 置換え語
	 * @return 置換え後の文章
	 */
	@Override
	protected String replacementFromIntermediateCode(String sourceLang, String source, String[] searchWords, String[] replacementWords) {
		for (int i = 0; i < searchWords.length; i++) {
			if (!searchWords[i].equals("") && !searchWords[i].equals("")) {
				int index = source.indexOf(searchWords[i]);
				if (index > -1) {
					source = source.replace(searchWords[i], StringUtil.markingWord(replacementWords[i], (i + 1)));
					if (sourceLang.toLowerCase().contains("ko")) {
						source = HangulAnalysis.convertParticl(source, replacementWords[i], index);
					}
				}
			}
			if (source.indexOf(replacementWords[i]) == 0 && !LANGUAGES.contains(sourceLang)) {
				char[] ch = source.toCharArray();
				ch[0] = Character.toUpperCase(ch[0]);
				source = new String(ch);
			}
		}
		return source;
	}
}
