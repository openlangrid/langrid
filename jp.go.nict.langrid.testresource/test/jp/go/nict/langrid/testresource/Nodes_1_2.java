/*
 * $Id: Nodes_1_2.java 1162 2014-03-19 15:23:57Z t-nakaguchi $
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

import static jp.go.nict.langrid.testresource.NodeType.CORE;
import static jp.go.nict.langrid.testresource.NodeType.SERVICE;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.lang.ObjectUtil;
import jp.go.nict.langrid.commons.nio.charset.CharsetUtil;
import jp.go.nict.langrid.testresource.loader.BeanPropertyReader;

/**
 * プロパティファイル及びインスタンスを定義する列挙型。
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1162 $
 */
public enum Nodes_1_2 {
	/**
	 * NICTコアノード。
	 */
	NICTCORE("ws_1_2/NICTCore.prop", CORE),

	/**
	 * NICTサービスノード。
	 */
	NICTSERVICE("ws_1_2/NICTService.prop", SERVICE),

	/**
	 * 京都大学サービスノード。
	 */
	KYOTOUSERVICE("ws_1_2/KyotoUService.prop", SERVICE),
			
	;

	private static Logger logger = Logger.getLogger(Nodes_1_2.class.getName());

	private NodeType nodeType;

	private ResourceLoader propertyLoader;

	private Nodes_1_2(String propertyPath){
		this.propertyLoader = new ResourceLoader(propertyPath);
		this.nodeType = SERVICE;
	}

	private Nodes_1_2(String propertyPath, NodeType nodeType){
		this.propertyLoader = new ResourceLoader(propertyPath);
		this.nodeType = nodeType;
	}

	public NodeType getNodeType(){
		return nodeType;
	}

	public String getPropertyPath() {
		return propertyLoader.getPath();
	}

	public InputStream loadProperties() throws IOException{
		return propertyLoader.load();
	}

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
	public <T> T loadTo(T bean
			, Set<String> tuplePropertyNames)
	throws IOException{
		T b = new BeanPropertyReader(CharsetUtil.newUTF8Decoder())
		.read(propertyLoader.load(), bean, tuplePropertyNames);
		invoke(
				bean, "setNodeType", new Class<?>[]{String.class}
				, nodeType.name());
		return b;
	}

	private static void invoke(Object instance, String methodName
			, Class<?>[] parameterTypes, Object... parameters){
		try{
			ObjectUtil.invoke(instance, methodName, parameterTypes, parameters);
		} catch(IllegalAccessException e){
			logger.log(
					Level.WARNING
					, "exception while invoking setInstance."
					, e);
		} catch(InvocationTargetException e){
			logger.log(
					Level.WARNING
					, "exception while invoking setInstance."
					, e);
		} catch(NoSuchMethodException e){
		}
	}

}
