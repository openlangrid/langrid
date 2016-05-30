/*
 * $Id: MessageManager.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import jp.go.nict.langrid.management.web.log.LogWriter;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class MessageManager{
	public static String getLangridErrorMessage(String errorCode, Locale locale) {
		String message = getMessage("message.error." + errorCode + ".Navigation", locale);
		if(message == null){
			return "";
		}
		return message;
	}
	
	/**
	 * 
	 * 
	 */
	public static String getMessage(String key){
		return getMessage(key, LOCALE, (Object[])null);
	}

	/**
	 * 
	 * 
	 */
	public static String getMessage(String key, Locale locale){
		return getMessage(key, locale, new Object[]{});
	}

	/**
	 * 
	 * 
	 */
	public static String getMessage(String key, Locale locale, Object... params) {
		Set<ResourceBundle> resourceBundles = getMessageManager(locale).getResourceBundles();
		for(ResourceBundle resourceBundle : resourceBundles) {
			try {
				return getMessage(resourceBundle, key, params);
			}catch(MissingResourceException e) {
				// search next message resource
			}
		}
		LogWriter.writeWarn("Service Manager system", "Message '" + key
				+ "' is not found in Message Resources.", MessageManager.class);
		return "";
	}

	/**
	 * 
	 * 
	 */
	public static String getMessage(String key, Map<String, String> params){
		Set<ResourceBundle> resourceBundles = getMessageManager(
				LOCALE).getResourceBundles();
		for(ResourceBundle resourceBundle : resourceBundles){
			try{
				String message = resourceBundle.getString(key);
				if(params == null){
					return message;
				}
				for(String paramKey : params.keySet()){
					if(params.get(paramKey) == null){
						continue;
					}
					message = message.replaceAll("\\$\\{" + paramKey + "\\}", params.get(paramKey));
				}
				return message;
			}catch(MissingResourceException e){
				// search next message resource
			}
		}
		LogWriter.writeWarn("Service Manager system", "Message '" + key
				+ "' is not found in Message Resources.", MessageManager.class);
		return "";
	}
	
	public static void setLocale(Locale locale){
		LOCALE = locale;
	}

	/**
	 * 
	 * 
	 */
	private MessageManager(){
		// load main property file.
		ResourceBundle rb = ResourceBundle.getBundle("properties.service_manager", LOCALE);
		resourceBundles.add(rb);
		// load localized message file.
		resourceBundles.add(ResourceBundle.getBundle(
				"jp.go.nict.langrid.management.web.view." + rb.getString("baseLocaleFile"), LOCALE));
		// load other property files
		String others = rb.getString("propertyFiles");
		if(others != null && ! others.equals("")){
		   for(String name : others.replaceAll("\\s+", "").split(",")){
		      resourceBundles.add(ResourceBundle.getBundle("properties." + name, LOCALE));
		   }
		}
	}

	private Set<ResourceBundle> getResourceBundles(){
		return resourceBundles;
	}

	private Set<ResourceBundle> resourceBundles = new HashSet<ResourceBundle>();

	private static String getMessage(ResourceBundle resourceBundle, String key, Object... params){
		String message = resourceBundle.getString(key);
		if(params == null){
			return message;
		}
		return MessageFormat.format(message, params);
	}
	
	private static MessageManager getMessageManager(Locale locale){
		if(messageManager == null || !Locale.getDefault().equals(locale)){
			Locale.setDefault(locale);
			messageManager = new MessageManager();
		}
		return messageManager;
	}

	private static Locale LOCALE = Locale.ENGLISH;
	private static MessageManager messageManager;
}
