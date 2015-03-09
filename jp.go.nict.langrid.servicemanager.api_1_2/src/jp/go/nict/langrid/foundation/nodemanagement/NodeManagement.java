/*
 * $Id:Node.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
package jp.go.nict.langrid.foundation.nodemanagement;

import static jp.go.nict.langrid.foundation.util.validation.AccessRightValidationPolicy.ADMINONLY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.beanutils.ConversionException;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.validator.annotation.EachElement;
import jp.go.nict.langrid.commons.validator.annotation.IntInRange;
import jp.go.nict.langrid.commons.validator.annotation.IntNotNegative;
import jp.go.nict.langrid.commons.validator.annotation.NotEmpty;
import jp.go.nict.langrid.commons.validator.annotation.NotNull;
import jp.go.nict.langrid.commons.validator.annotation.ValidatedMethod;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.NodeAndUserSearchResult;
import jp.go.nict.langrid.dao.NodeDao;
import jp.go.nict.langrid.dao.NodeUtil;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.entity.Node;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.foundation.AbstractLangridService;
import jp.go.nict.langrid.foundation.AttributedElementUpdater;
import jp.go.nict.langrid.foundation.UserChecker;
import jp.go.nict.langrid.foundation.annotation.AccessRightValidatedMethod;
import jp.go.nict.langrid.foundation.annotation.Log;
import jp.go.nict.langrid.foundation.annotation.TransactionMethod;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidAttribute;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidAttributeName;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidEnum;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidMatchingCondition;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidNodeId;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidNodeProfile;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidOrder;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidUserId;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.UnsupportedMatchingMethodException;
import jp.go.nict.langrid.service_1_2.foundation.Attribute;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.NodeNotFoundException;
import jp.go.nict.langrid.service_1_2.foundation.Order;
import jp.go.nict.langrid.service_1_2.foundation.nodemanagement.NodeAlreadyExistsException;
import jp.go.nict.langrid.service_1_2.foundation.nodemanagement.NodeEntry;
import jp.go.nict.langrid.service_1_2.foundation.nodemanagement.NodeEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.nodemanagement.NodeManagementService;
import jp.go.nict.langrid.service_1_2.foundation.nodemanagement.NodeNotInactiveException;
import jp.go.nict.langrid.service_1_2.foundation.nodemanagement.NodeProfile;
import jp.go.nict.langrid.service_1_2.foundation.nodemanagement.typed.NodeAddTimeOnlyAttribute;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.AttributeName;
import jp.go.nict.langrid.service_1_2.foundation.typed.Scope;
import jp.go.nict.langrid.service_1_2.foundation.util.ProfileKeyUtil;
import jp.go.nict.langrid.service_1_2.util.ExceptionConverter;
import jp.go.nict.langrid.service_1_2.util.ParameterValidator;
import jp.go.nict.langrid.servicesupervisor.frontend.processors.pre.AccessRightCheck;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1506 $
 */
public class NodeManagement
extends AbstractLangridService
implements NodeManagementService
{
	/**
	 * 
	 * 
	 */
	public NodeManagement(){}

	/**
	 * 
	 * 
	 */
	public NodeManagement(ServiceContext nodeContext){
		super(nodeContext);
	}

	@AccessRightValidatedMethod(policy=ADMINONLY)
	@TransactionMethod
	public void clear()
		throws AccessLimitExceededException, NoAccessPermissionException
		, ServiceConfigurationException, UnknownException
	{
		try{
			getNodeDao().clear();
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable t){
			t.printStackTrace();
			throw ExceptionConverter.convertException(t);
		}
	}

	@AccessRightValidatedMethod
	@ValidatedMethod
	@TransactionMethod
	public NodeEntrySearchResult searchNodes(
			@IntNotNegative int startIndex
			, @IntInRange(minimum=0, maximum=100) int maxCount
			, @NotNull @EachElement @ValidMatchingCondition MatchingCondition[] conditions
			, @NotNull @EachElement @ValidOrder Order[] orders
			, @NotEmpty @ValidEnum(Scope.class) String scope
			)
			throws AccessLimitExceededException,
			InvalidParameterException, NoAccessPermissionException,
			ServiceConfigurationException, UnknownException,
			UnsupportedMatchingMethodException
	{
		jp.go.nict.langrid.dao.MatchingCondition[] conds = null;
		try{
			conds = convert(
					conditions
					, jp.go.nict.langrid.dao.MatchingCondition[].class
					);
			for(jp.go.nict.langrid.dao.MatchingCondition c : conds){
				if(c.getFieldName().equals("registeredDate")){
					c.setFieldName("createdDateTime");
				} else if(c.getFieldName().equals("updatedDate")){
					c.setFieldName("updatedDateTime");
				}
			}
		} catch(ConversionException e){
			// 
			// Always return emtpy results for weird search conditions.
			// 
			logger.log(Level.INFO, "illegal matching condition specified: " + Arrays.toString(conditions), e);
			return new NodeEntrySearchResult();
		}
		jp.go.nict.langrid.dao.Order[] ords = null;
		try{
			ords = convert(orders, jp.go.nict.langrid.dao.Order[].class);
			for(jp.go.nict.langrid.dao.Order o : ords){
				if(o.getFieldName().equals("registeredDate")){
					o.setFieldName("createdDateTime");
				} else if(o.getFieldName().equals("updatedDate")){
					o.setFieldName("updatedDateTime");
				}
			}
		} catch(ConversionException e){
			throw new InvalidParameterException(
					"orders", e.getMessage()
					);
		}
		if(scope.equalsIgnoreCase("ALL")){
			return searchAllNodes(
					startIndex, maxCount
					, conds, ords);
		} else if(scope.equalsIgnoreCase("MINE")){
			return searchMyNodes(
					startIndex, maxCount
					, conds, ords);
		} else if(scope.equalsIgnoreCase("ACCESSIBLE")){
			return searchAccessibleNodes(
					startIndex, maxCount
					, conds, ords);
		}
		return new NodeEntrySearchResult(new NodeEntry[]{}, 0, true);
	}

	/**
	 * 
	 * 
	 */
	@AccessRightValidatedMethod(policy=ADMINONLY)
	@ValidatedMethod
	@TransactionMethod
	@Log
	public void addNode(
			@NotEmpty @ValidNodeId String nodeId
			, @NotNull @ValidNodeProfile NodeProfile profile
			, @NotNull @EachElement @ValidAttribute Attribute[] attributes)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, NodeAlreadyExistsException
		, ServiceConfigurationException, UnknownException
	{
		String authUserId = getServiceContext().getAuthUser();
		doAddNode(
				authUserId, getGridId(), nodeId, profile, attributes
				);
	}

	@AccessRightValidatedMethod(policy=ADMINONLY)
	@ValidatedMethod
	@TransactionMethod
	@Log
	public void addNodeAs(
			@NotEmpty @ValidUserId String ownerUserId
			, @NotEmpty @ValidNodeId String nodeId
			, @NotNull @ValidNodeProfile NodeProfile profile
			, @NotNull @EachElement @ValidAttribute Attribute[] attributes
			)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, NodeAlreadyExistsException
	, ServiceConfigurationException, UnknownException
	{
		try{
			getUserDao().getUser(getGridId(), ownerUserId);
		} catch(UserNotFoundException e){
			throw new InvalidParameterException(
					"ownerUserId"
					, "User \"" + ownerUserId + "\" not exist.");
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
		doAddNode(
				ownerUserId, getGridId(), nodeId, profile, attributes
				);
	}

	@ValidatedMethod
	@AccessRightValidatedMethod
	@TransactionMethod
	@Log
	public void deleteNode(
			@NotEmpty @ValidNodeId String nodeId
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, NodeNotFoundException, NodeNotInactiveException
		, UnknownException
	{
		Node node = validateNodeOwnerOrAdmin(nodeId);
		if(node.isActive()){
			throw new NodeNotInactiveException(nodeId);
		}
		try{
			getNodeDao().deleteNode(getGridId(), nodeId);
			getAccessLogDao().deleteAccessLogOfNode(getGridId(), nodeId);
		} catch(jp.go.nict.langrid.dao.NodeNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod
	@TransactionMethod
	public NodeProfile getNodeProfile(
			@NotEmpty @ValidNodeId String nodeId
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, NodeNotFoundException, UnknownException
	{
		try{
			return convert(
					getNodeDao().getNode(getGridId(), nodeId)
					, NodeProfile.class
					);
		} catch(jp.go.nict.langrid.dao.NodeNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod
	@TransactionMethod
	@Log
	public void setNodeProfile(
			@NotEmpty @ValidNodeId String nodeId
			, @NotNull @ValidNodeProfile NodeProfile profile
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, NodeNotFoundException, UnknownException
	{
		Node node = validateNodeOwnerOrAdmin(nodeId);
		try{
			copyProperties(node, profile);
			node.touchUpdatedDateTime();
		} catch(ConversionException e){
			Throwable t = e;
			if(e.getCause() != null){
				t = e.getCause();
			}
			throw new InvalidParameterException(
					"profile", t.toString());
//*
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
//*/
		}
	}

	/**
	 * 
	 * 
	 */
	@ValidatedMethod
	@AccessRightValidatedMethod
	@TransactionMethod
	public Attribute[] getNodeAttributes(
			@NotEmpty @ValidNodeId String nodeId
			, @NotNull @EachElement @ValidAttributeName String[] attributeNames
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, NodeNotFoundException, UnknownException
	{
		Node node = validateNodeOwnerOrAdmin(nodeId);
		Set<String> names = new HashSet<String>();
		for(String n : attributeNames){
			names.add(n);
		}
		try{
			List<Attribute> attrs = new ArrayList<Attribute>();
			if(names.size() == 0){
				for(jp.go.nict.langrid.dao.entity.Attribute a : node.getAttributes()){
					attrs.add(new Attribute(a.getName(), a.getValue()));
				}
			} else{
				for(jp.go.nict.langrid.dao.entity.Attribute a : node.getAttributes()){
					if(names.contains(a.getName())){
						attrs.add(new Attribute(a.getName(), a.getValue()));
					}
				}
			}
			return attrs.toArray(new Attribute[]{});
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	@ValidatedMethod
	@AccessRightValidatedMethod
	@TransactionMethod
	@Log
	public void setNodeAttributes(
			@NotEmpty @ValidNodeId String nodeId
			, @EachElement @ValidAttribute Attribute[] attributes
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, NodeNotFoundException, UnknownException
	{
		validateInputAttribute("attributes", attributes);
		Node node = validateNodeOwnerOrAdmin(nodeId);
		try{
			AttributedElementUpdater.updateAttributes(
					node, attributes
					, NodeUtil.getProperties());
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod
	@TransactionMethod
	@Log
	public void activateNode(
			@NotEmpty @ValidNodeId String nodeId
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, NodeNotFoundException, UnknownException
	{
		Node node = validateNodeOwnerOrAdmin(nodeId);
		try{
			if(node.isActive()) return;
			node.setActive(true);
			node.touchUpdatedDateTime();
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod(policy=ADMINONLY)
	@TransactionMethod
	@Log
	public void deactivateNode(
			@NotEmpty @ValidNodeId String nodeId
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, NodeNotFoundException, UnknownException
	{
		Node node = validateNodeOwnerOrAdmin(nodeId);
		if(!node.isActive()){
			return;
		}
		try{
			node.setActive(false);
			node.touchUpdatedDateTime();
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod
	@TransactionMethod
	public boolean isNodeActive(
			@NotEmpty @ValidNodeId String nodeId
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, NodeNotFoundException, UnknownException
	{
		try{
			return getNodeDao().getNode(getGridId(), nodeId).isActive();
		} catch(jp.go.nict.langrid.dao.NodeNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@AccessRightValidatedMethod
	@TransactionMethod
	public String getSelfNodeId() throws AccessLimitExceededException,
			NoAccessPermissionException, ServiceConfigurationException,
			UnknownException {
		return getServiceContext().getSelfNodeId();
	}

	private void doAddNode(
			String ownerUserId, String gridId, String nodeId
			, NodeProfile profile
			, Attribute[] attributes
			)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, NodeAlreadyExistsException
	, ServiceConfigurationException, UnknownException
	{
		if(!profile.getUrl().endsWith("/")){
			profile.setUrl(profile.getUrl() + "/");
		}
		Map<String, String> attrs = new HashMap<String, String>();
		try{

			// attributes check
			for(Attribute a : attributes){
				if(a.getName() == null){
					throw new InvalidParameterException("attributes[].name", "is null");
				}
				String name = a.getName().trim();
				if(name.length() == 0){
					throw new InvalidParameterException("attributes[].name", "is empty");
				}
				if(a.getValue() == null){
					throw new InvalidParameterException("attributes[\"" + name + "\"].value", "is null");
				}
				String value = a.getValue().trim();
				if(value == null){
					throw new InvalidParameterException("attributes[\"" + name + "\"].value", "is empty");
				}
				attrs.put(name, value);
			}
		} catch(InvalidParameterException e){
			throw e;
		} catch(Throwable t){
			t.printStackTrace();
			throw ExceptionConverter.convertException(t);
		}
		try{
			NodeDao dao = getNodeDao();
			Node n = new Node();
			n.setGridId(gridId);
			n.setNodeId(nodeId);
			n.setOwnerUserId(ownerUserId);
			copyProperties(n, profile);
			copyAttributes(n, attributes, NodeAddTimeOnlyAttribute.values());
			dao.addNode(n);

			// 
			// 
			String activeString = attrs.get(
					NodeAddTimeOnlyAttribute.defaultActive.name()
					);
			if(activeString != null && activeString.equalsIgnoreCase("true")){
				n.setActive(true);
			}
		} catch(jp.go.nict.langrid.dao.NodeAlreadyExistsException e){
			throw new NodeAlreadyExistsException(e.getNodeId());
		} catch(jp.go.nict.langrid.dao.DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			e.printStackTrace();
			throw ExceptionConverter.convertException(e);
		}
	}

	private NodeEntrySearchResult searchAllNodes(
			int startIndex, int maxCount
			, jp.go.nict.langrid.dao.MatchingCondition[] conditions
			, jp.go.nict.langrid.dao.Order[] orders
			)
		throws AccessLimitExceededException
		, NoAccessPermissionException, ServiceConfigurationException
		, UnknownException
	{
		try{
			NodeAndUserSearchResult result = getNodeDao().searchNodesAndUsers(
					startIndex, maxCount, getGridId(), conditions, orders
					);
			return convert(result, NodeEntrySearchResult.class);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	private NodeEntrySearchResult searchMyNodes(
			int startIndex
			, int maxCount
			, jp.go.nict.langrid.dao.MatchingCondition[] conditions
			, jp.go.nict.langrid.dao.Order[] orders
			)
		throws AccessLimitExceededException
		, NoAccessPermissionException, ServiceConfigurationException
		, UnknownException
	{
		try{
			conditions = ArrayUtil.append(
					conditions
					, new jp.go.nict.langrid.dao.MatchingCondition(
							"ownerUserId"
							, getUserChecker().getUser().getUserId()
							, MatchingMethod.COMPLETE
					)
					);
			NodeAndUserSearchResult result = getNodeDao().searchNodesAndUsers(
					startIndex, maxCount, getGridId(), conditions, orders
					);
			return convert(result, NodeEntrySearchResult.class);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			e.printStackTrace();
			throw ExceptionConverter.convertException(e);
		}
	}

	private NodeEntrySearchResult searchAccessibleNodes(
			int startIndex
			, int maxCount
			, jp.go.nict.langrid.dao.MatchingCondition[] conditions
			, jp.go.nict.langrid.dao.Order[] orders
			)
		throws AccessLimitExceededException
		, NoAccessPermissionException, ServiceConfigurationException
		, UnknownException
	{
		try{
			String userId = getUserChecker().getUser().getUserId();

			int totalCount = 0;
			boolean totalCountFixed = false;
			int start = 0;
			List<NodeEntry> entries = new ArrayList<NodeEntry>();
			while(entries.size() < maxCount){
				NodeAndUserSearchResult result = getNodeDao().searchNodesAndUsers(
						start, 30, getGridId(), conditions, orders
						);
				for(Pair<Node, User> s : result.getElements()){
					if(!s.getFirst().getOwnerUserId().equals(userId)){
						if(!AccessRightCheck.isAccessible(
								getAccessRightDao()
								, getGridId(), userId, getGridId(), s.getFirst().getNodeId()))
							continue;
					}
					if((startIndex <= totalCount) && (entries.size() < maxCount)){
						entries.add(convert(s, NodeEntry.class));
					}
					totalCount++;
				}
				start += 30;
				if(result.isTotalCountFixed() && result.getTotalCount() < start){
					totalCountFixed = true;
					break;
				}
			}
			return new NodeEntrySearchResult(
					entries.toArray(new NodeEntry[]{})
					, totalCount
					, totalCountFixed
					);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	protected static void validateInputAttribute(
			String parameterName, Attribute[] attributes)
		throws InvalidParameterException
	{
		ParameterValidator.objectNotNull(parameterName, attributes);

		Map<String, String> attrs = new HashMap<String, String>();
		for(Attribute e : attributes){
			if(e.getName() == null){
				throw new InvalidParameterException(
					parameterName, "has null key");
			}
			if(e.getName().length() == 0){
				throw new InvalidParameterException(
					parameterName, "has 0 length key");
			}
			attrs.put(e.getName(), e.getValue());
		}

		for(AttributeName key : ProfileKeyUtil.readonlyKeys){
			assertUnreservedKey(parameterName, attrs, key);
		}
	}

	private Node validateNodeOwnerOrAdmin(String nodeId)
	throws NoAccessPermissionException, NodeNotFoundException
	, ServiceConfigurationException, UnknownException
	{
		try{
			Node node = getNodeDao().getNode(getGridId(), nodeId);
			UserChecker checker = getUserChecker();
			checker.checkAuthUserExists();
			if(checker.isAuthUserAdmin()) return node;
			String userId = checker.getUserId();
			if(userId.equals(node.getOwnerUserId())) return node;
			throw new NoAccessPermissionException(userId);
		} catch(jp.go.nict.langrid.dao.NodeNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(NoAccessPermissionException e){
			throw e;
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	static void assertUnreservedKey(
		String parameterName, Map<String, String> attributes, AttributeName key
		)
		throws InvalidParameterException
	{
		if(attributes.get(key.getAttributeName()) != null){
			throwReservedKeyException(parameterName, key);
		}
	}

	/**
	 * 
	 * 
	 */
	static void throwReservedKeyException(
		String parameterName, AttributeName key)
		throws InvalidParameterException
	{
		throw new InvalidParameterException(
			String.format("%s.key[\"%s\"]", parameterName, key.getAttributeName())
			, "is reserved"
			);
	}

	private static Logger logger = Logger.getLogger(
			NodeManagement.class.getName());
}
