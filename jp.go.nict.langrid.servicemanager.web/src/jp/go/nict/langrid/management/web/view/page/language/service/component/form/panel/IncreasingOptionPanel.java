package jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import jp.go.nict.langrid.dao.entity.AppProvisionType;
import jp.go.nict.langrid.dao.entity.UseType;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class IncreasingOptionPanel extends Panel {
	public IncreasingOptionPanel(String panelId) throws ServiceManagerException {
		this(panelId
		   , new HashSet<String>(){{add(AppProvisionType.CLIENT_CONTROL.name());}}
		   , new HashSet<String>(){{add(UseType.NONPROFIT_USE.name());}}
		   , false);
	}

	public IncreasingOptionPanel(String panelId, Set<String> provi, Set<String> usage, boolean isFederation)
	throws ServiceManagerException
	{
		super(panelId);
		setRenderBodyOnly(true);

		useCg = new CheckGroup<String>("useChecks", usage);
		useCg.setRequired(true);
		add(useCg);
		useCg.add(new Check<String>("nonProfit" , new Model<String>(UseType.NONPROFIT_USE.name()) , useCg));
		useCg.add(new Check<String>("research"  , new Model<String>(UseType.RESEARCH_USE.name())  , useCg));
		WebMarkupContainer wc = new WebMarkupContainer("commercial-container");
		boolean isCommercialUse;
		isCommercialUse = ServiceFactory.getInstance().getGridService().isCommercialUse();
		wc.setVisible(isCommercialUse);
		wc.add(new Check<String>("commercial", new Model<String>(UseType.COMMERCIAL_USE.name()), useCg));
		useCg.add(wc);

		appCg = new CheckGroup<String>("appChecks", provi);
		add(appCg);
		appCg.add(new Check<String>("serverControl", new Model<String>(AppProvisionType.SERVER_CONTROL.name()), appCg));
		
		federation = new CheckBox("allowFederation", new Model<Boolean>(isFederation));
		add(federation);
	}

	public void doSubmitProcess(ServiceModel obj) {
	   Set<String> useTypeSet = new LinkedHashSet<String>();
	   useTypeSet.addAll(useCg.getModelObject());

	   Set<String> appTypeSet = new LinkedHashSet<String>();
	   appTypeSet.add(AppProvisionType.CLIENT_CONTROL.name());
	   appTypeSet.addAll(appCg.getModelObject());
	   obj.setAllowedAppProvision(appTypeSet);
	   obj.setAllowedUsage(useTypeSet);
	   obj.setFederatedUseAllowed(federation.getModelObject());
	}

	public void setIncreasingOptionInfo(ServiceModel obj) throws ServiceManagerException {
	   for(String s : obj.getAllowedAppProvision()) {
         appCg.getModelObject().add(s);
	   }
	   for(String s : obj.getAllowedUsage()) {
         useCg.getModelObject().add(s);
	   }
	   federation.setModelObject(obj.isFederatedUseAllowed());
	}

	public Set<String> getAllowedAppProvision() {
		Set<String> ret = new HashSet<String>();
	   ret.add(AppProvisionType.CLIENT_CONTROL.name());
		ret.addAll(appCg.getModelObject());
		return ret;
	}

	public Set<String> getAllowedUsage() {
		Set<String> ret = new HashSet<String>();
		ret.addAll(useCg.getModelObject());
		return ret;
	}
	
	public void setDefault(String gridId, String ownerUserId) throws ServiceManagerException{
	   UserModel model = ServiceFactory.getInstance().getUserService(gridId).get(ownerUserId);
	   Set<String> apps = new HashSet<String>();
	   apps.add(model.getDefaultAppProvisionType());
	   appCg.setModelObject(apps);
	   Set<String> uses = new HashSet<String>();
	   uses.add(model.getDefaultUseType());
	   useCg.setModelObject(uses);
	}
	
	private CheckGroup<String> useCg;
	private CheckGroup<String> appCg;
	private CheckBox federation;
}
