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
package jp.go.nict.langrid.commons.beanutils;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.junit.Test;

public class StringArrayToBeanTransformerTest{
	public static enum NodeType{
		CORE, SERVICE
	}
	static class Node{
		private String ownerUserId;
		private String nodeId;
		private String nodeName;
		private NodeType nodeType;
		private URL url;
		private String os;
		private String cpu;
		private String memory;
		public String getOwnerUserId() {
			return ownerUserId;
		}
		public void setOwnerUserId(String ownerUserId) {
			this.ownerUserId = ownerUserId;
		}
		public String getNodeId() {
			return nodeId;
		}
		public void setNodeId(String nodeId) {
			this.nodeId = nodeId;
		}
		public String getNodeName() {
			return nodeName;
		}
		public void setNodeName(String nodeName) {
			this.nodeName = nodeName;
		}
		public NodeType getNodeType() {
			return nodeType;
		}
		public void setNodeType(NodeType nodeType) {
			this.nodeType = nodeType;
		}
		public URL getUrl() {
			return url;
		}
		public void setUrl(URL url) {
			this.url = url;
		}
		public String getOs() {
			return os;
		}
		public void setOs(String os) {
			this.os = os;
		}
		public String getCpu() {
			return cpu;
		}
		public void setCpu(String cpu) {
			this.cpu = cpu;
		}
		public String getMemory() {
			return memory;
		}
		public void setMemory(String memory) {
			this.memory = memory;
		}
	}

	@Test
	public void test() throws Exception{
		String[] values = {
				"user1", "nict1", "Core Node in NICT", "CORE"
				, "http://langrid.nict.go.jp/langrid-1.2/"
				, "Fedora Core 6.0", "2.13GHz", "2GB"			
		};
		Converter c = new Converter();
		Node n = new StringArrayToBeanTransformer<Node>(Node.class, c, new String[]{
				"ownerUserId", "nodeId", "nodeName"
				, "nodeType", "url", "os", "cpu"
				, "memory", "specialNotes"
				, "basicAuthUserName"
				, "basicAuthPassword"}
				).transform(values);
		assertEquals("user1", n.getOwnerUserId());
		assertEquals("nict1", n.getNodeId());
		assertEquals("Core Node in NICT", n.getNodeName());
		assertEquals(NodeType.CORE, n.getNodeType());
		assertEquals(new URL("http://langrid.nict.go.jp/langrid-1.2/"), n.getUrl());
		assertEquals("Fedora Core 6.0", n.getOs());
		assertEquals("2.13GHz", n.getCpu());
		assertEquals("2GB", n.getMemory());
	}
}
