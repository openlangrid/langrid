/*
 * $Id: P2PGridbasisFederation.java 1043 2014-01-09 13:27:07Z t-nakaguchi $
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
package jp.go.nict.langrid.p2pgridbasis.federation;

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1043 $
 */
public class P2PGridbasisFederation {

	public P2PGridbasisFederation(String gridID) {
		try {
			DaoFactory factory = DaoFactory.createInstance();
			sg_list.clear();
			sg_list = factory.createFederationDao().listSourceGridIds(gridID);

			tg_list.clear();
			tg_list = factory.createFederationDao().listTargetGridIds(gridID);
		} catch (DaoException e) {
			e.printStackTrace();
		}
	}

	public int getFederationType(String gridID) {

		if(gridID == null){
			return P2PGridbasisFederationType.OFF;
		}

		int ret = P2PGridbasisFederationType.NONE;
		if(sg_list.contains(gridID)){
			ret |= P2PGridbasisFederationType.SOURCE;
		}
		if(tg_list.contains(gridID)){
			ret |= P2PGridbasisFederationType.TARGET;
		}
		return ret;
	}

	static List<String> sg_list = new ArrayList<String>();
	static List<String> tg_list = new ArrayList<String>();
}
