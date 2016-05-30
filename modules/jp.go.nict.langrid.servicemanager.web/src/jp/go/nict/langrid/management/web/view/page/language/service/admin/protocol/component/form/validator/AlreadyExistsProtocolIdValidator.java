/*
 * $Id: AlreadyExistsProtocolIdValidator.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.admin.protocol.component.form.validator;

import jp.go.nict.langrid.management.web.model.ProtocolModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;

import org.apache.wicket.Component;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class AlreadyExistsProtocolIdValidator implements IValidator<String> {
	/**
	 * 
	 * 
	 */
	public AlreadyExistsProtocolIdValidator(String gridId, Component component) {
		this.component = component;
		this.gridId = gridId;
	}
	
	public void validate(IValidatable<String> validatable) {
		try {
			String serviceTypeId = validatable.getValue();
			ProtocolModel model = ServiceFactory.getInstance().getProtocolService(gridId).get(serviceTypeId);
			if(model != null) {
				component.error(MessageManager.getMessage(
					"LanguageGridOperator.protocol.error.ProtocolIdAlreadyExists"
				   , component.getLocale()));
			}
		} catch(ServiceManagerException e) {
			component.error(MessageManager.getMessage(
					"ProvidingServices.language.service.error.SystemError", component.getLocale()));
		}
	}
	
	private Component component;
	private String gridId;
}
