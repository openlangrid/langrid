/*
 * $Id:HibernateDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2009 NICT Language Grid Project.
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
package jp.go.nict.langrid.dao.hibernate.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jp.go.nict.langrid.dao.TransactionListener;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 216 $
 */
public class TransactionListeners {
	/**
	 * 
	 * 
	 */
	public synchronized void add(TransactionListener listener){
		listeners.add(listener);
	}

	/**
	 * 
	 * 
	 */
	public synchronized void remove(TransactionListener listener){
		listeners.remove(listener);
	}

	/**
	 * 
	 * 
	 */
	public void fireBeginTransaction(){
		Collection<TransactionListener> copy = getCopy();
		if(copy == null) return;
		for(TransactionListener l : copy){
			l.onBeginTransaction();
		}
	}

	/**
	 * 
	 * 
	 */
	public void fireCommitTransaction(){
		Collection<TransactionListener> copy = getCopy();
		if(copy == null) return;
		for(TransactionListener l : copy){
			l.onCommitTransaction();
		}
	}

	/**
	 * 
	 * 
	 */
	public void fireRollbackTransaction(){
		Collection<TransactionListener> copy = getCopy();
		if(copy == null) return;
		for(TransactionListener l : copy){
			l.onRollbackTransaction();
		}
	}

	private synchronized Collection<TransactionListener> getCopy(){
		return new ArrayList<TransactionListener>(listeners);
	}

	private List<TransactionListener> listeners = new ArrayList<TransactionListener>();
}
