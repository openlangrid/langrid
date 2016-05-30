/**
 * Chunk.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.dependencyparser;

public class Chunk  implements java.io.Serializable {
    private java.lang.String chunkId;

    private jp.go.nict.langrid.ws_1_2.dependencyparser.Dependency dependency;

    private jp.go.nict.langrid.ws_1_2.morphologicalanalysis.Morpheme[] morphemes;

    public Chunk() {
    }

    public Chunk(
           java.lang.String chunkId,
           jp.go.nict.langrid.ws_1_2.dependencyparser.Dependency dependency,
           jp.go.nict.langrid.ws_1_2.morphologicalanalysis.Morpheme[] morphemes) {
           this.chunkId = chunkId;
           this.dependency = dependency;
           this.morphemes = morphemes;
    }


    /**
     * Gets the chunkId value for this Chunk.
     * 
     * @return chunkId
     */
    public java.lang.String getChunkId() {
        return chunkId;
    }


    /**
     * Sets the chunkId value for this Chunk.
     * 
     * @param chunkId
     */
    public void setChunkId(java.lang.String chunkId) {
        this.chunkId = chunkId;
    }


    /**
     * Gets the dependency value for this Chunk.
     * 
     * @return dependency
     */
    public jp.go.nict.langrid.ws_1_2.dependencyparser.Dependency getDependency() {
        return dependency;
    }


    /**
     * Sets the dependency value for this Chunk.
     * 
     * @param dependency
     */
    public void setDependency(jp.go.nict.langrid.ws_1_2.dependencyparser.Dependency dependency) {
        this.dependency = dependency;
    }


    /**
     * Gets the morphemes value for this Chunk.
     * 
     * @return morphemes
     */
    public jp.go.nict.langrid.ws_1_2.morphologicalanalysis.Morpheme[] getMorphemes() {
        return morphemes;
    }


    /**
     * Sets the morphemes value for this Chunk.
     * 
     * @param morphemes
     */
    public void setMorphemes(jp.go.nict.langrid.ws_1_2.morphologicalanalysis.Morpheme[] morphemes) {
        this.morphemes = morphemes;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Chunk)) return false;
        Chunk other = (Chunk) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.chunkId==null && other.getChunkId()==null) || 
             (this.chunkId!=null &&
              this.chunkId.equals(other.getChunkId()))) &&
            ((this.dependency==null && other.getDependency()==null) || 
             (this.dependency!=null &&
              this.dependency.equals(other.getDependency()))) &&
            ((this.morphemes==null && other.getMorphemes()==null) || 
             (this.morphemes!=null &&
              java.util.Arrays.equals(this.morphemes, other.getMorphemes())));
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
        if (getChunkId() != null) {
            _hashCode += getChunkId().hashCode();
        }
        if (getDependency() != null) {
            _hashCode += getDependency().hashCode();
        }
        if (getMorphemes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMorphemes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMorphemes(), i);
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
        new org.apache.axis.description.TypeDesc(Chunk.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/dependencyparser/", "Chunk"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("chunkId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "chunkId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dependency");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dependency"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/dependencyparser/", "Dependency"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("morphemes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "morphemes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/morphologicalanalysis/", "Morpheme"));
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
