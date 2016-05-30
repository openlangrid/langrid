/**
 * MorphemesDependencyParserService.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package localhost.jp_go_nict_langrid_webapps_mock.services.MorphemesDependencyParser;

public interface MorphemesDependencyParserService extends java.rmi.Remote {
    public jp.go.nict.langrid.ws_1_2.dependencyparser.Chunk[] parseDependency(java.lang.String language, jp.go.nict.langrid.ws_1_2.morphologicalanalysis.Morpheme[] morphs) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.LanguageNotUniquelyDecidedException, jp.go.nict.langrid.ws_1_2.ServiceNotFoundException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.NoValidEndpointsException, jp.go.nict.langrid.ws_1_2.ServiceNotActiveException, jp.go.nict.langrid.ws_1_2.UnsupportedLanguageException, jp.go.nict.langrid.ws_1_2.AccessLimitExceededException, jp.go.nict.langrid.ws_1_2.ServerBusyException, jp.go.nict.langrid.ws_1_2.NoAccessPermissionException, jp.go.nict.langrid.ws_1_2.ProcessFailedException;
}
