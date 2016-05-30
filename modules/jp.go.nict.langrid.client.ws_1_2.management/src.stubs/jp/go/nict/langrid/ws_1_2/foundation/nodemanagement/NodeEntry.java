/**
 * NodeEntry.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.foundation.nodemanagement;

public class NodeEntry  implements java.io.Serializable {
    private boolean active;

    private java.lang.String nodeId;

    private java.lang.String nodeName;

    private java.lang.String nodeType;

    private java.lang.String ownerUserId;

    private java.lang.String ownerUserOrganization;

    private java.util.Calendar registeredDate;

    private java.util.Calendar updatedDate;

    private java.lang.String url;

    public NodeEntry() {
    }

    public NodeEntry(
           boolean active,
           java.lang.String nodeId,
           java.lang.String nodeName,
           java.lang.String nodeType,
           java.lang.String ownerUserId,
           java.lang.String ownerUserOrganization,
           java.util.Calendar registeredDate,
           java.util.Calendar updatedDate,
           java.lang.String url) {
           this.active = active;
           this.nodeId = nodeId;
           this.nodeName = nodeName;
           this.nodeType = nodeType;
           this.ownerUserId = ownerUserId;
           this.ownerUserOrganization = ownerUserOrganization;
           this.registeredDate = registeredDate;
           this.updatedDate = updatedDate;
           this.url = url;
    }


    /**
     * Gets the active value for this NodeEntry.
     * 
     * @return active
     */
    public boolean isActive() {
        return active;
    }


    /**
     * Sets the active value for this NodeEntry.
     * 
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }


    /**
     * Gets the nodeId value for this NodeEntry.
     * 
     * @return nodeId
     */
    public java.lang.String getNodeId() {
        return nodeId;
    }


    /**
     * Sets the nodeId value for this NodeEntry.
     * 
     * @param nodeId
     */
    public void setNodeId(java.lang.String nodeId) {
        this.nodeId = nodeId;
    }


    /**
     * Gets the nodeName value for this NodeEntry.
     * 
     * @return nodeName
     */
    public java.lang.String getNodeName() {
        return nodeName;
    }


    /**
     * Sets the nodeName value for this NodeEntry.
     * 
     * @param nodeName
     */
    public void setNodeName(java.lang.String nodeName) {
        this.nodeName = nodeName;
    }


    /**
     * Gets the nodeType value for this NodeEntry.
     * 
     * @return nodeType
     */
    public java.lang.String getNodeType() {
        return nodeType;
    }


    /**
     * Sets the nodeType value for this NodeEntry.
     * 
     * @param nodeType
     */
    public void setNodeType(java.lang.String nodeType) {
        this.nodeType = nodeType;
    }


    /**
     * Gets the ownerUserId value for this NodeEntry.
     * 
     * @return ownerUserId
     */
    public java.lang.String getOwnerUserId() {
        return ownerUserId;
    }


    /**
     * Sets the ownerUserId value for this NodeEntry.
     * 
     * @param ownerUserId
     */
    public void setOwnerUserId(java.lang.String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }


    /**
     * Gets the ownerUserOrganization value for this NodeEntry.
     * 
     * @return ownerUserOrganization
     */
    public java.lang.String getOwnerUserOrganization() {
        return ownerUserOrganization;
    }


    /**
     * Sets the ownerUserOrganization value for this NodeEntry.
     * 
     * @param ownerUserOrganization
     */
    public void setOwnerUserOrganization(java.lang.String ownerUserOrganization) {
        this.ownerUserOrganization = ownerUserOrganization;
    }


    /**
     * Gets the registeredDate value for this NodeEntry.
     * 
     * @return registeredDate
     */
    public java.util.Calendar getRegisteredDate() {
        return registeredDate;
    }


    /**
     * Sets the registeredDate value for this NodeEntry.
     * 
     * @param registeredDate
     */
    public void setRegisteredDate(java.util.Calendar registeredDate) {
        this.registeredDate = registeredDate;
    }


    /**
     * Gets the updatedDate value for this NodeEntry.
     * 
     * @return updatedDate
     */
    public java.util.Calendar getUpdatedDate() {
        return updatedDate;
    }


    /**
     * Sets the updatedDate value for this NodeEntry.
     * 
     * @param updatedDate
     */
    public void setUpdatedDate(java.util.Calendar updatedDate) {
        this.updatedDate = updatedDate;
    }


    /**
     * Gets the url value for this NodeEntry.
     * 
     * @return url
     */
    public java.lang.String getUrl() {
        return url;
    }


    /**
     * Sets the url value for this NodeEntry.
     * 
     * @param url
     */
    public void setUrl(java.lang.String url) {
        this.url = url;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof NodeEntry)) return false;
        NodeEntry other = (NodeEntry) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.active == other.isActive() &&
            ((this.nodeId==null && other.getNodeId()==null) || 
             (this.nodeId!=null &&
              this.nodeId.equals(other.getNodeId()))) &&
            ((this.nodeName==null && other.getNodeName()==null) || 
             (this.nodeName!=null &&
              this.nodeName.equals(other.getNodeName()))) &&
            ((this.nodeType==null && other.getNodeType()==null) || 
             (this.nodeType!=null &&
              this.nodeType.equals(other.getNodeType()))) &&
            ((this.ownerUserId==null && other.getOwnerUserId()==null) || 
             (this.ownerUserId!=null &&
              this.ownerUserId.equals(other.getOwnerUserId()))) &&
            ((this.ownerUserOrganization==null && other.getOwnerUserOrganization()==null) || 
             (this.ownerUserOrganization!=null &&
              this.ownerUserOrganization.equals(other.getOwnerUserOrganization()))) &&
            ((this.registeredDate==null && other.getRegisteredDate()==null) || 
             (this.registeredDate!=null &&
              this.registeredDate.equals(other.getRegisteredDate()))) &&
            ((this.updatedDate==null && other.getUpdatedDate()==null) || 
             (this.updatedDate!=null &&
              this.updatedDate.equals(other.getUpdatedDate()))) &&
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
        _hashCode += (isActive() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getNodeId() != null) {
            _hashCode += getNodeId().hashCode();
        }
        if (getNodeName() != null) {
            _hashCode += getNodeName().hashCode();
        }
        if (getNodeType() != null) {
            _hashCode += getNodeType().hashCode();
        }
        if (getOwnerUserId() != null) {
            _hashCode += getOwnerUserId().hashCode();
        }
        if (getOwnerUserOrganization() != null) {
            _hashCode += getOwnerUserOrganization().hashCode();
        }
        if (getRegisteredDate() != null) {
            _hashCode += getRegisteredDate().hashCode();
        }
        if (getUpdatedDate() != null) {
            _hashCode += getUpdatedDate().hashCode();
        }
        if (getUrl() != null) {
            _hashCode += getUrl().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(NodeEntry.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/nodemanagement/", "NodeEntry"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("active");
        elemField.setXmlName(new javax.xml.namespace.QName("", "active"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nodeId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nodeId"));
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
        elemField.setFieldName("updatedDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "updatedDate"));
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
