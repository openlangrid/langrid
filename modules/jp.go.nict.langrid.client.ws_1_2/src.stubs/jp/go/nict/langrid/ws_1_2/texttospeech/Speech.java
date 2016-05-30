/**
 * Speech.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package jp.go.nict.langrid.ws_1_2.texttospeech;

public class Speech  implements java.io.Serializable {
    private byte[] audio;

    private java.lang.String audioType;

    private java.lang.String voiceType;

    public Speech() {
    }

    public Speech(
           byte[] audio,
           java.lang.String audioType,
           java.lang.String voiceType) {
           this.audio = audio;
           this.audioType = audioType;
           this.voiceType = voiceType;
    }


    /**
     * Gets the audio value for this Speech.
     * 
     * @return audio
     */
    public byte[] getAudio() {
        return audio;
    }


    /**
     * Sets the audio value for this Speech.
     * 
     * @param audio
     */
    public void setAudio(byte[] audio) {
        this.audio = audio;
    }


    /**
     * Gets the audioType value for this Speech.
     * 
     * @return audioType
     */
    public java.lang.String getAudioType() {
        return audioType;
    }


    /**
     * Sets the audioType value for this Speech.
     * 
     * @param audioType
     */
    public void setAudioType(java.lang.String audioType) {
        this.audioType = audioType;
    }


    /**
     * Gets the voiceType value for this Speech.
     * 
     * @return voiceType
     */
    public java.lang.String getVoiceType() {
        return voiceType;
    }


    /**
     * Sets the voiceType value for this Speech.
     * 
     * @param voiceType
     */
    public void setVoiceType(java.lang.String voiceType) {
        this.voiceType = voiceType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Speech)) return false;
        Speech other = (Speech) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.audio==null && other.getAudio()==null) || 
             (this.audio!=null &&
              java.util.Arrays.equals(this.audio, other.getAudio()))) &&
            ((this.audioType==null && other.getAudioType()==null) || 
             (this.audioType!=null &&
              this.audioType.equals(other.getAudioType()))) &&
            ((this.voiceType==null && other.getVoiceType()==null) || 
             (this.voiceType!=null &&
              this.voiceType.equals(other.getVoiceType())));
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
        if (getAudio() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAudio());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAudio(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getAudioType() != null) {
            _hashCode += getAudioType().hashCode();
        }
        if (getVoiceType() != null) {
            _hashCode += getVoiceType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // メタデータ型 / [en]-(Type metadata)
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Speech.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langrid.nict.go.jp/ws_1_2/texttospeech/", "Speech"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("audio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "audio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("audioType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "audioType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("voiceType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "voiceType"));
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
