/*
 * $Id: ProtocolLogic.java 1001 2013-11-26 01:29:16Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2009 NICT Language Grid Project.
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

import java.util.List;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.entity.Protocol;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1001 $
 */
public class ProtocolLogic
extends AbstractLogic{
	/**
	 * 
	 * 
	 */
	public ProtocolLogic() throws DaoException{
	}

	@DaoTransaction
	public void clear() throws DaoException{
		getProtocolDao().clear();
	}

	@DaoTransaction
	public List<Protocol> listProtocol(String gridId) throws DaoException{
		return getProtocolDao().listAllProtocols();
	}

	@DaoTransaction
	public void addProtocol(Protocol protocol) throws DaoException {
		getProtocolDao().addProtocol(protocol);
	}

	@DaoTransaction
	public void deleteProtocol(String protocolId) throws DaoException {
		getProtocolDao().deleteProtocol(protocolId);
	}
	
	@DaoTransaction
	public void transactUpdate(String protocolId, BlockP<Protocol> block)
			throws DaoException{
		Protocol p = getProtocolDao().getProtocol(protocolId);
		block.execute(p);
		p.touchUpdatedDateTime();
	}

	private static Logger logger = Logger.getLogger(
			ProtocolLogic.class.getName());
}
