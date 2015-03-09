/**
 * AgriCultureImageService.java
 *
 * このファイルはWSDLから自動生成されました / [en]-(This file was auto-generated from WSDL)
 * Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java生成器によって / [en]-(by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.)
 */

package net.ymcviet.ymc_service.services.AgricultureImageService;

public interface AgriCultureImageService extends java.rmi.Remote {
    public java.lang.String[] getSupportedLanguages() throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.ProcessFailedException;
    public byte[] getThumbnail(java.lang.String imageId) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.ProcessFailedException;
    public org.pangaea.agrigrid.ws.Category[] listAllCategories(java.lang.String language) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.ProcessFailedException;
    public org.pangaea.agrigrid.ws.image.ImageEntry[] searchImages(java.lang.String text, java.lang.String textLanguage, java.lang.String matchingMethod, java.lang.String[] categoryIds, org.pangaea.agrigrid.ws.Order[] orders) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.ProcessFailedException;
    public java.lang.String[] getCategoryNames(java.lang.String categoryId, java.lang.String[] languages) throws java.rmi.RemoteException, jp.go.nict.langrid.ws_1_2.InvalidParameterException, jp.go.nict.langrid.ws_1_2.ProcessFailedException;
}
