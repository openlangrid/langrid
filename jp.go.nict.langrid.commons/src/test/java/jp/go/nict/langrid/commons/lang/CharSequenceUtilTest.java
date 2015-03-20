/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2013 Language Grid Project.
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
package jp.go.nict.langrid.commons.lang;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;

import org.junit.Assert;
import org.junit.Test;

public class CharSequenceUtilTest {
	@Test
	public void test() throws Exception{
		Appendable ap = new StringBuilder();
		Assert.assertEquals("aaaaaaaaaa", ap.append(
				CharSequenceUtil.repeat('a', 10)
				).toString());
	}

	@Test
	public void test_OutputStreamWriter() throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter w = new OutputStreamWriter(baos, "UTF-8");
		try{
			Appendable ap = w;
			ap.append(CharSequenceUtil.repeat('a', 10));
			w.flush();
			Assert.assertEquals("aaaaaaaaaa", new String(baos.toByteArray()));
		} finally{
			w.close();
		}
	}
}
