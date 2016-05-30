/*
 * $Id: RepeatingLanguagePathPanel.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.component.form.panel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.management.web.model.InternalLanguageModel;
import jp.go.nict.langrid.management.web.model.ResourceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.ResourceTypeModel;
import jp.go.nict.langrid.management.web.model.ServiceTypeModel;
import jp.go.nict.langrid.management.web.model.enumeration.LanguagePathType;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.languagepath.LanguagePathModel;
import jp.go.nict.langrid.management.web.utility.LanguagePathUtil;
import jp.go.nict.langrid.management.web.view.page.language.component.form.validator.OtherLanguagePathValidator;
import jp.go.nict.langrid.management.web.view.page.language.component.form.validator.SameInlineLanguagePathValidator;
import jp.go.nict.langrid.management.web.view.page.language.component.form.validator.SameParallelLanguagePathValidator;

import org.apache.commons.lang.ArrayUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class RepeatingLanguagePathPanel extends Panel {
	/**
	 * 
	 * 
	 */
	public RepeatingLanguagePathPanel(String id, ServiceTypeModel typeModel, String metaKey) {
		super(id);
		domainId = typeModel.getDomainId();
		typeId = typeModel.getTypeId();
		repeater = new RepeatingView("repeater");
		add(repeater);
		setPathType(typeModel.getCurrentPathType());
		initialize(metaKey);
		setOutputMarkupId(true);
	}

	public RepeatingLanguagePathPanel(String id, ResourceTypeModel typeModel, String metaKey) {
		super(id);
		domainId = typeModel.getDomainId();
		typeId = typeModel.getResourceTypeId();
		repeater = new RepeatingView("repeater");
		add(repeater);
		setPathType(typeModel.getCurrentPathType());
		initialize(metaKey);
		setOutputMarkupId(true);
		Set<String> set = new HashSet<String>();
		for(ResourceMetaAttributeModel ma : typeModel.getMetaAttrbuteList()) {
			set.add(ma.getAttributeId());
		}
	}

	/**
	* 
	* 
	*/
	public void initialize(final String metaKey) {
		repeater.removeAll();
		addOrReplace(new AjaxSubmitLink("addPathLink") {
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				Iterator it = repeater.iterator();
				while(it.hasNext()) {
					try {
						AbstractLanguagePathPanel path = (AbstractLanguagePathPanel)it
							.next();
						path.setValueFromInput();
						if(repeater.size() == 1) {
							path.setAllVisibled();
						}
					} catch(InvalidLanguageTagException e) {
						e.printStackTrace();
					}
				}
				setLanguagePathComponents(repeater, metaKey);
				setRewriteComponent(target);
			}

			private static final long serialVersionUID = 1L;
		}.setDefaultFormProcessing(false).setVisible(
			!pathType.equals(LanguagePathType.UNKNOWN)));
		setLanguagePathComponents(repeater, metaKey);
		if(isCombinaitionLanguagePathType()) {
			setLanguagePathComponents(repeater, metaKey);
		}
	}

	public LanguagePathModel getLanguagePathModel() throws ServiceManagerException {
		Iterator it = repeater.iterator();
		if(pathType.equals(LanguagePathType.UNKNOWN)) {
			while(it.hasNext()) {
				OtherLanguagePathPanel panel = (OtherLanguagePathPanel)it.next();
				return panel.getPathValueModel();
			}
			return new LanguagePathModel();
		}
		LanguagePathModel model = new LanguagePathModel();
		if(pathType.equals(LanguagePathType.PAIR)) {
			List<LanguagePath> list = new ArrayList<LanguagePath>();
			String key = "";
			String combKey = "";
			while(it.hasNext()) {
				PairLanguagePathPanel panel = (PairLanguagePathPanel)it.next();
				list.add(panel.getPathValue());
				if(panel.isBidirection()) {
					list.add(panel.getPathValue().reverse());
				}
				key = panel.getKey();
				combKey = panel.getCombinationKey();
			}
			try {
				return LanguagePathUtil.convertToLanguagePairModel(
					list.toArray(new LanguagePath[]{}), key, combKey);
			} catch(InvalidLanguageTagException e) {
				throw new ServiceManagerException(e);
			}
		}
		if(pathType.equals(LanguagePathType.COMBINATION)) {
			List<Language> list = new ArrayList<Language>();
			while(it.hasNext()) {
				SingleLanguagePathPanel panel = (SingleLanguagePathPanel)it.next();
				for(Language l : panel.getPathValue().getPath()) {
					list.add(l);
				}
			}
			model.addPath("supportedLanguagePairs_AnyCombination",
				new LanguagePath(list.toArray(new Language[]{})));
			return model;
		}
		while(it.hasNext()) {
			AbstractLanguagePathPanel panel = (AbstractLanguagePathPanel)it.next();
			model.addPath(panel.getKey(), panel.getPathValue());
		}
		return model;
	}

	/**
	 * 
	 * 
	 */
	public void setValueModel(LanguagePathModel model, String metaKey) {
		initialize(metaKey);
		if(pathType.equals(LanguagePathType.UNKNOWN)) {
			Iterator it = repeater.iterator();
			while(it.hasNext()) {
				OtherLanguagePathPanel path = (OtherLanguagePathPanel)it.next();
				StringBuilder sb = new StringBuilder();
				for(String s : model.getOtherLanguages()) {
					sb.append(s);
					sb.append(" ");
				}
				path.setValue(sb.toString());
			}
			return;
		}
		if(pathType.equals(LanguagePathType.COMBINATION)) {
			LanguagePath lp = model.getCombinationPathArray()[0];
			repeater.removeAll();
			for(int i = 0; i < lp.getPath().length; i++) {
				setLanguagePathComponents(repeater, metaKey);
			}
			int i = 0;
			Iterator it = repeater.iterator();
			while(it.hasNext()) {
				SingleLanguagePathPanel panel = (SingleLanguagePathPanel)it.next();
				panel.setPathValue(new LanguagePath(lp.getPath()[i++]), false);
				if(1 < repeater.size()) {
					panel.setAllVisibled();
				}
			}
			return;
		}
		LanguagePath[] paths = model.getAllPath();
		Map<LanguagePath, Boolean> map = new LinkedHashMap<LanguagePath, Boolean>();
		for(LanguagePath path : paths) {
			boolean isBidirection = ArrayUtils.contains(paths, path.reverse());
			if(isBidirection && path.equals(path.reverse())) {
				isBidirection = false;
			}
			if(map.containsKey(path.reverse())) {
				continue;
			}
			map.put(path, isBidirection);
		}
		if(map.size() == 0) {
			map.put(new LanguagePath(InternalLanguageModel.getWildcard()), false);
		}
		int count = map.size();
		if(isCombinaitionLanguagePathType()) {
			count -= 1;
		}
		repeater.removeAll();
		for(int i = 0; i < count; i++) {
			setLanguagePathComponents(repeater, metaKey);
		}
		Iterator it = repeater.iterator();
		Iterator<LanguagePath> valueIt = map.keySet().iterator();
		while(it.hasNext()) {
			AbstractLanguagePathPanel panel = (AbstractLanguagePathPanel)it.next();
			LanguagePath path = valueIt.next();
			panel.setPathValue(path, map.get(path));
			if(1 < repeater.size()) {
				panel.setAllVisibled();
			}
		}
	}

	/**
	 * 
	 * 
	 */
	public AbstractFormValidator getValidatar() {
		if(pathType.equals(LanguagePathType.SINGLE)
				|| pathType.equals(LanguagePathType.COMBINATION)) {
			return new SameParallelLanguagePathValidator(this);
		} else if(pathType.equals(LanguagePathType.UNKNOWN)) {
			return new OtherLanguagePathValidator(this);
		}
		return new SameInlineLanguagePathValidator(this);
	}

	public List<String[]> getNoValidateValueList() {
		Iterator it = repeater.iterator();
		List<String[]> list = new ArrayList<String[]>();
		if(pathType.equals(LanguagePathType.PAIR)) {
			while(it.hasNext()) {
				PairLanguagePathPanel panel = (PairLanguagePathPanel)it.next();
				list.add(panel.getValueFromInput());
				if(panel.isBidirection()) {
					list.add(new String[]{panel.getValueFromInput()[1],
						panel.getValueFromInput()[0]});
				}
			}
			return list;
		}
		while(it.hasNext()) {
			AbstractLanguagePathPanel panel = (AbstractLanguagePathPanel)it.next();
			if(1 < panel.getValueFromInput().length) {
				String[] sts = new String[panel.getValueFromInput().length];
				int i = 0;
				for(String l : panel.getValueFromInput()) {
					sts[i++] = l;
				}
				list.add(sts);
			} else {
				list.add(new String[]{panel.getValueFromInput()[0]});
			}
		}
		return list;
	}

	public String getNoValidateOtherValue() {
		if(pathType.equals(LanguagePathType.UNKNOWN)) {
			Iterator it = repeater.iterator();
			while(it.hasNext()) {
				OtherLanguagePathPanel panel = (OtherLanguagePathPanel)it.next();
				return panel.getInput();
			}
		}
		return "";
	}

	/**
	* 
	* 
	*/
	public void setPathType(LanguagePathType pathType) {
		this.pathType = pathType;
	}

	private void setLanguagePathComponents(final RepeatingView repeater, String metaKey) {
		AbstractLanguagePathPanel panel = makeLanguagePathPanel(pathType, metaKey);
		repeater.add(panel);
		AjaxSubmitLink link = new AjaxSubmitLink("removePathLink") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				if(!isRemovableComponent()) {
					return;
				}
				repeater.remove(getParent());
				Iterator ite = repeater.iterator();
				while(ite.hasNext()) {
					AbstractLanguagePathPanel panel = (AbstractLanguagePathPanel)ite
						.next();
					if(repeater.size() == 1) {
						panel.switchVisible();
					}
				}
				setRewriteComponent(target);
			}

			private static final long serialVersionUID = 1L;
		};
		link.setDefaultFormProcessing(false)
			.setVisible(!pathType.equals(LanguagePathType.UNKNOWN)
				).setOutputMarkupPlaceholderTag(true);
		panel.add(link);
		if(repeater.size() == 1) {
			link.setVisible(false);
		}
		panel.addVisibleComponent(link);
	}

	private AbstractLanguagePathPanel makeLanguagePathPanel(LanguagePathType type, String metaKey) {
		if(type.equals(LanguagePathType.SINGLE)) {
			return new SingleLanguagePathPanel(repeater.newChildId());
		} else if(type.equals(LanguagePathType.COMBINATION)) {
			return new SingleLanguagePathPanel(repeater.newChildId());
		} else if(type.equals(LanguagePathType.PAIR)) {
			return new PairLanguagePathPanel(repeater.newChildId());
		} else if(type.equals(LanguagePathType.MULTI)) {
			return new MultihopTranslationLanguagePathPanel(repeater.newChildId());
		} else if(type.equals(LanguagePathType.BACKTRANSLATION)) {
			return new BackTranslationLanguagePathPanel(repeater.newChildId());
		} else if(type.equals(LanguagePathType.UNKNOWN)) {
			return new OtherLanguagePathPanel(repeater.newChildId(), metaKey);
		}
		return new SingleLanguagePathPanel(repeater.newChildId());
	}

	private void setRewriteComponent(AjaxRequestTarget target) {
		target.addComponent(this);
	}

	private boolean isRemovableComponent() {
		if(pathType.equals(LanguagePathType.COMBINATION)) {
			return repeater.size() > 2;
		}
		return repeater.size() != 1;
	}

	private boolean isCombinaitionLanguagePathType() {
		return pathType.equals(LanguagePathType.COMBINATION);
	}

	//   private String metaKey;
	private String domainId;
	private String typeId;
	private LanguagePathType pathType;
	private RepeatingView repeater;
}
