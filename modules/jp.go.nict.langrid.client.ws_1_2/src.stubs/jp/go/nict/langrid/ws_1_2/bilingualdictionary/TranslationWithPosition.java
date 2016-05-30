/**
 * TranslationWithPosition.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.bilingualdictionary;

public class TranslationWithPosition  implements java.io.Serializable {
    private int numberOfMorphemes;

    private int startIndex;

    private jp.go.nict.langrid.ws_1_2.bilingualdictionary.Translation translation;

    public TranslationWithPosition() {
    }

    public TranslationWithPosition(
           int numberOfMorphemes,
           int startIndex,
           jp.go.nict.langrid.ws_1_2.bilingualdictionary.Translation translation) {
           this.numberOfMorphemes = numberOfMorphemes;
           this.startIndex = startIndex;
           this.translation = translation;
    }


    /**
     * Gets the numberOfMorphemes value for this TranslationWithPosition.
     * 
     * @return numberOfMorphemes
     */
    public int getNumberOfMorphemes() {
        return numberOfMorphemes;
    }


    /**
     * Sets the numberOfMorphemes value for this TranslationWithPosition.
     * 
     * @param numberOfMorphemes
     */
    public void setNumberOfMorphemes(int numberOfMorphemes) {
        this.numberOfMorphemes = numberOfMorphemes;
    }


    /**
     * Gets the startIndex value for this TranslationWithPosition.
     * 
     * @return startIndex
     */
    public int getStartIndex() {
        return startIndex;
    }


    /**
     * Sets the startIndex value for this TranslationWithPosition.
     * 
     * @param startIndex
     */
    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }


    /**
     * Gets the translation value for this TranslationWithPosition.
     * 
     * @return translation
     */
    public jp.go.nict.langrid.ws_1_2.bilingualdictionary.Translation getTranslation() {
        return translation;
    }


    /**
     * Sets the translation value for this TranslationWithPosition.
     * 
     * @param translation
     */
    public void setTranslation(jp.go.nict.langrid.ws_1_2.bilingualdictionary.Translation translation) {
        this.translation = translation;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TranslationWithPosition)) return false;
        TranslationWithPosition other = (TranslationWithPosition) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.numberOfMorphemes == other.getNumberOfMorphemes() &&
            this.startIndex == other.getStartIndex() &&
            ((this.translation==null && other.getTranslation()==null) || 
             (this.translation!=null &&
              this.translation.equals(other.getTranslation())));
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
        _hashCode += getNumberOfMorphemes();
        _hashCode += getStartIndex();
        if (getTranslation() != null) {
            _hashCode += getTranslation().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TranslationWithPosition.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/bilingualdictionary/", "TranslationWithPosition"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberOfMorphemes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numberOfMorphemes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startIndex");
        elemField.setXmlName(new javax.xml.namespace.QName("", "startIndex"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("translation");
        elemField.setXmlName(new javax.xml.namespace.QName("", "translation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/bilingualdictionary/", "Translation"));
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
