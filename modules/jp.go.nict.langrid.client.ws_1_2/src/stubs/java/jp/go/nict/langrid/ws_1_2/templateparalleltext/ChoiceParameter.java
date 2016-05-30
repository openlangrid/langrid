/**
 * ChoiceParameter.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.templateparalleltext;

public class ChoiceParameter  implements java.io.Serializable {
    private jp.go.nict.langrid.ws_1_2.templateparalleltext.Choice[] choices;

    private java.lang.String parameterId;

    public ChoiceParameter() {
    }

    public ChoiceParameter(
           jp.go.nict.langrid.ws_1_2.templateparalleltext.Choice[] choices,
           java.lang.String parameterId) {
           this.choices = choices;
           this.parameterId = parameterId;
    }


    /**
     * Gets the choices value for this ChoiceParameter.
     * 
     * @return choices
     */
    public jp.go.nict.langrid.ws_1_2.templateparalleltext.Choice[] getChoices() {
        return choices;
    }


    /**
     * Sets the choices value for this ChoiceParameter.
     * 
     * @param choices
     */
    public void setChoices(jp.go.nict.langrid.ws_1_2.templateparalleltext.Choice[] choices) {
        this.choices = choices;
    }


    /**
     * Gets the parameterId value for this ChoiceParameter.
     * 
     * @return parameterId
     */
    public java.lang.String getParameterId() {
        return parameterId;
    }


    /**
     * Sets the parameterId value for this ChoiceParameter.
     * 
     * @param parameterId
     */
    public void setParameterId(java.lang.String parameterId) {
        this.parameterId = parameterId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ChoiceParameter)) return false;
        ChoiceParameter other = (ChoiceParameter) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.choices==null && other.getChoices()==null) || 
             (this.choices!=null &&
              java.util.Arrays.equals(this.choices, other.getChoices()))) &&
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
        if (getChoices() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getChoices());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getChoices(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getParameterId() != null) {
            _hashCode += getParameterId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ChoiceParameter.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/templateparalleltext/", "ChoiceParameter"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("choices");
        elemField.setXmlName(new javax.xml.namespace.QName("", "choices"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/templateparalleltext/", "Choice"));
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
