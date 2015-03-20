package jp.go.nict.langrid.management.web.model.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.ServiceSearchResult;
import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceContainerType;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.logic.ServiceLogicException;
import jp.go.nict.langrid.management.web.model.AtomicServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.AtomicServiceService;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ServiceModelUtil;

public class AtomicServiceServiceImpl extends LangridServiceServiceImpl<AtomicServiceModel>
implements AtomicServiceService
{
	public AtomicServiceServiceImpl() throws ServiceManagerException {
	}
	
	@Override
	public void add(AtomicServiceModel obj) throws ServiceManagerException {
		obj.setContainerType(ServiceContainerType.ATOMIC.name());
		super.add(obj);
	}

	public List<String> getRelatedResourceIdList(String serviceId) {
		return new ArrayList<String>() {{
			add("testResource"); add("testResource2");
		}};
	}

	public List<AtomicServiceModel> getListByRelatedId(String resourceId)
	throws ServiceManagerException
	{
		MatchingCondition[] conditions = new MatchingCondition[]{
				new MatchingCondition("resourceId", resourceId, MatchingMethod.COMPLETE)
		};
		try {
			List<AtomicServiceModel> list = new LangridList<AtomicServiceModel>();
			ServiceDao sd = DaoFactory.createInstance().createServiceDao();
			while(true) {
				ServiceSearchResult result = getLogic().searchServices(
						0, 30, userGridId, userId, serviceGridId, false
						, conditions, new Order[]{}, Scope.ALL);
				for(Service s : result.getElements()) {
					if(s.getContainerType() == null){
						s.setContainerType(ServiceContainerType.ATOMIC);
					}
					AtomicServiceModel asm = ServiceModelUtil.makeAtomicServiceModel(s);
					list.add(asm);
					InputStream is = null;
					try {
						is = sd.getServiceInstance(serviceGridId, s.getServiceId());
						if(is != null){
							byte[] body = StreamUtil.readAsBytes(is);
							asm.setInstance(body);
						}
					} catch(IOException e) {
						throw new ServiceManagerException(e, this.getClass());
					} finally {
						if(is != null){
							is.close();
						}
					}
				}
				if(result.getTotalCount() < 30){
					break;
				}
			}
			return list;
		} catch(ServiceLogicException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(IOException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public int getTotalCount(MatchingCondition[] conditions, Scope scope)
	throws ServiceManagerException {
	   try {
	      ArrayUtil.append(conditions
	         , new MatchingCondition(
	            "instanceType", InstanceType.EXTERNAL.name(), MatchingMethod.COMPLETE));
         return getLogic().searchServices(0, 1, userGridId, userId, serviceGridId, false
        		 , conditions, new Order[]{}
            , scope).getTotalCount();
		} catch(ServiceLogicException e) {
			throw new ServiceManagerException(e, this.getClass());
      } catch(DaoException e) {
         throw new ServiceManagerException(e, this.getClass());
      }
	}

	@Override
	protected AtomicServiceModel createNewObject() {
		return new AtomicServiceModel();
	}
	
	@Override
	protected AtomicServiceModel makeServiceModel(Service service)
	throws ServiceManagerException {
	   return ServiceModelUtil.makeAtomicServiceModel(service);
	}
	
	@Override
	protected Service setProperty(AtomicServiceModel model, Service service)
	throws ServiceManagerException
	{
	   return ServiceModelUtil.setAtomicServiceProperty(model, service);
	}

   private static final long serialVersionUID = 1L;
}
