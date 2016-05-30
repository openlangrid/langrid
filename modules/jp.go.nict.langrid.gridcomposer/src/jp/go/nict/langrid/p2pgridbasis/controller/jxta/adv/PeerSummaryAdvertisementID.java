/*
 * $Id: PeerSummaryAdvertisementID.java 328 2010-12-08 05:43:18Z t-nakaguchi $
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
package jp.go.nict.langrid.p2pgridbasis.controller.jxta.adv;

import jp.go.nict.langrid.p2pgridbasis.controller.jxta.summary.PeerSummaryID;
import net.jxta.id.ID;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 328 $
 */
public class PeerSummaryAdvertisementID extends ID {
	private static final long serialVersionUID = 1634183507500204675L;
	PeerSummaryID id;
	
	public PeerSummaryAdvertisementID(PeerSummaryID peerID) {
		this.id = peerID;
	}
	
	@Override
	public String getIDFormat() {
		return "langridsummary";
	}

	@Override
	public Object getUniqueValue() {
		return this.getIDFormat() + "-" + this.id.toString(); 
	}

    public boolean equals( Object target ) {
        if (this == target) {
            return true;
        }
        
        if (target instanceof PeerSummaryAdvertisementID ) {
        	PeerSummaryAdvertisementID targetID = (PeerSummaryAdvertisementID)target;
        	return id.equals(targetID.id);
        } else {
            return false;
        }
    }
    
    public int hashCode() {
        return this.getUniqueValue().hashCode();
    }
}
