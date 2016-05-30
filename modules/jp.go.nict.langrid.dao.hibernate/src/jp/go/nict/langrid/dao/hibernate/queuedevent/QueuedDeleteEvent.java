package jp.go.nict.langrid.dao.hibernate.queuedevent;

import java.io.Serializable;

import jp.go.nict.langrid.dao.hibernate.listener.EntityListeners;

public class QueuedDeleteEvent extends QueuedEvent{
	public QueuedDeleteEvent(Serializable id, Class<?> clazz){
		super(id);
		this.clazz = clazz;
	}

	public Class<?> getClazz(){
		return this.clazz;
	}

	@Override
	public void fire(EntityListeners listeners) {
		listeners.fireDelete(getId(), clazz);
	}

	private Class<?> clazz;
}
