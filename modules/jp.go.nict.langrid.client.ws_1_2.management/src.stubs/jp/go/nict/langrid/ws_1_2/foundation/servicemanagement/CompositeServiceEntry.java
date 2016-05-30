/**
 * CompositeServiceEntry.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.foundation.servicemanagement;

public class CompositeServiceEntry  extends jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceEntry  implements java.io.Serializable {
    private jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceEntry[] servicesInUse;

    public CompositeServiceEntry() {
    }

    public CompositeServiceEntry(
           boolean active,
           java.lang.String endpointUrl,
           java.lang.String instanceType,
           java.lang.String ownerUserId,
           java.util.Calendar registeredDate,
           java.lang.String serviceDescription,
           java.lang.String serviceId,
           java.lang.String serviceName,
           java.lang.String serviceType,
           jp.go.nict.langrid.ws_1_2.LanguagePath[] supportedLanguages,
           java.util.Calendar updatedDate,
           jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceEntry[] servicesInUse) {
        super(
            active,
            endpointUrl,
            instanceType,
            ownerUserId,
            registeredDate,
            serviceDescription,
            serviceId,
            serviceName,
            serviceType,
            supportedLanguages,
            updatedDate);
        this.servicesInUse = servicesInUse;
    }


    /**
     * Gets the servicesInUse value for this CompositeServiceEntry.
     * 
     * @return servicesInUse
     */
    public jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceEntry[] getServicesInUse() {
        return servicesInUse;
    }


    /**
     * Sets the servicesInUse value for this CompositeServiceEntry.
     * 
     * @param servicesInUse
     */
    public void setServicesInUse(jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceEntry[] servicesInUse) {
        this.servicesInUse = servicesInUse;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CompositeServiceEntry)) return false;
        CompositeServiceEntry other = (CompositeServiceEntry) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.servicesInUse==null && other.getServicesInUse()==null) || 
             (this.servicesInUse!=null &&
              java.util.Arrays.equals(this.servicesInUse, other.getServicesInUse())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getServicesInUse() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getServicesInUse());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getServicesInUse(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CompositeServiceEntry.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "CompositeServiceEntry"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("servicesInUse");
        elemField.setXmlName(new javax.xml.namespace.QName("", "servicesInUse"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/foundation/servicemanagement/", "ServiceEntry"));
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
