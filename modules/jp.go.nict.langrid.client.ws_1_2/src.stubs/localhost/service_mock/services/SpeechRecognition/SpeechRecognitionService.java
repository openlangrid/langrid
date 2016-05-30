/**
 * SpeechRecognitionService.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package localhost.service_mock.services.SpeechRecognition;

public interface SpeechRecognitionService extends javax.xml.rpc.Service {
    public java.lang.String getSpeechRecognitionAddress();

    public localhost.service_mock.services.SpeechRecognition.SpeechRecognition getSpeechRecognition() throws javax.xml.rpc.ServiceException;

    public localhost.service_mock.services.SpeechRecognition.SpeechRecognition getSpeechRecognition(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
