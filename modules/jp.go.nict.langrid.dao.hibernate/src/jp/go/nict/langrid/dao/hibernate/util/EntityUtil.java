package jp.go.nict.langrid.dao.hibernate.util;

import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Property;

public class EntityUtil {
	public static ProjectionList addIdColumnNames(Class<?> entityClass, ProjectionList projections){
		for(String name : jp.go.nict.langrid.dao.util.EntityUtil.getIdFieldNames(entityClass)){
			projections.add(Property.forName(name));
		}
		return projections;
	}
}
