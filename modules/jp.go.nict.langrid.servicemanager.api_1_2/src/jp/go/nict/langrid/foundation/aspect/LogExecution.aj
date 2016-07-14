/*
 * $Id: LogExecution.aj 302 2010-12-01 02:49:42Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2009 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 2.1 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package jp.go.nict.langrid.foundation.aspect;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.aspectj.lang.reflect.CodeSignature;

import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.foundation.AbstractLangridService;
import jp.go.nict.langrid.foundation.UserChecker;
import jp.go.nict.langrid.foundation.annotation.Log;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 302 $
 */
public aspect LogExecution {
	pointcut pc()
		: execution(@Log * (jp.go.nict.langrid.foundation..* && AbstractLangridService+).*(..))
		;

	before(AbstractLangridService self)
		: pc() && this(self)
	{
		try{
			CodeSignature sig = (CodeSignature)thisJoinPoint.getSignature();
			Class<?>[] types = sig.getParameterTypes();
			String[] names = sig.getParameterNames();
			Object[] args = thisJoinPoint.getArgs();
			Method m = thisJoinPoint.getThis().getClass().getDeclaredMethod(
					sig.getName(), types
					);
			Log l = m.getAnnotation(Log.class);
			if(l == null) return;
			Set<String> maskParameters = new HashSet<String>(Arrays.asList(l.maskParameters()));
			StringBuilder argStrings = new StringBuilder();
			for(int i = 0; i < args.length; i++){
				if(i != 0){
					argStrings.append(", ");
				}
				if(maskParameters.contains(names[i])){
					argStrings.append("*****");
				} else{
					Object a = args[i];
					if(a.getClass().isArray()){
						argStrings.append(ToStringBuilder.reflectionToString(a));
					} else if(a.getClass().equals(String.class)){
						argStrings.append("\"");
						argStrings.append(args[i].toString());
						argStrings.append("\"");
					} else if(a instanceof Calendar){
						argStrings.append("\"");
						argStrings.append(CalendarUtil.formatToDefault((Calendar)a));
						argStrings.append("\"");
					} else if(args[i] != null){
						argStrings.append(args[i].toString());
					} else{
						argStrings.append("null");
					}
				}
			}
			String userId = UserChecker.get(self).getUserId();
			logger.info(String.format(
					"userId: %s, operation: %s %s.%s(%s)"
					, userId, m.getReturnType().getName()
					, sig.getDeclaringTypeName(), sig.getName()
					, argStrings.toString()
					));
		} catch(DaoException e){
			logger.log(Level.SEVERE, "failed to access database." , e);
		} catch(NoSuchMethodException e){
			throw new RuntimeException(e);
		}
	}

	private static Logger logger = Logger.getLogger(
			LogExecution.class.getName());
}
