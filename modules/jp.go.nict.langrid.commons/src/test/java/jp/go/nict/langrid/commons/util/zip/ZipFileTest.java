/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2013 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.util.zip;

import java.io.File;

import jp.go.nict.langrid.commons.io.FileUtil;

import org.junit.Assert;

public class ZipFileTest {
//	@Test
	public void test() throws Exception{
		File workDir = new File(new File("test"), "work");
		workDir.mkdirs();
		System.out.println(workDir.getAbsolutePath());
		for(File f : workDir.listFiles()){
			boolean r = FileUtil.forceDelete(f);
			if(!r){
				System.out.println("ERROR: " + f.getAbsolutePath());
			}
			Assert.assertTrue(r);
		}
		ZipFileUtil.unzip(new File("test/data/test.zip"), new File("test/work"));
		Assert.assertTrue(new File("test/work/work").exists());
		Assert.assertTrue(new File("test/work/work").isDirectory());
		Assert.assertTrue(new File("test/work/work/data").exists());
		Assert.assertTrue(new File("test/work/work/data").isDirectory());
		Assert.assertTrue(new File("test/work/work/data/build.xml").exists());
		Assert.assertTrue(new File("test/work/work/data/build.xml").isFile());
		Assert.assertTrue(new File("test/work/work/ZipFileUtil.java").exists());
		Assert.assertTrue(new File("test/work/work/ZipFileUtil.java").isFile());
	}
}
