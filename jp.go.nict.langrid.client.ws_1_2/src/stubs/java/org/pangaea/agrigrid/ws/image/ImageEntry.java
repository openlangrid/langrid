/**
 * ImageEntry.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package org.pangaea.agrigrid.ws.image;

public class ImageEntry  implements java.io.Serializable {
    private org.pangaea.agrigrid.ws.Caption[] captions;

    private org.pangaea.agrigrid.ws.Category[] categories;

    private java.lang.String copyright;

    private java.util.Calendar createdAt;

    private java.lang.String fileName;

    private java.lang.String imageId;

    private java.lang.String license;

    private java.util.Calendar updatedAt;

    private java.lang.String url;

    public ImageEntry() {
    }

    public ImageEntry(
           org.pangaea.agrigrid.ws.Caption[] captions,
           org.pangaea.agrigrid.ws.Category[] categories,
           java.lang.String copyright,
           java.util.Calendar createdAt,
           java.lang.String fileName,
           java.lang.String imageId,
           java.lang.String license,
           java.util.Calendar updatedAt,
           java.lang.String url) {
           this.captions = captions;
           this.categories = categories;
           this.copyright = copyright;
           this.createdAt = createdAt;
           this.fileName = fileName;
           this.imageId = imageId;
           this.license = license;
           this.updatedAt = updatedAt;
           this.url = url;
    }


    /**
     * Gets the captions value for this ImageEntry.
     * 
     * @return captions
     */
    public org.pangaea.agrigrid.ws.Caption[] getCaptions() {
        return captions;
    }


    /**
     * Sets the captions value for this ImageEntry.
     * 
     * @param captions
     */
    public void setCaptions(org.pangaea.agrigrid.ws.Caption[] captions) {
        this.captions = captions;
    }


    /**
     * Gets the categories value for this ImageEntry.
     * 
     * @return categories
     */
    public org.pangaea.agrigrid.ws.Category[] getCategories() {
        return categories;
    }


    /**
     * Sets the categories value for this ImageEntry.
     * 
     * @param categories
     */
    public void setCategories(org.pangaea.agrigrid.ws.Category[] categories) {
        this.categories = categories;
    }


    /**
     * Gets the copyright value for this ImageEntry.
     * 
     * @return copyright
     */
    public java.lang.String getCopyright() {
        return copyright;
    }


    /**
     * Sets the copyright value for this ImageEntry.
     * 
     * @param copyright
     */
    public void setCopyright(java.lang.String copyright) {
        this.copyright = copyright;
    }


    /**
     * Gets the createdAt value for this ImageEntry.
     * 
     * @return createdAt
     */
    public java.util.Calendar getCreatedAt() {
        return createdAt;
    }


    /**
     * Sets the createdAt value for this ImageEntry.
     * 
     * @param createdAt
     */
    public void setCreatedAt(java.util.Calendar createdAt) {
        this.createdAt = createdAt;
    }


    /**
     * Gets the fileName value for this ImageEntry.
     * 
     * @return fileName
     */
    public java.lang.String getFileName() {
        return fileName;
    }


    /**
     * Sets the fileName value for this ImageEntry.
     * 
     * @param fileName
     */
    public void setFileName(java.lang.String fileName) {
        this.fileName = fileName;
    }


    /**
     * Gets the imageId value for this ImageEntry.
     * 
     * @return imageId
     */
    public java.lang.String getImageId() {
        return imageId;
    }


    /**
     * Sets the imageId value for this ImageEntry.
     * 
     * @param imageId
     */
    public void setImageId(java.lang.String imageId) {
        this.imageId = imageId;
    }


    /**
     * Gets the license value for this ImageEntry.
     * 
     * @return license
     */
    public java.lang.String getLicense() {
        return license;
    }


    /**
     * Sets the license value for this ImageEntry.
     * 
     * @param license
     */
    public void setLicense(java.lang.String license) {
        this.license = license;
    }


    /**
     * Gets the updatedAt value for this ImageEntry.
     * 
     * @return updatedAt
     */
    public java.util.Calendar getUpdatedAt() {
        return updatedAt;
    }


    /**
     * Sets the updatedAt value for this ImageEntry.
     * 
     * @param updatedAt
     */
    public void setUpdatedAt(java.util.Calendar updatedAt) {
        this.updatedAt = updatedAt;
    }


    /**
     * Gets the url value for this ImageEntry.
     * 
     * @return url
     */
    public java.lang.String getUrl() {
        return url;
    }


    /**
     * Sets the url value for this ImageEntry.
     * 
     * @param url
     */
    public void setUrl(java.lang.String url) {
        this.url = url;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ImageEntry)) return false;
        ImageEntry other = (ImageEntry) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.captions==null && other.getCaptions()==null) || 
             (this.captions!=null &&
              java.util.Arrays.equals(this.captions, other.getCaptions()))) &&
            ((this.categories==null && other.getCategories()==null) || 
             (this.categories!=null &&
              java.util.Arrays.equals(this.categories, other.getCategories()))) &&
            ((this.copyright==null && other.getCopyright()==null) || 
             (this.copyright!=null &&
              this.copyright.equals(other.getCopyright()))) &&
            ((this.createdAt==null && other.getCreatedAt()==null) || 
             (this.createdAt!=null &&
              this.createdAt.equals(other.getCreatedAt()))) &&
            ((this.fileName==null && other.getFileName()==null) || 
             (this.fileName!=null &&
              this.fileName.equals(other.getFileName()))) &&
            ((this.imageId==null && other.getImageId()==null) || 
             (this.imageId!=null &&
              this.imageId.equals(other.getImageId()))) &&
            ((this.license==null && other.getLicense()==null) || 
             (this.license!=null &&
              this.license.equals(other.getLicense()))) &&
            ((this.updatedAt==null && other.getUpdatedAt()==null) || 
             (this.updatedAt!=null &&
              this.updatedAt.equals(other.getUpdatedAt()))) &&
            ((this.url==null && other.getUrl()==null) || 
             (this.url!=null &&
              this.url.equals(other.getUrl())));
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
        if (getCaptions() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCaptions());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCaptions(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCategories() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCategories());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCategories(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCopyright() != null) {
            _hashCode += getCopyright().hashCode();
        }
        if (getCreatedAt() != null) {
            _hashCode += getCreatedAt().hashCode();
        }
        if (getFileName() != null) {
            _hashCode += getFileName().hashCode();
        }
        if (getImageId() != null) {
            _hashCode += getImageId().hashCode();
        }
        if (getLicense() != null) {
            _hashCode += getLicense().hashCode();
        }
        if (getUpdatedAt() != null) {
            _hashCode += getUpdatedAt().hashCode();
        }
        if (getUrl() != null) {
            _hashCode += getUrl().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ImageEntry.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://agrigrid.pangaea.org/ws/image/", "ImageEntry"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("captions");
        elemField.setXmlName(new javax.xml.namespace.QName("", "captions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://agrigrid.pangaea.org/ws/", "Caption"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("categories");
        elemField.setXmlName(new javax.xml.namespace.QName("", "categories"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://agrigrid.pangaea.org/ws/", "Category"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("copyright");
        elemField.setXmlName(new javax.xml.namespace.QName("", "copyright"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createdAt");
        elemField.setXmlName(new javax.xml.namespace.QName("", "createdAt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fileName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fileName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("imageId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "imageId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("license");
        elemField.setXmlName(new javax.xml.namespace.QName("", "license"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("updatedAt");
        elemField.setXmlName(new javax.xml.namespace.QName("", "updatedAt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("url");
        elemField.setXmlName(new javax.xml.namespace.QName("", "url"));
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
