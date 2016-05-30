package jp.go.nict.langrid.management.web.view.page.language.resource.component.form.panel;

import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.enumeration.OS;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.component.choice.OSDropDownChoice;
import jp.go.nict.langrid.management.web.view.component.text.CPUField;
import jp.go.nict.langrid.management.web.view.component.text.MemoryField;
import jp.go.nict.langrid.management.web.view.component.text.SpecialNoteField;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class ResourceOptionalFieldPanel extends Panel {
	public ResourceOptionalFieldPanel(String componentId) {
		super(componentId);
		setRenderBodyOnly(true);
		add(osChoice = new OSDropDownChoice("osInfo", new Model<OS>()));
		add(new CPUField("cpuInfo", cpuInfo = new Model<String>()));
		add(new MemoryField("memoryInfo", memoryInfo = new Model<String>()));
		add(new SpecialNoteField("specialNoteInfo", specialNoteInfo = new Model<String>()));
	}
	
	public void doSubmitProcess(ResourceModel obj) throws ServiceManagerException {
		obj.setCpuInfo(cpuInfo.getObject());
		obj.setMemoryInfo(memoryInfo.getObject());
		obj.setSpecialNoteInfo(specialNoteInfo.getObject());
		obj.setOsInfo(osChoice.getModelValue().equals("-1") ? "" : osChoice.getModelValue());
	}

	public void setOptionalInfo(ResourceModel obj) {
		cpuInfo.setObject(obj.getCpuInfo());
		memoryInfo.setObject(obj.getMemoryInfo());
		specialNoteInfo.setObject(obj.getSpecialNoteInfo());
		
		osChoice.setModel(new Model<OS>(OS.getByValue(obj.getOsInfo())));
	}
	
	private Model<String> cpuInfo;
	private Model<String> memoryInfo;
	private Model<String> specialNoteInfo;
	private OSDropDownChoice osChoice;
}
