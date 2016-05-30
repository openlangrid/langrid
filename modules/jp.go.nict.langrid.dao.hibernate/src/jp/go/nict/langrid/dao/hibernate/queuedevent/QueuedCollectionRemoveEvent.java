package jp.go.nict.langrid.dao.hibernate.queuedevent;

import java.io.Serializable;

import jp.go.nict.langrid.dao.hibernate.listener.EntityListeners;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class QueuedCollectionRemoveEvent extends QueuedEvent{
	public QueuedCollectionRemoveEvent(Serializable id, Class<?> ownerClass
			, String propertyName
			, Serializable collectionEntityId, Class<?> collectionEntityClass){
		super(id);
		this.ownerClass = ownerClass;
		this.propertyName = propertyName;
		this.collectionEntityId = collectionEntityId;
		this.collectionEntityClass = collectionEntityClass;
	}

	public int hashCode(){
		HashCodeBuilder b = new HashCodeBuilder();
		b.appendSuper(super.hashCode());
		b.append(propertyName);
		b.append(collectionEntityId);
		return b.hashCode();
	}

	public boolean equals(Object value){
		if(value == null) return false;
		if(!getClass().equals(value.getClass())) return false;
		QueuedCollectionRemoveEvent v = (QueuedCollectionRemoveEvent)value;
		EqualsBuilder b = new EqualsBuilder();
		b.append(propertyName, v.propertyName);
		b.append(getId(), v.getId());
		b.append(collectionEntityId, v.collectionEntityId);
		return b.isEquals();
	}

	public String toString(){
		ToStringBuilder b = new ToStringBuilder(this);
		b.appendSuper(super.toString());
		b.append("ownerClass", ownerClass);
		b.append("propertyName", propertyName);
		b.append("collectionEntityId", collectionEntityId);
		return b.toString();
	}

	public Class<?> getOwnerClass(){
		return ownerClass;
	}

	public Serializable getCollectionEntityId(){
		return collectionEntityId;
	}

	public Class<?> getCollectionEntityClass(){
		return collectionEntityClass;
	}

	@Override
	public void fire(EntityListeners listeners) {
		listeners.fireCollectionEntityRemove(
				getId(), ownerClass, propertyName
				, collectionEntityId, collectionEntityClass
				);
	}

	private Class<?> ownerClass;
	private String propertyName;
	private Serializable collectionEntityId;
	private Class<?> collectionEntityClass;
}
