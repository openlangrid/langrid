/**
 * MatchingCondition.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.foundation;

public class MatchingCondition  implements java.io.Serializable {
    private java.lang.String fieldName;

    private java.lang.String matchingMethod;

    private java.lang.String matchingValue;

    public MatchingCondition() {
    }

    public MatchingCondition(
           java.lang.String fieldName,
           java.lang.String matchingMethod,
           java.lang.String matchingValue) {
           this.fieldName = fieldName;
           this.matchingMethod = matchingMethod;
           this.matchingValue = matchingValue;
    }


    /**
     * Gets the fieldName value for this MatchingCondition.
     * 
     * @return fieldName
     */
    public java.lang.String getFieldName() {
        return fieldName;
    }


    /**
     * Sets the fieldName value for this MatchingCondition.
     * 
     * @param fieldName
     */
    public void setFieldName(java.lang.String fieldName) {
        this.fieldName = fieldName;
    }


    /**
     * Gets the matchingMethod value for this MatchingCondition.
     * 
     * @return matchingMethod
     */
    public java.lang.String getMatchingMethod() {
        return matchingMethod;
    }


    /**
     * Sets the matchingMethod value for this MatchingCondition.
     * 
     * @param matchingMethod
     */
    public void setMatchingMethod(java.lang.String matchingMethod) {
        this.matchingMethod = matchingMethod;
    }


    /**
     * Gets the matchingValue value for this MatchingCondition.
     * 
     * @return matchingValue
     */
    public java.lang.String getMatchingValue() {
        return matchingValue;
    }


    /**
     * Sets the matchingValue value for this MatchingCondition.
     * 
     * @param matchingValue
     */
    public void setMatchingValue(java.lang.String matchingValue) {
        this.matchingValue = matchingValue;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MatchingCondition)) return false;
        MatchingCondition other = (MatchingCondition) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fieldName==null && other.getFieldName()==null) || 
             (this.fieldName!=null &&
              this.fieldName.equals(other.getFieldName()))) &&
            ((this.matchingMethod==null && other.getMatchingMethod()==null) || 
             (this.matchingMethod!=null &&
              this.matchingMethod.equals(other.getMatchingMethod()))) &&
            ((this.matchingValue==null && other.getMatchingValue()==null) || 
             (this.matchingValue!=null &&
              this.matchingValue.equals(other.getMatchingValue())));
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
        if (getFieldName() != null) {
            _hashCode += getFieldName().hashCode();
        }
        if (getMatchingMethod() != null) {
            _hashCode += getMatchingMethod().hashCode();
        }
        if (getMatchingValue() != null) {
            _hashCode += getMatchingValue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MatchingCondition.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/", "MatchingCondition"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fieldName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fieldName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matchingMethod");
        elemField.setXmlName(new javax.xml.namespace.QName("", "matchingMethod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matchingValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "matchingValue"));
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
