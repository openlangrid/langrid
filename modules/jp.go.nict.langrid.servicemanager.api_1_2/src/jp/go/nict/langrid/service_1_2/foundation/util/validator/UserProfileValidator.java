/*
 * $Id: UserProfileValidator.java 225 2010-10-03 00:23:14Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
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
package jp.go.nict.langrid.service_1_2.foundation.util.validator;

import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserProfile;
import jp.go.nict.langrid.service_1_2.util.validator.AbstractObjectValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.service_1_2.util.validator.URLValidator;

public class UserProfileValidator 
	extends AbstractObjectValidator<UserProfileValidator, UserProfile>
{
	/**
	 * 
	 * 
	 */
	public UserProfileValidator(
			String parameterName, UserProfile profile)
	{
		super(parameterName, profile);
		if(profile == null) return;
		organization = new StringValidator(
				parameterName + ".displayName"
				, profile.getOrganization()
				);
	}

	@Override
	public UserProfileValidator notNull()
			throws InvalidParameterException
	{
		super.notNull();
		organization.notNull();
		return this;
	}

	/**
	 * 
	 * 
	 */
	public UserProfileValidator notEmpty()
			throws InvalidParameterException
	{
		organization.notNull().notEmpty();
		representative.notNull().notEmpty();
		emailAddress.notNull().notEmpty();
		homepageUrl.notNull().validAsURL();
		address.notNull().notEmpty();
		return this;
	}

	private StringValidator organization;
	private StringValidator representative;
	private StringValidator emailAddress;
	private URLValidator homepageUrl;
	private StringValidator address;
}
