/**
 * TemporaryUserEntry.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.foundation.usermanagement;

public class TemporaryUserEntry  implements java.io.Serializable {
    private java.util.Calendar beginAvailableDateTime;

    private java.util.Calendar endAvailableDateTime;

    private java.lang.String userId;

    public TemporaryUserEntry() {
    }

    public TemporaryUserEntry(
           java.util.Calendar beginAvailableDateTime,
           java.util.Calendar endAvailableDateTime,
           java.lang.String userId) {
           this.beginAvailableDateTime = beginAvailableDateTime;
           this.endAvailableDateTime = endAvailableDateTime;
           this.userId = userId;
    }


    /**
     * Gets the beginAvailableDateTime value for this TemporaryUserEntry.
     * 
     * @return beginAvailableDateTime
     */
    public java.util.Calendar getBeginAvailableDateTime() {
        return beginAvailableDateTime;
    }


    /**
     * Sets the beginAvailableDateTime value for this TemporaryUserEntry.
     * 
     * @param beginAvailableDateTime
     */
    public void setBeginAvailableDateTime(java.util.Calendar beginAvailableDateTime) {
        this.beginAvailableDateTime = beginAvailableDateTime;
    }


    /**
     * Gets the endAvailableDateTime value for this TemporaryUserEntry.
     * 
     * @return endAvailableDateTime
     */
    public java.util.Calendar getEndAvailableDateTime() {
        return endAvailableDateTime;
    }


    /**
     * Sets the endAvailableDateTime value for this TemporaryUserEntry.
     * 
     * @param endAvailableDateTime
     */
    public void setEndAvailableDateTime(java.util.Calendar endAvailableDateTime) {
        this.endAvailableDateTime = endAvailableDateTime;
    }


    /**
     * Gets the userId value for this TemporaryUserEntry.
     * 
     * @return userId
     */
    public java.lang.String getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this TemporaryUserEntry.
     * 
     * @param userId
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TemporaryUserEntry)) return false;
        TemporaryUserEntry other = (TemporaryUserEntry) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.beginAvailableDateTime==null && other.getBeginAvailableDateTime()==null) || 
             (this.beginAvailableDateTime!=null &&
              this.beginAvailableDateTime.equals(other.getBeginAvailableDateTime()))) &&
            ((this.endAvailableDateTime==null && other.getEndAvailableDateTime()==null) || 
             (this.endAvailableDateTime!=null &&
              this.endAvailableDateTime.equals(other.getEndAvailableDateTime()))) &&
            ((this.userId==null && other.getUserId()==null) || 
             (this.userId!=null &&
              this.userId.equals(other.getUserId())));
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
        if (getBeginAvailableDateTime() != null) {
            _hashCode += getBeginAvailableDateTime().hashCode();
        }
        if (getEndAvailableDateTime() != null) {
            _hashCode += getEndAvailableDateTime().hashCode();
        }
        if (getUserId() != null) {
            _hashCode += getUserId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TemporaryUserEntry.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/usermanagement/", "TemporaryUserEntry"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("beginAvailableDateTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "beginAvailableDateTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endAvailableDateTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "endAvailableDateTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userId"));
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
