/**
 * TrainingCorpusInfo.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.examplebasedtranslation;

public class TrainingCorpusInfo  implements java.io.Serializable {
    private java.lang.String corpusId;

    private int count;

    private java.util.Calendar lastUpdate;

    private float progress;

    private java.lang.String sourceLang;

    private java.lang.String status;

    private java.lang.String targetLang;

    public TrainingCorpusInfo() {
    }

    public TrainingCorpusInfo(
           java.lang.String corpusId,
           int count,
           java.util.Calendar lastUpdate,
           float progress,
           java.lang.String sourceLang,
           java.lang.String status,
           java.lang.String targetLang) {
           this.corpusId = corpusId;
           this.count = count;
           this.lastUpdate = lastUpdate;
           this.progress = progress;
           this.sourceLang = sourceLang;
           this.status = status;
           this.targetLang = targetLang;
    }


    /**
     * Gets the corpusId value for this TrainingCorpusInfo.
     * 
     * @return corpusId
     */
    public java.lang.String getCorpusId() {
        return corpusId;
    }


    /**
     * Sets the corpusId value for this TrainingCorpusInfo.
     * 
     * @param corpusId
     */
    public void setCorpusId(java.lang.String corpusId) {
        this.corpusId = corpusId;
    }


    /**
     * Gets the count value for this TrainingCorpusInfo.
     * 
     * @return count
     */
    public int getCount() {
        return count;
    }


    /**
     * Sets the count value for this TrainingCorpusInfo.
     * 
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }


    /**
     * Gets the lastUpdate value for this TrainingCorpusInfo.
     * 
     * @return lastUpdate
     */
    public java.util.Calendar getLastUpdate() {
        return lastUpdate;
    }


    /**
     * Sets the lastUpdate value for this TrainingCorpusInfo.
     * 
     * @param lastUpdate
     */
    public void setLastUpdate(java.util.Calendar lastUpdate) {
        this.lastUpdate = lastUpdate;
    }


    /**
     * Gets the progress value for this TrainingCorpusInfo.
     * 
     * @return progress
     */
    public float getProgress() {
        return progress;
    }


    /**
     * Sets the progress value for this TrainingCorpusInfo.
     * 
     * @param progress
     */
    public void setProgress(float progress) {
        this.progress = progress;
    }


    /**
     * Gets the sourceLang value for this TrainingCorpusInfo.
     * 
     * @return sourceLang
     */
    public java.lang.String getSourceLang() {
        return sourceLang;
    }


    /**
     * Sets the sourceLang value for this TrainingCorpusInfo.
     * 
     * @param sourceLang
     */
    public void setSourceLang(java.lang.String sourceLang) {
        this.sourceLang = sourceLang;
    }


    /**
     * Gets the status value for this TrainingCorpusInfo.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this TrainingCorpusInfo.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the targetLang value for this TrainingCorpusInfo.
     * 
     * @return targetLang
     */
    public java.lang.String getTargetLang() {
        return targetLang;
    }


    /**
     * Sets the targetLang value for this TrainingCorpusInfo.
     * 
     * @param targetLang
     */
    public void setTargetLang(java.lang.String targetLang) {
        this.targetLang = targetLang;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TrainingCorpusInfo)) return false;
        TrainingCorpusInfo other = (TrainingCorpusInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.corpusId==null && other.getCorpusId()==null) || 
             (this.corpusId!=null &&
              this.corpusId.equals(other.getCorpusId()))) &&
            this.count == other.getCount() &&
            ((this.lastUpdate==null && other.getLastUpdate()==null) || 
             (this.lastUpdate!=null &&
              this.lastUpdate.equals(other.getLastUpdate()))) &&
            this.progress == other.getProgress() &&
            ((this.sourceLang==null && other.getSourceLang()==null) || 
             (this.sourceLang!=null &&
              this.sourceLang.equals(other.getSourceLang()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.targetLang==null && other.getTargetLang()==null) || 
             (this.targetLang!=null &&
              this.targetLang.equals(other.getTargetLang())));
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
        if (getCorpusId() != null) {
            _hashCode += getCorpusId().hashCode();
        }
        _hashCode += getCount();
        if (getLastUpdate() != null) {
            _hashCode += getLastUpdate().hashCode();
        }
        _hashCode += new Float(getProgress()).hashCode();
        if (getSourceLang() != null) {
            _hashCode += getSourceLang().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getTargetLang() != null) {
            _hashCode += getTargetLang().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TrainingCorpusInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/examplebasedtranslation", "TrainingCorpusInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("corpusId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "corpusId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("count");
        elemField.setXmlName(new javax.xml.namespace.QName("", "count"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastUpdate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lastUpdate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("progress");
        elemField.setXmlName(new javax.xml.namespace.QName("", "progress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceLang");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sourceLang"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("targetLang");
        elemField.setXmlName(new javax.xml.namespace.QName("", "targetLang"));
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
