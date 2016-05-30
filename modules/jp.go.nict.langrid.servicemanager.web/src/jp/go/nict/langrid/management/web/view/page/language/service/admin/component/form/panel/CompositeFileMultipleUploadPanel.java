package jp.go.nict.langrid.management.web.view.page.language.service.admin.component.form.panel;

import java.util.List;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: Masaaki Kamiya $
 * @version $Revision: 12254 $
 */
public class CompositeFileMultipleUploadPanel extends FileMultipleUploadPanel {
	public CompositeFileMultipleUploadPanel(String gridId, String componentId, List<String> list) {
		super(gridId, componentId, list, false);
	}
}
