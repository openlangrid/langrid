package jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class ServiceAccessRightRadioPanel extends Panel {
   public ServiceAccessRightRadioPanel(String panelId) {
      super(panelId);
      setOutputMarkupId(true);
      Form form = new StatelessForm("form");
      group = new RadioGroup<AccessGroup>("radioGroup", new Model<AccessGroup>());
      Model<AccessGroup> model = new Model<AccessGroup>(AccessGroup.FORALLUSERS);
      Radio<AccessGroup> allUsers = new Radio<AccessGroup>("forAllUsers"
         , model);
      group.setModelObject(AccessGroup.FORALLUSERS);
      Radio<AccessGroup> membersOnly = new Radio<AccessGroup>("membersOnly"
         , new Model<AccessGroup>(AccessGroup.MEMBERSONLY));
      group.add(allUsers);
      group.add(membersOnly);
      form.add(group);
      add(form);
      group.add(new AjaxFormChoiceComponentUpdatingBehavior() {
         @Override
         protected void onUpdate(AjaxRequestTarget target) {
            doUpdate(group.getModelObject().equals(AccessGroup.MEMBERSONLY), target);
         }
      });
   }
   
   protected void doUpdate(boolean isMembersOnly, AjaxRequestTarget target){
      // noop
   }
   
   private RadioGroup<AccessGroup> group;
   
   private enum AccessGroup {
      FORALLUSERS
      , MEMBERSONLY
   }
}
