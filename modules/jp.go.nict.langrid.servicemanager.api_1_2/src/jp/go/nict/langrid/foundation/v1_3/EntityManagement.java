package jp.go.nict.langrid.foundation.v1_3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DomainNotFoundException;
import jp.go.nict.langrid.dao.entity.AccessLimit;
import jp.go.nict.langrid.dao.entity.AccessRight;
import jp.go.nict.langrid.dao.entity.AccessStat;
import jp.go.nict.langrid.dao.entity.BPELService;
import jp.go.nict.langrid.dao.entity.Domain;
import jp.go.nict.langrid.dao.entity.ExternalService;
import jp.go.nict.langrid.dao.entity.Federation;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.dao.entity.News;
import jp.go.nict.langrid.dao.entity.Protocol;
import jp.go.nict.langrid.dao.entity.Resource;
import jp.go.nict.langrid.dao.entity.ResourceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ResourceType;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceType;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.dao.util.EntityUtil;
import jp.go.nict.langrid.dao.util.JSON;
import jp.go.nict.langrid.foundation.AbstractLangridService;
import jp.go.nict.langrid.foundation.annotation.TransactionMethod;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;
import jp.go.nict.langrid.service_1_2.util.ExceptionConverter;
import jp.go.nict.langrid.service_1_3.foundation.EntityManagementService;
import jp.go.nict.langrid.service_1_3.foundation.KeyAndUpdate;
import jp.go.nict.langrid.service_1_3.foundation.NewerAndOlderKeys;
import jp.go.nict.langrid.servicecontainer.handler.RIProcessor;

public class EntityManagement
extends AbstractLangridService
implements EntityManagementService{
	@Override
	@TransactionMethod
	public NewerAndOlderKeys getNewerAndOlderKeys(
			String entityType, Calendar standardDateTime,
			MatchingCondition... conditions)
	throws ProcessFailedException, ServiceConfigurationException {
		Class<?> entityClass = getEntityClass(entityType);
		if(entityClass == null) throw new ProcessFailedException("unknown entity type: " + entityType);
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
			throw new ProcessFailedException(e);
		}
	}

	@Override
	@TransactionMethod
	public Object getEntity(String entityType, Object entityId) 
	throws ProcessFailedException, ServiceConfigurationException {
		JSON j = new JSON();
		try {
			Class<?> entityClass = getEntityClass(entityType);
			Object entity = getDaoContext().loadEntity(
					entityClass, new Converter().convert(entityId, EntityUtil.getIdClass(entityClass))
					);
			if(entity == null) return null;
			String s = j.format(entity);
			return j.parse(s);
		} catch(DomainNotFoundException e){
			throw new ProcessFailedException(e);
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			throw new ProcessFailedException(e);
		}
	}

	@Override
	@TransactionMethod
	public Collection<KeyAndUpdate> getKeysAndUpdates(
			String entityType, MatchingCondition... conditions)
	throws ProcessFailedException ,ServiceConfigurationException {
		List<KeyAndUpdate> ret = new ArrayList<>();
		Class<?> entityClass = getEntityClass(entityType);
		if(entityClass == null) throw new ProcessFailedException("unknown entity type: " + entityType);
		try{
			for(Pair<Object, Calendar> entry : 
				getDaoContext().listAllIdAndUpdates(
						entityClass,
						getConditions(conditions))){
				ret.add(new KeyAndUpdate(entry.getFirst(), entry.getSecond()));
			}
			return ret;
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			throw new ProcessFailedException(e);
		}
	}

	@Override
	@TransactionMethod
	public void setEntity(String entityType, Object entity)
	throws ProcessFailedException, ServiceConfigurationException {
		if(!entityType.equals("User")){
			throw new ProcessFailedException("setEntity only supports User.");
		}
		String sgid = RIProcessor.getCurrentServiceContext()
				.getRequestMimeHeaders()
				.getHeader(LangridConstants.HTTPHEADER_FEDERATEDCALL_SOURCEGRIDID)[0];
		JSON j = new JSON();
		User u = j.parse(j.format(entity), User.class);
		if(!u.getGridId().equals(sgid)){
			throw new ProcessFailedException("only set request from source grid is accepted.");
		}
		try{
			Object id = EntityUtil.getId(u);
			DaoContext dc = getDaoContext();
			dc.beginTransaction();
			try{
				User existing = dc.loadEntity(User.class, id);
				if(existing != null){
					dc.mergeEntity(u);
				} else{
					dc.saveEntity(u);
				}
				dc.commitTransaction();
			} finally{
			}
		} catch (Exception e) {
			throw new ProcessFailedException(e);
		}
	}

	private Class<?> getEntityClass(String entityType){
		switch(entityType){
			case "Grid": return Grid.class;
			case "Federation": return Federation.class;
			case "News": return News.class;
			case "Node": return Grid.class;
			case "User": return User.class;
			case "Domain": return Domain.class;
			case "ResourceType": return ResourceType.class;
			case "ResourceMetaAttribute": return ResourceMetaAttribute.class;
			case "ServiceType": return ServiceType.class;
			case "ServiceMetaAttribute": return ServiceMetaAttribute.class;
			case "Protocol": return Protocol.class;
			case "Resource": return Resource.class;
			case "Service": return Service.class;
			case "ExternalService": return ExternalService.class;
			case "BPELService": return BPELService.class;
			case "AccessRight": return AccessRight.class;
			case "AccessStat": return AccessStat.class;
			case "AccessLimit": return AccessLimit.class;
			default: return null;
		}
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
