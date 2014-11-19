/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2013 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.rpc.intf;

import java.lang.annotation.Annotation;

import jp.go.nict.langrid.commons.util.Trio;

/**
 * 
 * @author Takao Nakaguchi
 */
public class RpcAnnotationUtil {
	public static String getServiceDescriptions(Class<?> clazz, String descriptionLanguage){
		Service sa = clazz.getAnnotation(Service.class);
		Descriptions ds = clazz.getAnnotation(Descriptions.class);
		Description de = clazz.getAnnotation(Description.class);
		String first = null;
		Description[] empty = {};
		for(Description[] descs : new Description[][]{
				sa != null ? sa.descriptions() : empty,
				ds != null ? ds.value() : empty,
				de != null ? new Description[]{de} : empty}){
			for(Description d : descs){
				if(d.lang().equals(descriptionLanguage)) return d.value();
				if(first == null) first = d.value();
			}
		}
		return first;
	}

	public static String getMethodDescription(
			java.lang.reflect.Method intfMethod, java.lang.reflect.Method implMethod
			, String descriptionLanguage){
		String first = null;
		Description[] empty = {};
		for(java.lang.reflect.Method m : new java.lang.reflect.Method[]{implMethod, intfMethod}){
			Method ma = m.getAnnotation(Method.class);
			Descriptions ds = m.getAnnotation(Descriptions.class);
			Description de = m.getAnnotation(Description.class);
			for(Description[] descs : new Description[][]{
					ma != null ? ma.descriptions() : empty,
					ds != null ? ds.value() : empty,
					de != null ? new Description[]{de} : empty}){
				for(Description d : descs){
					if(d.lang().equals(descriptionLanguage)) return d.value();
					if(first == null) first = d.value();
				}
			}
		}
		return first;
	}

	public static Trio<String, String, String>[] getParameterInfo(
			java.lang.reflect.Method intfMethod, java.lang.reflect.Method implMethod
			, String descriptionLanguage){
		Annotation[][] implAnnots = implMethod.getParameterAnnotations();
		Annotation[][] intfAnnots = intfMethod.getParameterAnnotations();
		int n = implAnnots.length;
		@SuppressWarnings("unchecked")
		Trio<String, String, String>[] ret = new Trio[n];
		for(int i = 0; i < n; i++){
			String name = null, sample = null, desc = null;
			for(Annotation a : implAnnots[i]){
				if(a instanceof Parameter){
					Parameter p = (Parameter)a;
					name = p.name();
					sample = p.sample();
					desc = getDescription(p.descriptions(), descriptionLanguage);
				} else if(a instanceof Description){
					if(desc == null) desc = ((Description) a).value();
				} else if(a instanceof Descriptions){
					if(desc == null) desc = getDescription(((Descriptions)a).value(), descriptionLanguage);
				}
			}
			boolean descFound = desc != null;
			for(Annotation a : intfAnnots[i]){
				if(a instanceof Parameter){
					Parameter p = (Parameter)a;
					if(name == null) name = p.name();
					if(sample == null) sample = p.sample();
					if(!descFound) desc = getDescription(p.descriptions(), descriptionLanguage);
				} else if(a instanceof Description){
					if(desc == null) desc = ((Description) a).value();
				} else if(a instanceof Descriptions){
					if(desc == null) desc = getDescription(((Descriptions)a).value(), descriptionLanguage);
				}
			}
			if(name == null) name = "";
			if(sample == null) sample = "";
			if(desc == null) desc = "";
			ret[i] = Trio.create(name, sample, desc);
		}
		return ret;
	}

	/**
	 * Returns description value of specified language. returns the value of descs[0] if the
	 * description of specified language is not exist or null if descs.length is zero.
	 * @param descs array of Description
	 * @param lang target language
	 * @return
	 */
	public static  String getDescription(Description[] descs, String lang){
		if(descs.length == 0) return null;
		Description target = null;
		for(Description desc : descs){
			if(desc.lang().equals(lang)){
				target = desc;
				break;
			}
		}
		if(target != null) return target.value();
		return descs[0].value();
	}
}
