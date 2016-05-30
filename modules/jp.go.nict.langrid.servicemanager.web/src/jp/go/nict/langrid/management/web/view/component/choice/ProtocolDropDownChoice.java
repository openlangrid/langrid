/*
 * $Id: ProtocolDropDownChoice.java 406 2011-08-25 02:12:29Z t-nakaguchi $
 * 
 * This is a program for Language Grid Core Node. This combines multiple language resources and
 * provides composite language services. Copyright (C) 2005-2008 NICT Language Grid Project.
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.management.web.view.component.choice;

import jp.go.nict.langrid.management.web.model.ProtocolModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.WildcardListModel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ProtocolDropDownChoice extends DropDownChoice<ProtocolModel> {
	/**
	 * 
	 * 
	 * @throws ServiceManagerException 
	 */
	public ProtocolDropDownChoice(String gridId, String componentId)
	throws ServiceManagerException {
		super(componentId, new Model<ProtocolModel>(),
			new WildcardListModel<ProtocolModel>());
		setChoiceRenderer(new ProtocolChoiceRenderer());
		list = ServiceFactory.getInstance().getProtocolService(gridId).getAllList();
		setChoices(list);
	}

	@Override
	public CharSequence getDefaultChoice(Object selected) {
		return "";
	}

	public ProtocolModel getDefaultProtocolType() {
		if(list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	public void setModelById(String protocolId) {
		for(ProtocolModel model : list) {
			if(model.getProtocolId().equals(protocolId)) {
				setModelObject(model);
				return;
			}
		}
	}

	private LangridList<ProtocolModel> list;

	private class ProtocolChoiceRenderer implements IChoiceRenderer<ProtocolModel> {
		public ProtocolChoiceRenderer() {
		}

		@Override
		public Object getDisplayValue(ProtocolModel object) {
			return object.getProtocolName();
		}

		@Override
		public String getIdValue(ProtocolModel object, int index) {
			return object.getProtocolName();
		}
	}
}
