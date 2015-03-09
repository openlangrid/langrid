/**
 * SelectionResult.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package localhost.jp_go_nict_langrid_webapps_atomic.services.TranslationSelection;

public class SelectionResult  implements java.io.Serializable {
    private localhost.jp_go_nict_langrid_webapps_atomic.services.TranslationSelection.EstimationResult[] estimationResults;

    private int selectedIndex;

    public SelectionResult() {
    }

    public SelectionResult(
           localhost.jp_go_nict_langrid_webapps_atomic.services.TranslationSelection.EstimationResult[] estimationResults,
           int selectedIndex) {
           this.estimationResults = estimationResults;
           this.selectedIndex = selectedIndex;
    }


    /**
     * Gets the estimationResults value for this SelectionResult.
     * 
     * @return estimationResults
     */
    public localhost.jp_go_nict_langrid_webapps_atomic.services.TranslationSelection.EstimationResult[] getEstimationResults() {
        return estimationResults;
    }


    /**
     * Sets the estimationResults value for this SelectionResult.
     * 
     * @param estimationResults
     */
    public void setEstimationResults(localhost.jp_go_nict_langrid_webapps_atomic.services.TranslationSelection.EstimationResult[] estimationResults) {
        this.estimationResults = estimationResults;
    }


    /**
     * Gets the selectedIndex value for this SelectionResult.
     * 
     * @return selectedIndex
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }


    /**
     * Sets the selectedIndex value for this SelectionResult.
     * 
     * @param selectedIndex
     */
    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SelectionResult)) return false;
        SelectionResult other = (SelectionResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.estimationResults==null && other.getEstimationResults()==null) || 
             (this.estimationResults!=null &&
              java.util.Arrays.equals(this.estimationResults, other.getEstimationResults()))) &&
            this.selectedIndex == other.getSelectedIndex();
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
        if (getEstimationResults() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEstimationResults());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEstimationResults(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getSelectedIndex();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SelectionResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://localhost:8080/jp.go.nict.langrid.webapps.atomic/services/TranslationSelection", "SelectionResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estimationResults");
        elemField.setXmlName(new javax.xml.namespace.QName("", "estimationResults"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://localhost:8080/jp.go.nict.langrid.webapps.atomic/services/TranslationSelection", "EstimationResult"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("selectedIndex");
        elemField.setXmlName(new javax.xml.namespace.QName("", "selectedIndex"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
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
