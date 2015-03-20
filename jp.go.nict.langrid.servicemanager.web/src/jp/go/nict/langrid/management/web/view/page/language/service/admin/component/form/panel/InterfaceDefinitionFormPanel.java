package jp.go.nict.langrid.management.web.view.page.language.service.admin.component.form.panel;

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.management.web.model.InterfaceDefinitionModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.component.container.SwitchableContainer;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.component.form.validator.InterfaceFileUploadValidator;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.component.form.validator.SameInterfaceProtocolValidator;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.hibernate.lob.BlobImpl;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class InterfaceDefinitionFormPanel extends Panel {
	/**
	 * 
	 * 
	 */
   public InterfaceDefinitionFormPanel(String gridId, String componentId)
   throws ServiceManagerException
   {
      super(componentId);
      this.gridId = gridId;
      protocolLength = ServiceFactory.getInstance().getProtocolService(gridId).getAllList().size();
      add(rewrite = new WebMarkupContainer("rewrite"));
      rewrite.setOutputMarkupId(true);
      rewrite.add(repeater = new RepeatingView("repeat"));
      rewrite.add(addLink = new AjaxLink<String>("add", new Model<String>()) {
         @Override
         public void onClick(AjaxRequestTarget target) {
            try {
               if(protocolLength <= repeater.size()){
                  return;
               }
               addRepeatingComponent("", new ArrayList<String>(), false);
               target.addComponent(rewrite);
            } catch(ServiceManagerException e) {
               e.printStackTrace();
            }
         }
      });
      addRepeatingComponent("", new ArrayList<String>(), false);
   }

   /**
    * 
    * 
    */
   public List<InterfaceDefinitionModel> getValue(String typeName) throws ServiceManagerException {
         List<InterfaceDefinitionModel> valueList = new ArrayList<InterfaceDefinitionModel>();
         for(SwitchableContainer<ServiceInterfaceFormComponent, ServiceInterfacePanel> wmc : componentList) {
            if(wmc.getFirstComponent().isVisible()){
               InterfaceDefinitionModel idm = new InterfaceDefinitionModel();
               idm.setProtocolId(wmc.getFirstComponent().getProtocolComponent().getModelObject().getProtocolId());
               byte[] file = wmc.getFirstComponent().getFileComponent().getJarFileInstance();
               idm.setDefinition(new BlobImpl(file));
               valueList.add(idm);
            }
         }
         return valueList;
   }
   
   /**
    * 
    * 
    */
   public void setValue(String domainId, String typeId, List<InterfaceDefinitionModel> list)
   throws ServiceManagerException
   {
      repeater.removeAll();
      componentList = new ArrayList<SwitchableContainer<ServiceInterfaceFormComponent,ServiceInterfacePanel>>();
      for(InterfaceDefinitionModel model : list){
         List<String> nameList = ServiceFactory.getInstance().getServiceTypeService(gridId
            ).getDefinitionFileNames(domainId, typeId, model.getProtocolId());
         addRepeatingComponent(model.getProtocolId(), nameList, true);
      }
      addLink.setVisible(false);
   }
   
   public int getProcotolCount(){
      return protocolLength;
   }
   
   /**
    * 
    * 
    */
   public List<String> getProtocolList(){
      List<String> valueList = new ArrayList<String>();
    	  for(SwitchableContainer<ServiceInterfaceFormComponent, ServiceInterfacePanel> sc : componentList) {
      valueList.add(sc.getFirstComponent().getProtocolComponent().getConvertedInput().getProtocolId());
      }
      return valueList;
   }
   
   /**
    * 
    * 
    */
   public List<FileMultipleUploadPanel> getFileUploadComponentList(){
      List<FileMultipleUploadPanel> list = new ArrayList<FileMultipleUploadPanel>();
      for(SwitchableContainer<ServiceInterfaceFormComponent, ServiceInterfacePanel> sc : componentList){
    	  list.add(sc.getFirstComponent().getFileComponent());
      }
      return list;
   }
   
   /**
    * 
    * 
    */
   public List<IFormValidator> getValidator(boolean isRegist) {
      List<IFormValidator> list = new ArrayList<IFormValidator>();
      if (isRegist) {
         list.add(new SameInterfaceProtocolValidator(this));
         list.add(new InterfaceFileUploadValidator(this));
      }
      return list;
   }
   
   private void addRepeatingComponent(String protocolId, List<String> fileList, boolean isSwitch)
   throws ServiceManagerException
   {
      ServiceInterfaceFormComponent sic = new ServiceInterfaceFormComponent("input", gridId);
      ServiceInterfacePanel sip = new ServiceInterfacePanel("define", protocolId, fileList);
      SwitchableContainer<ServiceInterfaceFormComponent, ServiceInterfacePanel> sc = new SwitchableContainer<ServiceInterfaceFormComponent, ServiceInterfacePanel>(
         repeater.newChildId(), sic, sip);
      repeater.add(sc);
      
      if(isSwitch){
         sc.switchComponent();
      }
      sc.add(new AjaxLink<WebMarkupContainer>("delete", new Model<WebMarkupContainer>(sc)) {
         @Override
         public void onClick(AjaxRequestTarget target) {
            if(repeater.size() < 2){
               return;
            }
            componentList.remove(getModelObject());
            repeater.remove(getModelObject());
            target.addComponent(rewrite);
         }
      }.setVisible( ! isSwitch));
      componentList.add(sc);
   }
   
   private RepeatingView repeater;
   private WebMarkupContainer rewrite;
   private AjaxLink<String> addLink;
   
   private String gridId;
   private int protocolLength;
   
   private List<SwitchableContainer<ServiceInterfaceFormComponent, ServiceInterfacePanel>>componentList = new ArrayList<SwitchableContainer<ServiceInterfaceFormComponent, ServiceInterfacePanel>>();
}
