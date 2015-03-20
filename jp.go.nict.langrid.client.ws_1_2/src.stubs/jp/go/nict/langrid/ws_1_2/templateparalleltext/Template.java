/**
 * Template.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.templateparalleltext;

public class Template  implements java.io.Serializable {
    private jp.go.nict.langrid.ws_1_2.Category[] categories;

    private jp.go.nict.langrid.ws_1_2.templateparalleltext.ChoiceParameter[] choiceParameters;

    private java.lang.String template;

    private java.lang.String templateId;

    private jp.go.nict.langrid.ws_1_2.templateparalleltext.ValueParameter[] valueParameters;

    public Template() {
    }

    public Template(
           jp.go.nict.langrid.ws_1_2.Category[] categories,
           jp.go.nict.langrid.ws_1_2.templateparalleltext.ChoiceParameter[] choiceParameters,
           java.lang.String template,
           java.lang.String templateId,
           jp.go.nict.langrid.ws_1_2.templateparalleltext.ValueParameter[] valueParameters) {
           this.categories = categories;
           this.choiceParameters = choiceParameters;
           this.template = template;
           this.templateId = templateId;
           this.valueParameters = valueParameters;
    }


    /**
     * Gets the categories value for this Template.
     * 
     * @return categories
     */
    public jp.go.nict.langrid.ws_1_2.Category[] getCategories() {
        return categories;
    }


    /**
     * Sets the categories value for this Template.
     * 
     * @param categories
     */
    public void setCategories(jp.go.nict.langrid.ws_1_2.Category[] categories) {
        this.categories = categories;
    }


    /**
     * Gets the choiceParameters value for this Template.
     * 
     * @return choiceParameters
     */
    public jp.go.nict.langrid.ws_1_2.templateparalleltext.ChoiceParameter[] getChoiceParameters() {
        return choiceParameters;
    }


    /**
     * Sets the choiceParameters value for this Template.
     * 
     * @param choiceParameters
     */
    public void setChoiceParameters(jp.go.nict.langrid.ws_1_2.templateparalleltext.ChoiceParameter[] choiceParameters) {
        this.choiceParameters = choiceParameters;
    }


    /**
     * Gets the template value for this Template.
     * 
     * @return template
     */
    public java.lang.String getTemplate() {
        return template;
    }


    /**
     * Sets the template value for this Template.
     * 
     * @param template
     */
    public void setTemplate(java.lang.String template) {
        this.template = template;
    }


    /**
     * Gets the templateId value for this Template.
     * 
     * @return templateId
     */
    public java.lang.String getTemplateId() {
        return templateId;
    }


    /**
     * Sets the templateId value for this Template.
     * 
     * @param templateId
     */
    public void setTemplateId(java.lang.String templateId) {
        this.templateId = templateId;
    }


    /**
     * Gets the valueParameters value for this Template.
     * 
     * @return valueParameters
     */
    public jp.go.nict.langrid.ws_1_2.templateparalleltext.ValueParameter[] getValueParameters() {
        return valueParameters;
    }


    /**
     * Sets the valueParameters value for this Template.
     * 
     * @param valueParameters
     */
    public void setValueParameters(jp.go.nict.langrid.ws_1_2.templateparalleltext.ValueParameter[] valueParameters) {
        this.valueParameters = valueParameters;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Template)) return false;
        Template other = (Template) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.categories==null && other.getCategories()==null) || 
             (this.categories!=null &&
              java.util.Arrays.equals(this.categories, other.getCategories()))) &&
            ((this.choiceParameters==null && other.getChoiceParameters()==null) || 
             (this.choiceParameters!=null &&
              java.util.Arrays.equals(this.choiceParameters, other.getChoiceParameters()))) &&
            ((this.template==null && other.getTemplate()==null) || 
             (this.template!=null &&
              this.template.equals(other.getTemplate()))) &&
            ((this.templateId==null && other.getTemplateId()==null) || 
             (this.templateId!=null &&
              this.templateId.equals(other.getTemplateId()))) &&
            ((this.valueParameters==null && other.getValueParameters()==null) || 
             (this.valueParameters!=null &&
              java.util.Arrays.equals(this.valueParameters, other.getValueParameters())));
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
        if (getChoiceParameters() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getChoiceParameters());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getChoiceParameters(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTemplate() != null) {
            _hashCode += getTemplate().hashCode();
        }
        if (getTemplateId() != null) {
            _hashCode += getTemplateId().hashCode();
        }
        if (getValueParameters() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getValueParameters());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getValueParameters(), i);
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
        new org.apache.axis.description.TypeDesc(Template.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/templateparalleltext/", "Template"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("categories");
        elemField.setXmlName(new javax.xml.namespace.QName("", "categories"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/", "Category"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("choiceParameters");
        elemField.setXmlName(new javax.xml.namespace.QName("", "choiceParameters"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/templateparalleltext/", "ChoiceParameter"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("template");
        elemField.setXmlName(new javax.xml.namespace.QName("", "template"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("templateId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "templateId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("valueParameters");
        elemField.setXmlName(new javax.xml.namespace.QName("", "valueParameters"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/templateparalleltext/", "ValueParameter"));
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
