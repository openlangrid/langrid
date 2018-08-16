/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
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
package jp.go.nict.langrid.management.logic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.SAXException;

import jp.go.nict.langrid.bpel.ProcessAnalysisException;
import jp.go.nict.langrid.bpel.ProcessAnalyzer;
import jp.go.nict.langrid.bpel.entity.EndpointReference;
import jp.go.nict.langrid.bpel.entity.PartnerLink;
import jp.go.nict.langrid.bpel.entity.ProcessInfo;
import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.jxpath.BPELUtil;
import jp.go.nict.langrid.commons.jxpath.WSDLUtil;
import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.commons.lang.block.BlockPR;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.Protocols;
import jp.go.nict.langrid.commons.ws.util.InvalidLangridUriException;
import jp.go.nict.langrid.commons.ws.util.LangridUriUtil;
import jp.go.nict.langrid.dao.AccessLogDao;
import jp.go.nict.langrid.dao.AccessLogSearchResult;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.NodeSearchResult;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.ServiceNotFoundException;
import jp.go.nict.langrid.dao.ServiceSearchResult;
import jp.go.nict.langrid.dao.ServiceTypeNotFoundException;
import jp.go.nict.langrid.dao.entity.AccessLog;
import jp.go.nict.langrid.dao.entity.BPELService;
import jp.go.nict.langrid.dao.entity.ExternalService;
import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.dao.entity.Invocation;
import jp.go.nict.langrid.dao.entity.Node;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceContainerType;
import jp.go.nict.langrid.dao.entity.ServiceDeployment;
import jp.go.nict.langrid.dao.entity.ServiceEndpoint;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.dao.util.BPELServiceInstanceReader;
import jp.go.nict.langrid.dao.util.BPRBPELServiceInstanceReader;
import jp.go.nict.langrid.dao.util.ExternalServiceInstanceReader;
import jp.go.nict.langrid.dao.util.LobUtil;
import jp.go.nict.langrid.management.logic.qos.AvailabilityCalculator;
import jp.go.nict.langrid.management.logic.qos.QoSCalculator;
import jp.go.nict.langrid.management.logic.qos.SuccessRateCalculator;
import jp.go.nict.langrid.management.logic.qos.ThroughputCalculator;
import jp.go.nict.langrid.management.logic.service.InvalidServiceInstanceException;
import jp.go.nict.langrid.management.logic.service.ProcessDeployer;
import jp.go.nict.langrid.management.logic.service.ServiceActivator;
import jp.go.nict.langrid.management.logic.service.wsdlgenerator.WSDLGenerator;
import jp.go.nict.langrid.servicesupervisor.frontend.processors.pre.AccessRightCheck;
import jp.go.nict.langrid.servicesupervisor.frontend.processors.pre.FederatedUseCheck;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class ServiceLogic
extends AbstractLogic{
	/**
	 * 
	 * 
	 */
	public ServiceLogic(String activeBpelServicesUrl)
	throws DaoException{
		this.activeBpelServicesUrl = activeBpelServicesUrl;
	}

	/**
	 * 
	 * 
	 */
	public ServiceLogic(
			String activeBpelServicesUrl
			, String activeBpelDeployUserId, String activeBpelDeployPassword
			, String activeBpelDeployBinding)
	throws DaoException{
		this.activeBpelServicesUrl = activeBpelServicesUrl;
		this.activeBpelDeployUserId = activeBpelDeployUserId;
		this.activeBpelDeployPassword = activeBpelDeployPassword;
		this.activeBpelDeployBinding = activeBpelDeployBinding;
	}

	@DaoTransaction
	public void clear() throws DaoException, ServiceLogicException{
		getServiceDeploymentDao().clear();
		getAccessRightDao().clear();
		getAccessLimitDao().clear();
		getAccessStateDao().clear();
		getAccessLogDao().clear();
		getServiceDao().clear();
	}

	@DaoTransaction
	public ServiceSearchResult searchServices(
			int startIndex , int maxCount
			, String userGridId, String userId
			, String serviceGridId, boolean acrossGrids
			, MatchingCondition[] conditions, Order[] orders
			, Scope scope
			) throws DaoException, ServiceLogicException
	{
		if(scope.equals(Scope.ALL)){
			return searchAllServices(
					startIndex, maxCount, serviceGridId, acrossGrids
					, conditions, orders);
		} else if(scope.equals(Scope.MINE)){
			return searchMyServices(
					startIndex, maxCount, userGridId, userId
					, conditions, orders);
		} else if(scope.equals(Scope.ACCESSIBLE)){
			return searchAccessibleServices(
					startIndex, maxCount, userGridId, userId
					, serviceGridId, acrossGrids, conditions, orders);
		}
		return new ServiceSearchResult(new Service[]{}, 0, true);
	}

	@DaoTransaction
	public ServiceSearchResult searchServicesOverGrids(
			int startIndex , int maxCount
			, String userGridId, String userId
			, MatchingCondition[] conditions, Order[] orders
			, Scope scope
			) throws DaoException, ServiceLogicException
	{
		if(scope.equals(Scope.ALL)){
			return searchAllServices(
					startIndex, maxCount
					, userGridId, true
					, conditions, orders);
		} else if(scope.equals(Scope.MINE)){
			return searchMyServices(
					startIndex, maxCount, userGridId, userId
					, conditions, orders);
		} else if(scope.equals(Scope.ACCESSIBLE)){
			return searchAccessibleServices(
					startIndex, maxCount, userGridId, userId
					, userGridId, true
					, conditions, orders);
		}
		return new ServiceSearchResult(new Service[]{}, 0, true);
	}

	@DaoTransaction
	public List<Service> listAllServices(String serviceGridId) throws DaoException, ServiceLogicException {
		return getServiceDao().listAllServices(serviceGridId);
	}

	@DaoTransaction
	public void addService(
			String ownerUserId, Service service, String coreNodeUrl, boolean allowAllAccess)
		throws DaoException, ServiceLogicException
	{
		String sgid = service.getGridId();
		String sid = service.getServiceId();
		try{
			if(service.getInstance() != null){
				validateInputInstanceInstance(
						sgid, sid, service.getInstanceType()
						, StreamUtil.readAsBytes(service.getInstance().getBinaryStream())
						);
			}

			if(service instanceof ExternalService){
				setUpInstanceAndAttributes(coreNodeUrl, (ExternalService)service);
			} else{
				setUpInstanceAndAttributes(coreNodeUrl, (BPELService)service);
			}
			service.setOwnerUserId(ownerUserId);
			getServiceDao().addService(service);

			// 
			// 
			getAccessRightDao().setServiceDefaultAccessRight(
					service.getGridId(), service.getServiceId()
					, allowAllAccess);

			// 
			// 
			ProcessDeployer deployer = new ProcessDeployer(
					activeBpelServicesUrl, activeBpelDeployUserId, activeBpelDeployPassword
					);
			ServiceActivator.activateParents(
					getServiceDao(), service.getGridId(), service.getServiceId()
					, deployer, activeBpelDeployBinding
					);
		} catch(URISyntaxException e){
			throw new ServiceLogicException(
					service.getGridId(), service.getServiceId()
					, ExceptionUtil.getMessageWithStackTrace(e));
		} catch(SAXException e){
			throw new ServiceLogicException(
					service.getGridId(), service.getServiceId()
					, ExceptionUtil.getMessageWithStackTrace(e));
		} catch(SQLException e){
			throw new ServiceLogicException(
					service.getGridId(), service.getServiceId()
					, ExceptionUtil.getMessageWithStackTrace(e));
		} catch(IOException e){
			throw new ServiceLogicException(
					service.getGridId(), service.getServiceId()
					, ExceptionUtil.getMessageWithStackTrace(e));
		} catch(InvalidServiceInstanceException e){
			throw new ServiceLogicException(
					service.getGridId(), service.getServiceId(),
					"Service instance isn't valid data.["
					+ e.toString() + "]");
		}
	}

	@DaoTransaction
	public void deleteService(String serviceGridId, String serviceId)
	throws DaoException, ServiceLogicException{
		Service service = getServiceDao().getService(serviceGridId, serviceId);
		if(service.isActive()){
			throw new ServiceNotInactiveException(
					serviceGridId, serviceId, "service is active");
		}

		getServiceDao().deleteService(serviceGridId, serviceId);

		// 
		// Deletes related logs, access privileges, and access restrictions"
		// 
		getAccessRightDao().deleteAccessRightsOfService(serviceGridId, serviceId);
		getAccessLimitDao().deleteAccessLimitsOfService(serviceGridId, serviceId);
		getAccessLogDao().deleteAccessLogsOfService(serviceGridId, serviceId);
		getAccessStateDao().deleteAccessStatOfService(serviceGridId, serviceId);
	}

	@DaoTransaction
	public void deleteServicesOfUser(String userGridId, String userId)
	throws ServiceLogicException, DaoException{
		for(Service s : getServiceDao().listServicesOfUser(userGridId, userId)){
			String sgid = s.getGridId();
			String sid = s.getServiceId();
			// 
			// Deletes related logs, access privileges, and access restrictions
			// 
			getAccessRightDao().deleteAccessRightsOfService(sgid, sid);
			getAccessLimitDao().deleteAccessLimitsOfService(sgid, sid);
			getAccessLogDao().deleteAccessLogsOfService(sgid, sid);
			getAccessStateDao().deleteAccessStatOfService(sgid, sid);
		}
		getServiceDao().deleteServicesOfUser(userGridId, userId);
	}

	@DaoTransaction
	public <T> T transactRead(String serviceGridId, String serviceId, BlockPR<Service, T> block)
	throws ServiceLogicException, DaoException{
		return block.execute(getServiceDao().getService(serviceGridId, serviceId));
	}

	@DaoTransaction
	public void transactUpdate(String serviceGridId, String serviceId, BlockP<Service> block)
	throws ServiceLogicException, DaoException{
		Service s = getServiceDao().getService(serviceGridId, serviceId);
		block.execute(s);
		s.touchUpdatedDateTime();
	}

	@DaoTransaction
	public byte[] getServiceWsdl(
			String coreNodeUrl, String serviceGridId, String serviceId
			)
	throws ServiceNotFoundException, DaoException, ServiceLogicException{
		ServiceDao dao = getServiceDao();
		Service s = dao.getService(serviceGridId, serviceId);
		try{
			if(!coreNodeUrl.endsWith("/")) coreNodeUrl += "/";
			String did = s.getServiceTypeDomainId();
			String tid = s.getServiceTypeId();
			URI targetNamespace = (did != null && tid != null)
					? LangridUriUtil.createServiceTypeUri(did, tid)
					: LangridUriUtil.createServiceUri(serviceGridId, serviceId);
			String serviceName = serviceGridId + ":" + serviceId;
			String endpointUrl = coreNodeUrl + "invoker/" + serviceGridId + ":" + serviceId;
			if(did != null && tid != null){
				byte[] base = StreamUtil.readAsBytes(getServiceTypeDao().getServiceType(did, tid)
						.getInterfaceDefinitions().get(Protocols.SOAP_RPCENCODED)
						.getDefinition().getBinaryStream());
//				System.out.println("wsdl for type[" + did + ":" + tid + "]");
				byte[] result = WSDLGenerator.generate(targetNamespace, serviceName, endpointUrl
						, new ByteArrayInputStream(base), true
						);
//				System.out.println("---- generated ----");
//				System.out.println(new String(result, "UTF-8"));
				return result;
			} else{
				byte[] base = StreamUtil.readAsBytes(dao.getServiceWsdl(serviceGridId, serviceId));
//				System.out.println("wsdl for service[" + serviceGridId + ":" + serviceId + "]");
				byte[] result = WSDLGenerator.generate(targetNamespace, serviceName, endpointUrl
						, new ByteArrayInputStream(base), false
						);
//				System.out.println("---- generated ----");
//				System.out.println(new String(result, "UTF-8"));
				return result;
			}
		} catch(SQLException e){
			throw new ServiceLogicException(serviceGridId, serviceId, e);
		} catch(IOException e){
			throw new ServiceLogicException(serviceGridId, serviceId, e);
		}
	}

	@DaoTransaction
	public byte[] getServiceTypeWsdl(
			String coreNodeUrl, String domainId, String serviceTypeId, String serviceGridId
			)
	throws ServiceTypeNotFoundException, DaoException, ServiceLogicException{
		String did = domainId;
		String tid = serviceTypeId;
		String serviceId = "Abstract" + tid;
		try{
			if(!coreNodeUrl.endsWith("/")) coreNodeUrl += "/";
			URI targetNamespace = LangridUriUtil.createServiceTypeUri(did, tid);
			String serviceName = serviceGridId + ":" + serviceId;
			String endpointUrl = coreNodeUrl + "invoker/" + serviceGridId + ":" + serviceId;
			byte[] base = StreamUtil.readAsBytes(getServiceTypeDao().getServiceType(did, tid)
					.getInterfaceDefinitions().get(Protocols.SOAP_RPCENCODED)
					.getDefinition().getBinaryStream());
			byte[] result = WSDLGenerator.generate(targetNamespace, serviceName, endpointUrl
					, new ByteArrayInputStream(base), true
					);
			return result;
		} catch(SQLException e){
			throw new ServiceLogicException(serviceGridId, serviceId, e);
		} catch(IOException e){
			throw new ServiceLogicException(serviceGridId, serviceId, e);
		}
	}

	@DaoTransaction
	public void activateService(
			String serviceGridId, String serviceId
			)
	throws ServiceNotActivatableException, ServiceLogicException, DaoException{
		ServiceDao dao = getServiceDao();
		InputStream instance = dao.getServiceInstance(serviceGridId, serviceId);
		Service service = dao.getService(serviceGridId, serviceId);

		if(service.isActive()) return;
		ProcessDeployer deployer = new ProcessDeployer(
				activeBpelServicesUrl
				, activeBpelDeployUserId
				, activeBpelDeployPassword
				);
		ServiceActivator.activate(
				dao, service, instance
				, deployer, activeBpelDeployBinding
				);
		service.setActive(true);
		service.touchUpdatedDateTime();
	}

	@DaoTransaction
	public void restartService(
			String serviceGridId, String serviceId
			)
	throws ServiceNotActivatableException, ServiceLogicException, DaoException{
		ServiceDao dao = getServiceDao();
		InputStream instance = dao.getServiceInstance(serviceGridId, serviceId);
		Service service = dao.getService(serviceGridId, serviceId);

		if(service.isActive()) return;
		ProcessDeployer deployer = new ProcessDeployer(
				activeBpelServicesUrl
				, activeBpelDeployUserId
				, activeBpelDeployPassword
				);
		ServiceActivator.activate(
				dao, service, instance
				, deployer, activeBpelDeployBinding
				);
		service.setActive(true);
		service.touchUpdatedDateTime();
	}

	@DaoTransaction
	public void deactivateService(
			String serviceGridId, String serviceId
			)
	throws ServiceLogicException, ServiceNotFoundException, DaoException{
		ServiceDao dao = getServiceDao();
		Service service = dao.getService(serviceGridId, serviceId);
		if(!service.isActive()) return;
		try{
			ProcessDeployer deployer = new ProcessDeployer(
					activeBpelServicesUrl
					, activeBpelDeployUserId
					, activeBpelDeployPassword
					);
			ServiceActivator.deactivate(service, deployer);
			service.setActive(false);
			service.touchUpdatedDateTime();
		} catch(ServiceNotDeactivatableException e){
			throw new ServiceLogicException(
					serviceGridId, serviceId, ExceptionUtil.getMessageWithStackTrace(e));
		}
	}

	@DaoTransaction
	public boolean isServiceActive(
			String serviceGridId, String serviceId
			)
	throws ServiceNotFoundException, DaoException, ServiceLogicException{
		ServiceDao dao = getServiceDao();
		return dao.getService(serviceGridId, serviceId).isActive();
	}

	@DaoTransaction
	public Invocation[] getExternalInvocations(
			String serviceGridId, String serviceId
			)
	throws ServiceNotFoundException, DaoException, ServiceLogicException{
		ServiceDao dao = getServiceDao();
		Service s = dao.getService(serviceGridId, serviceId);
		if(s instanceof BPELService){
			return ((BPELService)s).getInvocations().toArray(
					new Invocation[]{});
		} else{
			return new Invocation[]{};
		}
	}

	@DaoTransaction
	public boolean isServiceVisible(
			String serviceGridId, String serviceId
			)
	throws ServiceNotFoundException, DaoException, ServiceLogicException {
		ServiceDao dao = getServiceDao();
		return dao.getService(serviceGridId, serviceId).isVisible();
	}

	@DaoTransaction
	public void setServiceVisible(
			String serviceGridId, String serviceId, boolean visible)
	throws ServiceNotFoundException, DaoException, ServiceLogicException {
		ServiceDao dao = getServiceDao();
		Service s = dao.getService(serviceGridId, serviceId);
		s.setVisible(visible);
		s.touchUpdatedDateTime();
	}

	@DaoTransaction
	public QoSResult[][] getQoS(
			String[] qosTypes,
			String serviceGridId, String[] serviceId,
			Calendar startDateTime, Calendar endDateTime)
	throws DaoException, ServiceLogicException{
		Map<String, QoSCalculator[]> calcs = new LinkedHashMap<>();
		for(String s : serviceId) {
			calcs.put(s, ArrayUtil.collect(qosTypes, QoSCalculator.class, t -> {
				switch(t) {
				case "AVAILABILITY": return new AvailabilityCalculator();
				case "SUCCESS_RATE": return new SuccessRateCalculator();
				case "THROUGHPUT": return new ThroughputCalculator();
				}
				return null;
			}));
		}
		AccessLogDao aldao = getAccessLogDao();
		int start = 0;
		while(true) {
			AccessLogSearchResult r = aldao.searchAccessLog(start, 100, null,  null,
					serviceGridId, serviceId,
					startDateTime, endDateTime,
					new MatchingCondition[] {},
					new Order[] {});
			if(r.getElements() == null || r.getElements().length == 0) break;
			for(AccessLog al : r.getElements()) {
				for(QoSCalculator c : calcs.get(al.getServiceId())) c.addLog(al);
			}
			start += 100;
		}
		QoSResult[][] ret = new QoSResult[serviceId.length][qosTypes.length];
		for(int i = 0; i < serviceId.length; i++) {
			ret[i] = ArrayUtil.collect(calcs.get(serviceId[i]), QoSResult.class, c -> c.getResult());
		}
		return ret;
	}

	/**
	 * スループットを求める。成功した呼び出しの所要時間から，秒間何リクエストに相当するかを計算する。
	 */
	@DaoTransaction
	public QoSResult getThroughput(String serviceGridId, String serviceId,
			Calendar startDateTime, Calendar endDateTime)
					throws ServiceNotFoundException, DaoException, ServiceLogicException {
		AccessLogDao aldao = getAccessLogDao();
		int start = 0;
		QoSCalculator c = new ThroughputCalculator();
		while(true) {
			AccessLogSearchResult r = aldao.searchAccessLog(start, 100, null,  null, serviceGridId, new String[] {serviceId},
					startDateTime, endDateTime,
					new MatchingCondition[] {new MatchingCondition("responseCode", 200, MatchingMethod.COMPLETE)},
					new Order[] {});
			if(r.getElements() == null || r.getElements().length == 0) break;
			for(AccessLog al : r.getElements()) {
				c.addLog(al);
			}
			start += 100;
		}
		return c.getResult();
	}

	/**
	 * Success Rateを求める。正常呼び出し回数(アクセス制限違反やパラメータが不正などの使い方に問題が
	 * ある呼び出しは含めない)で成功回数を割った数値。
	 */
	@DaoTransaction
	public QoSResult getSuccessRate(String serviceGridId, String serviceId,
			Calendar startDateTime, Calendar endDateTime)
	throws ServiceNotFoundException, DaoException, ServiceLogicException {
		AccessLogDao aldao = getAccessLogDao();
		int start = 0;
		QoSCalculator c = new SuccessRateCalculator();
		while(true) {
			AccessLogSearchResult r = aldao.searchAccessLog(start, 100, null,  null,
					serviceGridId, new String[] {serviceId},
					startDateTime, endDateTime,
					new MatchingCondition[] {},
					new Order[] {});
			if(r.getElements() == null || r.getElements().length == 0) break;
			for(AccessLog al : r.getElements()) {
				c.addLog(al);
			}
			start += 100;
		}
		return c.getResult();
	}

	/**
	 * Availabilityを求める。全呼び出し回数で通常呼び出し回数(成功した呼び出しとアクセス制限違反やパラメータが
	 * 不正などの使い方に問題がある呼び出しを含む)を割った数値。
	 */
	@DaoTransaction
	public QoSResult getAvailability(String serviceGridId, String serviceId,
			Calendar startDateTime, Calendar endDateTime)
					throws ServiceNotFoundException, DaoException, ServiceLogicException {
		AccessLogDao aldao = getAccessLogDao();
		int start = 0;
		QoSCalculator c = new AvailabilityCalculator();
		while(true) {
			AccessLogSearchResult r = aldao.searchAccessLog(start, 100, null,  null,
					serviceGridId, new String[] {serviceId},
					startDateTime, endDateTime,
					new MatchingCondition[] {},
					new Order[] {});
			if(r.getElements() == null || r.getElements().length == 0) break;
			for(AccessLog al : r.getElements()) {
				c.addLog(al);
			}
			start += 100;
		}
		return c.getResult();
	}
/*  TODO 
	@DaoTransaction
	public QoSResult getReputation(String serviceGridId, String serviceId,
			Calendar startDateTime, Calendar endDateTime)
					throws ServiceNotFoundException, DaoException, ServiceLogicException {
		AccessLogDao aldao = getAccessLogDao();
		int total = 0;
		int start = 0;
		int available = 0;
		while(true) {
			AccessLogSearchResult r = aldao.searchAccessLog(start, 100, null,  null,
					serviceGridId, new String[] {serviceId},
					startDateTime, endDateTime,
					new MatchingCondition[] {},
					new Order[] {});
			if(r.getElements() == null || r.getElements().length == 0) break;
			for(AccessLog al : r.getElements()) {
				if(al.getResponseCode() == 200 ||
						al.getFaultString().contains("InvalidParameterException") ||
						al.getFaultString().contains("UnsupportLanguageException") ||
						al.getFaultString().contains("UnsupportLanguagePairException") ||
						al.getFaultString().contains("AccessLimitExceededException")) {
					available++;
				}
				total++;
			}
			start += 100;
		}
		System.out.println("total: " + total);
		return new QoSResult(QoSType.REPUTATION, total, 1.0 * available / total);
	}
*/

	private ServiceSearchResult searchAllServices(
			int startIndex, int maxCount
			, String serviceGridId, boolean acrossGrids
			, MatchingCondition[] conditions, Order[] orders
			)
		throws DaoException, ServiceLogicException
	{
		return getServiceDao().searchServices(
					startIndex, maxCount, serviceGridId, acrossGrids
					, conditions, orders
					);
	}

	private ServiceSearchResult searchMyServices(
			int startIndex, int maxCount
			, String serviceAndUserGridId, String userId
			, MatchingCondition[] conditions, Order[] orders
			)
		throws DaoException, ServiceLogicException
	{
		conditions = ArrayUtil.append(
				conditions
				, new MatchingCondition("gridId", serviceAndUserGridId)
				, new MatchingCondition("ownerUserId", userId)
				);
		return getServiceDao().searchServices(
				startIndex, maxCount, serviceAndUserGridId, false, conditions, orders
				);
	}

	private ServiceSearchResult searchAccessibleServices(
			int startIndex, int maxCount
			, String userGridId, String userId
			, String serviceGridId, boolean acrossGrids
			, MatchingCondition[] conditions, Order[] orders
			)
		throws DaoException, ServiceLogicException
	{
		User user = getUserDao().getUser(userGridId, userId);
		if(!user.isAbleToCallServices()){
			return new ServiceSearchResult(new Service[]{}, 0, true);
		}

		int totalCount = 0;
		boolean totalCountFixed = false;
		int start = 0;
		List<Service> entries = new ArrayList<Service>();
		while(entries.size() < maxCount){
			ServiceSearchResult result = getServiceDao().searchServices(
					start, 30, serviceGridId, acrossGrids, conditions, orders
					);
			for(Service s : result.getElements()){
				if(!s.getOwnerUserId().equals(userId)){
					if(!FederatedUseCheck.isInvocable(
							userGridId, serviceGridId, s.isFederatedUseAllowed())){
						continue;
					}
					if(!AccessRightCheck.isAccessible(
							getAccessRightDao()
							, userGridId, userId
							, s.getGridId(), s.getServiceId()))
						continue;
				}
				if((startIndex <= totalCount) && (entries.size() < maxCount)){
					entries.add(s);
				}
				totalCount++;
			}
			start += 30;
			if(result.isTotalCountFixed() && result.getTotalCount() < start){
				totalCountFixed = true;
				break;
			}
		}
		return new ServiceSearchResult(
				entries.toArray(new Service[]{})
				, totalCount, totalCountFixed
				);
	}

	private static void validateInputInstanceInstance(
			String serviceGridId, String serviceId
			, InstanceType instanceType, byte[] instance)
	throws IOException, SAXException, ServiceLogicException, URISyntaxException{
		if(instance == null) throw new ServiceLogicException(
				serviceGridId, serviceId, "instance is null."
				);
		if(instance.length == 0) return;
		ByteArrayInputStream is = new ByteArrayInputStream(instance);
		switch(instanceType){
			case EXTERNAL:{
				ExternalServiceInstanceReader r = new ExternalServiceInstanceReader(is);
				if(WSDLUtil.getTargetNamespace(r.getWsdl()) == null){
					throw new ServiceLogicException(
							serviceGridId, serviceId
							, "can't find targetNamespace of WSDL");
				}
				break;
			}
			case BPEL:
				BPELServiceInstanceReader r = new BPRBPELServiceInstanceReader(is);
				byte[] b = null;
				InputStream bis = r.getBpel();
				try{
					b = StreamUtil.readAsBytes(bis);
				} finally{
					bis.close();
				}
				if(BPELUtil.getTargetNamespace(new ByteArrayInputStream(b)) == null
						&& BPELUtil.getWSBPEL_2_0_TargetNamespace(new ByteArrayInputStream(b)) == null)
				{
					throw new ServiceLogicException(serviceGridId, serviceId
							, "can't find targetNamespace of BPEL");
				}
				for(int i = 0; i < r.getWsdlCount(); i++){
					if(WSDLUtil.getTargetNamespace(r.getWsdl(i)) == null){
						throw new ServiceLogicException(serviceGridId, serviceId
								, "can't find targetNamespace of WSDL[" + i + "]");
					}
				}
		}
	}

	private void setUpInstanceAndAttributes(String coreNodeUrl, ExternalService service)
	throws IOException, MalformedURLException
		, SAXException, URISyntaxException, DaoException
	{
		// wsdl
		try{
			Blob instance = service.getInstance();
			if(instance != null){
				InputStream is = new ExternalServiceInstanceReader(
						instance.getBinaryStream()
						).getWsdl();
				service.setWsdl(LobUtil.createBlob(is));
			}
		} catch(SQLException e){
			throw new DaoException(e);
		}

		// actual endpoint
		List<ServiceEndpoint> endpoints = service.getServiceEndpoints();
		if(endpoints.size() == 0){
			try{
				Blob instance =service.getWsdl();
				ServiceEndpoint ep = getEndpoint(
						service.getGridId(), service.getServiceId()
						, instance != null ? instance.getBinaryStream() : null
						);
				if(ep != null){
					service.getServiceEndpoints().add(ep);
				}
			} catch(SQLException e){
				throw new DaoException(e);
			}
		}

		// service deployment
		for(ServiceEndpoint ep : endpoints){
			String host = ep.getUrl().getHost();
			try{
				NodeSearchResult r = getNodeDao().searchNodes(0, Integer.MAX_VALUE,
						service.getGridId(), new MatchingCondition[]{
						new MatchingCondition("url", host, MatchingMethod.PARTIAL)
						}, new Order[]{});
				String urlString = ep.getUrl().toExternalForm();
				String currentNodeId = null;
				String currentNodeUrl = "";
				for(Node n : r.getElements()){
					String nodeUrl = n.getUrl().toString();
					if(urlString.startsWith(nodeUrl)){
						if(nodeUrl.length() > currentNodeUrl.length()){
							currentNodeId = n.getNodeId();
							currentNodeUrl = nodeUrl;
						}
					}
				}
				if(currentNodeId != null){
					String path = urlString.substring(currentNodeUrl.length());
					if(path.charAt(0) == '/'){
						path = path.substring(1);
					}
					ServiceDeployment d = new ServiceDeployment(
							service.getGridId(), service.getServiceId(), currentNodeId
							, path, true);
					service.getServiceDeployments().add(d);
				}
			} catch(DaoException e){
				logger.log(Level.SEVERE, "DAO Exception.", e);
			}
		}
	}

	private void setUpInstanceAndAttributes(
			String coreNodeUrl, BPELService service)
		throws IOException, ServiceLogicException
		, SAXException, URISyntaxException, DaoException
		, InvalidServiceInstanceException
	{
		service.setContainerType(ServiceContainerType.COMPOSITE);

		InputStream is = null;
		try{
			is = service.getInstance().getBinaryStream();
		} catch(SQLException e){
			throw new DaoException(e);
		}
		if(is != null){
			BPELServiceInstanceReader r = new BPRBPELServiceInstanceReader(is);
			ProcessInfo pi = null;
			try{
				pi = ProcessAnalyzer.analyze(r);
			} catch(ProcessAnalysisException e){
				throw new InvalidServiceInstanceException(e);
			}
			String serviceId = service.getServiceId();
			if(!serviceId.equals(pi.getBpel().getProcessName())){
				throw new InvalidServiceInstanceException(
						"processName:" + pi.getBpel().getProcessName()
						+ " isn't equals to serviceId:" + serviceId);
			}
			boolean found = false;
			URI bpelTns = pi.getBpel().getTargetNamespace();
			for(PartnerLink p : pi.getBpel().getPartnerLinks()){
				if(p.getMyRole() != null){
					// check if the serviceId and */service/@name are same
					if(!serviceId.equals(p.getService())){
						throw new InvalidServiceInstanceException(
							"service name:" + p.getService()
							+ " of process isn't equals to serviceId:"
							+ serviceId);
					}
					URI wsdlTns = new URI(
							p.getPartnerLinkType().getNamespaceURI()
							);
					if(!bpelTns.equals(wsdlTns)){
						logger.warning(
								"targetNamespace mismatch. bpel:"
								+ bpelTns + " wsdl:" + wsdlTns
								);
					}
					found = true;
					break;
				}
			}
			if(!found){
				throw new InvalidServiceInstanceException(
					"service of process isn't found");
			}

			// 
			// 
			service.getPartnerServiceNamespaceURIs().clear();
			for(PartnerLink pl : pi.getBpel().getPartnerLinks()){
				EndpointReference ref = pl.getEndpointReference();
				if(ref == null) continue;
				try{
					URI uri = new URI(ref.getServiceName().getNamespaceURI());
					service.getPartnerServiceNamespaceURIs().add(uri.toString());
					Invocation i = new Invocation();
					i.setOwnerServiceGridId(service.getGridId());
					i.setOwnerServiceId(service.getServiceId());
					i.setInvocationName(pl.getName());
					boolean informationset = false;
					try{
						Pair<String, String> ids = LangridUriUtil.extractServiceIds(uri);
						Service s = getServiceDao().getService(ids.getFirst(), ids.getSecond());
						i.setServiceGridId(ids.getFirst());
						i.setServiceId(ids.getSecond());
						i.setServiceName(s.getServiceName());
						i.setDomainId(s.getServiceTypeDomainId());
						i.setServiceTypeId(s.getServiceTypeId());
						informationset = true;
					} catch(InvalidLangridUriException e){
					} catch(ServiceNotFoundException e){
					} catch(DaoException e){
						logger.log(Level.SEVERE, "DAO Exception.", e);
					}
					if(!informationset){
						try{
							Pair<String, String> ids = LangridUriUtil.extractServiceTypeIds(uri);
							if(getServiceTypeDao().isServiceTypeExist(ids.getFirst(), ids.getSecond())){
								i.setDomainId(ids.getFirst());
								i.setServiceTypeId(ids.getSecond());
							}
						} catch(InvalidLangridUriException e2){
						}
					}
					service.getInvocations().add(i);
				} catch(URISyntaxException e){
					logger.warning(
							"URI syntax error.  serviceId: " + service.getServiceId()
							+ "  uri: " + ref.getServiceName().getNamespaceURI()
							);
				}
			}
		}
	}

	private static ServiceEndpoint getEndpoint(
			String gridId, String serviceId, InputStream wsdl)
		throws IOException, MalformedURLException
	{
		URL serviceAddress = null;
		if(wsdl != null){
			try{
				serviceAddress = WSDLUtil.getServiceAddress(wsdl);
			} catch(MalformedURLException e){
				e.printStackTrace();
			} catch(IOException e){
				e.printStackTrace();
			} catch(SAXException e){
				e.printStackTrace();
			}
		}
		if(serviceAddress == null){
			return null;
		} else{
			return new ServiceEndpoint(gridId, serviceId
					, Protocols.DEFAULT, serviceAddress, true);
		}
	}

	private String activeBpelServicesUrl;
	private String activeBpelDeployUserId = "";
	private String activeBpelDeployPassword = "";
	private String activeBpelDeployBinding = "RPC";

	private String droolsServicesUrl;
	private String droolsDeployUserId = "";
	private String droolsDeployPassword = "";

	private static Logger logger = Logger.getLogger(
			ServiceLogic.class.getName());

}
