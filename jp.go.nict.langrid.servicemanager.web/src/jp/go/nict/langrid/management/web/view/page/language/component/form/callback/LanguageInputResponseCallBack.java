package jp.go.nict.langrid.management.web.view.page.language.component.form.callback;

import java.io.Serializable;
import java.util.List;

import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.page.language.component.modal.PathModel;

import org.apache.wicket.ajax.AjaxRequestTarget;

public interface LanguageInputResponseCallBack extends Serializable{
   void call(AjaxRequestTarget target, List<PathModel> list)
   throws ServiceManagerException;
}
