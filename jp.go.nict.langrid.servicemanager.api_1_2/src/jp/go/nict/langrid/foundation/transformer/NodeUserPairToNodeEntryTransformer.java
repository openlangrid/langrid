/*
 * $Id: NodeUserPairToNodeEntryTransformer.java 302 2010-12-01 02:49:42Z t-nakaguchi $
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
package jp.go.nict.langrid.foundation.transformer;

import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.dao.entity.Node;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.service_1_2.foundation.nodemanagement.NodeEntry;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 302 $
 */
public class NodeUserPairToNodeEntryTransformer
implements Transformer<Pair<Node, User>, NodeEntry>{
	/**
	 * 
	 * 
	 */
	public NodeUserPairToNodeEntryTransformer(Converter converter){
		this.converter = converter;
	}

	public NodeEntry transform(Pair<Node, User> value)
			throws TransformationException {
		NodeEntry entry = converter.convert(value.getFirst(), NodeEntry.class);
		entry.setOwnerUserOrganization(value.getSecond().getOrganization());
		return entry;
	}

	private Converter converter;
}
