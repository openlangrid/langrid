package jp.go.nict.langrid.management.web.view.page.language.service.admin.component.form.panel;

import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.component.choice.ProtocolDropDownChoice;

import org.apache.wicket.markup.html.panel.Panel;

public class ServiceInterfaceFormComponent extends Panel {
	public ServiceInterfaceFormComponent(String panelId, String gridId)
			throws ServiceManagerException
	{
		super(panelId);
		filePanel = new FileMultipleUploadPanel(gridId, "files", false);
		add(filePanel);
		protocolChoice = new ProtocolDropDownChoice(gridId, "protocol");
		add(protocolChoice);
	}

	public FileMultipleUploadPanel getFileComponent(){
		return filePanel;
	}

	public ProtocolDropDownChoice getProtocolComponent(){
		return protocolChoice;
	}

	private FileMultipleUploadPanel filePanel;
	private ProtocolDropDownChoice protocolChoice;
}
