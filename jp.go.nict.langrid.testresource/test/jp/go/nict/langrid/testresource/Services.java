/*
 * $Id: Services.java 1162 2014-03-19 15:23:57Z t-nakaguchi $
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
package jp.go.nict.langrid.testresource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * プロパティファイル及びインスタンスを定義する列挙型が実装するインタフェース。
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1162 $
 */
public interface Services {
	/**
	 * プロパティのパスを取得する。
	 * @return プロパティのパス
	 */
	String getPropertyPath();

	/**
	 * プロパティをロードする。
	 * @return プロパティ
	 * @throws IOException ロードに失敗した
	 */
	InputStream loadProperties() throws IOException;

	/**
	 * インスタンスのパスを取得する。
	 * @return インスタンスのパス
	 */
	String getInstancePath();

	/**
	 * インスタンスをロードする。
	 * インスタンス情報が無い場合、空のストリームが返される。
	 * @return インスタンス
	 * @throws IOException ロードに失敗した
	 */
	InputStream loadInstance() throws IOException;

	/**
	 * インスタンスのタイプを取得する。
	 * "EXTERNAL", "BPEL"等。
	 * @return インスタンスのタイプ
	 */
	InstanceType getInstanceType();

	/**
	 * 指定されたbeanにプロパティをロードする。
	 * instancePathが設定されている場合、setInstanceメソッドにinstanceのバイナリが、
	 * setInstanceSizeメソッドにinstanceのサイズが渡される。
	 * 各々のメソッドを持たない場合、単に無視される。
	 * @param <T> beanのクラス
	 * @param bean プロパティを読み込むbean
	 * @param beanUtils プロパティの設定に使用するBeanUtilsBean
	 * @param tuplePropertyNames タプルとして記述されているプロパティ名
	 * @return bean
	 * @throws IOException 入力に失敗した
	 */
	<T> T loadTo(T bean, Set<String> tuplePropertyNames)
	throws IOException;
}
