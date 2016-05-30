/**
 * UnsupportedLanguagePairException.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2;

public class UnsupportedLanguagePairException  extends jp.go.nict.langrid.ws_1_2.InvalidParameterException  implements java.io.Serializable {
    private java.lang.String language1;

    private java.lang.String language2;

    private java.lang.String parameterName1;

    private java.lang.String parameterName2;

    public UnsupportedLanguagePairException() {
    }

    public UnsupportedLanguagePairException(
           java.lang.String description,
           java.lang.String parameterName,
           java.lang.String language1,
           java.lang.String language2,
           java.lang.String parameterName1,
           java.lang.String parameterName2) {
        super(
            description,
            parameterName);
        this.language1 = language1;
        this.language2 = language2;
        this.parameterName1 = parameterName1;
        this.parameterName2 = parameterName2;
    }


    /**
     * Gets the language1 value for this UnsupportedLanguagePairException.
     * 
     * @return language1
     */
    public java.lang.String getLanguage1() {
        return language1;
    }


    /**
     * Sets the language1 value for this UnsupportedLanguagePairException.
     * 
     * @param language1
     */
    public void setLanguage1(java.lang.String language1) {
        this.language1 = language1;
    }


    /**
     * Gets the language2 value for this UnsupportedLanguagePairException.
     * 
     * @return language2
     */
    public java.lang.String getLanguage2() {
        return language2;
    }


    /**
     * Sets the language2 value for this UnsupportedLanguagePairException.
     * 
     * @param language2
     */
    public void setLanguage2(java.lang.String language2) {
        this.language2 = language2;
    }


    /**
     * Gets the parameterName1 value for this UnsupportedLanguagePairException.
     * 
     * @return parameterName1
     */
    public java.lang.String getParameterName1() {
        return parameterName1;
    }


    /**
     * Sets the parameterName1 value for this UnsupportedLanguagePairException.
     * 
     * @param parameterName1
     */
    public void setParameterName1(java.lang.String parameterName1) {
        this.parameterName1 = parameterName1;
    }


    /**
     * Gets the parameterName2 value for this UnsupportedLanguagePairException.
     * 
     * @return parameterName2
     */
    public java.lang.String getParameterName2() {
        return parameterName2;
    }


    /**
     * Sets the parameterName2 value for this UnsupportedLanguagePairException.
     * 
     * @param parameterName2
     */
    public void setParameterName2(java.lang.String parameterName2) {
        this.parameterName2 = parameterName2;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UnsupportedLanguagePairException)) return false;
        UnsupportedLanguagePairException other = (UnsupportedLanguagePairException) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.language1==null && other.getLanguage1()==null) || 
             (this.language1!=null &&
              this.language1.equals(other.getLanguage1()))) &&
            ((this.language2==null && other.getLanguage2()==null) || 
             (this.language2!=null &&
              this.language2.equals(other.getLanguage2()))) &&
            ((this.parameterName1==null && other.getParameterName1()==null) || 
             (this.parameterName1!=null &&
              this.parameterName1.equals(other.getParameterName1()))) &&
            ((this.parameterName2==null && other.getParameterName2()==null) || 
             (this.parameterName2!=null &&
              this.parameterName2.equals(other.getParameterName2())));
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
        if (getLanguage1() != null) {
            _hashCode += getLanguage1().hashCode();
        }
        if (getLanguage2() != null) {
            _hashCode += getLanguage2().hashCode();
        }
        if (getParameterName1() != null) {
            _hashCode += getParameterName1().hashCode();
        }
        if (getParameterName2() != null) {
            _hashCode += getParameterName2().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UnsupportedLanguagePairException.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnsupportedLanguagePairException"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("language1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "language1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("language2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "language2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parameterName1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "parameterName1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parameterName2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "parameterName2"));
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
