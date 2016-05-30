/*
 * $Id: ManualPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.other;

import java.io.IOException;

import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.utility.FileUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.session.ServiceManagerSession;
import jp.go.nict.langrid.management.web.view.utility.RequestResponseUtil;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.util.file.File;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ManualPage extends ServiceManagerPage{
	/**
	 * 
	 * 
	 */
	public ManualPage(){
		add(new Label("languageEnglish", manualEnName));
		StatelessLink link = new StatelessLink("manual"){
			@Override
			public void onClick(){
				try{
					File manual = new File(
						getServletContext().getRealPath("/") + MessageUtil.MANUAL_DIR + "/" + manualEnName);
					RequestResponseUtil.setRequestForFile(getRequestCycle(), manual, "");
				}catch(IOException e){
					ServiceManagerSession session = (ServiceManagerSession)getSession();
					LogWriter.writeError(session.getUserId(), e, getPageClass());
					error(MessageManager.getMessage("Manual.error.download", getLocale()));
				}
			}
			
			private static final long serialVersionUID = 1L;
		};
		Image img = new Image("manualImg"){
			@Override
			protected void onComponentTag(ComponentTag tag){
				tag.put("src", "images/common/pdf.gif");
			}
		};
		link.add(img);
		add(link);
		long fileSize = FileUtil.getFileSizeToKB(
			getServletContext().getRealPath("/") + MessageUtil.MANUAL_DIR + "/" + manualEnName);
		if(fileSize == 0) {
			add(new Label("fileSizeEnglish", "Not found"));
			link.setEnabled(false);
		}else{
			add(new Label("fileSizeEnglish", String.valueOf(fileSize)));
		}
	}
	
	private String manualEnName = MessageManager.getMessage(
			"Manual.label.FileNameEnglish", getLocale());
	private static final long serialVersionUID = 1L;
}