package jp.go.nict.langrid.foundation.v1_3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer;

import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DomainNotFoundException;
import jp.go.nict.langrid.dao.entity.AccessLimit;
import jp.go.nict.langrid.dao.entity.AccessRight;
import jp.go.nict.langrid.dao.entity.AccessStat;
import jp.go.nict.langrid.dao.entity.BPELService;
import jp.go.nict.langrid.dao.entity.Domain;
import jp.go.nict.langrid.dao.entity.ExternalService;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.dao.entity.Protocol;
import jp.go.nict.langrid.dao.entity.Resource;
import jp.go.nict.langrid.dao.entity.ResourceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ResourceType;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceType;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.dao.util.EntityUtil;
import jp.go.nict.langrid.foundation.AbstractLangridService;
import jp.go.nict.langrid.foundation.annotation.TransactionMethod;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;
import jp.go.nict.langrid.service_1_2.util.ExceptionConverter;
import jp.go.nict.langrid.service_1_3.foundation.EntityManagementService;
import jp.go.nict.langrid.service_1_3.foundation.NewerAndOlderKeys;

public class EntityManagement
extends AbstractLangridService
implements EntityManagementService{
	@Override
	@TransactionMethod
	public NewerAndOlderKeys getNewerAndOlderKeys(String entityType, Calendar standardDateTime,
			MatchingCondition... conditions)
			throws ServiceConfigurationException, UnknownException {
		Class<?> entityClass = getEntityClass(entityType);
		try{
			List<Object> newer = new ArrayList<>();
			List<Object> older = new ArrayList<>();
			for(Pair<Object, Calendar> entry : 
				getDaoContext().listAllIdAndUpdates(
						entityClass,
						getConditions(conditions))){
				if(entry.getSecond().after(standardDateTime)){
					newer.add(entry.getFirst());
				} else{
					older.add(entry.getFirst());
				}
			}
			Pair<Serializable[], Serializable[]> ret = Pair.create(newer.toArray(new Serializable[]{}), older.toArray(new Serializable[]{}));
			return new NewerAndOlderKeys(ret.getFirst(), ret.getSecond());
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@Override
	@TransactionMethod
	public Object getEntity(String entityType, Object entityId) 
	throws ServiceConfigurationException, UnknownException {
		JSON j = new JSON(){
			@Override
			protected Object preformat(Context context, Object value) throws Exception {
				if(value instanceof JavassistLazyInitializer) return null;
				return super.preformat(context, value);
			}
		};
		try {
			Class<?> entityClass = getEntityClass(entityType);
			String s = j.format(getDaoContext().loadEntity(
					entityClass, (Serializable)new Converter().convert(entityId, EntityUtil.getIdClass(entityClass))
					));
			return j.parse(s);
		} catch(DomainNotFoundException e){
			throw ExceptionConverter.convertToUnknownException(e);
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@Override
	public Object getGridEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	private Class<?> getEntityClass(String entityType){
		switch(entityType){
			case "Grid": return Grid.class;
			case "User": return User.class;
			case "Domain": return Domain.class;
			case "ResourceType": return ResourceType.class;
			case "ResourceMetaAttribute": return ResourceMetaAttribute.class;
			case "ServiceType": return ServiceType.class;
			case "ServiceMetaAttribute": return ServiceMetaAttribute.class;
			case "Protocol": return Protocol.class;
			case "Resource": return Resource.class;
			case "ExternalService": return ExternalService.class;
			case "BPELService": return BPELService.class;
			case "AccessRight": return AccessRight.class;
			case "AccessStat": return AccessStat.class;
			case "AccessLimit": return AccessLimit.class;
			default: return null;
		}
	}

	@SuppressWarnings("unchecked")
	private Pair<String, String>[] getConditions(String entityType, String gridId){
		return new Pair[]{
			new Pair<String, String>("gridId", gridId)
		};
	}

	@SuppressWarnings("unchecked")
	private Pair<String, String>[] getConditions(MatchingCondition...conditions){
		@SuppressWarnings("rawtypes")
		Pair[] ret = new Pair[conditions.length];
		for(int i = 0; i < ret.length; i++){
			MatchingCondition c = conditions[i];
			ret[i] = new Pair<String, String>(c.getFieldName(), c.getMatchingValue());
		}
		return ret;
	}
}
