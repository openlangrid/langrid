/*
 * $Id: BeanPropertyReader.java 1162 2014-03-19 15:23:57Z t-nakaguchi $
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
package jp.go.nict.langrid.testresource.loader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.CharsetDecoder;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.io.PropertyFileParser;
import jp.go.nict.langrid.commons.lang.ObjectUtil;
import jp.go.nict.langrid.commons.lang.StringUtil;

/**
 * JavaBeanのプロパティをプロパティファイルから読み込んで設定する。
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1162 $
 */
public class BeanPropertyReader extends PropertyFileParser {
	/**
	 * コンストラクタ。
	 * @param decoder デコーダ
	 */
	public BeanPropertyReader(CharsetDecoder decoder) {
		super(decoder);
	}

	/**
	 * beanのプロパティを読み込んで設定して返す。
	 * 設定時に例外が発生した場合、ログに記録して例外は無視する。
	 * @param <T> beanのクラス
	 * @param stream 読み込むストリーム
	 * @param bean プロパティを読み込むbean
	 * @return bean
	 * @throws IOException 入力に失敗した
	 */
	public <T> T read(InputStream stream, T bean)
	throws IOException
	{
		return read(stream, bean, new HashSet<String>());
	}

	/**
	 * beanのプロパティを読み込んで設定して返す。
	 * 設定時に例外が発生した場合、ログに記録して例外は無視する。
	 * @param <T> beanのクラス
	 * @param stream 読み込むストリーム
	 * @param bean プロパティを読み込むbean
	 * @param beanUtils 型変換に使用するBeanUtilsBean
	 * @param tuplePropertyNames タプルが設定されるプロパティ名 
	 * @return bean
	 * @throws IOException 入力に失敗した
	 */
	public <T> T read(InputStream stream, final T bean
			, final Set<String> tuplePropertyNames)
	throws IOException
	{
		parse(stream, new Listener(){
			public void propertyFound(String name, String value) {
				Object v = value;
				if(tuplePropertyNames.contains(name)){
					try{
						v = StringUtil.decodeTuple(value);
					} catch(ParseException e){
						logger.log(Level.WARNING, String.format(
								"exception while parsing tuple property(\"%s\":\"%s\")"
								, name, value), e);
					}
				}
				try{
					ObjectUtil.setProperty(bean, name, v);
				} catch(IllegalAccessException e){
					logger.log(Level.WARNING, String.format(
							"exception while setting property(\"%s\":\"%s\")"
							, name, value), e);
				} catch(InvocationTargetException e){
					logger.log(Level.WARNING, String.format(
							"exception while setting property(\"%s\":\"%s\")"
							, name, value), e);
				} catch(IllegalArgumentException e){
					logger.log(Level.WARNING, String.format(
							"exception while setting property(\"%s\":\"%s\")"
							, name, value), e);
				}
			}
		});
		return bean;
	}

	private static Logger logger = Logger.getLogger(BeanPropertyReader.class.getName());
}
