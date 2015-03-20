/*
 * $Id: MultidirectionalLanguagePathPanel.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.component.panel;

import java.util.Collection;

import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.management.web.model.enumeration.MetaAttribute;
import jp.go.nict.langrid.management.web.model.languagepath.LanguagePathModel;
import jp.go.nict.langrid.management.web.utility.LanguagePathUtil;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class MultidirectionalLanguagePathPanel extends Panel {
	public MultidirectionalLanguagePathPanel(String componentId, LanguagePathModel model)
	throws InvalidLanguageTagException
	{
		super(componentId);
		add(getCombinationPathViewComponent(model.getCombinationPathArray()));
		add(getMultihopLanguagePathLabel(model.getMultihopPathArray()));
		add(getMultidirectionalPathViewComponent(model.getDirectionalPathArray()));
		add(getSingleLanguagePathLabel(model.getSinglePathArray()));
		add(getOtherLanguageLabel(model.getOtherLanguages()));
		add(new Label("noLanguage", "-").setVisible(addCount == 0));
	}

	private Component getMultihopLanguagePathLabel(LanguagePath[] paths)
	throws InvalidLanguageTagException
	{
		WebMarkupContainer container = new WebMarkupContainer("multihopContainer");
		RepeatingView rv = new RepeatingView("multihops");
		if(paths != null) {
			for(LanguagePath path : paths) {
				WebMarkupContainer wmc = new WebMarkupContainer(rv.newChildId());
				rv.add(wmc.add(new Label("multihopLanguages"
						, LanguagePathUtil.makeLanguagePathString(new LanguagePath[]{path}
						, MetaAttribute.SUPPORTEDLANGUAGEPATHS_PATHLIST, getLocale())
					).setRenderBodyOnly(true)));
			}
		}
		container.add(rv);
		container.setVisible(rv.size() != 0);
		addCount += rv.size();
		return container;
	}

	private Component getOtherLanguageLabel(Collection<String> languages) {
		WebMarkupContainer container = new WebMarkupContainer("otherContainer");
		container.setVisibilityAllowed(languages.size() != 0);

		StringBuilder sb = new StringBuilder();
		if(languages != null) {
			for(String language : languages) {
				sb.append(language);
				sb.append(", ");
			}
		}
		container.add(new Label("otherLanguage", sb.toString().replaceAll(", $", "")));
		addCount += languages.size();
		return container;
	}

	private Component getSingleLanguagePathLabel(LanguagePath[] paths)
	throws InvalidLanguageTagException
	{
		WebMarkupContainer container = new WebMarkupContainer("singleContainer");
		RepeatingView rv = new RepeatingView("singles");
		if(paths != null) {
			WebMarkupContainer wmc = new WebMarkupContainer(rv.newChildId());
			rv.add(wmc.add(new Label("singleLanguages"
					, LanguagePathUtil.makeLanguagePathString(paths
						, MetaAttribute.SUPPORTEDLANGUAGES, getLocale())
				).setRenderBodyOnly(true)));
		}
		container.add(rv);
		container.setVisible(rv.size() != 0);
		addCount += rv.size();
		return container;
	}

	private Component getCombinationPathViewComponent(LanguagePath[] paths)
	throws InvalidLanguageTagException
	{
		WebMarkupContainer container = new WebMarkupContainer("combinationContainer");
		RepeatingView rv = new RepeatingView("combinations");
		if(paths != null) {
			for(LanguagePath path : paths) {
				WebMarkupContainer wmc = new WebMarkupContainer(rv.newChildId());
				rv.add(wmc.add(new Label("combinationPairs"
					, LanguagePathUtil.makeLanguagePathString(
						new LanguagePath[]{path}
						 , MetaAttribute.SUPPORTEDLANGUAGEPAIRS_ANYCOMBINATION
						 , getLocale())
					).setRenderBodyOnly(true)));
			}
		}
		container.add(rv);
		container.setVisible(rv.size() != 0);
		addCount += rv.size();
		return container;
	}

	private Component getMultidirectionalPathViewComponent(
		Collection<LanguagePath[]> pathList)
	throws InvalidLanguageTagException
	{
		WebMarkupContainer container = new WebMarkupContainer("multidirectionalContainer");
		RepeatingView rv = new RepeatingView("multidirectionals");
		if(pathList != null) {
			for(LanguagePath[] paths : pathList) {
				WebMarkupContainer wmc = new WebMarkupContainer(rv.newChildId());
				rv.add(wmc.add(new Label("multidirectionalPairs"
					, LanguagePathUtil.makeLanguagePathString(
						paths, MetaAttribute.SUPPORTEDLANGUAGEPAIRS_PAIRLIST
						, getLocale())
					).setRenderBodyOnly(true)));
			}
		}
		container.add(rv);
		container.setVisible(rv.size() != 0);
		addCount += rv.size();
		return container;
	}

	private int addCount = 0;
}
