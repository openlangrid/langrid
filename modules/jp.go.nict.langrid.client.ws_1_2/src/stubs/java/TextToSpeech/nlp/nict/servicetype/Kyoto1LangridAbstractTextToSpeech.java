/**
 * Kyoto1LangridAbstractTextToSpeech.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package TextToSpeech.nlp.nict.servicetype;

public interface Kyoto1LangridAbstractTextToSpeech extends javax.xml.rpc.Service {
    public java.lang.String getTextToSpeechAddress();

    public TextToSpeech.nlp.nict.servicetype.TextToSpeechService getTextToSpeech() throws javax.xml.rpc.ServiceException;

    public TextToSpeech.nlp.nict.servicetype.TextToSpeechService getTextToSpeech(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
