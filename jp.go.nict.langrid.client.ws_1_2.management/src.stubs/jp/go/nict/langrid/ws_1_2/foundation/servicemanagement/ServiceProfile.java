/**
 * ServiceProfile.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.foundation.servicemanagement;

public class ServiceProfile  implements java.io.Serializable {
    private java.lang.String copyrightInfo;

    private java.lang.String licenseInfo;

    private java.lang.String serviceDescription;

    private java.lang.String serviceName;

    public ServiceProfile() {
    }

    public ServiceProfile(
           java.lang.String copyrightInfo,
           java.lang.String licenseInfo,
           java.lang.String serviceDescription,
           java.lang.String serviceName) {
           this.copyrightInfo = copyrightInfo;
           this.licenseInfo = licenseInfo;
           this.serviceDescription = serviceDescription;
           this.serviceName = serviceName;
    }


    /**
     * Gets the copyrightInfo value for this ServiceProfile.
     * 
     * @return copyrightInfo
     */
    public java.lang.String getCopyrightInfo() {
        return copyrightInfo;
    }


    /**
     * Sets the copyrightInfo value for this ServiceProfile.
     * 
     * @param copyrightInfo
     */
    public void setCopyrightInfo(java.lang.String copyrightInfo) {
        this.copyrightInfo = copyrightInfo;
    }


    /**
     * Gets the licenseInfo value for this ServiceProfile.
     * 
     * @return licenseInfo
     */
    public java.lang.String getLicenseInfo() {
        return licenseInfo;
    }


    /**
     * Sets the licenseInfo value for this ServiceProfile.
     * 
     * @param licenseInfo
     */
    public void setLicenseInfo(java.lang.String licenseInfo) {
        this.licenseInfo = licenseInfo;
    }


    /**
     * Gets the serviceDescription value for this ServiceProfile.
     * 
     * @return serviceDescription
     */
    public java.lang.String getServiceDescription() {
        return serviceDescription;
    }


    /**
     * Sets the serviceDescription value for this ServiceProfile.
     * 
     * @param serviceDescription
     */
    public void setServiceDescription(java.lang.String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }


    /**
     * Gets the serviceName value for this ServiceProfile.
     * 
     * @return serviceName
     */
    public java.lang.String getServiceName() {
        return serviceName;
    }


    /**
     * Sets the serviceName value for this ServiceProfile.
     * 
     * @param serviceName
     */
    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ServiceProfile)) return false;
        ServiceProfile other = (ServiceProfile) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.copyrightInfo==null && other.getCopyrightInfo()==null) || 
             (this.copyrightInfo!=null &&
              this.copyrightInfo.equals(other.getCopyrightInfo()))) &&
            ((this.licenseInfo==null && other.getLicenseInfo()==null) || 
             (this.licenseInfo!=null &&
              this.licenseInfo.equals(other.getLicenseInfo()))) &&
            ((this.serviceDescription==null && other.getServiceDescription()==null) || 
             (this.serviceDescription!=null &&
              this.serviceDescription.equals(other.getServiceDescription()))) &&
            ((this.serviceName==null && other.getServiceName()==null) || 
             (this.serviceName!=null &&
              this.serviceName.equals(other.getServiceName())));
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
        if (getCopyrightInfo() != null) {
            _hashCode += getCopyrightInfo().hashCode();
        }
        if (getLicenseInfo() != null) {
            _hashCode += getLicenseInfo().hashCode();
        }
        if (getServiceDescription() != null) {
            _hashCode += getServiceDescription().hashCode();
        }
        if (getServiceName() != null) {
            _hashCode += getServiceName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ServiceProfile.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceProfile"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("copyrightInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "copyrightInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("licenseInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "licenseInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "serviceDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "serviceName"));
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
