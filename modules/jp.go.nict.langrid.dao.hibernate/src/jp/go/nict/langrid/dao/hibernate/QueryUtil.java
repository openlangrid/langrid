/*
 * $Id:HibernateUserDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
package jp.go.nict.langrid.dao.hibernate;

import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

import jp.go.nict.langrid.commons.beanutils.ConversionException;
import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.AttributedElement;
import jp.go.nict.langrid.dao.entity.BPELService;
import jp.go.nict.langrid.dao.entity.ExternalService;
import jp.go.nict.langrid.dao.entity.Node;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.dao.hibernate.searchsupport.SearchSupport;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 389 $
 */
public class QueryUtil {
	/**
	 * 
	 * 
	 */
	public static <T extends AttributedElement<?>, U> Query buildJoinSearchQuery(
			Session session, Class<T> clazz, Class<U> joinedClass
			, String joinColumn, String joinedClassJoinColumn
			, Map<String, Class<?>> fields
			, MatchingCondition[] conditions
			, Order[] orders
			)
	{
		StringBuilder hql = new StringBuilder();
		hql.append("select this_,joined_")
			.append(" from ")
			.append(clazz.getName())
			.append(" this_,")
			.append(joinedClass.getName())
			.append(" joined_")
			.append(" where this_.")
			.append(joinColumn)
			.append("=joined_.")
			.append(joinedClassJoinColumn)
			;
		Map<String, Object> params = new HashMap<String, Object>();
		hql.append(buildConditionsQuery(clazz, conditions, fields, params));
		hql.append(buildOrderByQuery(clazz, orders));
		Query q = session.createQuery(hql.toString());
		for(Map.Entry<String, Object> e : params.entrySet()){
			q.setParameter(e.getKey(), e.getValue());
		}
		return q;
	}

	/**
	 * 
	 * 
	 */
	public static <T extends AttributedElement<?>> Query buildSearchQuery(
			Session session, Class<T> clazz
			, Map<String, Class<?>> fields
			, MatchingCondition[] conditions
			, Order[] orders
			)
	{
		StringBuilder hql = new StringBuilder();
		hql.append("select this_")
			.append(" from ")
			.append(clazz.getName())
			.append(" this_")
			.append(" where 1=1");
		Map<String, Object> params = new HashMap<String, Object>();
		hql.append(buildConditionsQuery(clazz, conditions, fields, params));
		hql.append(buildOrderByQuery(clazz, orders));
		Query q = session.createQuery(hql.toString());
		for(Map.Entry<String, Object> e : params.entrySet()){
			q.setParameter(e.getKey(), e.getValue());
		}
		return q;
	}

	/**
	 * 
	 * 
	 */
	public static <T> Query buildSearchQueryWithSearchSupports(
			Session session, Class<T> clazz
			, Map<String, SearchSupport<T>> searchSupports
			, SearchSupport<T> defaultSearchSupport
			, MatchingCondition[] conditions
			, Order[] orders
			, String... additionalConditions
			)
	{
		StringBuilder hql = new StringBuilder();
		hql.append("select this_")
			.append(" from ")
			.append(clazz.getName())
			.append(" this_")
			.append(" where 1=1");
		Map<String, Object> params = new HashMap<String, Object>();
		hql.append(buildConditionsQueryWithSearchSupports(
				conditions, "this_"
				, searchSupports, defaultSearchSupport
				, params
				));
		for(String q : additionalConditions){
			hql.append(" and ");
			hql.append(q);
		}
		hql.append(buildOrderByQuery(clazz, orders));
		Query q = session.createQuery(hql.toString());
		for(Map.Entry<String, Object> e : params.entrySet()){
			q.setParameter(e.getKey(), e.getValue());
		}
		return q;
	}

	/**
	 * 
	 * 
	 */
	public static Query buildSearchQueryWithSearchSupports(
			Session session
			, Map<String, SearchSupport<Service>> searchSupports
			, SearchSupport<Service> defaultSearchSupport
			, String serviceGridId, boolean acrossGrids
			, MatchingCondition[] conditions, Order[] orders
			, String... additionalConditions
			)
	{
		StringBuilder hql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		hql.append("select this_")
			.append(" from  ")
			.append(Service.class.getName())
			.append(" this_");
		if(acrossGrids){
			hql.append(" where (" +
					"this_.gridId=:gridId" +
					" or exists(select f from Federation f where f.sourceGridId=:gridId and this_.gridId=f.targetGridId"
					+ " and f.connected=true and this_.federatedUseAllowed=true)" +
					")");
			params.put("gridId", serviceGridId);
		} else{
			hql.append(" where this_.gridId=:gridId");
			params.put("gridId", serviceGridId);
		}
		hql.append(buildConditionsQueryWithSearchSupports(
				conditions, "this_"
				, searchSupports, defaultSearchSupport
				, params
				));
		for(String q : additionalConditions){
			hql.append(" and ");
			hql.append(q);
		}
		hql.append(buildOrderByQuery(Service.class, orders));
		Query q = session.createQuery(hql.toString());
		for(Map.Entry<String, Object> e : params.entrySet()){
			q.setParameter(e.getKey(), e.getValue());
		}
		return q;
	}

	/**
	 * 
	 * 
	 */
	public static <T> Query buildServiceRankingSearchQueryWithSearchSupports(
			Session session
			, Map<String, SearchSupport<T>> searchSupports
			, SearchSupport<T> defaultSearchSupport
			, String serviceGridId, String nodeId, boolean acrossGrids
			, MatchingCondition[] conditions, Order[] orders
			, int sinceDays, String... additionalConditions
			)
	{
		StringBuilder hql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		hql.append("select service.gridId, service.serviceId, service.ownerUserId" +
				", sum(stat.accessCount), sum(stat.requestBytes)" +
				", sum(stat.responseBytes), sum(stat.responseMillis)" +
				//", cast(sum(stat.responseMillis) as double) / cast(sum(stat.accessCount) as double)" +
				", cast(sum(stat.responseMillis) / sum(stat.accessCount) as double)" +
				" from Service service, AccessStat stat");
		hql.append(" where service.gridId=stat.serviceAndNodeGridId" +
				" and service.serviceId=stat.serviceId");
		if(acrossGrids){
			hql.append(" and (" +
					"service.gridId=:gridId" +
					" or exists(select f from Federation f" +
					" where f.sourceGridId=:gridId and service.gridId=f.targetGridId and service.federatedUseAllowed=true)" +
					")");
			params.put("gridId", serviceGridId);
		} else{
			hql.append(" and service.gridId=:gridId");
			params.put("gridId", serviceGridId);
		}
		if(nodeId != null){
			hql.append(" and stat.nodeId=:nodeId");
			params.put("nodeId", nodeId);
		}
		hql.append(buildConditionsQueryWithSearchSupports(
				conditions, "service"
				, searchSupports, defaultSearchSupport
				, params
				));
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1 * sinceDays);
		c = CalendarUtil.createBeginningOfDay(c);
		hql.append(" and stat.baseDateTime >= :sinceDateTime");
		params.put("sinceDateTime", c);
		for(String q : additionalConditions){
			hql.append(" and ");
			hql.append(q);
		}
		hql.append(" group by service.containerType, service.gridId, service.serviceId, service.ownerUserId");
		boolean first = true;
		for(Order o : orders){
			if(first){
				hql.append(" order by ");
				first = false;
			} else{
				hql.append(",");
			}
			if(o.getFieldName().toLowerCase().equals("responsemillisave")){
				hql.append("cast(sum(stat.responseMillis) / sum(stat.accessCount) as double)");
			} else if(o.getFieldName().toLowerCase().equals("accesscount")){
				hql.append("sum(stat.accessCount)");
			} else{
				hql.append(o.getFieldName());
			}
			switch(o.getDirection()){
				case ASCENDANT:
					hql.append(" asc");
					break;
				case DESCENDANT:
					hql.append(" desc");
					break;
			}
		}
		Query q = session.createQuery(hql.toString());
		for(Map.Entry<String, Object> e : params.entrySet()){
			q.setParameter(e.getKey(), e.getValue());
		}
		return q;
	}
	
	/**
	 * 
	 * 
	 */
	public static <T> Query buildRowCountQuery(
			Session session, Class<T> clazz
			, Map<String, Class<?>> fields
			, MatchingCondition[] conditions)
	{
		StringBuilder hql = new StringBuilder();
		hql.append("select count(*)")
			.append(" from ")
			.append(clazz.getName())
			.append(" this_")
			.append(" where 1=1");
		Map<String, Object> params = new HashMap<String, Object>();
		hql.append(buildConditionsQuery(clazz, conditions, fields, params));
		Query q = session.createQuery(hql.toString());
		for(Map.Entry<String, Object> e : params.entrySet()){
			q.setParameter(e.getKey(), e.getValue());
		}
		return q;
	}

	/**
	 * 
	 * 
	 */
	public static <T> Query buildRowCountQueryWithSearchSupports(
			Session session, Class<?> clazz
			, Map<String, SearchSupport<T>> searchSupports
			, SearchSupport<T> defaultSearchSupport
			, String serviceGridId, boolean acrossGrids
			, MatchingCondition[] conditions
			, String... additionalConditions
			)
	{
		StringBuilder hql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		hql.append("select count(*)")
			.append(" from ")
			.append(clazz.getName())
			.append(" this_");
		if(acrossGrids){
			hql.append(" where (" +
					"this_.gridId=:gridId" +
					" or exists(select f from Federation f where f.sourceGridId=:gridId and this_.gridId=f.targetGridId and this_.federatedUseAllowed=true)" +
					")");
			params.put("gridId", serviceGridId);
		} else{
			hql.append(" where this_.gridId=:gridId");
			params.put("gridId", serviceGridId);
		}
		hql.append(buildConditionsQueryWithSearchSupports(
				conditions, "this_"
				, searchSupports, defaultSearchSupport
				, params
				));
		for(String q : additionalConditions){
			hql.append(" and ");
			hql.append(q);
		}
		Query q = session.createQuery(hql.toString());
		for(Map.Entry<String, Object> e : params.entrySet()){
			q.setParameter(e.getKey(), e.getValue());
		}
		return q;
	}

	/**
	 * 
	 * 
	 */
	public static void assertValidColumnName(String name)
	throws IllegalArgumentException
	{
		for(char c : name.toCharArray()){
			if(c == '.') continue;
			if(Character.isJavaIdentifierPart(c)) continue;
			throw new IllegalArgumentException(
					"invalid character in field name"
					);
		}
	}

	/**
	 * 
	 * 
	 */
	public static String buildOrderByQuery(Class<?> entityClass, Order[] orders)
	{
		return buildOrderByQuery(entityClass, "this_", orders);
	}

	/**
	 * 
	 * 
	 */
	public static String buildOrderByQuery(
			Class<?> entityClass, String defaultTableAlias, Order[] orders)
	{
		StringBuilder b = new StringBuilder();
		boolean first = true;
		for(Order o : orders){
			String fieldName = o.getFieldName();
			Boolean string = isString(entityClass, fieldName);
			if(string == null) continue;
			if(first){
				b.append(" order by ");
				first = false;
			} else{
				b.append(",");
			}

			if(string){
				b.append("lower(");
			}
			assertValidColumnName(fieldName);
			if(fieldName.indexOf('.') == -1){
				b.append(defaultTableAlias);
				b.append(".");
			}
			b.append(fieldName);
			if(string){
				b.append(")");
			}
			switch(o.getDirection()){
				case ASCENDANT:
					b.append(" asc");
					break;
				case DESCENDANT:
					b.append(" desc");
			}
		}
		return b.toString();
	}

	/**
	 * 
	 * 
	 */
	public static String buildOrderByQuerySub(
			Class<?> entityClass, String defaultTableAlias, Order[] orders)
	{
		StringBuilder b = new StringBuilder();
		boolean first = true;
		for(Order o : orders){
			String fieldName = o.getFieldName();
			Boolean string = isString(entityClass, fieldName);
			if(string == null) continue;
			if(first){
				first = false;
			} else{
				b.append(",");
			}

			if(string){
				b.append("lower(");
			}
			assertValidColumnName(fieldName);
			if(fieldName.indexOf('.') == -1){
				b.append(defaultTableAlias);
				b.append(".");
			}
			b.append(fieldName);
			if(string){
				b.append(")");
			}
			switch(o.getDirection()){
				case ASCENDANT:
					b.append(" asc");
					break;
				case DESCENDANT:
					b.append(" desc");
			}
		}
		return b.toString();
	}

	/**
	 * 
	 * 
	 */
	public static String buildOrderByQuery(
			Set<String> capitalIgnoreFields, Order[] orders)
	{
		StringBuilder b = new StringBuilder();
		boolean first = true;
		for(Order o : orders){
			if(first){
				b.append(" order by ");
				first = false;
			} else{
				b.append(",");
			}

			String name = o.getFieldName();
			if(capitalIgnoreFields.contains(name)){
				b.append("lower(");
				b.append(name);
				b.append(")");
			} else{
				b.append(name);
			}
			switch(o.getDirection()){
				case ASCENDANT:
					b.append(" asc");
					break;
				case DESCENDANT:
					b.append(" desc");
			}
		}
		return b.toString();
	}

	public static String buildConditionsQuery(
			Class<?> entityClass
			, MatchingCondition[] conditions
			, Map<String, Class<?>> fields
			, Map<String, Object> parameters
			)
	{
		String propQueryFormat
			= " and this_.%s %s :propValue%d";
		String attrQueryFormat
			= " and (select count(*) from this_.attributes attr%1$d"
			+ " where attr%1$d.name=:attrName%1$d and attr%1$d.value %2$s :attrValue%1$d)=1";

		StringBuilder hql = new StringBuilder();
		int index = 0;
		for(MatchingCondition c : conditions){
			String name = c.getFieldName();
			Object value = c.getMatchingValue();
			String operator = "like";
			switch(c.getMatchingMethod()){
				default:
				case COMPLETE:
					operator = "=";
					break;
				case PREFIX:
					value = value + "%";
					break;
				case SUFFIX:
					value = "%" + value;
					break;
				case PARTIAL:
					value = "%" + value + "%";
					break;
				case GT:
					operator = ">";
					break;
				case GE:
					operator = ">=";
					break;
				case LT:
					operator = "<";
					break;
				case LE:
					operator = "<=";
					break;
				case CONTAINS:
					operator = "in";
					break;
			}
			int p = name.indexOf('.');
			if(p != -1 && isFieldCollectionAttribute(entityClass, name.substring(0, p))){
				String colName = name.substring(0, p);
				String fName = name.substring(p + 1);
				hql.append(String.format(" and (select count(*) from this_.%1$s %1$s where", colName));
				boolean first = true;
				for(String v : value.toString().split(" ")){
					v = v.trim();
					if(v.length() == 0) continue;
					if(!first){
						hql.append(" or");
					} else{
						first = false;
					}
					hql.append(String.format(
							" (:cpValue%4$d %3$s %1$s.%2$s)",
							colName, fName, operator, index));
					parameters.put("cpValue" + index, v);
					index++;
				}
				hql.append(")=1");
			} else{
				Class<?> fieldClass = fields.get(name);
				if(fieldClass != null){
					if(Clob.class.isAssignableFrom(fieldClass)){
						if(!(value instanceof Clob)){
							value = Hibernate.createClob(value.toString());
						}
					} else if(Blob.class.isAssignableFrom(fieldClass)){
						if(!(value instanceof Blob)){
							if(value instanceof byte[]){
								value = Hibernate.createBlob((byte[])value);
							} else{
								value = Hibernate.createBlob(
										StringUtil.toUTF8Bytes(value.toString())
										);
							}
						}
					}
	
					String attr = getFieldAttribute(entityClass, name);
					if(attr != null){
						name += "." + attr;
						hql.append(String.format(
								propQueryFormat, name, operator, index
								));
						parameters.put("propValue" + index, value);
					} else{
						try{
							Object propValue = converter.convert(value, fieldClass);
							hql.append(String.format(
									propQueryFormat, name, operator, index
									));
							parameters.put(
									"propValue" + index
									, propValue
									);
						} catch(ConversionException e){
							logger.warning(String.format(
									"condition %s is ignored"
									+ " because failed to convert approprite type."
									, c));
						}
					}
				} else{
					hql.append(String.format(
							attrQueryFormat, index, operator
							));
					parameters.put("attrName" + index, name);
					parameters.put("attrValue" + index, value);
				}
				index++;
			}
		}
		return hql.toString();
	}

	public static <T> String buildConditionsQueryWithSearchSupports(
			MatchingCondition[] conditions, String elementAlias
			, Map<String, SearchSupport<T>> conditionBuilders
			, SearchSupport<T> defaultConditionBuilder
			, Map<String, Object> parameters
			)
	{
		StringBuilder hql = new StringBuilder();
		int index = 0;
		for(MatchingCondition c : conditions){
			String name = c.getFieldName();
			SearchSupport<T> b = conditionBuilders.get(name);
			if(b != null){
				b.appendWhereClouse(c, elementAlias, "param" + index, hql, parameters);
			} else{
				defaultConditionBuilder.appendWhereClouse(
						c, elementAlias, "param" + index, hql, parameters
						);
			}
			index++;
		}
		return hql.toString();
	}

	private static String getFieldAttribute(Class<?> entityClass, String fieldName){
		return fieldAttributes.get(
				entityClass.getName() + "#" + fieldName
				);
	}

	private static boolean isFieldCollectionAttribute(Class<?> entityClass, String fieldName){
		return fieldCollectionAttributes.contains(
				entityClass.getName() + "#" + fieldName
				);
	}

	private static Boolean isString(Class<?> entityClass, String fieldName){
		String fqname = entityClass.getName() + "#" + fieldName;
		Boolean isString = fieldIsString.get(fqname);
		if(isString == null){
			while(!entityClass.equals(Object.class)){
				try{
					Field f = entityClass.getDeclaredField(fieldName);
					isString = f.getType().equals(String.class);
					break;
				} catch(NoSuchFieldException e){
				}
				entityClass = entityClass.getSuperclass();
			}
			if(isString != null){
				fieldIsString.put(fqname, isString);
			}
		}
		return isString;
	}

	private static Map<String, String> fieldAttributes
			= new HashMap<String, String>();
	private static Set<String> fieldCollectionAttributes
			= new HashSet<String>();
	private static Map<String, Boolean> fieldIsString
			= new ConcurrentHashMap<String, Boolean>();
	private static Converter converter = new Converter();
	private static Logger logger = Logger.getLogger(
			QueryUtil.class.getName());
	static{
		// for Service
		String[] prefixes = {
				Service.class.getName()
				, ExternalService.class.getName()
				, BPELService.class.getName()
		};
		for(String p : prefixes){
			fieldAttributes.put(p + "#supportedLanguages", "stringValue");
			fieldAttributes.put(p + "#endpointUrl", "stringValue");
			fieldAttributes.put(p + "#targetNamespace", "stringValue");
		}
		// for User
		fieldAttributes.put(User.class.getName() + "#homepageUrl", "stringValue");
		fieldCollectionAttributes.add(User.class.getName() + "#roles");
		// for Node
		fieldAttributes.put(Node.class.getName() + "#url", "stringValue");
		fieldAttributes = Collections.unmodifiableMap(fieldAttributes);
	}
}
