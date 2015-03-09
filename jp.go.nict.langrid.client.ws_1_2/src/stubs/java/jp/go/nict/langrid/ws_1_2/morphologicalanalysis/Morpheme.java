/**
 * Morpheme.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.morphologicalanalysis;

public class Morpheme  implements java.io.Serializable {
    private java.lang.String lemma;

    private java.lang.String partOfSpeech;

    private java.lang.String word;

    public Morpheme() {
    }

    public Morpheme(
           java.lang.String lemma,
           java.lang.String partOfSpeech,
           java.lang.String word) {
           this.lemma = lemma;
           this.partOfSpeech = partOfSpeech;
           this.word = word;
    }


    /**
     * Gets the lemma value for this Morpheme.
     * 
     * @return lemma
     */
    public java.lang.String getLemma() {
        return lemma;
    }


    /**
     * Sets the lemma value for this Morpheme.
     * 
     * @param lemma
     */
    public void setLemma(java.lang.String lemma) {
        this.lemma = lemma;
    }


    /**
     * Gets the partOfSpeech value for this Morpheme.
     * 
     * @return partOfSpeech
     */
    public java.lang.String getPartOfSpeech() {
        return partOfSpeech;
    }


    /**
     * Sets the partOfSpeech value for this Morpheme.
     * 
     * @param partOfSpeech
     */
    public void setPartOfSpeech(java.lang.String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }


    /**
     * Gets the word value for this Morpheme.
     * 
     * @return word
     */
    public java.lang.String getWord() {
        return word;
    }


    /**
     * Sets the word value for this Morpheme.
     * 
     * @param word
     */
    public void setWord(java.lang.String word) {
        this.word = word;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Morpheme)) return false;
        Morpheme other = (Morpheme) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.lemma==null && other.getLemma()==null) || 
             (this.lemma!=null &&
              this.lemma.equals(other.getLemma()))) &&
            ((this.partOfSpeech==null && other.getPartOfSpeech()==null) || 
             (this.partOfSpeech!=null &&
              this.partOfSpeech.equals(other.getPartOfSpeech()))) &&
            ((this.word==null && other.getWord()==null) || 
             (this.word!=null &&
              this.word.equals(other.getWord())));
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
        if (getLemma() != null) {
            _hashCode += getLemma().hashCode();
        }
        if (getPartOfSpeech() != null) {
            _hashCode += getPartOfSpeech().hashCode();
        }
        if (getWord() != null) {
            _hashCode += getWord().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Morpheme.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/morphologicalanalysis/", "Morpheme"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lemma");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lemma"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("partOfSpeech");
        elemField.setXmlName(new javax.xml.namespace.QName("", "partOfSpeech"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("word");
        elemField.setXmlName(new javax.xml.namespace.QName("", "word"));
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
