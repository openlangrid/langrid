/**
 * NodeProfile.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.foundation.nodemanagement;

public class NodeProfile  implements java.io.Serializable {
    private java.lang.String cpu;

    private java.lang.String memory;

    private java.lang.String nodeName;

    private java.lang.String nodeType;

    private java.lang.String os;

    private java.lang.String specialNotes;

    private java.lang.String url;

    public NodeProfile() {
    }

    public NodeProfile(
           java.lang.String cpu,
           java.lang.String memory,
           java.lang.String nodeName,
           java.lang.String nodeType,
           java.lang.String os,
           java.lang.String specialNotes,
           java.lang.String url) {
           this.cpu = cpu;
           this.memory = memory;
           this.nodeName = nodeName;
           this.nodeType = nodeType;
           this.os = os;
           this.specialNotes = specialNotes;
           this.url = url;
    }


    /**
     * Gets the cpu value for this NodeProfile.
     * 
     * @return cpu
     */
    public java.lang.String getCpu() {
        return cpu;
    }


    /**
     * Sets the cpu value for this NodeProfile.
     * 
     * @param cpu
     */
    public void setCpu(java.lang.String cpu) {
        this.cpu = cpu;
    }


    /**
     * Gets the memory value for this NodeProfile.
     * 
     * @return memory
     */
    public java.lang.String getMemory() {
        return memory;
    }


    /**
     * Sets the memory value for this NodeProfile.
     * 
     * @param memory
     */
    public void setMemory(java.lang.String memory) {
        this.memory = memory;
    }


    /**
     * Gets the nodeName value for this NodeProfile.
     * 
     * @return nodeName
     */
    public java.lang.String getNodeName() {
        return nodeName;
    }


    /**
     * Sets the nodeName value for this NodeProfile.
     * 
     * @param nodeName
     */
    public void setNodeName(java.lang.String nodeName) {
        this.nodeName = nodeName;
    }


    /**
     * Gets the nodeType value for this NodeProfile.
     * 
     * @return nodeType
     */
    public java.lang.String getNodeType() {
        return nodeType;
    }


    /**
     * Sets the nodeType value for this NodeProfile.
     * 
     * @param nodeType
     */
    public void setNodeType(java.lang.String nodeType) {
        this.nodeType = nodeType;
    }


    /**
     * Gets the os value for this NodeProfile.
     * 
     * @return os
     */
    public java.lang.String getOs() {
        return os;
    }


    /**
     * Sets the os value for this NodeProfile.
     * 
     * @param os
     */
    public void setOs(java.lang.String os) {
        this.os = os;
    }


    /**
     * Gets the specialNotes value for this NodeProfile.
     * 
     * @return specialNotes
     */
    public java.lang.String getSpecialNotes() {
        return specialNotes;
    }


    /**
     * Sets the specialNotes value for this NodeProfile.
     * 
     * @param specialNotes
     */
    public void setSpecialNotes(java.lang.String specialNotes) {
        this.specialNotes = specialNotes;
    }


    /**
     * Gets the url value for this NodeProfile.
     * 
     * @return url
     */
    public java.lang.String getUrl() {
        return url;
    }


    /**
     * Sets the url value for this NodeProfile.
     * 
     * @param url
     */
    public void setUrl(java.lang.String url) {
        this.url = url;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof NodeProfile)) return false;
        NodeProfile other = (NodeProfile) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cpu==null && other.getCpu()==null) || 
             (this.cpu!=null &&
              this.cpu.equals(other.getCpu()))) &&
            ((this.memory==null && other.getMemory()==null) || 
             (this.memory!=null &&
              this.memory.equals(other.getMemory()))) &&
            ((this.nodeName==null && other.getNodeName()==null) || 
             (this.nodeName!=null &&
              this.nodeName.equals(other.getNodeName()))) &&
            ((this.nodeType==null && other.getNodeType()==null) || 
             (this.nodeType!=null &&
              this.nodeType.equals(other.getNodeType()))) &&
            ((this.os==null && other.getOs()==null) || 
             (this.os!=null &&
              this.os.equals(other.getOs()))) &&
            ((this.specialNotes==null && other.getSpecialNotes()==null) || 
             (this.specialNotes!=null &&
              this.specialNotes.equals(other.getSpecialNotes()))) &&
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
        if (getCpu() != null) {
            _hashCode += getCpu().hashCode();
        }
        if (getMemory() != null) {
            _hashCode += getMemory().hashCode();
        }
        if (getNodeName() != null) {
            _hashCode += getNodeName().hashCode();
        }
        if (getNodeType() != null) {
            _hashCode += getNodeType().hashCode();
        }
        if (getOs() != null) {
            _hashCode += getOs().hashCode();
        }
        if (getSpecialNotes() != null) {
            _hashCode += getSpecialNotes().hashCode();
        }
        if (getUrl() != null) {
            _hashCode += getUrl().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(NodeProfile.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeProfile"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cpu");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cpu"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("memory");
        elemField.setXmlName(new javax.xml.namespace.QName("", "memory"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nodeName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nodeName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nodeType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nodeType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("os");
        elemField.setXmlName(new javax.xml.namespace.QName("", "os"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("specialNotes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "specialNotes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
