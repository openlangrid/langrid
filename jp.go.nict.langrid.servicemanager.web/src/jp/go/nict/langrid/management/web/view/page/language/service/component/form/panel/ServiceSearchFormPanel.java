/*
* $Id: ServiceSearchFormPanel.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.language.util.LanguagePathUtil;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.InternalLanguageModel;
import jp.go.nict.langrid.management.web.model.ServiceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.enumeration.LanguagePathType;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.model.LangridSearchCondition;
import jp.go.nict.langrid.management.web.view.page.language.component.form.panel.LanguageFormPanel;
import jp.go.nict.langrid.management.web.view.page.language.component.form.panel.LanguagePairFormPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.choice.ProvisionControlDropDownChoice;
import jp.go.nict.langrid.management.web.view.page.language.service.component.choice.ServiceTypeDropDownChoice;
import jp.go.nict.langrid.management.web.view.page.language.service.component.choice.UsingServiceDropDownChoice;
import jp.go.nict.langrid.management.web.view.page.language.service.component.text.SearchTextField;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.string.Strings;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class ServiceSearchFormPanel extends AbstractSearchFormPanel<LangridSearchCondition>{
	/**
	 * 
	 * 
	 * @throws ServiceManagerException
	 */
	public ServiceSearchFormPanel(String gridId, String componentId) throws ServiceManagerException{
		super(componentId, gridId);
		setOutputMarkupId(true);
	}

	@Override
	public LangridSearchCondition getSearchCondition(){
		LangridSearchCondition condition = new LangridSearchCondition();
		// add service name condition
		if(!serviceNameField.getValue().equals("")){
			condition.putOrReplaceCondition(
					"serviceName", serviceNameField.getValue(), MatchingMethod.PARTIAL);
		}
		// add login user can call service condition
		if(accessRightCheck.getModelObject() != null){
			condition.setScope(accessRightCheck.getModelObject() ? Scope.ACCESSIBLE : Scope.ALL);
		}
		// add service type condition
		if(serviceTypeField.getModelObject() != null) {
			String typeId = serviceTypeField.getModelObject().getTypeId();
			if(typeId.equals("Other")){
				typeId = null;
			}
			condition.putOrReplaceCondition("serviceTypeId", typeId, MatchingMethod.COMPLETE);
		}
		// add Purpose of Use condition
		if(provision.getModelObject() != null) {
			condition.putOrReplaceCondition(
					"allowedAppProvision", provision.getModelObject().toString(), MatchingMethod.IN);
		}
		// add Type of Application Control condition
		if(usingService.getModelObject() != null) {
			condition.putOrReplaceCondition(
					"allowedUse", usingService.getModelObject().toString(), MatchingMethod.IN);
		}
		// add language path conditions
		if(languagePath.getModelObject() != null){
			if( ! isAllWildcard(languagePath.getModelObject())){
				if(serviceTypeField.getModelObject() == null) {
					condition.putOrReplaceCondition(
						"supportedAllLanguages"
						, makeLanguagePathString(languagePath.getModelObject())
						, MatchingMethod.LANGUAGEPATH);
				} else {
					Collection<ServiceMetaAttributeModel> c = serviceTypeField.getModelObject().getMetaAttrbuteList();
					if(c == null || c.isEmpty()) {
						condition.putOrReplaceCondition(
							"supportedAllLanguages"
							, makeLanguagePathString(languagePath.getModelObject())
							, MatchingMethod.LANGUAGEPATH);
					}else{
						Iterator<ServiceMetaAttributeModel> ite = c.iterator();
						Set<String> attrIdSet = new HashSet<String>();
						while(ite.hasNext()){
							String attrId = ite.next().getAttributeId();
							if(attrId != null && ! attrIdSet.contains(attrId)){
								attrIdSet.add(attrId.split("_")[0]);
							}
						}
						for(String s : attrIdSet){
							condition.putOrReplaceCondition(
								s, makeLanguagePathString(languagePath.getModelObject())
								, MatchingMethod.LANGUAGEPATH);
						}
					}
				}
			}
		}
		return condition;
	}

	/**
	 * 
	 * 
	 */
	public void setVisibleLogin(boolean isLogin){
		accessRightContainer.setVisible(isLogin);
	}

	protected Component getRewriteComponent(){
		return this;
	}

	@Override
	protected void initialize(final Form form, String gridId) throws ServiceManagerException{
		languagePathId = "languagePath";
		form.add(serviceNameField = new SearchTextField("serviceNameField"));
		form.add(serviceTypeField = new ServiceTypeDropDownChoice(gridId , "serviceTypeField"){
			@Override
			public CharSequence getDefaultChoice(Object selected){
				String option = getLocalizer()
						.getString(getId() + ".nullValid", this, "");
				if(Strings.isEmpty(option)){
					option = getLocalizer().getString("nullValid", this, "");
				}
				// The <option> tag buffer
				AppendingStringBuffer buffer = new AppendingStringBuffer(32 + option
						.length());
				// Add option tag
				buffer.append("\n<option");
				// Add body of option tag
				buffer.append(" value=\"\">").append(option).append("</option>");
				return buffer;
			}

			private static final long serialVersionUID = 1L;
		});

		serviceTypeField.add(new AjaxFormComponentUpdatingBehavior("onchange"){
			@Override
			protected void onUpdate(AjaxRequestTarget target){
				if(serviceTypeField.getInput() != null && !serviceTypeField.getInput().equals("")){
					form.addOrReplace(
							languagePath = getLanguagePathPanel(
									serviceTypeField.getModelObject().getTypeSet()));
				}else{
					form.addOrReplace(languagePath = new LanguageFormPanel(
							languagePathId));
				}
				languagePath.add(new WebMarkupContainer("removePathLink")
						.setVisible(false));
				target.addComponent(getRewriteComponent());
			}

			private static final long serialVersionUID = 1L;
		});
	   Set<LanguagePathType> set = new HashSet<LanguagePathType>();
	   set.add(LanguagePathType.SINGLE);
	   form.add(languagePath = getLanguagePathPanel(set));
		
		languagePath.add(new WebMarkupContainer("removePathLink").setVisible(false));
		form.add(accessRightContainer = new WebMarkupContainer("accessRightContainer"));
		accessRightContainer.add(accessRightCheck = new CheckBox("accessRightCheck",
				new Model<Boolean>(false)));
		form.add(provision = new ProvisionControlDropDownChoice("provision"));
		form.add(usingService = new UsingServiceDropDownChoice("usingService"));
	}

	private FormComponentPanel getLanguagePathPanel(Set<LanguagePathType> set){
	   for(LanguagePathType type : set){
	      if(type.equals(LanguagePathType.SINGLE)){
	         return new LanguageFormPanel(languagePathId);
	      }else if(type.equals(LanguagePathType.UNKNOWN)){
	         return new LanguageFormPanel(languagePathId);
	      }else{
	         return new LanguagePairFormPanel(languagePathId);
	      }
	   }
	   return new LanguageFormPanel(languagePathId);
	}
	
	private String makeLanguagePathString(LanguagePath[] paths){
	   return LanguagePathUtil.encodeLanguagePathArray(paths);
	}
	
	private boolean isAllWildcard(LanguagePath[] paths){
		boolean has = false;
		for(LanguagePath path : paths){
			if(path.getSource().equals(InternalLanguageModel.getWildcard())){
				has = true;
				if(path.getTarget() != null && ! path.getTarget().equals(InternalLanguageModel.getWildcard())){
					has = false;
				}
			}
		}
		return has;
	}

	private CheckBox accessRightCheck;
	private WebMarkupContainer accessRightContainer;
	private FormComponentPanel<LanguagePath[]> languagePath;
	private String languagePathId;
	private SearchTextField serviceNameField;
	private ServiceTypeDropDownChoice serviceTypeField;
	private ProvisionControlDropDownChoice provision;
	private UsingServiceDropDownChoice usingService;

	private static final long serialVersionUID = 1L;
}
