/*
 * $Id: MessageUtil.java 1506 2015-03-02 16:03:34Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.utility.resource;

import javax.servlet.ServletContext;

import jp.go.nict.langrid.commons.parameter.ParameterLoader;
import jp.go.nict.langrid.commons.parameter.ParameterRequiredException;
import jp.go.nict.langrid.commons.ws.param.ServletContextParameterContext;

/**
 *  
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1506 $
 */
public class MessageUtil {

	/**
	 *  
	 */
	public static final void setContext(ServletContext context)
			throws ParameterRequiredException {
		new ParameterLoader().load(cm, new ServletContextParameterContext(context));
	}

	// get from context
	/**
	 *  
	 */
	public static final String getCoreNodeUrl() {
		return cm.getCorenodeUrl();
	}

	public static final String getActiveBpelUrl() {
		return cm.getActiveBpelUrl();
	}

	public static final String getSelfNodeId() {
		return cm.getSelfNodeId();
	}

	/**
	 *  
	 */
	public static final String getServiceManagerOperatingOrganization() {
		return cm.getServiceManagerOperatingOrganization();
	}

	/**
	 *  
	 */
	public static final String getServiceManagerCopyright() {
		return cm.getServiceManagerCopyright();
	}

	public static final String getServiceGridName() {
		return cm.getServiceGridName();
	}

	public static final String getServiceGridId() {
		return cm.getServiceGridId();
	}

	public static final String getOperatorUserId() {
		return cm.getOperatorUserId();
	}
	
	public static final boolean isOpenLangrid(){
		return cm.isOpenlangrid();
	}

	private static final ContextInitialMessage cm = new ContextInitialMessage();
	// Job
	public static final boolean IS_INVOKE_JOB_ON_DAY = Boolean
			.valueOf(MessageManager.getMessage("job.day.invoke"));
	public static final boolean IS_INVOKE_JOB_ON_REGULAR = Boolean
			.valueOf(MessageManager.getMessage("job.regular.invoke"));
	public static final boolean IS_INVOKE_JOB_ON_FEWMINUTES = Boolean
			.valueOf(MessageManager.getMessage("job.fewminutes.invoke"));
	public static final boolean IS_INVOKE_JOB_ON_TIME = Boolean
			.valueOf(MessageManager.getMessage("job.time.invoke"));

	// File upload
	public static final String LIMIT_BPEL_FILE_SIZE = MessageManager
			.getMessage("LimitBPELFileSize");
	public static final String LIMIT_UPLOAD_FILE_SIZE = MessageManager
			.getMessage("LimitUploadFileSize");
	public static final String LIMIT_WSDL_FILE_SIZE = MessageManager
			.getMessage("LimitWSDLFileSize");

	// cache
	public static final int CACHE_TIME = Integer.valueOf(MessageManager
			.getMessage("cache.time"));
	public static final boolean USE_CACHE = Boolean.valueOf(MessageManager
			.getMessage("cache.use"));
	public static final String MANUAL_DIR = MessageManager
			.getMessage("Manual.file.Dir");
	public static final String ISO_LANGUAGE_CODE_URL = "http://www.loc.gov/standards/iso639-2/php/code_list.php";
}
