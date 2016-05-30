/**
 * BoundChoiceParameter.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.templateparalleltext;

public class BoundChoiceParameter  implements java.io.Serializable {
    private java.lang.String choiceId;

    private java.lang.String parameterId;

    public BoundChoiceParameter() {
    }

    public BoundChoiceParameter(
           java.lang.String choiceId,
           java.lang.String parameterId) {
           this.choiceId = choiceId;
           this.parameterId = parameterId;
    }


    /**
     * Gets the choiceId value for this BoundChoiceParameter.
     * 
     * @return choiceId
     */
    public java.lang.String getChoiceId() {
        return choiceId;
    }


    /**
     * Sets the choiceId value for this BoundChoiceParameter.
     * 
     * @param choiceId
     */
    public void setChoiceId(java.lang.String choiceId) {
        this.choiceId = choiceId;
    }


    /**
     * Gets the parameterId value for this BoundChoiceParameter.
     * 
     * @return parameterId
     */
    public java.lang.String getParameterId() {
        return parameterId;
    }


    /**
     * Sets the parameterId value for this BoundChoiceParameter.
     * 
     * @param parameterId
     */
    public void setParameterId(java.lang.String parameterId) {
        this.parameterId = parameterId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BoundChoiceParameter)) return false;
        BoundChoiceParameter other = (BoundChoiceParameter) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.choiceId==null && other.getChoiceId()==null) || 
             (this.choiceId!=null &&
              this.choiceId.equals(other.getChoiceId()))) &&
            ((this.parameterId==null && other.getParameterId()==null) || 
             (this.parameterId!=null &&
              this.parameterId.equals(other.getParameterId())));
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
        if (getChoiceId() != null) {
            _hashCode += getChoiceId().hashCode();
        }
        if (getParameterId() != null) {
            _hashCode += getParameterId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BoundChoiceParameter.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/templateparalleltext/", "BoundChoiceParameter"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("choiceId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "choiceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parameterId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "parameterId"));
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
