/**
 * ResourceEntry.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.foundation.resourcemanagement;

public class ResourceEntry  implements java.io.Serializable {
    private boolean active;

    private boolean authorized;

    private java.lang.String ownerUserId;

    private java.lang.String ownerUserOrganization;

    private java.util.Calendar registeredDate;

    private java.lang.String resourceId;

    private java.lang.String resourceName;

    private java.lang.String resourceType;

    private jp.go.nict.langrid.ws_1_2.LanguagePath[] supportedLanguages;

    private java.util.Calendar updatedDate;

    public ResourceEntry() {
    }

    public ResourceEntry(
           boolean active,
           boolean authorized,
           java.lang.String ownerUserId,
           java.lang.String ownerUserOrganization,
           java.util.Calendar registeredDate,
           java.lang.String resourceId,
           java.lang.String resourceName,
           java.lang.String resourceType,
           jp.go.nict.langrid.ws_1_2.LanguagePath[] supportedLanguages,
           java.util.Calendar updatedDate) {
           this.active = active;
           this.authorized = authorized;
           this.ownerUserId = ownerUserId;
           this.ownerUserOrganization = ownerUserOrganization;
           this.registeredDate = registeredDate;
           this.resourceId = resourceId;
           this.resourceName = resourceName;
           this.resourceType = resourceType;
           this.supportedLanguages = supportedLanguages;
           this.updatedDate = updatedDate;
    }


    /**
     * Gets the active value for this ResourceEntry.
     * 
     * @return active
     */
    public boolean isActive() {
        return active;
    }


    /**
     * Sets the active value for this ResourceEntry.
     * 
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }


    /**
     * Gets the authorized value for this ResourceEntry.
     * 
     * @return authorized
     */
    public boolean isAuthorized() {
        return authorized;
    }


    /**
     * Sets the authorized value for this ResourceEntry.
     * 
     * @param authorized
     */
    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }


    /**
     * Gets the ownerUserId value for this ResourceEntry.
     * 
     * @return ownerUserId
     */
    public java.lang.String getOwnerUserId() {
        return ownerUserId;
    }


    /**
     * Sets the ownerUserId value for this ResourceEntry.
     * 
     * @param ownerUserId
     */
    public void setOwnerUserId(java.lang.String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }


    /**
     * Gets the ownerUserOrganization value for this ResourceEntry.
     * 
     * @return ownerUserOrganization
     */
    public java.lang.String getOwnerUserOrganization() {
        return ownerUserOrganization;
    }


    /**
     * Sets the ownerUserOrganization value for this ResourceEntry.
     * 
     * @param ownerUserOrganization
     */
    public void setOwnerUserOrganization(java.lang.String ownerUserOrganization) {
        this.ownerUserOrganization = ownerUserOrganization;
    }


    /**
     * Gets the registeredDate value for this ResourceEntry.
     * 
     * @return registeredDate
     */
    public java.util.Calendar getRegisteredDate() {
        return registeredDate;
    }


    /**
     * Sets the registeredDate value for this ResourceEntry.
     * 
     * @param registeredDate
     */
    public void setRegisteredDate(java.util.Calendar registeredDate) {
        this.registeredDate = registeredDate;
    }


    /**
     * Gets the resourceId value for this ResourceEntry.
     * 
     * @return resourceId
     */
    public java.lang.String getResourceId() {
        return resourceId;
    }


    /**
     * Sets the resourceId value for this ResourceEntry.
     * 
     * @param resourceId
     */
    public void setResourceId(java.lang.String resourceId) {
        this.resourceId = resourceId;
    }


    /**
     * Gets the resourceName value for this ResourceEntry.
     * 
     * @return resourceName
     */
    public java.lang.String getResourceName() {
        return resourceName;
    }


    /**
     * Sets the resourceName value for this ResourceEntry.
     * 
     * @param resourceName
     */
    public void setResourceName(java.lang.String resourceName) {
        this.resourceName = resourceName;
    }


    /**
     * Gets the resourceType value for this ResourceEntry.
     * 
     * @return resourceType
     */
    public java.lang.String getResourceType() {
        return resourceType;
    }


    /**
     * Sets the resourceType value for this ResourceEntry.
     * 
     * @param resourceType
     */
    public void setResourceType(java.lang.String resourceType) {
        this.resourceType = resourceType;
    }


    /**
     * Gets the supportedLanguages value for this ResourceEntry.
     * 
     * @return supportedLanguages
     */
    public jp.go.nict.langrid.ws_1_2.LanguagePath[] getSupportedLanguages() {
        return supportedLanguages;
    }


    /**
     * Sets the supportedLanguages value for this ResourceEntry.
     * 
     * @param supportedLanguages
     */
    public void setSupportedLanguages(jp.go.nict.langrid.ws_1_2.LanguagePath[] supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }


    /**
     * Gets the updatedDate value for this ResourceEntry.
     * 
     * @return updatedDate
     */
    public java.util.Calendar getUpdatedDate() {
        return updatedDate;
    }


    /**
     * Sets the updatedDate value for this ResourceEntry.
     * 
     * @param updatedDate
     */
    public void setUpdatedDate(java.util.Calendar updatedDate) {
        this.updatedDate = updatedDate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResourceEntry)) return false;
        ResourceEntry other = (ResourceEntry) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.active == other.isActive() &&
            this.authorized == other.isAuthorized() &&
            ((this.ownerUserId==null && other.getOwnerUserId()==null) || 
             (this.ownerUserId!=null &&
              this.ownerUserId.equals(other.getOwnerUserId()))) &&
            ((this.ownerUserOrganization==null && other.getOwnerUserOrganization()==null) || 
             (this.ownerUserOrganization!=null &&
              this.ownerUserOrganization.equals(other.getOwnerUserOrganization()))) &&
            ((this.registeredDate==null && other.getRegisteredDate()==null) || 
             (this.registeredDate!=null &&
              this.registeredDate.equals(other.getRegisteredDate()))) &&
            ((this.resourceId==null && other.getResourceId()==null) || 
             (this.resourceId!=null &&
              this.resourceId.equals(other.getResourceId()))) &&
            ((this.resourceName==null && other.getResourceName()==null) || 
             (this.resourceName!=null &&
              this.resourceName.equals(other.getResourceName()))) &&
            ((this.resourceType==null && other.getResourceType()==null) || 
             (this.resourceType!=null &&
              this.resourceType.equals(other.getResourceType()))) &&
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
        _hashCode += (isAuthorized() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getOwnerUserId() != null) {
            _hashCode += getOwnerUserId().hashCode();
        }
        if (getOwnerUserOrganization() != null) {
            _hashCode += getOwnerUserOrganization().hashCode();
        }
        if (getRegisteredDate() != null) {
            _hashCode += getRegisteredDate().hashCode();
        }
        if (getResourceId() != null) {
            _hashCode += getResourceId().hashCode();
        }
        if (getResourceName() != null) {
            _hashCode += getResourceName().hashCode();
        }
        if (getResourceType() != null) {
            _hashCode += getResourceType().hashCode();
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
        new org.apache.axis.description.TypeDesc(ResourceEntry.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/resourcemanagement/", "ResourceEntry"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("active");
        elemField.setXmlName(new javax.xml.namespace.QName("", "active"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("authorized");
        elemField.setXmlName(new javax.xml.namespace.QName("", "authorized"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ownerUserId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ownerUserId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ownerUserOrganization");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ownerUserOrganization"));
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
        elemField.setFieldName("resourceId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "resourceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resourceName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "resourceName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resourceType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "resourceType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("supportedLanguages");
        elemField.setXmlName(new javax.xml.namespace.QName("", "supportedLanguages"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "LanguagePath"));
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
