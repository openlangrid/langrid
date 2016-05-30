package jp.go.nict.langrid.dao.hibernate.queuedevent;

import java.io.Serializable;

import jp.go.nict.langrid.dao.hibernate.listener.EntityListeners;

public class QueuedInsertEvent extends QueuedEvent{
	public QueuedInsertEvent(Serializable id, Object value){
		super(id);
		this.value = value;
	}

	public Object getValue(){
		return value;
	}

	@Override
	public void fire(EntityListeners listeners) {
		listeners.fireInsert(getId(), value);
	}

	private Object value;
}
