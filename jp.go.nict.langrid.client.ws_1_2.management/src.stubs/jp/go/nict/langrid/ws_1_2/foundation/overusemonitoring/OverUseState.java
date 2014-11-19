/**
 * OverUseState.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.foundation.overusemonitoring;

public class OverUseState  implements java.io.Serializable {
    private java.util.Calendar baseDateTime;

    private long currentCount;

    private java.util.Calendar lastAccessDateTime;

    private int limitCount;

    private java.lang.String limitType;

    private java.lang.String period;

    private java.lang.String serviceId;

    private java.lang.String userId;

    public OverUseState() {
    }

    public OverUseState(
           java.util.Calendar baseDateTime,
           long currentCount,
           java.util.Calendar lastAccessDateTime,
           int limitCount,
           java.lang.String limitType,
           java.lang.String period,
           java.lang.String serviceId,
           java.lang.String userId) {
           this.baseDateTime = baseDateTime;
           this.currentCount = currentCount;
           this.lastAccessDateTime = lastAccessDateTime;
           this.limitCount = limitCount;
           this.limitType = limitType;
           this.period = period;
           this.serviceId = serviceId;
           this.userId = userId;
    }


    /**
     * Gets the baseDateTime value for this OverUseState.
     * 
     * @return baseDateTime
     */
    public java.util.Calendar getBaseDateTime() {
        return baseDateTime;
    }


    /**
     * Sets the baseDateTime value for this OverUseState.
     * 
     * @param baseDateTime
     */
    public void setBaseDateTime(java.util.Calendar baseDateTime) {
        this.baseDateTime = baseDateTime;
    }


    /**
     * Gets the currentCount value for this OverUseState.
     * 
     * @return currentCount
     */
    public long getCurrentCount() {
        return currentCount;
    }


    /**
     * Sets the currentCount value for this OverUseState.
     * 
     * @param currentCount
     */
    public void setCurrentCount(long currentCount) {
        this.currentCount = currentCount;
    }


    /**
     * Gets the lastAccessDateTime value for this OverUseState.
     * 
     * @return lastAccessDateTime
     */
    public java.util.Calendar getLastAccessDateTime() {
        return lastAccessDateTime;
    }


    /**
     * Sets the lastAccessDateTime value for this OverUseState.
     * 
     * @param lastAccessDateTime
     */
    public void setLastAccessDateTime(java.util.Calendar lastAccessDateTime) {
        this.lastAccessDateTime = lastAccessDateTime;
    }


    /**
     * Gets the limitCount value for this OverUseState.
     * 
     * @return limitCount
     */
    public int getLimitCount() {
        return limitCount;
    }


    /**
     * Sets the limitCount value for this OverUseState.
     * 
     * @param limitCount
     */
    public void setLimitCount(int limitCount) {
        this.limitCount = limitCount;
    }


    /**
     * Gets the limitType value for this OverUseState.
     * 
     * @return limitType
     */
    public java.lang.String getLimitType() {
        return limitType;
    }


    /**
     * Sets the limitType value for this OverUseState.
     * 
     * @param limitType
     */
    public void setLimitType(java.lang.String limitType) {
        this.limitType = limitType;
    }


    /**
     * Gets the period value for this OverUseState.
     * 
     * @return period
     */
    public java.lang.String getPeriod() {
        return period;
    }


    /**
     * Sets the period value for this OverUseState.
     * 
     * @param period
     */
    public void setPeriod(java.lang.String period) {
        this.period = period;
    }


    /**
     * Gets the serviceId value for this OverUseState.
     * 
     * @return serviceId
     */
    public java.lang.String getServiceId() {
        return serviceId;
    }


    /**
     * Sets the serviceId value for this OverUseState.
     * 
     * @param serviceId
     */
    public void setServiceId(java.lang.String serviceId) {
        this.serviceId = serviceId;
    }


    /**
     * Gets the userId value for this OverUseState.
     * 
     * @return userId
     */
    public java.lang.String getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this OverUseState.
     * 
     * @param userId
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OverUseState)) return false;
        OverUseState other = (OverUseState) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.baseDateTime==null && other.getBaseDateTime()==null) || 
             (this.baseDateTime!=null &&
              this.baseDateTime.equals(other.getBaseDateTime()))) &&
            this.currentCount == other.getCurrentCount() &&
            ((this.lastAccessDateTime==null && other.getLastAccessDateTime()==null) || 
             (this.lastAccessDateTime!=null &&
              this.lastAccessDateTime.equals(other.getLastAccessDateTime()))) &&
            this.limitCount == other.getLimitCount() &&
            ((this.limitType==null && other.getLimitType()==null) || 
             (this.limitType!=null &&
              this.limitType.equals(other.getLimitType()))) &&
            ((this.period==null && other.getPeriod()==null) || 
             (this.period!=null &&
              this.period.equals(other.getPeriod()))) &&
            ((this.serviceId==null && other.getServiceId()==null) || 
             (this.serviceId!=null &&
              this.serviceId.equals(other.getServiceId()))) &&
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
        if (getBaseDateTime() != null) {
            _hashCode += getBaseDateTime().hashCode();
        }
        _hashCode += new Long(getCurrentCount()).hashCode();
        if (getLastAccessDateTime() != null) {
            _hashCode += getLastAccessDateTime().hashCode();
        }
        _hashCode += getLimitCount();
        if (getLimitType() != null) {
            _hashCode += getLimitType().hashCode();
        }
        if (getPeriod() != null) {
            _hashCode += getPeriod().hashCode();
        }
        if (getServiceId() != null) {
            _hashCode += getServiceId().hashCode();
        }
        if (getUserId() != null) {
            _hashCode += getUserId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OverUseState.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/overusemonitoring/", "OverUseState"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("baseDateTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "baseDateTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("currentCount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "currentCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastAccessDateTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lastAccessDateTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("limitCount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "limitCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("limitType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "limitType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("period");
        elemField.setXmlName(new javax.xml.namespace.QName("", "period"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "serviceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
