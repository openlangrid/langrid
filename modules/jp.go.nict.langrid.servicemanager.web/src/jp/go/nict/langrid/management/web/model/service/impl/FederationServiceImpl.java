package jp.go.nict.langrid.management.web.model.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.FederationNotFoundException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.Federation;
import jp.go.nict.langrid.management.logic.FederationLogic;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.FederationModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.FederationService;
import jp.go.nict.langrid.management.web.model.service.LangridList;

public class FederationServiceImpl implements FederationService {
	public FederationServiceImpl() throws DaoException {
		logic = new FederationLogic();
	}

	@Override
	public void add(FederationModel obj) throws ServiceManagerException {
		try {
			new FederationLogic().addFederation(
				obj.getSourceGridId(), obj.getSourceGridName(),
				null, null,
				obj.getTargetGridId(), obj.getTargetGridName(),
				obj.getTargetGridUserId(), obj.getTargetGridUserOrganization(),
				obj.getTargetGridUserHomepage(),
				obj.getTargetGridAccessToken(),
				obj.isRequesting(), obj.isConnected(), false, false);
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public void delete(FederationModel condition)
	throws ServiceManagerException
	{
		deleteById(condition.getSourceGridId(), condition.getTargetGridId());
	}

	@Override
	public void deleteById(String sourceGridId, String targetGridId)
	throws ServiceManagerException
	{
		try {
			logic.deleteFederation(sourceGridId, targetGridId);
		} catch(FederationNotFoundException e) {
			throw new ServiceManagerException(e);
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public void edit(final FederationModel obj) throws ServiceManagerException {
		// noop
	}

	@Override
	public FederationModel get(String sourceGridId, String targetGridId)
	throws ServiceManagerException
	{
		try {
			for(Federation f : logic.listAllFederations()) {
				if(f.getSourceGridId().equals(sourceGridId)
					&& f.getTargetGridId().equals(targetGridId)) {
					return makeModel(f);
				}
			}
			return null;
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public void setRequesting(
		String sourceGridId, String targetGridId, boolean isRequesting)
	throws ServiceManagerException
	{
		try {
			logic.setRequesting(sourceGridId, targetGridId, isRequesting);
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public FederationModel get(String id) throws ServiceManagerException {
		return null;
	}

	@Override
	public LangridList<FederationModel> getList(int index, int count,
		MatchingCondition[] conditions, Order[] orders, Scope scope)
	throws ServiceManagerException
	{
		try {
			LangridList<FederationModel> list = new LangridList<FederationModel>();
			List<Federation> result = logic.listAllFederations();
			for(Federation f : result) {
				list.add(makeModel(f));
			}
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public LangridList<String> getConnectedSourceGridIdList(String gridId, Order order)
	throws ServiceManagerException
	{
		try {
			LangridList<String> list = new LangridList<String>();
			List<Federation> result = logic.listAllFederations();
			sortByStringField(result, order);
			for(Federation f : result) {
				if(gridId.equals(f.getTargetGridId()) && !f.isRequesting()
					&& f.isConnected()) {
					list.add(f.getSourceGridId());
				}
			}
			list.setTotalCount(result.size());
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}
	
	private void sortByStringField(List<Federation> list, final Order order) {
		if(order == null) {
			return;
		}
		
		Collections.sort(list, new Comparator<Federation>() {
			public int compare(Federation o1, Federation o2) {
				try {
					Field f1 = o1.getClass().getDeclaredField(order.getFieldName());
					String value = (String)f1.get(o1);
					
					Field f2 = o2.getClass().getDeclaredField(order.getFieldName());
					String value2 = (String)f2.get(o2);
					
					return value.compareTo(value2);
				} catch(NoSuchFieldException e) {
				} catch(SecurityException e) {
				} catch(IllegalArgumentException e) {
				} catch(IllegalAccessException e) {
				}
				return 0;
			};
		});
	}

	public LangridList<FederationModel> getAllRelatedSourceGridList(
		String gridId, int first, int count)
	throws ServiceManagerException
	{
		try {
			List<FederationModel> list = new ArrayList<FederationModel>();
			LangridList<FederationModel> subList = new LangridList<FederationModel>();
			List<Federation> result = logic.listAllFederations();
			for(Federation f : result) {
				if(gridId.equals(f.getTargetGridId())
					&& (f.isConnected() || f.isRequesting())) {
					list.add(makeModel(f));
				}
			}
			if(first + count < list.size()) {
				list = list.subList(first, first + count);
			}
			for(FederationModel fm : list) {
				subList.add(fm);
			}
			return subList;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public int getAllRelatedSourceGridListTotalCount(String gridId)
	throws ServiceManagerException
	{
		List<FederationModel> list = new ArrayList<FederationModel>();
		try {
			List<Federation> result = logic.listAllFederations();
			for(Federation f : result) {
				if(gridId.equals(f.getTargetGridId())
					&& (f.isConnected() || f.isRequesting())) {
					list.add(makeModel(f));
				}
			}
			return list.size();
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public LangridList<String> getReachableTargetGridIdListFrom(String sourceGridId)
	throws ServiceManagerException{
		try {
			LangridList<String> list = new LangridList<String>();
			Collection<String> ids = logic.listAllReachableGridIdsFrom(sourceGridId);
			list.addAll(ids);
			list.setTotalCount(ids.size());
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public LangridList<String> getReachableTargetGridIdListTo(String targetGridId)
	throws ServiceManagerException{
		try {
			LangridList<String> list = new LangridList<String>();
			Collection<String> ids = logic.listAllReachableGridIdsTo(targetGridId);
			list.addAll(ids);
			list.setTotalCount(ids.size());
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public LangridList<String> getConnectedTargetGridIdList(String gridId, Order order)
	throws ServiceManagerException{
		try {
			LangridList<String> list = new LangridList<String>();
			List<Federation> result = logic.listAllFederations();
			sortByStringField(result, order);
			for(Federation f : result) {
				if(gridId.equals(f.getSourceGridId()) && !f.isRequesting()
					&& f.isConnected()) {
					list.add(f.getTargetGridId());
				}
			}
			list.setTotalCount(result.size());
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public LangridList<FederationModel> getAllRelatedTargetGridList(
		String gridId, int first, int count)
	throws ServiceManagerException
	{
		try {
			List<FederationModel> list = new ArrayList<FederationModel>();
			LangridList<FederationModel> subList = new LangridList<FederationModel>();
			List<Federation> result = logic.listAllFederations();
			for(Federation f : result) {
				if(gridId.equals(f.getSourceGridId())
					&& (f.isConnected() || f.isRequesting()))
				{
					list.add(makeModel(f));
				}
			}
			if(first + count < list.size()) {
				list = list.subList(first, first + count);
			}
			for(FederationModel fm : list) {
				subList.add(fm);
			}
			return subList;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public int getAllRelatedTargetGridListTotalCount(String gridId)
	throws ServiceManagerException
	{
		List<Federation> list = new ArrayList<Federation>();
		try {
			List<Federation> result = logic.listAllFederations();
			for(Federation f : result) {
				if(gridId.equals(f.getSourceGridId())
					&& (f.isConnected() || f.isRequesting()))
				{
					list.add(f);
				}
			}
			return list.size();
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void setConnected(
			String sourceGridId, String targetGridId, boolean isConnected)
		throws ServiceManagerException
		{
			try {
				logic.setConnection(sourceGridId, targetGridId, isConnected);
			} catch(DaoException e) {
				throw new ServiceManagerException(e, this.getClass());
			}
		}

	public void setRelations(
			String sourceGridId, String targetGridId, boolean symmetric, boolean transitive)
	throws ServiceManagerException{
		try {
			logic.update(sourceGridId, targetGridId, f -> {
				f.setSymmetric(symmetric);
				f.setTransitive(transitive);
			});
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public int getTotalCount(MatchingCondition[] conditions, Scope scope)
	throws ServiceManagerException
	{
		try {
			return logic.listAllFederations().size();
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public void setScopeParameter(
		String serviceGridId, String userGridId, String userId)
	{
		this.serviceGridId = serviceGridId;
		this.userGridId = userGridId;
		this.userId = userId;
	}
	
	@Override
	public boolean isExist(String dataId) throws ServiceManagerException {
		return get(dataId) != null;
	}

	private String serviceGridId;
	private String userGridId;
	private String userId;

	private FederationModel makeModel(Federation entity) {
		FederationModel model = new FederationModel();
		model.setRequesting(entity.isRequesting());
		model.setTargetGridAccessToken(entity.getTargetGridAccessToken());
		model.setTargetGridUserId(entity.getTargetGridUserId());
		model.setTargetGridId(entity.getTargetGridId());
		model.setSourceGridId(entity.getSourceGridId());
		model.setSourceGridName(entity.getSourceGridName());
		model.setTargetGridName(entity.getTargetGridName());
		model.setTargetGridUserHomepage(entity.getTargetGridHomepageUrl());
		model.setTargetGridUserOrganization(entity.getTargetGridOrganization());
		model.setConnected(entity.isConnected());
		return model;
	}

	private FederationLogic logic;
	private static final long serialVersionUID = 1L;
}
