/**
 * LemmaNode.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.dictionary;

public class LemmaNode  implements java.io.Serializable {
    private java.lang.String[] conceptNodes;

    private java.lang.String domain;

    private java.lang.String headWord;

    private java.lang.String language;

    private java.lang.String nodeId;

    private java.lang.String partOfSpeech;

    private java.lang.String pronounciation;

    private java.lang.String[] relations;

    public LemmaNode() {
    }

    public LemmaNode(
           java.lang.String[] conceptNodes,
           java.lang.String domain,
           java.lang.String headWord,
           java.lang.String language,
           java.lang.String nodeId,
           java.lang.String partOfSpeech,
           java.lang.String pronounciation,
           java.lang.String[] relations) {
           this.conceptNodes = conceptNodes;
           this.domain = domain;
           this.headWord = headWord;
           this.language = language;
           this.nodeId = nodeId;
           this.partOfSpeech = partOfSpeech;
           this.pronounciation = pronounciation;
           this.relations = relations;
    }


    /**
     * Gets the conceptNodes value for this LemmaNode.
     * 
     * @return conceptNodes
     */
    public java.lang.String[] getConceptNodes() {
        return conceptNodes;
    }


    /**
     * Sets the conceptNodes value for this LemmaNode.
     * 
     * @param conceptNodes
     */
    public void setConceptNodes(java.lang.String[] conceptNodes) {
        this.conceptNodes = conceptNodes;
    }


    /**
     * Gets the domain value for this LemmaNode.
     * 
     * @return domain
     */
    public java.lang.String getDomain() {
        return domain;
    }


    /**
     * Sets the domain value for this LemmaNode.
     * 
     * @param domain
     */
    public void setDomain(java.lang.String domain) {
        this.domain = domain;
    }


    /**
     * Gets the headWord value for this LemmaNode.
     * 
     * @return headWord
     */
    public java.lang.String getHeadWord() {
        return headWord;
    }


    /**
     * Sets the headWord value for this LemmaNode.
     * 
     * @param headWord
     */
    public void setHeadWord(java.lang.String headWord) {
        this.headWord = headWord;
    }


    /**
     * Gets the language value for this LemmaNode.
     * 
     * @return language
     */
    public java.lang.String getLanguage() {
        return language;
    }


    /**
     * Sets the language value for this LemmaNode.
     * 
     * @param language
     */
    public void setLanguage(java.lang.String language) {
        this.language = language;
    }


    /**
     * Gets the nodeId value for this LemmaNode.
     * 
     * @return nodeId
     */
    public java.lang.String getNodeId() {
        return nodeId;
    }


    /**
     * Sets the nodeId value for this LemmaNode.
     * 
     * @param nodeId
     */
    public void setNodeId(java.lang.String nodeId) {
        this.nodeId = nodeId;
    }


    /**
     * Gets the partOfSpeech value for this LemmaNode.
     * 
     * @return partOfSpeech
     */
    public java.lang.String getPartOfSpeech() {
        return partOfSpeech;
    }


    /**
     * Sets the partOfSpeech value for this LemmaNode.
     * 
     * @param partOfSpeech
     */
    public void setPartOfSpeech(java.lang.String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }


    /**
     * Gets the pronounciation value for this LemmaNode.
     * 
     * @return pronounciation
     */
    public java.lang.String getPronounciation() {
        return pronounciation;
    }


    /**
     * Sets the pronounciation value for this LemmaNode.
     * 
     * @param pronounciation
     */
    public void setPronounciation(java.lang.String pronounciation) {
        this.pronounciation = pronounciation;
    }


    /**
     * Gets the relations value for this LemmaNode.
     * 
     * @return relations
     */
    public java.lang.String[] getRelations() {
        return relations;
    }


    /**
     * Sets the relations value for this LemmaNode.
     * 
     * @param relations
     */
    public void setRelations(java.lang.String[] relations) {
        this.relations = relations;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LemmaNode)) return false;
        LemmaNode other = (LemmaNode) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.conceptNodes==null && other.getConceptNodes()==null) || 
             (this.conceptNodes!=null &&
              java.util.Arrays.equals(this.conceptNodes, other.getConceptNodes()))) &&
            ((this.domain==null && other.getDomain()==null) || 
             (this.domain!=null &&
              this.domain.equals(other.getDomain()))) &&
            ((this.headWord==null && other.getHeadWord()==null) || 
             (this.headWord!=null &&
              this.headWord.equals(other.getHeadWord()))) &&
            ((this.language==null && other.getLanguage()==null) || 
             (this.language!=null &&
              this.language.equals(other.getLanguage()))) &&
            ((this.nodeId==null && other.getNodeId()==null) || 
             (this.nodeId!=null &&
              this.nodeId.equals(other.getNodeId()))) &&
            ((this.partOfSpeech==null && other.getPartOfSpeech()==null) || 
             (this.partOfSpeech!=null &&
              this.partOfSpeech.equals(other.getPartOfSpeech()))) &&
            ((this.pronounciation==null && other.getPronounciation()==null) || 
             (this.pronounciation!=null &&
              this.pronounciation.equals(other.getPronounciation()))) &&
            ((this.relations==null && other.getRelations()==null) || 
             (this.relations!=null &&
              java.util.Arrays.equals(this.relations, other.getRelations())));
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
        if (getConceptNodes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getConceptNodes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getConceptNodes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDomain() != null) {
            _hashCode += getDomain().hashCode();
        }
        if (getHeadWord() != null) {
            _hashCode += getHeadWord().hashCode();
        }
        if (getLanguage() != null) {
            _hashCode += getLanguage().hashCode();
        }
        if (getNodeId() != null) {
            _hashCode += getNodeId().hashCode();
        }
        if (getPartOfSpeech() != null) {
            _hashCode += getPartOfSpeech().hashCode();
        }
        if (getPronounciation() != null) {
            _hashCode += getPronounciation().hashCode();
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LemmaNode.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/dictionary/", "LemmaNode"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("conceptNodes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "conceptNodes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("domain");
        elemField.setXmlName(new javax.xml.namespace.QName("", "domain"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("headWord");
        elemField.setXmlName(new javax.xml.namespace.QName("", "headWord"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("language");
        elemField.setXmlName(new javax.xml.namespace.QName("", "language"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nodeId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nodeId"));
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
        elemField.setFieldName("pronounciation");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pronounciation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("relations");
        elemField.setXmlName(new javax.xml.namespace.QName("", "relations"));
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
