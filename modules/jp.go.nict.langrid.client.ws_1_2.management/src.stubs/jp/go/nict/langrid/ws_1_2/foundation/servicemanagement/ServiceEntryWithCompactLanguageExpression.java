/**
 * ServiceEntryWithCompactLanguageExpression.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.foundation.servicemanagement;

public class ServiceEntryWithCompactLanguageExpression  implements java.io.Serializable {
    private boolean active;

    private java.lang.String endpointUrl;

    private java.lang.String instanceType;

    private java.lang.String ownerUserId;

    private java.util.Calendar registeredDate;

    private java.lang.String serviceDescription;

    private java.lang.String serviceId;

    private java.lang.String serviceName;

    private java.lang.String serviceType;

    private jp.go.nict.langrid.ws_1_2.LanguagePathWithType[] supportedLanguages;

    private java.util.Calendar updatedDate;

    public ServiceEntryWithCompactLanguageExpression() {
    }

    public ServiceEntryWithCompactLanguageExpression(
           boolean active,
           java.lang.String endpointUrl,
           java.lang.String instanceType,
           java.lang.String ownerUserId,
           java.util.Calendar registeredDate,
           java.lang.String serviceDescription,
           java.lang.String serviceId,
           java.lang.String serviceName,
           java.lang.String serviceType,
           jp.go.nict.langrid.ws_1_2.LanguagePathWithType[] supportedLanguages,
           java.util.Calendar updatedDate) {
           this.active = active;
           this.endpointUrl = endpointUrl;
           this.instanceType = instanceType;
           this.ownerUserId = ownerUserId;
           this.registeredDate = registeredDate;
           this.serviceDescription = serviceDescription;
           this.serviceId = serviceId;
           this.serviceName = serviceName;
           this.serviceType = serviceType;
           this.supportedLanguages = supportedLanguages;
           this.updatedDate = updatedDate;
    }


    /**
     * Gets the active value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @return active
     */
    public boolean isActive() {
        return active;
    }


    /**
     * Sets the active value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }


    /**
     * Gets the endpointUrl value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @return endpointUrl
     */
    public java.lang.String getEndpointUrl() {
        return endpointUrl;
    }


    /**
     * Sets the endpointUrl value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @param endpointUrl
     */
    public void setEndpointUrl(java.lang.String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }


    /**
     * Gets the instanceType value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @return instanceType
     */
    public java.lang.String getInstanceType() {
        return instanceType;
    }


    /**
     * Sets the instanceType value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @param instanceType
     */
    public void setInstanceType(java.lang.String instanceType) {
        this.instanceType = instanceType;
    }


    /**
     * Gets the ownerUserId value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @return ownerUserId
     */
    public java.lang.String getOwnerUserId() {
        return ownerUserId;
    }


    /**
     * Sets the ownerUserId value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @param ownerUserId
     */
    public void setOwnerUserId(java.lang.String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }


    /**
     * Gets the registeredDate value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @return registeredDate
     */
    public java.util.Calendar getRegisteredDate() {
        return registeredDate;
    }


    /**
     * Sets the registeredDate value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @param registeredDate
     */
    public void setRegisteredDate(java.util.Calendar registeredDate) {
        this.registeredDate = registeredDate;
    }


    /**
     * Gets the serviceDescription value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @return serviceDescription
     */
    public java.lang.String getServiceDescription() {
        return serviceDescription;
    }


    /**
     * Sets the serviceDescription value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @param serviceDescription
     */
    public void setServiceDescription(java.lang.String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }


    /**
     * Gets the serviceId value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @return serviceId
     */
    public java.lang.String getServiceId() {
        return serviceId;
    }


    /**
     * Sets the serviceId value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @param serviceId
     */
    public void setServiceId(java.lang.String serviceId) {
        this.serviceId = serviceId;
    }


    /**
     * Gets the serviceName value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @return serviceName
     */
    public java.lang.String getServiceName() {
        return serviceName;
    }


    /**
     * Sets the serviceName value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @param serviceName
     */
    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }


    /**
     * Gets the serviceType value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @return serviceType
     */
    public java.lang.String getServiceType() {
        return serviceType;
    }


    /**
     * Sets the serviceType value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @param serviceType
     */
    public void setServiceType(java.lang.String serviceType) {
        this.serviceType = serviceType;
    }


    /**
     * Gets the supportedLanguages value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @return supportedLanguages
     */
    public jp.go.nict.langrid.ws_1_2.LanguagePathWithType[] getSupportedLanguages() {
        return supportedLanguages;
    }


    /**
     * Sets the supportedLanguages value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @param supportedLanguages
     */
    public void setSupportedLanguages(jp.go.nict.langrid.ws_1_2.LanguagePathWithType[] supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }


    /**
     * Gets the updatedDate value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @return updatedDate
     */
    public java.util.Calendar getUpdatedDate() {
        return updatedDate;
    }


    /**
     * Sets the updatedDate value for this ServiceEntryWithCompactLanguageExpression.
     * 
     * @param updatedDate
     */
    public void setUpdatedDate(java.util.Calendar updatedDate) {
        this.updatedDate = updatedDate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ServiceEntryWithCompactLanguageExpression)) return false;
        ServiceEntryWithCompactLanguageExpression other = (ServiceEntryWithCompactLanguageExpression) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.active == other.isActive() &&
            ((this.endpointUrl==null && other.getEndpointUrl()==null) || 
             (this.endpointUrl!=null &&
              this.endpointUrl.equals(other.getEndpointUrl()))) &&
            ((this.instanceType==null && other.getInstanceType()==null) || 
             (this.instanceType!=null &&
              this.instanceType.equals(other.getInstanceType()))) &&
            ((this.ownerUserId==null && other.getOwnerUserId()==null) || 
             (this.ownerUserId!=null &&
              this.ownerUserId.equals(other.getOwnerUserId()))) &&
            ((this.registeredDate==null && other.getRegisteredDate()==null) || 
             (this.registeredDate!=null &&
              this.registeredDate.equals(other.getRegisteredDate()))) &&
            ((this.serviceDescription==null && other.getServiceDescription()==null) || 
             (this.serviceDescription!=null &&
              this.serviceDescription.equals(other.getServiceDescription()))) &&
            ((this.serviceId==null && other.getServiceId()==null) || 
             (this.serviceId!=null &&
              this.serviceId.equals(other.getServiceId()))) &&
            ((this.serviceName==null && other.getServiceName()==null) || 
             (this.serviceName!=null &&
              this.serviceName.equals(other.getServiceName()))) &&
            ((this.serviceType==null && other.getServiceType()==null) || 
             (this.serviceType!=null &&
              this.serviceType.equals(other.getServiceType()))) &&
            ((this.supportedLanguages==null && other.getSupportedLanguages()==null) || 
             (this.supportedLanguages!=null &&
              java.util.Arrays.equals(this.supportedLanguages, other.getSupportedLanguages()))) &&
            ((this.updatedDate==null && other.getUpdatedDate()==null) || 
             (this.updatedDate!=null &&
              this.updatedDate.equals(other.getUpdatedDate())));
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
        _hashCode += (isActive() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getEndpointUrl() != null) {
            _hashCode += getEndpointUrl().hashCode();
        }
        if (getInstanceType() != null) {
            _hashCode += getInstanceType().hashCode();
        }
        if (getOwnerUserId() != null) {
            _hashCode += getOwnerUserId().hashCode();
        }
        if (getRegisteredDate() != null) {
            _hashCode += getRegisteredDate().hashCode();
        }
        if (getServiceDescription() != null) {
            _hashCode += getServiceDescription().hashCode();
        }
        if (getServiceId() != null) {
            _hashCode += getServiceId().hashCode();
        }
        if (getServiceName() != null) {
            _hashCode += getServiceName().hashCode();
        }
        if (getServiceType() != null) {
            _hashCode += getServiceType().hashCode();
        }
        if (getSupportedLanguages() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSupportedLanguages());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSupportedLanguages(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getUpdatedDate() != null) {
            _hashCode += getUpdatedDate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ServiceEntryWithCompactLanguageExpression.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceEntryWithCompactLanguageExpression"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("active");
        elemField.setXmlName(new javax.xml.namespace.QName("", "active"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endpointUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "endpointUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("instanceType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "instanceType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ownerUserId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ownerUserId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registeredDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "registeredDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "serviceDescription"));
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
        elemField.setFieldName("serviceName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "serviceName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "serviceType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("supportedLanguages");
        elemField.setXmlName(new javax.xml.namespace.QName("", "supportedLanguages"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "LanguagePathWithType"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("updatedDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "updatedDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
