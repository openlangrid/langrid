/**
 * ServiceDeploymentAlreadyExistsException.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.foundation.servicemanagement;

public class ServiceDeploymentAlreadyExistsException  extends jp.go.nict.langrid.ws_1_2.LangridException  implements java.io.Serializable {
    private java.lang.String nodeId;

    private java.lang.String serviceId;

    private java.lang.String servicePath;

    public ServiceDeploymentAlreadyExistsException() {
    }

    public ServiceDeploymentAlreadyExistsException(
           java.lang.String description,
           java.lang.String nodeId,
           java.lang.String serviceId,
           java.lang.String servicePath) {
        super(
            description);
        this.nodeId = nodeId;
        this.serviceId = serviceId;
        this.servicePath = servicePath;
    }


    /**
     * Gets the nodeId value for this ServiceDeploymentAlreadyExistsException.
     * 
     * @return nodeId
     */
    public java.lang.String getNodeId() {
        return nodeId;
    }


    /**
     * Sets the nodeId value for this ServiceDeploymentAlreadyExistsException.
     * 
     * @param nodeId
     */
    public void setNodeId(java.lang.String nodeId) {
        this.nodeId = nodeId;
    }


    /**
     * Gets the serviceId value for this ServiceDeploymentAlreadyExistsException.
     * 
     * @return serviceId
     */
    public java.lang.String getServiceId() {
        return serviceId;
    }


    /**
     * Sets the serviceId value for this ServiceDeploymentAlreadyExistsException.
     * 
     * @param serviceId
     */
    public void setServiceId(java.lang.String serviceId) {
        this.serviceId = serviceId;
    }


    /**
     * Gets the servicePath value for this ServiceDeploymentAlreadyExistsException.
     * 
     * @return servicePath
     */
    public java.lang.String getServicePath() {
        return servicePath;
    }


    /**
     * Sets the servicePath value for this ServiceDeploymentAlreadyExistsException.
     * 
     * @param servicePath
     */
    public void setServicePath(java.lang.String servicePath) {
        this.servicePath = servicePath;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ServiceDeploymentAlreadyExistsException)) return false;
        ServiceDeploymentAlreadyExistsException other = (ServiceDeploymentAlreadyExistsException) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.nodeId==null && other.getNodeId()==null) || 
             (this.nodeId!=null &&
              this.nodeId.equals(other.getNodeId()))) &&
            ((this.serviceId==null && other.getServiceId()==null) || 
             (this.serviceId!=null &&
              this.serviceId.equals(other.getServiceId()))) &&
            ((this.servicePath==null && other.getServicePath()==null) || 
             (this.servicePath!=null &&
              this.servicePath.equals(other.getServicePath())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getNodeId() != null) {
            _hashCode += getNodeId().hashCode();
        }
        if (getServiceId() != null) {
            _hashCode += getServiceId().hashCode();
        }
        if (getServicePath() != null) {
            _hashCode += getServicePath().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ServiceDeploymentAlreadyExistsException.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceDeploymentAlreadyExistsException"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nodeId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nodeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "serviceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("servicePath");
        elemField.setXmlName(new javax.xml.namespace.QName("", "servicePath"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * メタデータオブジェクトの型を返却 / [en]-(Return type metadata object)
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }


    /**
     * Writes the exception data to the faultDetails
     */
    public void writeDetails(javax.xml.namespace.QName qname, org.apache.axis.encoding.SerializationContext context) throws java.io.IOException {
        context.serialize(qname, null, this);
    }
}
