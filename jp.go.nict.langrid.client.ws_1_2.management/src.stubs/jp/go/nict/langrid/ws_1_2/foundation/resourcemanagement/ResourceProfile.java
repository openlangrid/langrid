/**
 * ResourceProfile.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.foundation.resourcemanagement;

public class ResourceProfile  implements java.io.Serializable {
    private java.lang.String copyrightInfo;

    private java.lang.String cpuInfo;

    private java.lang.String howToGetMembershipInfo;

    private java.lang.String licenseInfo;

    private boolean membersOnly;

    private java.lang.String memoryInfo;

    private java.lang.String resourceDescription;

    private java.lang.String resourceName;

    private java.lang.String specialNoteInfo;

    public ResourceProfile() {
    }

    public ResourceProfile(
           java.lang.String copyrightInfo,
           java.lang.String cpuInfo,
           java.lang.String howToGetMembershipInfo,
           java.lang.String licenseInfo,
           boolean membersOnly,
           java.lang.String memoryInfo,
           java.lang.String resourceDescription,
           java.lang.String resourceName,
           java.lang.String specialNoteInfo) {
           this.copyrightInfo = copyrightInfo;
           this.cpuInfo = cpuInfo;
           this.howToGetMembershipInfo = howToGetMembershipInfo;
           this.licenseInfo = licenseInfo;
           this.membersOnly = membersOnly;
           this.memoryInfo = memoryInfo;
           this.resourceDescription = resourceDescription;
           this.resourceName = resourceName;
           this.specialNoteInfo = specialNoteInfo;
    }


    /**
     * Gets the copyrightInfo value for this ResourceProfile.
     * 
     * @return copyrightInfo
     */
    public java.lang.String getCopyrightInfo() {
        return copyrightInfo;
    }


    /**
     * Sets the copyrightInfo value for this ResourceProfile.
     * 
     * @param copyrightInfo
     */
    public void setCopyrightInfo(java.lang.String copyrightInfo) {
        this.copyrightInfo = copyrightInfo;
    }


    /**
     * Gets the cpuInfo value for this ResourceProfile.
     * 
     * @return cpuInfo
     */
    public java.lang.String getCpuInfo() {
        return cpuInfo;
    }


    /**
     * Sets the cpuInfo value for this ResourceProfile.
     * 
     * @param cpuInfo
     */
    public void setCpuInfo(java.lang.String cpuInfo) {
        this.cpuInfo = cpuInfo;
    }


    /**
     * Gets the howToGetMembershipInfo value for this ResourceProfile.
     * 
     * @return howToGetMembershipInfo
     */
    public java.lang.String getHowToGetMembershipInfo() {
        return howToGetMembershipInfo;
    }


    /**
     * Sets the howToGetMembershipInfo value for this ResourceProfile.
     * 
     * @param howToGetMembershipInfo
     */
    public void setHowToGetMembershipInfo(java.lang.String howToGetMembershipInfo) {
        this.howToGetMembershipInfo = howToGetMembershipInfo;
    }


    /**
     * Gets the licenseInfo value for this ResourceProfile.
     * 
     * @return licenseInfo
     */
    public java.lang.String getLicenseInfo() {
        return licenseInfo;
    }


    /**
     * Sets the licenseInfo value for this ResourceProfile.
     * 
     * @param licenseInfo
     */
    public void setLicenseInfo(java.lang.String licenseInfo) {
        this.licenseInfo = licenseInfo;
    }


    /**
     * Gets the membersOnly value for this ResourceProfile.
     * 
     * @return membersOnly
     */
    public boolean isMembersOnly() {
        return membersOnly;
    }


    /**
     * Sets the membersOnly value for this ResourceProfile.
     * 
     * @param membersOnly
     */
    public void setMembersOnly(boolean membersOnly) {
        this.membersOnly = membersOnly;
    }


    /**
     * Gets the memoryInfo value for this ResourceProfile.
     * 
     * @return memoryInfo
     */
    public java.lang.String getMemoryInfo() {
        return memoryInfo;
    }


    /**
     * Sets the memoryInfo value for this ResourceProfile.
     * 
     * @param memoryInfo
     */
    public void setMemoryInfo(java.lang.String memoryInfo) {
        this.memoryInfo = memoryInfo;
    }


    /**
     * Gets the resourceDescription value for this ResourceProfile.
     * 
     * @return resourceDescription
     */
    public java.lang.String getResourceDescription() {
        return resourceDescription;
    }


    /**
     * Sets the resourceDescription value for this ResourceProfile.
     * 
     * @param resourceDescription
     */
    public void setResourceDescription(java.lang.String resourceDescription) {
        this.resourceDescription = resourceDescription;
    }


    /**
     * Gets the resourceName value for this ResourceProfile.
     * 
     * @return resourceName
     */
    public java.lang.String getResourceName() {
        return resourceName;
    }


    /**
     * Sets the resourceName value for this ResourceProfile.
     * 
     * @param resourceName
     */
    public void setResourceName(java.lang.String resourceName) {
        this.resourceName = resourceName;
    }


    /**
     * Gets the specialNoteInfo value for this ResourceProfile.
     * 
     * @return specialNoteInfo
     */
    public java.lang.String getSpecialNoteInfo() {
        return specialNoteInfo;
    }


    /**
     * Sets the specialNoteInfo value for this ResourceProfile.
     * 
     * @param specialNoteInfo
     */
    public void setSpecialNoteInfo(java.lang.String specialNoteInfo) {
        this.specialNoteInfo = specialNoteInfo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResourceProfile)) return false;
        ResourceProfile other = (ResourceProfile) obj;
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
            ((this.cpuInfo==null && other.getCpuInfo()==null) || 
             (this.cpuInfo!=null &&
              this.cpuInfo.equals(other.getCpuInfo()))) &&
            ((this.howToGetMembershipInfo==null && other.getHowToGetMembershipInfo()==null) || 
             (this.howToGetMembershipInfo!=null &&
              this.howToGetMembershipInfo.equals(other.getHowToGetMembershipInfo()))) &&
            ((this.licenseInfo==null && other.getLicenseInfo()==null) || 
             (this.licenseInfo!=null &&
              this.licenseInfo.equals(other.getLicenseInfo()))) &&
            this.membersOnly == other.isMembersOnly() &&
            ((this.memoryInfo==null && other.getMemoryInfo()==null) || 
             (this.memoryInfo!=null &&
              this.memoryInfo.equals(other.getMemoryInfo()))) &&
            ((this.resourceDescription==null && other.getResourceDescription()==null) || 
             (this.resourceDescription!=null &&
              this.resourceDescription.equals(other.getResourceDescription()))) &&
            ((this.resourceName==null && other.getResourceName()==null) || 
             (this.resourceName!=null &&
              this.resourceName.equals(other.getResourceName()))) &&
            ((this.specialNoteInfo==null && other.getSpecialNoteInfo()==null) || 
             (this.specialNoteInfo!=null &&
              this.specialNoteInfo.equals(other.getSpecialNoteInfo())));
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
        if (getCpuInfo() != null) {
            _hashCode += getCpuInfo().hashCode();
        }
        if (getHowToGetMembershipInfo() != null) {
            _hashCode += getHowToGetMembershipInfo().hashCode();
        }
        if (getLicenseInfo() != null) {
            _hashCode += getLicenseInfo().hashCode();
        }
        _hashCode += (isMembersOnly() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getMemoryInfo() != null) {
            _hashCode += getMemoryInfo().hashCode();
        }
        if (getResourceDescription() != null) {
            _hashCode += getResourceDescription().hashCode();
        }
        if (getResourceName() != null) {
            _hashCode += getResourceName().hashCode();
        }
        if (getSpecialNoteInfo() != null) {
            _hashCode += getSpecialNoteInfo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResourceProfile.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/resourcemanagement/", "ResourceProfile"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("copyrightInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "copyrightInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cpuInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cpuInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("howToGetMembershipInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "howToGetMembershipInfo"));
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
        elemField.setFieldName("membersOnly");
        elemField.setXmlName(new javax.xml.namespace.QName("", "membersOnly"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("memoryInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "memoryInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resourceDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "resourceDescription"));
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
        elemField.setFieldName("specialNoteInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "specialNoteInfo"));
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
