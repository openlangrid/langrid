/*
 * $Id: AlreadyExistsServiceIdV
alidator.java 9280 2009-04-20 04:57:40Z Masaaki Kamiya $
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
package jp.go.nict.langrid.management.web.view.page.language.service.atomic.component.form.validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.go.nict.langrid.management.web.model.ServiceEndpointModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version
 */
public class DuplicatedEndpointValidator implements IValidator<String>{
	/**
	 * 
	 * 
	 */
	public DuplicatedEndpointValidator(FormComponent<String> component, String gridId, String serviceId){
		this(component, gridId, serviceId, new HashSet<ServiceEndpointModel>());
	}

	/**
	 * 
	 * 
	 */
	public DuplicatedEndpointValidator(
			FormComponent<String> component, String gridId, String serviceId, Set<ServiceEndpointModel> excludeSet)
	{
		this.component = component;
		this.serviceId = serviceId;
		this.excludeSet = excludeSet;
		this.gridId = gridId;
	}

	public void validate(IValidatable<String> arg0){
		try{
		   for(ServiceEndpointModel exclude : excludeSet){
				if(arg0.getValue().equals(exclude.getUrl())){
					return;
				}
			}
			
			List<ServiceEndpointModel> list = ServiceFactory.getInstance().getAtomicServiceService(
					gridId).getEndpointList(serviceId);
			for(ServiceEndpointModel ep : list){
				if(ep.getUrl().equals(arg0.getValue())){
					component.error(MessageManager.getMessage(
							"ProvidingServices.language.service.error.endpoint.Duplicated"
							, component.getLocale()));
				}
			}
		}catch(ServiceManagerException e){
			component.error(MessageManager.getMessage(
					"ProvidingServices.language.service.error.SystemError", component.getLocale()));
		}
	}	

	private FormComponent<String> component;
	private String serviceId;
	private Set<ServiceEndpointModel> excludeSet;
	private String gridId;
	
	private static final long serialVersionUID = 1L;
}
