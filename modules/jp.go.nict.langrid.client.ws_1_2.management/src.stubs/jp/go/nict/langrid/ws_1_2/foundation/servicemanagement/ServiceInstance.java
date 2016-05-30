/**
 * ServiceInstance.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.foundation.servicemanagement;

public class ServiceInstance  implements java.io.Serializable {
    private byte[] instance;

    private int instanceSize;

    private java.lang.String instanceType;

    private java.lang.String serviceType;

    private jp.go.nict.langrid.ws_1_2.LanguagePath[] supportedLanguages;

    public ServiceInstance() {
    }

    public ServiceInstance(
           byte[] instance,
           int instanceSize,
           java.lang.String instanceType,
           java.lang.String serviceType,
           jp.go.nict.langrid.ws_1_2.LanguagePath[] supportedLanguages) {
           this.instance = instance;
           this.instanceSize = instanceSize;
           this.instanceType = instanceType;
           this.serviceType = serviceType;
           this.supportedLanguages = supportedLanguages;
    }


    /**
     * Gets the instance value for this ServiceInstance.
     * 
     * @return instance
     */
    public byte[] getInstance() {
        return instance;
    }


    /**
     * Sets the instance value for this ServiceInstance.
     * 
     * @param instance
     */
    public void setInstance(byte[] instance) {
        this.instance = instance;
    }


    /**
     * Gets the instanceSize value for this ServiceInstance.
     * 
     * @return instanceSize
     */
    public int getInstanceSize() {
        return instanceSize;
    }


    /**
     * Sets the instanceSize value for this ServiceInstance.
     * 
     * @param instanceSize
     */
    public void setInstanceSize(int instanceSize) {
        this.instanceSize = instanceSize;
    }


    /**
     * Gets the instanceType value for this ServiceInstance.
     * 
     * @return instanceType
     */
    public java.lang.String getInstanceType() {
        return instanceType;
    }


    /**
     * Sets the instanceType value for this ServiceInstance.
     * 
     * @param instanceType
     */
    public void setInstanceType(java.lang.String instanceType) {
        this.instanceType = instanceType;
    }


    /**
     * Gets the serviceType value for this ServiceInstance.
     * 
     * @return serviceType
     */
    public java.lang.String getServiceType() {
        return serviceType;
    }


    /**
     * Sets the serviceType value for this ServiceInstance.
     * 
     * @param serviceType
     */
    public void setServiceType(java.lang.String serviceType) {
        this.serviceType = serviceType;
    }


    /**
     * Gets the supportedLanguages value for this ServiceInstance.
     * 
     * @return supportedLanguages
     */
    public jp.go.nict.langrid.ws_1_2.LanguagePath[] getSupportedLanguages() {
        return supportedLanguages;
    }


    /**
     * Sets the supportedLanguages value for this ServiceInstance.
     * 
     * @param supportedLanguages
     */
    public void setSupportedLanguages(jp.go.nict.langrid.ws_1_2.LanguagePath[] supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ServiceInstance)) return false;
        ServiceInstance other = (ServiceInstance) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.instance==null && other.getInstance()==null) || 
             (this.instance!=null &&
              java.util.Arrays.equals(this.instance, other.getInstance()))) &&
            this.instanceSize == other.getInstanceSize() &&
            ((this.instanceType==null && other.getInstanceType()==null) || 
             (this.instanceType!=null &&
              this.instanceType.equals(other.getInstanceType()))) &&
            ((this.serviceType==null && other.getServiceType()==null) || 
             (this.serviceType!=null &&
              this.serviceType.equals(other.getServiceType()))) &&
            ((this.supportedLanguages==null && other.getSupportedLanguages()==null) || 
             (this.supportedLanguages!=null &&
              java.util.Arrays.equals(this.supportedLanguages, other.getSupportedLanguages())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getInstance() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getInstance());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getInstance(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getInstanceSize();
        if (getInstanceType() != null) {
            _hashCode += getInstanceType().hashCode();
        }
        if (getServiceType() != null) {
            _hashCode += getServiceType().hashCode();
        }
        if (getSupportedLanguages() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSupportedLanguages());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSupportedLanguages(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ServiceInstance.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceInstance"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("instance");
        elemField.setXmlName(new javax.xml.namespace.QName("", "instance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("instanceSize");
        elemField.setXmlName(new javax.xml.namespace.QName("", "instanceSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("instanceType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "instanceType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "serviceType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("supportedLanguages");
        elemField.setXmlName(new javax.xml.namespace.QName("", "supportedLanguages"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "LanguagePath"));
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

}
