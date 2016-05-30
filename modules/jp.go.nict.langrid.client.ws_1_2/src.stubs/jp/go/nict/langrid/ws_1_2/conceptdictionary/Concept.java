/**
 * Concept.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.conceptdictionary;

public class Concept  implements java.io.Serializable {
    private java.lang.String conceptId;

    private jp.go.nict.langrid.ws_1_2.conceptdictionary.Gloss[] glosses;

    private java.lang.String partOfSpeech;

    private java.lang.String[] relations;

    private jp.go.nict.langrid.ws_1_2.conceptdictionary.Lemma[] synset;

    public Concept() {
    }

    public Concept(
           java.lang.String conceptId,
           jp.go.nict.langrid.ws_1_2.conceptdictionary.Gloss[] glosses,
           java.lang.String partOfSpeech,
           java.lang.String[] relations,
           jp.go.nict.langrid.ws_1_2.conceptdictionary.Lemma[] synset) {
           this.conceptId = conceptId;
           this.glosses = glosses;
           this.partOfSpeech = partOfSpeech;
           this.relations = relations;
           this.synset = synset;
    }


    /**
     * Gets the conceptId value for this Concept.
     * 
     * @return conceptId
     */
    public java.lang.String getConceptId() {
        return conceptId;
    }


    /**
     * Sets the conceptId value for this Concept.
     * 
     * @param conceptId
     */
    public void setConceptId(java.lang.String conceptId) {
        this.conceptId = conceptId;
    }


    /**
     * Gets the glosses value for this Concept.
     * 
     * @return glosses
     */
    public jp.go.nict.langrid.ws_1_2.conceptdictionary.Gloss[] getGlosses() {
        return glosses;
    }


    /**
     * Sets the glosses value for this Concept.
     * 
     * @param glosses
     */
    public void setGlosses(jp.go.nict.langrid.ws_1_2.conceptdictionary.Gloss[] glosses) {
        this.glosses = glosses;
    }


    /**
     * Gets the partOfSpeech value for this Concept.
     * 
     * @return partOfSpeech
     */
    public java.lang.String getPartOfSpeech() {
        return partOfSpeech;
    }


    /**
     * Sets the partOfSpeech value for this Concept.
     * 
     * @param partOfSpeech
     */
    public void setPartOfSpeech(java.lang.String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }


    /**
     * Gets the relations value for this Concept.
     * 
     * @return relations
     */
    public java.lang.String[] getRelations() {
        return relations;
    }


    /**
     * Sets the relations value for this Concept.
     * 
     * @param relations
     */
    public void setRelations(java.lang.String[] relations) {
        this.relations = relations;
    }


    /**
     * Gets the synset value for this Concept.
     * 
     * @return synset
     */
    public jp.go.nict.langrid.ws_1_2.conceptdictionary.Lemma[] getSynset() {
        return synset;
    }


    /**
     * Sets the synset value for this Concept.
     * 
     * @param synset
     */
    public void setSynset(jp.go.nict.langrid.ws_1_2.conceptdictionary.Lemma[] synset) {
        this.synset = synset;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Concept)) return false;
        Concept other = (Concept) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.conceptId==null && other.getConceptId()==null) || 
             (this.conceptId!=null &&
              this.conceptId.equals(other.getConceptId()))) &&
            ((this.glosses==null && other.getGlosses()==null) || 
             (this.glosses!=null &&
              java.util.Arrays.equals(this.glosses, other.getGlosses()))) &&
            ((this.partOfSpeech==null && other.getPartOfSpeech()==null) || 
             (this.partOfSpeech!=null &&
              this.partOfSpeech.equals(other.getPartOfSpeech()))) &&
            ((this.relations==null && other.getRelations()==null) || 
             (this.relations!=null &&
              java.util.Arrays.equals(this.relations, other.getRelations()))) &&
            ((this.synset==null && other.getSynset()==null) || 
             (this.synset!=null &&
              java.util.Arrays.equals(this.synset, other.getSynset())));
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
        if (getConceptId() != null) {
            _hashCode += getConceptId().hashCode();
        }
        if (getGlosses() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGlosses());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGlosses(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPartOfSpeech() != null) {
            _hashCode += getPartOfSpeech().hashCode();
        }
        if (getRelations() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRelations());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRelations(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSynset() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSynset());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSynset(), i);
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
        new org.apache.axis.description.TypeDesc(Concept.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/conceptdictionary/", "Concept"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("conceptId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "conceptId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("glosses");
        elemField.setXmlName(new javax.xml.namespace.QName("", "glosses"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/conceptdictionary/", "Gloss"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("partOfSpeech");
        elemField.setXmlName(new javax.xml.namespace.QName("", "partOfSpeech"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("relations");
        elemField.setXmlName(new javax.xml.namespace.QName("", "relations"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("synset");
        elemField.setXmlName(new javax.xml.namespace.QName("", "synset"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/conceptdictionary/", "Lemma"));
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
