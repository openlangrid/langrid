package jp.go.nict.langrid.dao.hibernate.queuedevent;

import java.io.Serializable;

import jp.go.nict.langrid.dao.hibernate.listener.EntityListeners;

import org.apache.commons.lang.builder.ToStringBuilder;

public abstract class QueuedEvent {
	public QueuedEvent(Serializable id){
		this.id = id;
	}

	public int hashCode(){
		return id.hashCode();
	}

	public boolean equals(Object value){
		if(value == null) return false;
		if(!getClass().equals(value.getClass())) return false;
		return id.equals(((QueuedEvent)value).getId());
	}

	public String toString(){
		ToStringBuilder b = new ToStringBuilder(this);
		b.append("id", id);
		return b.toString();
	}

	public Serializable getId(){
		return id;
	}

	public abstract void fire(EntityListeners listeners);

	private Serializable id;
}
