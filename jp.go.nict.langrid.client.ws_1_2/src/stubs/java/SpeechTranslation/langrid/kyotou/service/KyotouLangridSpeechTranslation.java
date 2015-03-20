/**
 * KyotouLangridSpeechTranslation.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package SpeechTranslation.langrid.kyotou.service;

public interface KyotouLangridSpeechTranslation extends javax.xml.rpc.Service {
    public java.lang.String getSpeechTranslationAddress();

    public SpeechTranslation.langrid.kyotou.service.AxisSpeechTranslationHandler getSpeechTranslation() throws javax.xml.rpc.ServiceException;

    public SpeechTranslation.langrid.kyotou.service.AxisSpeechTranslationHandler getSpeechTranslation(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
