package jp.go.nict.langrid.management.web.view.model.provider;

import java.util.Iterator;
import java.util.Locale;

import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.management.web.model.InternalLanguageModel;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class LanguageDataProvider implements IDataProvider<Language> {
   public LanguageDataProvider(Locale locale) {
      this.locale = locale;
   }
   
   @Override
   public Iterator<Language> iterator(int first, int count) {
      return InternalLanguageModel.getLanguageList().iterator();
   }

   @Override
   public IModel<Language> model(Language object) {
      return new Model<Language>(object);
   }

   @Override
   public int size() {
      return InternalLanguageModel.getLanguageList().size();
   }

   @Override
   public void detach() {
   }
   
   private Locale locale;
}
