/*
 * $Id: NodeLoader_1_2.java 1162 2014-03-19 15:23:57Z t-nakaguchi $
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
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.go.nict.langrid.commons.io.PropertyFileReader;
import jp.go.nict.langrid.commons.nio.charset.CharsetUtil;
import jp.go.nict.langrid.service_1_2.foundation.Attribute;
import jp.go.nict.langrid.service_1_2.foundation.nodemanagement.NodeProfile;
import jp.go.nict.langrid.testresource.Nodes_1_2;

import org.apache.commons.beanutils.BeanUtilsBean;

public class NodeLoader_1_2
{
	public NodeProfile loadProfile(Nodes_1_2 resource) throws IOException{
		return loadProfile(resource, new NodeProfile());
	}

	public Attribute[] loadAttributes(Nodes_1_2 resource) throws IOException{
		return loadAttributes(resource, Attribute.class);
	}

	protected BeanUtilsBean getProfileBeanUtils(){
		return profileBeanUtils;
	}

	protected Set<String> getProfileTupleProperties(){
		return profileTuples;
	}

	protected Iterable<String> getProfileProperties(){
		return profileProps;
	}

	public class NodeIdHolder{
		public void setNodeId(String nodeId){
			this.nodeId = nodeId;
		}
		String nodeId;
	}

	/**
	 * ノードIDを読み込む。
	 * @param resource 読み込むリソース
	 * @return ノードID
	 * @throws IOException 読込に失敗した
	 */
	public String loadId(Nodes_1_2 resource)
	throws IOException
	{
		return resource.loadTo(
				new NodeIdHolder(), getProfileTupleProperties()
				).nodeId;
	}

	/**
	 * サービスプロファイルを読み込む。
	 * @param resource 読み込むリソース
	 * @return サービスプロファイル
	 * @throws IOException 読込に失敗した
	 */
	protected NodeProfile loadProfile(Nodes_1_2 resource, NodeProfile profile)
	throws IOException
	{
		return resource.loadTo(
				profile, getProfileTupleProperties()
				);
	}
	
	/**
	 * サービス属性を読み込む。
	 * @param resource 読み込むリソース
	 * @return サービス属性
	 * @throws IOException 読込に失敗した
	 */
	protected Attribute[] loadAttributes(Nodes_1_2 resource, Class<Attribute> clazz)
	throws IOException
	{
		Map<String, String> props = new PropertyFileReader(
				CharsetUtil.newUTF8Decoder()).read(resource.loadProperties()
						);
		for(String name : getProfileProperties()){
			props.remove(name);
		}
		for(String name : getProfileTupleProperties()){
			props.remove(name);
		}
		props.remove("nodeId");
		List<Attribute> attrs = new ArrayList<Attribute>();
		try{
			for(Map.Entry<String, String> entry : props.entrySet()){
				attrs.add(clazz.getConstructor(
						String.class, String.class
						).newInstance(
								entry.getKey(), entry.getValue()
								));
			}
		} catch(IllegalAccessException e){
			throw new RuntimeException(e);
		} catch(InstantiationException e){
			throw new RuntimeException(e);
		} catch(InvocationTargetException e){
			throw new RuntimeException(e);
		} catch(NoSuchMethodException e){
			throw new RuntimeException(e);
		}
		return attrs.toArray((Attribute[])Array.newInstance(clazz, 0));
	}

	protected static void addProperties(
			Class<?> clazz, Set<String> properties
			, Set<String> tupleProperties)
	{
		for(Method m : clazz.getDeclaredMethods()){
			if(m.getParameterTypes().length != 1) continue;
			String name = m.getName();
			if(!name.startsWith("set")) continue;
			if(!Modifier.isPublic(m.getModifiers())) continue;
			String propName = name.substring(3, 4).toLowerCase() + name.substring(4);
			if(m.getParameterTypes()[0].isArray()){
				tupleProperties.add(propName);
			} else{
				properties.add(propName);
			}
		}
	}
	private static BeanUtilsBean profileBeanUtils = new BeanUtilsBean();
	private static Set<String> profileTuples = new HashSet<String>();
	private static Set<String> profileProps = new HashSet<String>();

	static{
		// リフレクションでNodeProfileのプロパティとタプルを
		// profileProps、profileTuplesに設定する。
		addProperties(NodeProfile.class, profileProps, profileTuples); 
	}
}
