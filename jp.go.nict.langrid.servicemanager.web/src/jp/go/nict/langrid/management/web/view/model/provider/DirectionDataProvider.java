package jp.go.nict.langrid.management.web.view.model.provider;

import java.util.Iterator;

import jp.go.nict.langrid.management.web.model.enumeration.LanguagePathDirection;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class DirectionDataProvider implements IDataProvider<LanguagePathDirection> {
//   public DirectionDataProvider() {
//   }
//   
//   public DirectionDataProvider(List<LanguagePathDirection> list) {
//      this.list = list;
//   }
   
   @Override
   public Iterator<LanguagePathDirection> iterator(int first, int count) {
//      if(list == null){
//         return Arrays.asList(LanguagePathDirection.values()).iterator();
//      }
//      return list.iterator();
      return LanguagePathDirection.getListWithoutCombination().iterator();
   }

   @Override
   public IModel<LanguagePathDirection> model(LanguagePathDirection object) {
      return new Model<LanguagePathDirection>(object);
   }

   @Override
   public int size() {
//      if(list == null){
//         return LanguagePathDirection.getCodeList().size();
//      }
//      return list.size();
      return LanguagePathDirection.getListWithoutCombination().size();
   }

   @Override
   public void detach() {
//      list = null;
   }
   
//   private List<LanguagePathDirection> list;
}
