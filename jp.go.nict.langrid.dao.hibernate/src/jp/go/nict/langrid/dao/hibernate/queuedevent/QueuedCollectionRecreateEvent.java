package jp.go.nict.langrid.dao.hibernate.queuedevent;

import java.io.Serializable;

import jp.go.nict.langrid.dao.hibernate.listener.EntityListeners;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class QueuedCollectionRecreateEvent extends QueuedEvent{
	public QueuedCollectionRecreateEvent(Serializable id, Class<?> ownerClass
			, Serializable collectionEntityId, Object collectionEntityValue){
		super(id);
		this.ownerClass = ownerClass;
		this.collectionEntityId = collectionEntityId;
		this.collectionEntityValue = collectionEntityValue;
	}

	public int hashCode(){
		HashCodeBuilder b = new HashCodeBuilder();
		b.appendSuper(super.hashCode());
		b.append(collectionEntityId);
		return b.hashCode();
	}

	public boolean equals(Object value){
		if(value == null) return false;
		if(!getClass().equals(value.getClass())) return false;
		QueuedCollectionRecreateEvent v = (QueuedCollectionRecreateEvent)value;
		EqualsBuilder b = new EqualsBuilder();
		b.append(getId(), v.getId());
		b.append(collectionEntityId, v.collectionEntityId);
		return b.isEquals();
	}

	public String toString(){
		ToStringBuilder b = new ToStringBuilder(this);
		b.appendSuper(super.toString());
		b.append("ownerClass", ownerClass);
		b.append("collectionEntityId", collectionEntityId);
		return b.toString();
	}

	public Class<?> getOwnerClass(){
		return ownerClass;
	}

	public Serializable getCollectionEntityId(){
		return collectionEntityId;
	}

	public Object getCollectionEntityValue(){
		return collectionEntityValue;
	}

	@Override
	public void fire(EntityListeners listeners) {
		listeners.fireCollectionEntityRecreate(
				getId(), ownerClass
				, collectionEntityId, collectionEntityValue
				);
	}

	private Class<?> ownerClass;
	private Serializable collectionEntityId;
	private Object collectionEntityValue;
}
