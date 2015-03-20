package jp.go.nict.langrid.management.web.model.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.ServiceNotFoundException;
import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.dao.entity.Invocation;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceContainerType;
import jp.go.nict.langrid.dao.util.BPRBPELServiceInstanceReader;
import jp.go.nict.langrid.management.logic.InvocationLogic;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.logic.ServiceLogicException;
import jp.go.nict.langrid.management.web.model.CompositeServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.CompositeServiceService;
import jp.go.nict.langrid.management.web.model.service.ServiceModelUtil;
import jp.go.nict.langrid.management.web.utility.FileUtil;
import jp.go.nict.langrid.management.web.utility.JarUtil;

public class CompositeServiceServiceImpl
extends LangridServiceServiceImpl<CompositeServiceModel>
implements CompositeServiceService
{
	public CompositeServiceServiceImpl() throws ServiceManagerException {
	}

	@Override
	public final void add(CompositeServiceModel obj) throws ServiceManagerException {
		obj.setContainerType(ServiceContainerType.COMPOSITE.name());
		super.add(obj);
	}

	@Override
	protected CompositeServiceModel createNewObject() {
		return new CompositeServiceModel();
	}

	@Override
	public void deleteById(String serviceId) throws ServiceManagerException {
		deactivate(serviceId);
		super.deleteById(serviceId);
	}

	public Invocation[] getInvocations(String serviceId) throws ServiceManagerException {
	   try {
         return getLogic().getExternalInvocations(serviceGridId, serviceId);
      } catch(ServiceNotFoundException e) {
         throw new ServiceManagerException(e, this.getClass());
		} catch(ServiceLogicException e) {
			throw new ServiceManagerException(e, this.getClass());
      } catch(DaoException e) {
         throw new ServiceManagerException(e, this.getClass());
      }
	}

	public int getTotalCount(MatchingCondition[] conditions, Scope scope)
	throws ServiceManagerException {
	   try {
         ArrayUtil.append(conditions
            , new MatchingCondition(
               "instanceType", InstanceType.BPEL.name(), MatchingMethod.COMPLETE));
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
	public byte[] getBpel(String serviceId) throws ServiceManagerException {
		try {
			InputStream is = new BPRBPELServiceInstanceReader(new ByteArrayInputStream(get(serviceId).getInstance())).getBpel();
			try{
				return StreamUtil.readAsBytes(is);
			} finally {
				is.close();
			}
		} catch (IOException e) {
			throw new ServiceManagerException(e);
//		} catch(SQLException e) {
//		   throw new ServiceManagerException(e);
      }
	}

	@Override
	public List<String> getWsdlNames(String serviceId) throws ServiceManagerException {
	   CompositeServiceModel model = get(serviceId);
	   try {
		   JarInputStream jis = new JarInputStream(new ByteArrayInputStream(model.getInstance()));
		   List<String> list = new ArrayList<String>();
		   JarEntry je;
		   while((je = jis.getNextJarEntry()) != null){
			   list.add(je.getName());
		   }
		   return list;
	   } catch (IOException e) {
	      throw new ServiceManagerException(e);
//	   } catch(SQLException e) {
//	      throw new ServiceManagerException(e);
      }
	}

	@Override
	public File getDefinisionFileListZip(String serviceId) throws ServiceManagerException {
	   try {

	   	   CompositeServiceModel model = get(serviceId);
	   	   File orig = FileUtil.saveToTemp(new ByteArrayInputStream(model.getInstance()), serviceId + ".jar");
         return JarUtil.convertJarToZip(orig, serviceId + ".zip");
	   } catch(IOException e) {
	      throw new ServiceManagerException(e);
//	   } catch(SQLException e) {
//	      throw new ServiceManagerException(e);
	   }
	}

	@Override
	public void deleteInvocation(String serviceId, List<Invocation> list)
	throws ServiceManagerException {
		try {
			InvocationLogic logic = new InvocationLogic();
			for(Invocation i : list){
				logic.deleteInvocation(serviceGridId, serviceId, i.getInvocationName());
			}
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	protected CompositeServiceModel makeServiceModel(Service service)
	throws ServiceManagerException
	{
		if(service.getInstanceType().equals(InstanceType.Java)){
			return ServiceModelUtil.makeJavaCompositeServiceModel(service);
		}else{
			return ServiceModelUtil.makeCompositeServiceModel(service);
		}
	}

	@Override
	protected Service setProperty(CompositeServiceModel model, Service service)
	throws ServiceManagerException
	{
		if(model.getInstanceType().equals(InstanceType.Java)){
			return ServiceModelUtil.setJavaCompositeServiceProperty(model, service);
		}else{
			return ServiceModelUtil.setCompositeServiceProperty(model, service);
		}
	}

	private static final long serialVersionUID = 1L;
}
