/**
 * ConceptNode.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.dictionary;

public class ConceptNode  implements java.io.Serializable {
    private java.lang.String gloss;

    private java.lang.String nodeId;

    private java.lang.String[] relations;

    private java.lang.String[] synset;

    private java.lang.String[] usageExamples;

    public ConceptNode() {
    }

    public ConceptNode(
           java.lang.String gloss,
           java.lang.String nodeId,
           java.lang.String[] relations,
           java.lang.String[] synset,
           java.lang.String[] usageExamples) {
           this.gloss = gloss;
           this.nodeId = nodeId;
           this.relations = relations;
           this.synset = synset;
           this.usageExamples = usageExamples;
    }


    /**
     * Gets the gloss value for this ConceptNode.
     * 
     * @return gloss
     */
    public java.lang.String getGloss() {
        return gloss;
    }


    /**
     * Sets the gloss value for this ConceptNode.
     * 
     * @param gloss
     */
    public void setGloss(java.lang.String gloss) {
        this.gloss = gloss;
    }


    /**
     * Gets the nodeId value for this ConceptNode.
     * 
     * @return nodeId
     */
    public java.lang.String getNodeId() {
        return nodeId;
    }


    /**
     * Sets the nodeId value for this ConceptNode.
     * 
     * @param nodeId
     */
    public void setNodeId(java.lang.String nodeId) {
        this.nodeId = nodeId;
    }


    /**
     * Gets the relations value for this ConceptNode.
     * 
     * @return relations
     */
    public java.lang.String[] getRelations() {
        return relations;
    }


    /**
     * Sets the relations value for this ConceptNode.
     * 
     * @param relations
     */
    public void setRelations(java.lang.String[] relations) {
        this.relations = relations;
    }


    /**
     * Gets the synset value for this ConceptNode.
     * 
     * @return synset
     */
    public java.lang.String[] getSynset() {
        return synset;
    }


    /**
     * Sets the synset value for this ConceptNode.
     * 
     * @param synset
     */
    public void setSynset(java.lang.String[] synset) {
        this.synset = synset;
    }


    /**
     * Gets the usageExamples value for this ConceptNode.
     * 
     * @return usageExamples
     */
    public java.lang.String[] getUsageExamples() {
        return usageExamples;
    }


    /**
     * Sets the usageExamples value for this ConceptNode.
     * 
     * @param usageExamples
     */
    public void setUsageExamples(java.lang.String[] usageExamples) {
        this.usageExamples = usageExamples;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConceptNode)) return false;
        ConceptNode other = (ConceptNode) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.gloss==null && other.getGloss()==null) || 
             (this.gloss!=null &&
              this.gloss.equals(other.getGloss()))) &&
            ((this.nodeId==null && other.getNodeId()==null) || 
             (this.nodeId!=null &&
              this.nodeId.equals(other.getNodeId()))) &&
            ((this.relations==null && other.getRelations()==null) || 
             (this.relations!=null &&
              java.util.Arrays.equals(this.relations, other.getRelations()))) &&
            ((this.synset==null && other.getSynset()==null) || 
             (this.synset!=null &&
              java.util.Arrays.equals(this.synset, other.getSynset()))) &&
            ((this.usageExamples==null && other.getUsageExamples()==null) || 
             (this.usageExamples!=null &&
              java.util.Arrays.equals(this.usageExamples, other.getUsageExamples())));
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
        if (getGloss() != null) {
            _hashCode += getGloss().hashCode();
        }
        if (getNodeId() != null) {
            _hashCode += getNodeId().hashCode();
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
        if (getUsageExamples() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getUsageExamples());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getUsageExamples(), i);
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
        new org.apache.axis.description.TypeDesc(ConceptNode.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/dictionary/", "ConceptNode"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gloss");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gloss"));
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
        elemField.setFieldName("relations");
        elemField.setXmlName(new javax.xml.namespace.QName("", "relations"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("synset");
        elemField.setXmlName(new javax.xml.namespace.QName("", "synset"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usageExamples");
        elemField.setXmlName(new javax.xml.namespace.QName("", "usageExamples"));
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
