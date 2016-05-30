package jp.go.nict.langrid.dao.hibernate.queuedevent;

import java.io.Serializable;

import jp.go.nict.langrid.dao.hibernate.listener.EntityListeners;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class QueuedUpdateEvent extends QueuedEvent{
	public QueuedUpdateEvent(Serializable id, Object value, String[] modifiedProperties){
		super(id);
		this.value = value;
		this.modifiedProperties = modifiedProperties;
	}

	public int hashCode(){
		HashCodeBuilder b = new HashCodeBuilder();
		b.appendSuper(super.hashCode());
		b.append(modifiedProperties);
		return b.hashCode();
	}

	public boolean equals(Object value){
		if(value == null) return false;
		if(!getClass().equals(value.getClass())) return false;
		QueuedUpdateEvent v = (QueuedUpdateEvent)value;
		EqualsBuilder b = new EqualsBuilder();
		b.append(getId(), v.getId());
		b.append(modifiedProperties, v.modifiedProperties);
		return b.isEquals();
	}

	public String toString(){
		ToStringBuilder b = new ToStringBuilder(this);
		b.appendSuper(super.toString());
		b.append("modifiedProperties", modifiedProperties);
		return b.toString();
	}

	public Object getValue(){
		return value;
	}

	public String[] getMofifiedProperties(){
		return modifiedProperties;
	}

	@Override
	public void fire(EntityListeners listeners) {
		if(modifiedProperties == null){
			listeners.fireUpdate(getId(), value, new String[]{});
		} else{
			listeners.fireUpdate(getId(), value, modifiedProperties);
		}
	}

	private Object value;
	private String[] modifiedProperties;
}
