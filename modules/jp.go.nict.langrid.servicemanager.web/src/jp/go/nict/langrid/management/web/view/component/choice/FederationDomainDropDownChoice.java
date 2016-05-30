/*
 * $Id: DomainDropDownChoice.java 596 2012-12-03 04:05:39Z t-nakaguchi $
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

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.DomainModel;
import jp.go.nict.langrid.management.web.model.FederationModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.WildcardListModel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: trangmx $
 * @version $Revision: 596 $
 */
public class FederationDomainDropDownChoice extends DropDownChoice<DomainModel> {
	/**
	 * 
	 * 
	 */
	public FederationDomainDropDownChoice(String gridId, String componentId)
	throws ServiceManagerException {
		super(componentId, new Model<DomainModel>(), new WildcardListModel<DomainModel>());
		setChoiceRenderer(new DomainChoiceRenderer());
		targetFederationList = ServiceFactory.getInstance().getFederationService(gridId).getConnectedTargetGridIdList(gridId, new Order("targetGridId", OrderDirection.ASCENDANT));
		list = new ArrayList<DomainModel>();
		List<DomainModel> selfGridDomains = ServiceFactory.getInstance().getDomainService(gridId).getList(
				0, 1, new MatchingCondition[]{}, new Order[]{}, Scope.ALL);
		list.addAll(selfGridDomains);
		for (String tergetGridID : targetFederationList) {
			List<DomainModel> subList = ServiceFactory.getInstance().getDomainService(tergetGridID).getList(
					0, 1, new MatchingCondition[]{}, new Order[]{}, Scope.ALL);
			list.addAll(subList);
		}
		setChoices(list);
	}
	
	@Override
	public CharSequence getDefaultChoice(Object selected) {
		return "";
	}

	/**
	 * 
	 * 
	 */
	public DomainModel getDefaultDomain() {
		if(list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 
	 * 
	 */
	public int getListSize() {
		return list.size();
	}
	
	/**
	 * 
	 * 
	 */
    public void setModelById(String domainId){
        for(DomainModel model : list){
           if(model.getDomainId().equals(domainId)){
              setModelObject(model);
              return;
           }
        }
    }
	
    /**
     * 
     * 
     */
    public DomainModel getSelectedDomain() {
		return getModelObject();
	}
    
	private List<DomainModel> list;
	private List<String> targetFederationList;

	/**
	 * 
	 * 
	 * @author Masaaki Kamiya
	 * @author $Author: t-nakaguchi $
	 * @version $Revision: 596 $
	 */
	private class DomainChoiceRenderer implements IChoiceRenderer<DomainModel> {
		public DomainChoiceRenderer() {
		}

		@Override
		public Object getDisplayValue(DomainModel object) {
			return object.getDomainName();
		}

		@Override
		public String getIdValue(DomainModel object, int index) {
			return object.getDomainId();
		}
		private static final long serialVersionUID = -793615461072422855L;
	}
}
