package jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
public class EndpointAddressProtocol {
	public EndpointAddressProtocol(){
	}

	public EndpointAddressProtocol(String invocationName, String protocol) {
		this.endpointAddress = invocationName;
		this.protocol = protocol;
	}

	@Override
	public boolean equals(Object value){
		return EqualsBuilder.reflectionEquals(this, value);
	}

	@Override
	public int hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public String getEndpointAddress() {
		return endpointAddress;
	}
	
	public void setEndpointAddress(String invocationName) {
		this.endpointAddress = invocationName;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	@Id
	String endpointAddress;

	String protocol;
}
