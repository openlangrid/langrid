/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) NICT Language Grid Project.
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

import static jp.go.nict.langrid.commons.util.ArrayUtil.append;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.EntityKey;
import org.hibernate.event.EventListeners;
import org.hibernate.event.PostCollectionRecreateEvent;
import org.hibernate.event.PostCollectionRecreateEventListener;
import org.hibernate.event.PostCollectionRemoveEvent;
import org.hibernate.event.PostCollectionRemoveEventListener;
import org.hibernate.event.PostCollectionUpdateEvent;
import org.hibernate.event.PostCollectionUpdateEventListener;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.dao.AbstractDaoContext;
import jp.go.nict.langrid.dao.ConnectException;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.EntityListener;
import jp.go.nict.langrid.dao.TransactionListener;
import jp.go.nict.langrid.dao.entity.AcceptableRemoteAddress;
import jp.go.nict.langrid.dao.entity.AccessLimit;
import jp.go.nict.langrid.dao.entity.AccessLog;
import jp.go.nict.langrid.dao.entity.AccessRight;
import jp.go.nict.langrid.dao.entity.AccessStat;
import jp.go.nict.langrid.dao.entity.BPELService;
import jp.go.nict.langrid.dao.entity.Domain;
import jp.go.nict.langrid.dao.entity.ExternalService;
import jp.go.nict.langrid.dao.entity.Federation;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.dao.entity.GridAttribute;
import jp.go.nict.langrid.dao.entity.Invocation;
import jp.go.nict.langrid.dao.entity.News;
import jp.go.nict.langrid.dao.entity.Node;
import jp.go.nict.langrid.dao.entity.NodeAttribute;
import jp.go.nict.langrid.dao.entity.OperationRequest;
import jp.go.nict.langrid.dao.entity.OverUseLimit;
import jp.go.nict.langrid.dao.entity.Protocol;
import jp.go.nict.langrid.dao.entity.Resource;
import jp.go.nict.langrid.dao.entity.ResourceAttribute;
import jp.go.nict.langrid.dao.entity.ResourceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ResourceType;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceActionSchedule;
import jp.go.nict.langrid.dao.entity.ServiceAttribute;
import jp.go.nict.langrid.dao.entity.ServiceDeployment;
import jp.go.nict.langrid.dao.entity.ServiceEndpoint;
import jp.go.nict.langrid.dao.entity.ServiceInterfaceDefinition;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceType;
import jp.go.nict.langrid.dao.entity.SystemProperty;
import jp.go.nict.langrid.dao.entity.TemporaryUser;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.dao.entity.UserAttribute;
import jp.go.nict.langrid.dao.entity.UserRole;
import jp.go.nict.langrid.dao.entity.WebappService;
import jp.go.nict.langrid.dao.hibernate.listener.EntityListeners;
import jp.go.nict.langrid.dao.hibernate.listener.TransactionListeners;
import jp.go.nict.langrid.dao.hibernate.platform.PlatformUtil;
import jp.go.nict.langrid.dao.hibernate.queuedevent.QueuedCollectionRecreateEvent;
import jp.go.nict.langrid.dao.hibernate.queuedevent.QueuedCollectionRemoveEvent;
import jp.go.nict.langrid.dao.hibernate.queuedevent.QueuedCollectionUpdateEvent;
import jp.go.nict.langrid.dao.hibernate.queuedevent.QueuedDeleteEvent;
import jp.go.nict.langrid.dao.hibernate.queuedevent.QueuedEvent;
import jp.go.nict.langrid.dao.hibernate.queuedevent.QueuedInsertEvent;
import jp.go.nict.langrid.dao.hibernate.queuedevent.QueuedUpdateEvent;
import jp.go.nict.langrid.dao.hibernate.util.EntityUtil;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class HibernateDaoContext
extends AbstractDaoContext
implements DaoContext{
	public <T> void addEntityListener(Class<T> clazz, EntityListener<T> listener){
		listeners.add(clazz, listener);
	}

	public <T> void removeEntityListener(Class<T> clazz, EntityListener<T> listener){
		listeners.remove(clazz, listener);
	}

	/**
	 * 
	 * 
	 */
	public void fireUpdate(Serializable id, Object entity, String[] modifiedFields){
		listeners.fireUpdate(id, entity, modifiedFields);
	}

	public void addTransactionListener(TransactionListener listener){
		transactionListeners.add(listener);
	}

	public void removeTransactionListener(TransactionListener listener){
		transactionListeners.remove(listener);
	}

	public void beginTransaction()
	throws ConnectException, DaoException
	{
		int n = nest.get();
		if(n == 0){
			Session s = null;
			try{
				s = getSession();
			} catch(HibernateException e){
				throw new DaoException(e);
			}
			try{
				s.beginTransaction();
				session.set(s);
				eventReceiver.get().transactionStarted();
			} catch(HibernateException e){
				throw new ConnectException(e);
			}
			shouldRollback.set(false);
			commitFailed.set(false);
		}
		nest.set(n + 1);
	}

	public void commitTransaction() throws DaoException {
		int n = nest.get();
		if(n == 0){
			if(commitFailed.get()) return;
			else throw new DaoException("too many commit call");
		}
		n--;
		nest.set(n);
		if(session.get() == null){
			logger.warning("Ignore commitTransaction call because" +
					" transaction not successfully started.");
			return;
		}
		if(n == 0){
			Session s = session.get();
			session.set(null);
			try{
				if(shouldRollback.get()){
					s.getTransaction().rollback();
				} else{
					try{
						s.getTransaction().commit();
						eventReceiver.get().transactionCommitted();
					} catch(HibernateException e){
						commitFailed.set(true);
						try{
							s.getTransaction().rollback();
						} finally{
							eventReceiver.get().transactionRollbacked();
						}
						throw new DaoException(e);
					} finally{
					}
				}
			} catch(HibernateException e){
				throw new DaoException(e);
			}
		}
	}

	public void rollbackTransaction() throws DaoException {
		int n = nest.get();
		if(n == 0){
			if(commitFailed.get()) return;
			else throw new DaoException("too many rollback call");
		}
		n--;
		nest.set(n);
		if(session.get() == null){
			logger.warning("Ignore commitTransaction call because transaction not successfully started.");
			return;
		}
		if(n == 0){
			Session s = session.get();
			session.set(null);
			try{
				s.getTransaction().rollback();
			} catch(HibernateException e){
				throw new DaoException(e);
			} finally{
				eventReceiver.get().transactionRollbacked();
			}
		} else{
			shouldRollback.set(true);
		}
	}

	public int getTransactionNestCount(){
		return nest.get();
	}

	@SuppressWarnings("unchecked")
	public <T> T loadEntity(Class<T> clazz, Serializable id) throws DaoException{
		beginTransaction();
		try{
			T ret = (T)getSession().load(clazz, id);
			commitTransaction();
			return ret;
		} catch(HibernateException e){
			rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void mergeEntity(Object entity) throws DaoException{
		if(entity instanceof ServiceType){
			throw new DaoException("Don't merge ServiceType because that causes duplication of ServiceInterfaceDefinition.");
		}
		beginTransaction();
		try{
			getSession().saveOrUpdate(entity);
			commitTransaction();
		} catch(HibernateException e){
			rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void refreshEntity(Object entity) throws DaoException{
		beginTransaction();
		try{
			getSession().refresh(entity);
			commitTransaction();
		} catch(HibernateException e){
			rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void updateEntity(Object entity) throws DaoException{
		beginTransaction();
		try{
			getSession().update(entity);
			commitTransaction();
		} catch(HibernateException e){
			rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public <T> List<T> listEntity(Class<T> clazz) throws DaoException{
		beginTransaction();
		try{
			@SuppressWarnings("unchecked")
			List<T> ret = getSession().createCriteria(clazz).list();
			commitTransaction();
			return ret;
		} catch(HibernateException e){
			rollbackTransaction();
			throw new DaoException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public long generateSequence() throws DaoException{
		beginTransaction();
		try{
			long seq = ((Number)getSession().createSQLQuery(seqQuery)
					.uniqueResult()).longValue();
			commitTransaction();
			return seq;
		} catch(HibernateException e){
			rollbackTransaction();
			throw new DaoException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public Session getSession(){
		synchronized(HibernateDaoContext.class){
			if(factory == null){
				try{
					initialize();
				} catch(IOException e){
					throw new RuntimeException(e);
				} catch(HibernateException e){
					e.printStackTrace();
					throw e;
				}
			}
			return factory.getCurrentSession();
		}
	}

	@Override
	public List<Pair<Object, Calendar>> listAllIdAndUpdates(
			Class<?> entityClass,
			@SuppressWarnings("unchecked") Pair<String, String>... conditions)
	throws DaoException {
		Session session = getSession();
		beginTransaction();
		try{
			Criteria criteria = session
				.createCriteria(entityClass)
				.setProjection(
					EntityUtil.addIdColumnNames(entityClass, Projections.projectionList())
					.add(Projections.property("updatedDateTime")));
			for(Pair<String, String> c : conditions){
				criteria.add(Property.forName(c.getFirst()).eq(c.getSecond()));
			}
			criteria.addOrder(Property.forName("updatedDateTime").asc());
			@SuppressWarnings("unchecked")
			List<Object[]> list = criteria.list();
			commitTransaction();
			List<Pair<Object, Calendar>> ret = new ArrayList<>();
			for(Object[] r : list){
				try {
					ret.add(Pair.create(
							jp.go.nict.langrid.dao.util.EntityUtil.getId(entityClass, r), (Calendar)r[r.length - 1]));
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}
			return ret;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			rollbackTransaction();
			throw new DaoException(e);
		}
	}

	private static void logAdditionalInfo(RuntimeException exception){
		if(exception instanceof JDBCException){
			SQLException e = ((JDBCException)exception).getSQLException();
			while(e != null){
				logger.log(Level.WARNING, "Next SQLException", e);
				e = e.getNextException();
			}
		}
	}

	public static synchronized void addEntityClass(List<Class<?>> additionalEntities)
	throws DaoException{
		if(factory != null){
			throw new DaoException("context already initialized.");
		}
		HibernateDaoContext.additionalEntities.addAll(additionalEntities);
	}

	private static EntityListeners listeners = new EntityListeners();
	private static TransactionListeners transactionListeners = new TransactionListeners();

	private static ThreadLocal<Integer> nest = new ThreadLocal<Integer>(){
		protected Integer initialValue(){ return 0;}
	};

	private static ThreadLocal<Session> session = new ThreadLocal<Session>();
	private static ThreadLocal<Boolean> shouldRollback = new ThreadLocal<Boolean>(){
		protected Boolean initialValue(){ return false;}
	};
	private static ThreadLocal<Boolean> commitFailed = new ThreadLocal<Boolean>(){
		protected Boolean initialValue(){ return false;}
	};
	private static class EventReceiver{
		public void transactionStarted(){
			queueEnabled = false;
			transactionListeners.fireBeginTransaction();
		}
		public void transactionCommitted(){
			queueEnabled = false;
			flushQueue();
			transactionListeners.fireCommitTransaction();
		}
		public void transactionRollbacked(){
			queueEnabled = false;
			clearQueue();
			transactionListeners.fireRollbackTransaction();
		}
		public void onPostInsert(PostInsertEvent event){
			if(queueEnabled){
				queue.add(new QueuedInsertEvent(event.getId(), event.getEntity()));
				return;
			}
			listeners.fireInsert(event.getId(), event.getEntity());
		}
		public void onPostUpdate(PostUpdateEvent event){
			String[] modified = null;
			String[] names = event.getPersister().getPropertyNames();
			if(event.getOldState() != null){
				int[] modifiedIndices = event.getPersister().findModified(
						event.getOldState(), event.getState()
						, event.getEntity(), event.getSession());
				modified = new String[modifiedIndices.length];
				for(int i = 0; i < modifiedIndices.length; i++){
					modified[i] = names[modifiedIndices[i]];
				}
			} else{
				modified = names;
			}
			if(queueEnabled){
				queue.add(new QueuedUpdateEvent(event.getId(), event.getEntity(), modified));
				return;
			}
			listeners.fireUpdate(event.getId(), event.getEntity(), modified);
		}
		public void onPostDelete(PostDeleteEvent event){
			if(queueEnabled){
				queue.add(new QueuedDeleteEvent(event.getId(), event.getEntity().getClass()));
				return;
			}
			listeners.fireDelete(event.getId(), event.getEntity().getClass());
		}
		public void onPostRecreateCollection(PostCollectionRecreateEvent event) {
			Serializable ownerId = event.getAffectedOwnerIdOrNull();
			Object owner = event.getAffectedOwnerOrNull();
			Class<?> ownerClass = owner != null ? owner.getClass() : null;
			Map<?, ?> entityMap = event.getSession().getPersistenceContext().getEntitiesByKey();
			for(Object s : entityMap.keySet()){
				EntityKey k = (EntityKey)s;
				if(!k.getEntityName().equals(event.getAffectedOwnerEntityName())){
					Object o = entityMap.get(k);
					if(queueEnabled){
						queue.add(new QueuedCollectionRecreateEvent(
								ownerId, ownerClass
								, k.getIdentifier(), o));
					} else{
						listeners.fireCollectionEntityRecreate(
								ownerId, ownerClass
								, k.getIdentifier(), o);
					}
				}
			}
		}
		public void onPostUpdateCollection(PostCollectionUpdateEvent event) {
			Serializable ownerId = event.getAffectedOwnerIdOrNull();
			Object owner = event.getAffectedOwnerOrNull();
			Class<?> ownerClass = owner != null ? owner.getClass() : null;
			String propertyName = event.getCollection().getRole();
			if(ownerClass != null){
				propertyName = propertyName.substring(ownerClass.getName().length() + 1);
			}
			Map<?, ?> entityMap = event.getSession().getPersistenceContext().getEntitiesByKey();
			for(Object s : entityMap.keySet()){
				EntityKey k = (EntityKey)s;
				if(!k.getEntityName().equals(event.getAffectedOwnerEntityName())){
					Object o = entityMap.get(k);
					if(queueEnabled){
						queue.add(new QueuedCollectionUpdateEvent(
								ownerId, ownerClass, propertyName
								, k.getIdentifier(), o));
					} else{
						listeners.fireCollectionEntityUpdate(
								ownerId, ownerClass, propertyName
								, k.getIdentifier(), o);
					}
				}
			}
		}
		public void onPostRemoveCollection(PostCollectionRemoveEvent event) {
			Serializable ownerId = event.getAffectedOwnerIdOrNull();
			Object owner = event.getAffectedOwnerOrNull();
			Class<?> ownerClass = owner != null ? owner.getClass() : null;
			String propertyName = "";
			if(event.getCollection() != null){
				propertyName = event.getCollection().getRole();
				if(ownerClass != null){
					propertyName = propertyName.substring(ownerClass.getName().length() + 1);
				}
			}
			Map<?, ?> entityMap = event.getSession().getPersistenceContext().getEntitiesByKey();
			for(Object s : entityMap.keySet()){
				EntityKey k = (EntityKey)s;
				if(!k.getEntityName().equals(event.getAffectedOwnerEntityName())){
					Object o = entityMap.get(k);
					if(queueEnabled){
						queue.add(new QueuedCollectionRemoveEvent(
								ownerId, ownerClass, propertyName
								, k.getIdentifier(), o.getClass()));
					} else{
						listeners.fireCollectionEntityRemove(
								ownerId, ownerClass, propertyName
								, k.getIdentifier(), o.getClass());
					}
				}
			}
		}
		private void flushQueue(){
			synchronized(listeners){
				for(QueuedEvent e : queue){
					e.fire(listeners);
				}
			}
			queue.clear();
		}

		private void clearQueue(){
			queue.clear();
		}

		private boolean queueEnabled = true;
		private Set<QueuedEvent> queue = new LinkedHashSet<QueuedEvent>();
	}
	private static ThreadLocal<EventReceiver> eventReceiver = new ThreadLocal<EventReceiver>(){
		protected EventReceiver initialValue(){ return new EventReceiver();};
	};
	static class EventReceiverBridge
	implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener
	, PostCollectionRecreateEventListener, PostCollectionUpdateEventListener
	, PostCollectionRemoveEventListener{
		public void onPostInsert(PostInsertEvent event){
			eventReceiver.get().onPostInsert(event);
		}
		public void onPostUpdate(PostUpdateEvent event){
			eventReceiver.get().onPostUpdate(event);
		}
		public void onPostDelete(PostDeleteEvent event){
			eventReceiver.get().onPostDelete(event);
		}
		public void onPostRecreateCollection(PostCollectionRecreateEvent event) {
			eventReceiver.get().onPostRecreateCollection(event);
		}
		public void onPostUpdateCollection(PostCollectionUpdateEvent event) {
			eventReceiver.get().onPostUpdateCollection(event);
		}
		public void onPostRemoveCollection(PostCollectionRemoveEvent event) {
			eventReceiver.get().onPostRemoveCollection(event);
		}
		private static final long serialVersionUID = 3215151182996067644L;
	}
	private static EventReceiverBridge eventReceiverBridge = new EventReceiverBridge();

	private static SessionFactory factory;
	private static String seqQuery;
	private static List<Class<?>> additionalEntities = new ArrayList<Class<?>>();
	private static Logger logger = Logger.getLogger(
			HibernateDaoContext.class.getName());

	private static void initialize()
	throws IOException{
		URL resource = PlatformUtil.getHibernateResource(
				HibernateDaoContext.class
				);
		Properties props = new Properties();
		props.load(HibernateDaoContext.class.getResourceAsStream(
				"/jp/go/nict/langrid/dao/hibernate/hibernate.properties"));
		AnnotationConfiguration config = new AnnotationConfiguration()
					.addAnnotatedClass(SystemProperty.class)
					.addAnnotatedClass(Grid.class)
					.addAnnotatedClass(GridAttribute.class)
					.addAnnotatedClass(Federation.class)
					.addAnnotatedClass(User.class)
					.addAnnotatedClass(UserAttribute.class)
					.addAnnotatedClass(UserRole.class)
					.addAnnotatedClass(TemporaryUser.class)
					.addAnnotatedClass(Resource.class)
					.addAnnotatedClass(ResourceAttribute.class)
					.addAnnotatedClass(Service.class)
					.addAnnotatedClass(ServiceAttribute.class)
					.addAnnotatedClass(ServiceDeployment.class)
					.addAnnotatedClass(ServiceEndpoint.class)
					.addAnnotatedClass(ExternalService.class)
					.addAnnotatedClass(BPELService.class)
					.addAnnotatedClass(WebappService.class)
					.addAnnotatedClass(Invocation.class)
					.addAnnotatedClass(Node.class)
					.addAnnotatedClass(NodeAttribute.class)
					.addAnnotatedClass(AccessLog.class)
					.addAnnotatedClass(AccessLimit.class)
					.addAnnotatedClass(AccessRight.class)
					.addAnnotatedClass(AccessStat.class)
					.addAnnotatedClass(OverUseLimit.class)
					.addAnnotatedClass(AcceptableRemoteAddress.class)
					.addAnnotatedClass(OperationRequest.class)
					.addAnnotatedClass(ServiceActionSchedule.class)
					.addAnnotatedClass(News.class)
					.addAnnotatedClass(Domain.class)
					.addAnnotatedClass(Protocol.class)
					.addAnnotatedClass(ResourceType.class)
					.addAnnotatedClass(ResourceMetaAttribute.class)
					.addAnnotatedClass(ServiceType.class)
					.addAnnotatedClass(ServiceMetaAttribute.class)
					.addAnnotatedClass(ServiceInterfaceDefinition.class)
					.addProperties(props)
					.configure(resource);
		for(Class<?> c : additionalEntities){
			config.addAnnotatedClass(c);
		}
		config.addProperties(props)
				.configure(resource);
		EventListeners el = config.getEventListeners();
		el.setPostInsertEventListeners(append(el.getPostInsertEventListeners(), eventReceiverBridge));
		el.setPostUpdateEventListeners(append(el.getPostUpdateEventListeners(), eventReceiverBridge));
		el.setPostDeleteEventListeners(append(el.getPostDeleteEventListeners(), eventReceiverBridge));
		el.setPostCollectionRecreateEventListeners(
				append(el.getPostCollectionRecreateEventListeners(), eventReceiverBridge));
		el.setPostCollectionUpdateEventListeners(
				append(el.getPostCollectionUpdateEventListeners(), eventReceiverBridge));
		el.setPostCollectionRemoveEventListeners(
				append(el.getPostCollectionRemoveEventListeners(), eventReceiverBridge));
		factory = config.buildSessionFactory();
		seqQuery = Dialect.getDialect(config.getProperties())
			.getSequenceNextValString("hibernate_sequence");
	}
}
