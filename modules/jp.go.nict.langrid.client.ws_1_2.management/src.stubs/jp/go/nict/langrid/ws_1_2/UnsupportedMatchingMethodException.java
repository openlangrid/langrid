/**
 * UnsupportedMatchingMethodException.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2;

public class UnsupportedMatchingMethodException  extends jp.go.nict.langrid.ws_1_2.InvalidParameterException  implements java.io.Serializable {
    private java.lang.String matchingMethod;

    private java.lang.String[] validMethods;

    public UnsupportedMatchingMethodException() {
    }

    public UnsupportedMatchingMethodException(
           java.lang.String description,
           java.lang.String parameterName,
           java.lang.String matchingMethod,
           java.lang.String[] validMethods) {
        super(
            description,
            parameterName);
        this.matchingMethod = matchingMethod;
        this.validMethods = validMethods;
    }


    /**
     * Gets the matchingMethod value for this UnsupportedMatchingMethodException.
     * 
     * @return matchingMethod
     */
    public java.lang.String getMatchingMethod() {
        return matchingMethod;
    }


    /**
     * Sets the matchingMethod value for this UnsupportedMatchingMethodException.
     * 
     * @param matchingMethod
     */
    public void setMatchingMethod(java.lang.String matchingMethod) {
        this.matchingMethod = matchingMethod;
    }


    /**
     * Gets the validMethods value for this UnsupportedMatchingMethodException.
     * 
     * @return validMethods
     */
    public java.lang.String[] getValidMethods() {
        return validMethods;
    }


    /**
     * Sets the validMethods value for this UnsupportedMatchingMethodException.
     * 
     * @param validMethods
     */
    public void setValidMethods(java.lang.String[] validMethods) {
        this.validMethods = validMethods;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UnsupportedMatchingMethodException)) return false;
        UnsupportedMatchingMethodException other = (UnsupportedMatchingMethodException) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.matchingMethod==null && other.getMatchingMethod()==null) || 
             (this.matchingMethod!=null &&
              this.matchingMethod.equals(other.getMatchingMethod()))) &&
            ((this.validMethods==null && other.getValidMethods()==null) || 
             (this.validMethods!=null &&
              java.util.Arrays.equals(this.validMethods, other.getValidMethods())));
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
        if (getMatchingMethod() != null) {
            _hashCode += getMatchingMethod().hashCode();
        }
        if (getValidMethods() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getValidMethods());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getValidMethods(), i);
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
        new org.apache.axis.description.TypeDesc(UnsupportedMatchingMethodException.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnsupportedMatchingMethodException"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matchingMethod");
        elemField.setXmlName(new javax.xml.namespace.QName("", "matchingMethod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("validMethods");
        elemField.setXmlName(new javax.xml.namespace.QName("", "validMethods"));
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
