/**
 * Translation.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.bilingualdictionary;

public class Translation  implements java.io.Serializable {
    private java.lang.String headWord;

    private java.lang.String[] targetWords;

    public Translation() {
    }

    public Translation(
           java.lang.String headWord,
           java.lang.String[] targetWords) {
           this.headWord = headWord;
           this.targetWords = targetWords;
    }


    /**
     * Gets the headWord value for this Translation.
     * 
     * @return headWord
     */
    public java.lang.String getHeadWord() {
        return headWord;
    }


    /**
     * Sets the headWord value for this Translation.
     * 
     * @param headWord
     */
    public void setHeadWord(java.lang.String headWord) {
        this.headWord = headWord;
    }


    /**
     * Gets the targetWords value for this Translation.
     * 
     * @return targetWords
     */
    public java.lang.String[] getTargetWords() {
        return targetWords;
    }


    /**
     * Sets the targetWords value for this Translation.
     * 
     * @param targetWords
     */
    public void setTargetWords(java.lang.String[] targetWords) {
        this.targetWords = targetWords;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Translation)) return false;
        Translation other = (Translation) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.headWord==null && other.getHeadWord()==null) || 
             (this.headWord!=null &&
              this.headWord.equals(other.getHeadWord()))) &&
            ((this.targetWords==null && other.getTargetWords()==null) || 
             (this.targetWords!=null &&
              java.util.Arrays.equals(this.targetWords, other.getTargetWords())));
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
        if (getHeadWord() != null) {
            _hashCode += getHeadWord().hashCode();
        }
        if (getTargetWords() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTargetWords());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTargetWords(), i);
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
        new org.apache.axis.description.TypeDesc(Translation.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/bilingualdictionary/", "Translation"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("headWord");
        elemField.setXmlName(new javax.xml.namespace.QName("", "headWord"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("targetWords");
        elemField.setXmlName(new javax.xml.namespace.QName("", "targetWords"));
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
