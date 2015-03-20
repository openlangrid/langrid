/*
 * $Id: NoInputOtherPanel.java 303 2010-12-01 04:21:52Z t-nakaguchi $
 * 
 * This is a program for Language Grid Core Node. This combines multiple language resources and
 * provides composite language services. Copyright (C) 2005-2008 NICT Language Grid Project.
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.management.web.view.page.language.component.form.panel;

import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.LanguagePath;

/**
 * Otherタイプの言語パス入力フォームパネル<br/>
 * 入力はテキスト形式<br/>
 * フォーマットは
 * <ol>
 *  <li>入力は半角アルファベット、半角<->(),のみ</li>
 *  <li>言語パスのくくりは()</li>
 *  <li>言語パスの区切りはカンマ","</li>
 *  <li>言語名、言語コードどちらの入力も許可、ただし空白も含め大文字小文も区別した完全一致のみ</li>
 *  <li>3Hop以上の言語パスは最後の関連が"->"でそれ以外の関連は"-"</li>
 *  <li>*は言語名も*</li>
 *  <li><->と->は同じ言語パスに含まれない</li>
 * </ol>
 * ex.(English-ko-Japanese->zh),(ja),( en <->Chinese (China))
 * 
 * TODO Code refactoring
 * 
 * @author Masaaki Kamiya
 */
public class NoInputOtherPanel extends AbstractLanguagePathPanel{
	/**
	 * コンストラクタ
	 * 
	 * @param componentId
	 */
	public NoInputOtherPanel(String componentId){
		super(componentId);
	}
	
	/**
	 * noop
	 */
	@Override
	public String[] getValueFromInput() {return new String[]{};}
	
	public String getInput(){
	   return "";
	}
	
	public void setValue(String pathValue){}

	/**
	 * noop
	 */
	@Override
   public void setPathValue(LanguagePath path, boolean isBidirection) {}
   /**
    * noop, return null always.
    */
   @Override
   public LanguagePath getPathValue() {return null;}
   @Override
   public void setValueFromInput() throws InvalidLanguageTagException {}
   /**
    * noop
    */
   @Override
   public void setSimplexOnly() {}
   /**
    * noop
    */
   @Override
   public String getKey() {return "";}
   public String[] getKeys(){
      return metaAttributeKey;
   }

   private String[] metaAttributeKey = new String[]{
      "supportedLanguages"
      , "supportedLanguagePairs_PairList"
      , "supportedLanguagePaths_PathList"
      , "supportedLanguagePairs_AnyCombination"
   };
}
