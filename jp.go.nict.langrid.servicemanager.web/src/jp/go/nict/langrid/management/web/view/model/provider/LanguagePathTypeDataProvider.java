package jp.go.nict.langrid.management.web.view.model.provider;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import jp.go.nict.langrid.management.web.model.enumeration.LanguagePathType;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class LanguagePathTypeDataProvider implements IDataProvider<LanguagePathType> {
   public LanguagePathTypeDataProvider() {
      typeSet = LanguagePathType.getWithoutUnknown();
   }
   
   public LanguagePathTypeDataProvider(Set<LanguagePathType> typeSet) {
      this.typeSet = typeSet;
   }
   
   @Override
   public Iterator<LanguagePathType> iterator(int first, int count) {
      return typeSet.iterator();
   }

   @Override
   public IModel<LanguagePathType> model(LanguagePathType object) {
      return new Model<LanguagePathType>(object);
   }

   @Override
   public int size() {
      return typeSet.size();
   }

   @Override
   public void detach() {
   }
   
   private Collection<LanguagePathType> typeSet;
}
