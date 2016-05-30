/**
 * AdjacencyPair.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.adjacencypair;

public class AdjacencyPair  implements java.io.Serializable {
    private java.lang.String category;

    private java.lang.String firstTurn;

    private java.lang.String[] secondTurns;

    public AdjacencyPair() {
    }

    public AdjacencyPair(
           java.lang.String category,
           java.lang.String firstTurn,
           java.lang.String[] secondTurns) {
           this.category = category;
           this.firstTurn = firstTurn;
           this.secondTurns = secondTurns;
    }


    /**
     * Gets the category value for this AdjacencyPair.
     * 
     * @return category
     */
    public java.lang.String getCategory() {
        return category;
    }


    /**
     * Sets the category value for this AdjacencyPair.
     * 
     * @param category
     */
    public void setCategory(java.lang.String category) {
        this.category = category;
    }


    /**
     * Gets the firstTurn value for this AdjacencyPair.
     * 
     * @return firstTurn
     */
    public java.lang.String getFirstTurn() {
        return firstTurn;
    }


    /**
     * Sets the firstTurn value for this AdjacencyPair.
     * 
     * @param firstTurn
     */
    public void setFirstTurn(java.lang.String firstTurn) {
        this.firstTurn = firstTurn;
    }


    /**
     * Gets the secondTurns value for this AdjacencyPair.
     * 
     * @return secondTurns
     */
    public java.lang.String[] getSecondTurns() {
        return secondTurns;
    }


    /**
     * Sets the secondTurns value for this AdjacencyPair.
     * 
     * @param secondTurns
     */
    public void setSecondTurns(java.lang.String[] secondTurns) {
        this.secondTurns = secondTurns;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AdjacencyPair)) return false;
        AdjacencyPair other = (AdjacencyPair) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.category==null && other.getCategory()==null) || 
             (this.category!=null &&
              this.category.equals(other.getCategory()))) &&
            ((this.firstTurn==null && other.getFirstTurn()==null) || 
             (this.firstTurn!=null &&
              this.firstTurn.equals(other.getFirstTurn()))) &&
            ((this.secondTurns==null && other.getSecondTurns()==null) || 
             (this.secondTurns!=null &&
              java.util.Arrays.equals(this.secondTurns, other.getSecondTurns())));
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
        if (getCategory() != null) {
            _hashCode += getCategory().hashCode();
        }
        if (getFirstTurn() != null) {
            _hashCode += getFirstTurn().hashCode();
        }
        if (getSecondTurns() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSecondTurns());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSecondTurns(), i);
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
        new org.apache.axis.description.TypeDesc(AdjacencyPair.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/adjacencypair/", "AdjacencyPair"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("category");
        elemField.setXmlName(new javax.xml.namespace.QName("", "category"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firstTurn");
        elemField.setXmlName(new javax.xml.namespace.QName("", "firstTurn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("secondTurns");
        elemField.setXmlName(new javax.xml.namespace.QName("", "secondTurns"));
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
