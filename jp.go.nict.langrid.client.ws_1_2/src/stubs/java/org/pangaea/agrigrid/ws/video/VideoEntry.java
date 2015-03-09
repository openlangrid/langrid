/**
 * VideoEntry.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package org.pangaea.agrigrid.ws.video;

public class VideoEntry  implements java.io.Serializable {
    private org.pangaea.agrigrid.ws.Caption[] captions;

    private java.lang.String copyright;

    private java.util.Calendar createdAt;

    private java.lang.String fileName;

    private java.lang.String license;

    private java.lang.String[] subtitleLanguages;

    private java.lang.String[] tags;

    private java.util.Calendar updatedAt;

    private java.lang.String url;

    private java.lang.String videoId;

    public VideoEntry() {
    }

    public VideoEntry(
           org.pangaea.agrigrid.ws.Caption[] captions,
           java.lang.String copyright,
           java.util.Calendar createdAt,
           java.lang.String fileName,
           java.lang.String license,
           java.lang.String[] subtitleLanguages,
           java.lang.String[] tags,
           java.util.Calendar updatedAt,
           java.lang.String url,
           java.lang.String videoId) {
           this.captions = captions;
           this.copyright = copyright;
           this.createdAt = createdAt;
           this.fileName = fileName;
           this.license = license;
           this.subtitleLanguages = subtitleLanguages;
           this.tags = tags;
           this.updatedAt = updatedAt;
           this.url = url;
           this.videoId = videoId;
    }


    /**
     * Gets the captions value for this VideoEntry.
     * 
     * @return captions
     */
    public org.pangaea.agrigrid.ws.Caption[] getCaptions() {
        return captions;
    }


    /**
     * Sets the captions value for this VideoEntry.
     * 
     * @param captions
     */
    public void setCaptions(org.pangaea.agrigrid.ws.Caption[] captions) {
        this.captions = captions;
    }


    /**
     * Gets the copyright value for this VideoEntry.
     * 
     * @return copyright
     */
    public java.lang.String getCopyright() {
        return copyright;
    }


    /**
     * Sets the copyright value for this VideoEntry.
     * 
     * @param copyright
     */
    public void setCopyright(java.lang.String copyright) {
        this.copyright = copyright;
    }


    /**
     * Gets the createdAt value for this VideoEntry.
     * 
     * @return createdAt
     */
    public java.util.Calendar getCreatedAt() {
        return createdAt;
    }


    /**
     * Sets the createdAt value for this VideoEntry.
     * 
     * @param createdAt
     */
    public void setCreatedAt(java.util.Calendar createdAt) {
        this.createdAt = createdAt;
    }


    /**
     * Gets the fileName value for this VideoEntry.
     * 
     * @return fileName
     */
    public java.lang.String getFileName() {
        return fileName;
    }


    /**
     * Sets the fileName value for this VideoEntry.
     * 
     * @param fileName
     */
    public void setFileName(java.lang.String fileName) {
        this.fileName = fileName;
    }


    /**
     * Gets the license value for this VideoEntry.
     * 
     * @return license
     */
    public java.lang.String getLicense() {
        return license;
    }


    /**
     * Sets the license value for this VideoEntry.
     * 
     * @param license
     */
    public void setLicense(java.lang.String license) {
        this.license = license;
    }


    /**
     * Gets the subtitleLanguages value for this VideoEntry.
     * 
     * @return subtitleLanguages
     */
    public java.lang.String[] getSubtitleLanguages() {
        return subtitleLanguages;
    }


    /**
     * Sets the subtitleLanguages value for this VideoEntry.
     * 
     * @param subtitleLanguages
     */
    public void setSubtitleLanguages(java.lang.String[] subtitleLanguages) {
        this.subtitleLanguages = subtitleLanguages;
    }


    /**
     * Gets the tags value for this VideoEntry.
     * 
     * @return tags
     */
    public java.lang.String[] getTags() {
        return tags;
    }


    /**
     * Sets the tags value for this VideoEntry.
     * 
     * @param tags
     */
    public void setTags(java.lang.String[] tags) {
        this.tags = tags;
    }


    /**
     * Gets the updatedAt value for this VideoEntry.
     * 
     * @return updatedAt
     */
    public java.util.Calendar getUpdatedAt() {
        return updatedAt;
    }


    /**
     * Sets the updatedAt value for this VideoEntry.
     * 
     * @param updatedAt
     */
    public void setUpdatedAt(java.util.Calendar updatedAt) {
        this.updatedAt = updatedAt;
    }


    /**
     * Gets the url value for this VideoEntry.
     * 
     * @return url
     */
    public java.lang.String getUrl() {
        return url;
    }


    /**
     * Sets the url value for this VideoEntry.
     * 
     * @param url
     */
    public void setUrl(java.lang.String url) {
        this.url = url;
    }


    /**
     * Gets the videoId value for this VideoEntry.
     * 
     * @return videoId
     */
    public java.lang.String getVideoId() {
        return videoId;
    }


    /**
     * Sets the videoId value for this VideoEntry.
     * 
     * @param videoId
     */
    public void setVideoId(java.lang.String videoId) {
        this.videoId = videoId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VideoEntry)) return false;
        VideoEntry other = (VideoEntry) obj;
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
            ((this.copyright==null && other.getCopyright()==null) || 
             (this.copyright!=null &&
              this.copyright.equals(other.getCopyright()))) &&
            ((this.createdAt==null && other.getCreatedAt()==null) || 
             (this.createdAt!=null &&
              this.createdAt.equals(other.getCreatedAt()))) &&
            ((this.fileName==null && other.getFileName()==null) || 
             (this.fileName!=null &&
              this.fileName.equals(other.getFileName()))) &&
            ((this.license==null && other.getLicense()==null) || 
             (this.license!=null &&
              this.license.equals(other.getLicense()))) &&
            ((this.subtitleLanguages==null && other.getSubtitleLanguages()==null) || 
             (this.subtitleLanguages!=null &&
              java.util.Arrays.equals(this.subtitleLanguages, other.getSubtitleLanguages()))) &&
            ((this.tags==null && other.getTags()==null) || 
             (this.tags!=null &&
              java.util.Arrays.equals(this.tags, other.getTags()))) &&
            ((this.updatedAt==null && other.getUpdatedAt()==null) || 
             (this.updatedAt!=null &&
              this.updatedAt.equals(other.getUpdatedAt()))) &&
            ((this.url==null && other.getUrl()==null) || 
             (this.url!=null &&
              this.url.equals(other.getUrl()))) &&
            ((this.videoId==null && other.getVideoId()==null) || 
             (this.videoId!=null &&
              this.videoId.equals(other.getVideoId())));
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
        if (getCopyright() != null) {
            _hashCode += getCopyright().hashCode();
        }
        if (getCreatedAt() != null) {
            _hashCode += getCreatedAt().hashCode();
        }
        if (getFileName() != null) {
            _hashCode += getFileName().hashCode();
        }
        if (getLicense() != null) {
            _hashCode += getLicense().hashCode();
        }
        if (getSubtitleLanguages() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSubtitleLanguages());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSubtitleLanguages(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTags() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTags());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTags(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getUpdatedAt() != null) {
            _hashCode += getUpdatedAt().hashCode();
        }
        if (getUrl() != null) {
            _hashCode += getUrl().hashCode();
        }
        if (getVideoId() != null) {
            _hashCode += getVideoId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VideoEntry.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://agrigrid.pangaea.org/ws/video/", "VideoEntry"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("captions");
        elemField.setXmlName(new javax.xml.namespace.QName("", "captions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://agrigrid.pangaea.org/ws/", "Caption"));
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
        elemField.setFieldName("license");
        elemField.setXmlName(new javax.xml.namespace.QName("", "license"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subtitleLanguages");
        elemField.setXmlName(new javax.xml.namespace.QName("", "subtitleLanguages"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tags");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tags"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("videoId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "videoId"));
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
