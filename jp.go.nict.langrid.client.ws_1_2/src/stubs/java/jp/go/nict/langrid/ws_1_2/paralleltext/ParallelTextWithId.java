/**
 * ParallelTextWithId.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.paralleltext;

public class ParallelTextWithId  extends jp.go.nict.langrid.ws_1_2.paralleltext.ParallelText  implements java.io.Serializable {
    private jp.go.nict.langrid.ws_1_2.Category[] categories;

    private java.lang.String paralleltTextId;

    public ParallelTextWithId() {
    }

    public ParallelTextWithId(
           java.lang.String source,
           java.lang.String target,
           jp.go.nict.langrid.ws_1_2.Category[] categories,
           java.lang.String paralleltTextId) {
        super(
            source,
            target);
        this.categories = categories;
        this.paralleltTextId = paralleltTextId;
    }


    /**
     * Gets the categories value for this ParallelTextWithId.
     * 
     * @return categories
     */
    public jp.go.nict.langrid.ws_1_2.Category[] getCategories() {
        return categories;
    }


    /**
     * Sets the categories value for this ParallelTextWithId.
     * 
     * @param categories
     */
    public void setCategories(jp.go.nict.langrid.ws_1_2.Category[] categories) {
        this.categories = categories;
    }


    /**
     * Gets the paralleltTextId value for this ParallelTextWithId.
     * 
     * @return paralleltTextId
     */
    public java.lang.String getParalleltTextId() {
        return paralleltTextId;
    }


    /**
     * Sets the paralleltTextId value for this ParallelTextWithId.
     * 
     * @param paralleltTextId
     */
    public void setParalleltTextId(java.lang.String paralleltTextId) {
        this.paralleltTextId = paralleltTextId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ParallelTextWithId)) return false;
        ParallelTextWithId other = (ParallelTextWithId) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.categories==null && other.getCategories()==null) || 
             (this.categories!=null &&
              java.util.Arrays.equals(this.categories, other.getCategories()))) &&
            ((this.paralleltTextId==null && other.getParalleltTextId()==null) || 
             (this.paralleltTextId!=null &&
              this.paralleltTextId.equals(other.getParalleltTextId())));
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
        if (getParalleltTextId() != null) {
            _hashCode += getParalleltTextId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ParallelTextWithId.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/paralleltext/", "ParallelTextWithId"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("categories");
        elemField.setXmlName(new javax.xml.namespace.QName("", "categories"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "Category"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paralleltTextId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "paralleltTextId"));
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
