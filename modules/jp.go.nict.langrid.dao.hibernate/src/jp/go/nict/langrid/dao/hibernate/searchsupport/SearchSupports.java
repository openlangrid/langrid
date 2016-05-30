/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
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
package jp.go.nict.langrid.dao.hibernate.searchsupport;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Embeddable;

import jp.go.nict.langrid.dao.entity.Attribute;
import jp.go.nict.langrid.dao.entity.AttributedElement;
import jp.go.nict.langrid.dao.entity.OldServiceType;
import jp.go.nict.langrid.dao.entity.Resource;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.UpdateManagedEntity;
import jp.go.nict.langrid.dao.entity.User;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class SearchSupports {
	public static Map<String, SearchSupport<Service>> getServiceSearchSupports(){
		return serviceSearchSupports;
	}

	public static Map<String, SearchSupport<Resource>> getResourceSearchSupports(){
		return resourceSearchSupports;
	}

	public static Map<String, SearchSupport<User>> getUserSearchSupports(){
		return userSearchSupports;
	}

	private static Map<String, SearchSupport<Service>> serviceSearchSupports
			= new HashMap<String, SearchSupport<Service>>();
	private static Map<String, SearchSupport<Resource>> resourceSearchSupports
			= new HashMap<String, SearchSupport<Resource>>();
	private static Map<String, SearchSupport<User>> userSearchSupports
			= new HashMap<String, SearchSupport<User>>();

	@SuppressWarnings({"unchecked", "rawtypes"})
	static class AnyTypeEnumSearchSupport<T>
	extends EnumSearchSupport<T, OldServiceType>{
		public AnyTypeEnumSearchSupport(Class clazz) {
			super((Class<OldServiceType>)clazz);
		}
	}

	private static <T> void addSearchSupportsByReflection(
			Class<T> clazz, Map<String, SearchSupport<T>> searchSupports){
		for(Field f : clazz.getDeclaredFields()){
			if(f.getType().isAssignableFrom(Set.class)){
				searchSupports.put(
						f.getName()
						, new StringSetSearchSupport<T>()
						);
				continue;
			}
			if(Collection.class.isAssignableFrom(f.getType())) continue;
			if(Enum.class.isAssignableFrom(f.getType())){
				searchSupports.put(
						f.getName()
						, new AnyTypeEnumSearchSupport<T>(f.getType()));
				continue;
			}
			if(f.getType().equals(boolean.class)){
				searchSupports.put(
						f.getName()
						, new BooleanSearchSupport<T>());
				continue;
			}
			if(Number.class.isAssignableFrom(f.getType())){
				searchSupports.put(
						f.getName()
						, new NumberSearchSupport<T>());
				continue;
			}
			if(Calendar.class.isAssignableFrom(f.getType())){
				searchSupports.put(
						f.getName()
						, new CalendarSearchSupport<T>());
				continue;
			}
			boolean needStringValueSuffix
				= f.getType().getAnnotation(Embeddable.class) != null;
			searchSupports.put(
					f.getName()
					, new StringSearchSupport<T>(
							needStringValueSuffix ? ".stringValue" : ""
							)
					);
		}
	}

	private static <T extends AttributedElement<U>, U extends Attribute>
		void addLanguageSearchSupports(Map<String, SearchSupport<T>> searchSupports){
		LanguagePathArrayAnyCombinationSearchSupport<T, U> combination
				= new LanguagePathArrayAnyCombinationSearchSupport<T, U>();
		LanguagePathArrayPathListSearchSupport<T, U> list
				= new LanguagePathArrayPathListSearchSupport<T, U>();
		LanguagePathArrayExSearchSupport<T, U> pairOr
				= new LanguagePathArrayExSearchSupport<T, U>("_PairList");
		LanguagePathArrayExSearchSupport<T, U> pathOr
				= new LanguagePathArrayExSearchSupport<T, U>("_PathList");
		searchSupports.put("supportedLanguages", combination);
		searchSupports.put("supportedLanguagePairs", pairOr);
		searchSupports.put("supportedLanguagePairs_AnyCombination", combination);
		searchSupports.put("supportedLanguagePairs_PairList", list);
		searchSupports.put("supportedLanguagePaths", pathOr);
		searchSupports.put("supportedLanguagePaths_AnyCombination", combination);
		searchSupports.put("supportedLanguagePaths_PathList", list);
		searchSupports.put("supportedAllLanguages", new LanguagePathArrayAllSearchSupport<T, U>());
	}

	private static <T extends UpdateManagedEntity> void addDateSearchSupports(
			Map<String, SearchSupport<T>> searchSupports){
		searchSupports.put("createdDateTime", new CalendarSearchSupport<T>());
		searchSupports.put("updatedDateTime", new CalendarSearchSupport<T>());
	}

	static{
		addSearchSupportsByReflection(Service.class, serviceSearchSupports);
		addLanguageSearchSupports(serviceSearchSupports);
		addDateSearchSupports(serviceSearchSupports);
		serviceSearchSupports.put("serviceType", new ServiceTypeSearchSupport<Service>());
		serviceSearchSupports.put("containerType", new ServiceContainerTypeSearchSupport<Service>());

		addSearchSupportsByReflection(Resource.class, resourceSearchSupports);
		addLanguageSearchSupports(resourceSearchSupports);
		addDateSearchSupports(resourceSearchSupports);

		addSearchSupportsByReflection(User.class, userSearchSupports);
		addDateSearchSupports(userSearchSupports);
}
}
