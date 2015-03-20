/**
 * UserAccessEntry.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.foundation.servicemonitor;

public class UserAccessEntry  implements java.io.Serializable {
    private int accessCount;

    private java.lang.String serviceId;

    private java.lang.String serviceName;

    private long transferredSize;

    private java.lang.String userId;

    private java.lang.String userOrganization;

    public UserAccessEntry() {
    }

    public UserAccessEntry(
           int accessCount,
           java.lang.String serviceId,
           java.lang.String serviceName,
           long transferredSize,
           java.lang.String userId,
           java.lang.String userOrganization) {
           this.accessCount = accessCount;
           this.serviceId = serviceId;
           this.serviceName = serviceName;
           this.transferredSize = transferredSize;
           this.userId = userId;
           this.userOrganization = userOrganization;
    }


    /**
     * Gets the accessCount value for this UserAccessEntry.
     * 
     * @return accessCount
     */
    public int getAccessCount() {
        return accessCount;
    }


    /**
     * Sets the accessCount value for this UserAccessEntry.
     * 
     * @param accessCount
     */
    public void setAccessCount(int accessCount) {
        this.accessCount = accessCount;
    }


    /**
     * Gets the serviceId value for this UserAccessEntry.
     * 
     * @return serviceId
     */
    public java.lang.String getServiceId() {
        return serviceId;
    }


    /**
     * Sets the serviceId value for this UserAccessEntry.
     * 
     * @param serviceId
     */
    public void setServiceId(java.lang.String serviceId) {
        this.serviceId = serviceId;
    }


    /**
     * Gets the serviceName value for this UserAccessEntry.
     * 
     * @return serviceName
     */
    public java.lang.String getServiceName() {
        return serviceName;
    }


    /**
     * Sets the serviceName value for this UserAccessEntry.
     * 
     * @param serviceName
     */
    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }


    /**
     * Gets the transferredSize value for this UserAccessEntry.
     * 
     * @return transferredSize
     */
    public long getTransferredSize() {
        return transferredSize;
    }


    /**
     * Sets the transferredSize value for this UserAccessEntry.
     * 
     * @param transferredSize
     */
    public void setTransferredSize(long transferredSize) {
        this.transferredSize = transferredSize;
    }


    /**
     * Gets the userId value for this UserAccessEntry.
     * 
     * @return userId
     */
    public java.lang.String getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this UserAccessEntry.
     * 
     * @param userId
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }


    /**
     * Gets the userOrganization value for this UserAccessEntry.
     * 
     * @return userOrganization
     */
    public java.lang.String getUserOrganization() {
        return userOrganization;
    }


    /**
     * Sets the userOrganization value for this UserAccessEntry.
     * 
     * @param userOrganization
     */
    public void setUserOrganization(java.lang.String userOrganization) {
        this.userOrganization = userOrganization;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UserAccessEntry)) return false;
        UserAccessEntry other = (UserAccessEntry) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.accessCount == other.getAccessCount() &&
            ((this.serviceId==null && other.getServiceId()==null) || 
             (this.serviceId!=null &&
              this.serviceId.equals(other.getServiceId()))) &&
            ((this.serviceName==null && other.getServiceName()==null) || 
             (this.serviceName!=null &&
              this.serviceName.equals(other.getServiceName()))) &&
            this.transferredSize == other.getTransferredSize() &&
            ((this.userId==null && other.getUserId()==null) || 
             (this.userId!=null &&
              this.userId.equals(other.getUserId()))) &&
            ((this.userOrganization==null && other.getUserOrganization()==null) || 
             (this.userOrganization!=null &&
              this.userOrganization.equals(other.getUserOrganization())));
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
        _hashCode += getAccessCount();
        if (getServiceId() != null) {
            _hashCode += getServiceId().hashCode();
        }
        if (getServiceName() != null) {
            _hashCode += getServiceName().hashCode();
        }
        _hashCode += new Long(getTransferredSize()).hashCode();
        if (getUserId() != null) {
            _hashCode += getUserId().hashCode();
        }
        if (getUserOrganization() != null) {
            _hashCode += getUserOrganization().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UserAccessEntry.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemonitor/", "UserAccessEntry"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accessCount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "accessCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "serviceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "serviceName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transferredSize");
        elemField.setXmlName(new javax.xml.namespace.QName("", "transferredSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userOrganization");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userOrganization"));
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
