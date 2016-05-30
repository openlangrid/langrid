/**
 * UnsupportedLanguagePathException.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2;

public class UnsupportedLanguagePathException  extends jp.go.nict.langrid.ws_1_2.InvalidParameterException  implements java.io.Serializable {
    private jp.go.nict.langrid.ws_1_2.LanguagePath languagePath;

    private java.lang.String[] parameterNames;

    public UnsupportedLanguagePathException() {
    }

    public UnsupportedLanguagePathException(
           java.lang.String description,
           java.lang.String parameterName,
           jp.go.nict.langrid.ws_1_2.LanguagePath languagePath,
           java.lang.String[] parameterNames) {
        super(
            description,
            parameterName);
        this.languagePath = languagePath;
        this.parameterNames = parameterNames;
    }


    /**
     * Gets the languagePath value for this UnsupportedLanguagePathException.
     * 
     * @return languagePath
     */
    public jp.go.nict.langrid.ws_1_2.LanguagePath getLanguagePath() {
        return languagePath;
    }


    /**
     * Sets the languagePath value for this UnsupportedLanguagePathException.
     * 
     * @param languagePath
     */
    public void setLanguagePath(jp.go.nict.langrid.ws_1_2.LanguagePath languagePath) {
        this.languagePath = languagePath;
    }


    /**
     * Gets the parameterNames value for this UnsupportedLanguagePathException.
     * 
     * @return parameterNames
     */
    public java.lang.String[] getParameterNames() {
        return parameterNames;
    }


    /**
     * Sets the parameterNames value for this UnsupportedLanguagePathException.
     * 
     * @param parameterNames
     */
    public void setParameterNames(java.lang.String[] parameterNames) {
        this.parameterNames = parameterNames;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UnsupportedLanguagePathException)) return false;
        UnsupportedLanguagePathException other = (UnsupportedLanguagePathException) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.languagePath==null && other.getLanguagePath()==null) || 
             (this.languagePath!=null &&
              this.languagePath.equals(other.getLanguagePath()))) &&
            ((this.parameterNames==null && other.getParameterNames()==null) || 
             (this.parameterNames!=null &&
              java.util.Arrays.equals(this.parameterNames, other.getParameterNames())));
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
        if (getLanguagePath() != null) {
            _hashCode += getLanguagePath().hashCode();
        }
        if (getParameterNames() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getParameterNames());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getParameterNames(), i);
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
        new org.apache.axis.description.TypeDesc(UnsupportedLanguagePathException.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "UnsupportedLanguagePathException"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("languagePath");
        elemField.setXmlName(new javax.xml.namespace.QName("", "languagePath"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "LanguagePath"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parameterNames");
        elemField.setXmlName(new javax.xml.namespace.QName("", "parameterNames"));
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

}
