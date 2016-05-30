/*
 * $Id:ServiceManagement.java 5259 2007-09-06 10:10:27Z nakaguchi $
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
package jp.go.nict.langrid.foundation.servicemanagement;

import static jp.go.nict.langrid.foundation.util.validation.AccessRightValidationPolicy.SERVICEOWNER_OR_ADMIN;

import java.util.Iterator;

import jp.go.nict.langrid.commons.validator.annotation.NotEmpty;
import jp.go.nict.langrid.commons.validator.annotation.ValidatedMethod;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.foundation.AbstractLangridService;
import jp.go.nict.langrid.foundation.annotation.AccessRightValidatedMethod;
import jp.go.nict.langrid.foundation.annotation.TransactionMethod;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidNodeId;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidServiceId;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.foundation.NodeNotFoundException;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceDeployment;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceDeploymentAlreadyExistsException;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceDeploymentManagementService;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceDeploymentNotFoundException;
import jp.go.nict.langrid.service_1_2.util.ExceptionConverter;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:5259 $
 */
public class ServiceDeploymentManagement
extends AbstractLangridService
implements ServiceDeploymentManagementService
{
	/**
	 * 
	 * 
	 */
	public ServiceDeploymentManagement(){}

	/**
	 * 
	 * 
	 */
	public ServiceDeploymentManagement(ServiceContext serviceContext){
		super(serviceContext);
	}

	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@ValidatedMethod
	@TransactionMethod
	public ServiceDeployment[] listServiceDeployments(
			@NotEmpty @ValidServiceId String serviceId)
	throws AccessLimitExceededException, InvalidParameterException,
	NoAccessPermissionException, ServiceConfigurationException,
	ServiceNotFoundException, UnknownException
	{
		try{
			ServiceDao dao = getServiceDao();
			Service s = dao.getService(getGridId(), serviceId);
			return convert(
					s.getServiceDeployments().toArray(
							new jp.go.nict.langrid.dao.entity.ServiceDeployment[]{})
					, ServiceDeployment[].class
					);
		} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
			throw new ServiceNotFoundException(e.getServiceId());
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@ValidatedMethod
	@TransactionMethod
	public void addServiceDeployment(
			@NotEmpty @ValidServiceId String serviceId,
			@NotEmpty @ValidNodeId String nodeId,
			@NotEmpty String servicePath)
	throws AccessLimitExceededException,
	InvalidParameterException, NoAccessPermissionException,
	NodeNotFoundException, ServiceConfigurationException,
	ServiceDeploymentAlreadyExistsException, ServiceNotFoundException,
	UnknownException
	{
		try{
			ServiceDao dao = getServiceDao();
			Service s = dao.getService(getGridId(), serviceId);
			s.getServiceDeployments().add(new jp.go.nict.langrid.dao.entity.ServiceDeployment(
					s.getGridId(), s.getServiceId(), nodeId, servicePath, true
					));
		} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
			throw new ServiceNotFoundException(e.getServiceId());
		} catch(DaoException e){
			throw convertException(e);
		}
	}

	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@ValidatedMethod
	@TransactionMethod
	public void deleteServiceDeployment(
			@NotEmpty @ValidServiceId String serviceId,
			@NotEmpty @ValidNodeId String nodeId)
	throws AccessLimitExceededException,
			InvalidParameterException, NoAccessPermissionException,
			NodeNotFoundException, ServiceConfigurationException,
			ServiceDeploymentNotFoundException, ServiceNotFoundException,
			UnknownException {
		try{
			ServiceDao sdao = getServiceDao();
			Service service = sdao.getService(getGridId(), serviceId);
			Iterator<jp.go.nict.langrid.dao.entity.ServiceDeployment> it =
				service.getServiceDeployments().iterator();
			boolean removed = false;
			while(it.hasNext()){
				jp.go.nict.langrid.dao.entity.ServiceDeployment sd = it.next();
				if(!sd.getServiceId().equals(serviceId)) continue;
				if(!sd.getNodeId().equals(nodeId)) continue;
				it.remove();
				removed = true;
				break;
			}
			if(!removed){
				throw new ServiceDeploymentNotFoundException(
						serviceId, nodeId, "unknown service deployment"
						);
			}
		} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
			throw convertException(e);
		} catch(jp.go.nict.langrid.dao.NodeNotFoundException e){
			throw convertException(e);
		} catch(ServiceDeploymentNotFoundException e){
			throw e;
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@ValidatedMethod
	@TransactionMethod
	public void enableServiceDeployment(
			@NotEmpty @ValidServiceId String serviceId,
			@NotEmpty @ValidNodeId String nodeId
			)
	throws AccessLimitExceededException,
	InvalidParameterException, NoAccessPermissionException,
	NodeNotFoundException, ServiceConfigurationException,
	ServiceDeploymentNotFoundException, ServiceNotFoundException,
	UnknownException
	{
		try{
			jp.go.nict.langrid.dao.entity.ServiceDeployment sd =
				getServiceDeploymentDao().getServiceDeployment(
					getGridId(), serviceId, nodeId);
			if(!sd.isEnabled())
				sd.setEnabled(true);
		} catch(jp.go.nict.langrid.dao.ServiceDeploymentNotFoundException e){
			throw new ServiceDeploymentNotFoundException(
					serviceId, nodeId, e.toString()
					);
		} catch(DaoException e){
			throw convertException(e);
		}
	}

	@AccessRightValidatedMethod(policy=SERVICEOWNER_OR_ADMIN, argNames="serviceId")
	@ValidatedMethod
	@TransactionMethod
	public void disableServiceDeployment(
			@NotEmpty @ValidServiceId String serviceId,
			@NotEmpty @ValidNodeId String nodeId
			)
	throws AccessLimitExceededException,
	InvalidParameterException, NoAccessPermissionException,
	NodeNotFoundException, ServiceConfigurationException,
	ServiceDeploymentNotFoundException, ServiceNotFoundException,
	UnknownException
	{
		try{
			jp.go.nict.langrid.dao.entity.ServiceDeployment sd =
				getServiceDeploymentDao().getServiceDeployment(
					getGridId(), serviceId, nodeId);
			if(sd.isEnabled())
				sd.setEnabled(false);
		} catch(jp.go.nict.langrid.dao.ServiceDeploymentNotFoundException e){
			throw new ServiceDeploymentNotFoundException(
					serviceId, nodeId, e.toString()
					);
		} catch(DaoException e){
			throw convertException(e);
		}
	}
}
