/**
 * ResourceInstance.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.foundation.resourcemanagement;

public class ResourceInstance  implements java.io.Serializable {
    private java.lang.String resourceType;

    private jp.go.nict.langrid.ws_1_2.LanguagePath[] supportedLanguages;

    public ResourceInstance() {
    }

    public ResourceInstance(
           java.lang.String resourceType,
           jp.go.nict.langrid.ws_1_2.LanguagePath[] supportedLanguages) {
           this.resourceType = resourceType;
           this.supportedLanguages = supportedLanguages;
    }


    /**
     * Gets the resourceType value for this ResourceInstance.
     * 
     * @return resourceType
     */
    public java.lang.String getResourceType() {
        return resourceType;
    }


    /**
     * Sets the resourceType value for this ResourceInstance.
     * 
     * @param resourceType
     */
    public void setResourceType(java.lang.String resourceType) {
        this.resourceType = resourceType;
    }


    /**
     * Gets the supportedLanguages value for this ResourceInstance.
     * 
     * @return supportedLanguages
     */
    public jp.go.nict.langrid.ws_1_2.LanguagePath[] getSupportedLanguages() {
        return supportedLanguages;
    }


    /**
     * Sets the supportedLanguages value for this ResourceInstance.
     * 
     * @param supportedLanguages
     */
    public void setSupportedLanguages(jp.go.nict.langrid.ws_1_2.LanguagePath[] supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResourceInstance)) return false;
        ResourceInstance other = (ResourceInstance) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.resourceType==null && other.getResourceType()==null) || 
             (this.resourceType!=null &&
              this.resourceType.equals(other.getResourceType()))) &&
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
        if (getResourceType() != null) {
            _hashCode += getResourceType().hashCode();
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
        new org.apache.axis.description.TypeDesc(ResourceInstance.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/resourcemanagement/", "ResourceInstance"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resourceType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "resourceType"));
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
