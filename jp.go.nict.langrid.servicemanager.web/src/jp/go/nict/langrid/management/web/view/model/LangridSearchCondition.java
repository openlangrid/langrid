package jp.go.nict.langrid.management.web.view.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.logic.Scope;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.wicket.IClusterable;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class LangridSearchCondition implements IClusterable{
	/**
	 * 
	 * 
	 */
	public LangridSearchCondition(){
		orderMap.put("serviceName", OrderDirection.ASCENDANT);
	}
	
	/**
	 * 
	 * 
	 */
//	public void addCondition(String targetName, String targetString, MatchingMethod method){
//		if( ! matchingMap.containsKey(targetName)){
//			return;
//		}
//		List<MatchingCondition> list = matchingMap.get(targetName);
//		MatchingCondition co = new MatchingCondition(targetName, targetString, method);
//		list.add(co);
//		matchingMap.put(targetName, list);
//	}
	
//	public void addCondition(String targetName, Object targetObject){
//	   if( ! matchingMap.containsKey(targetName)){
//	      return;
//	   }
//	   List<MatchingCondition> list = matchingMap.get(targetName);
//	   MatchingCondition co = new MatchingCondition(targetName, targetObject);
//	   list.add(co);
//	   matchingMap.put(targetName, list);
//	}

	/**
	 * 
	 * 
	 */
	public void clearOrder(){
		orderMap = new LinkedHashMap<String, OrderDirection>();
		isDarty = true;
	}

	/**
	 * 
	 * 
	 */
	public MatchingCondition[] getConditions(){
		List<MatchingCondition> list = new ArrayList<MatchingCondition>();
		
//		MatchingCondition[] mcs = new MatchingCondition[matchingMap.size()];
		int i = 0;
		for(String key : matchingMap.keySet()){
			for(MatchingCondition mc : matchingMap.get(key)){
//				mcs[i++] = mc;
				list.add(mc);
			}
		}
		for(String key : noChangeMatchingMap.keySet()){
		   for(MatchingCondition mc : noChangeMatchingMap.get(key)){
//				mcs[i++] = mc;
		      list.add(mc);
		   }
		}
//		return mcs;
		isDarty = false;
		return list.toArray(new MatchingCondition[]{});
	}

	/**
	 * 
	 * 
	 */
	public OrderDirection getOrder(String key){
		return orderMap.get(key);
	}

	/**
	 * 
	 * 
	 */
	public Order[] getOrders(){
		Order[] orders = new Order[orderMap.size()];
		int i = 0;
		for(String key : orderMap.keySet()){
			orders[i++] = new Order(key, orderMap.get(key));
		}
		return orders;
	}

	/**
	 * 
	 * 
	 */
	public Scope getScope(){
		return scope;
	}
	
	/**
	 * 
	 * 
	 */
	public boolean hasCondition(String key){
		return matchingMap.get(key) != null;
	}

	/**
	 * 
	 * 
	 */
	public void putOrReplaceCondition(
			String targetName, String targetString, MatchingMethod method)
	{
		MatchingCondition co = new MatchingCondition(targetName, targetString, method);
		List<MatchingCondition> list = new ArrayList<MatchingCondition>();
		list.add(co);
		matchingMap.put(targetName, list);
	    isDarty = true;
	}

	public void putOrReplaceCondition(
	   String targetName, Object targetObject)
	{
	   MatchingCondition co = new MatchingCondition(targetName, targetObject);
	   List<MatchingCondition> list = new ArrayList<MatchingCondition>();
	   list.add(co);
	   matchingMap.put(targetName, list);
	     isDarty = true;
	}
	
	public void addNoChangeCondition(
	   String targetName, Object targetObject)
	{
	   MatchingCondition co = new MatchingCondition(targetName, targetObject);
	   List<MatchingCondition> list = new ArrayList<MatchingCondition>();
	   list.add(co);
	   noChangeMatchingMap.put(targetName, list);
	     isDarty = true;
	}
	
   public void addNoChangeCondition(
      String targetName, String targetString, MatchingMethod method)
   {
      MatchingCondition co = new MatchingCondition(targetName, targetString, method);
      List<MatchingCondition> list = new ArrayList<MatchingCondition>();
      list.add(co);
      noChangeMatchingMap.put(targetName, list);
      isDarty = true;
   }

	/**
	 * 
	 * 
	 */
	public void putOrReplaceOrder(Order[] orders){
		for(Order order : orders){
			orderMap.put(order.getFieldName(), order.getDirection());
		}
	    isDarty = true;
	}

	/**
	 * 
	 * 
	 */
	public void putOrReplaceOrder(String targetName, OrderDirection direction){
		orderMap.put(targetName, direction);
	    isDarty = true;
	}
	
	public void clearCondition(){
	   matchingMap = new HashMap<String, List<MatchingCondition>>();
	     isDarty = true;
	}

	/**
	 * 
	 * 
	 */
	public void setScope(Scope scope){
		this.scope = scope;
	    isDarty = true;
	}
	
	public boolean isDarty(){
	   return isDarty;
	}
	
	@Override
	public String toString() {
	   return ToStringBuilder.reflectionToString(this);
	}

	private HashMap<String, List<MatchingCondition>> matchingMap
		= new HashMap<String, List<MatchingCondition>>();
	private HashMap<String, List<MatchingCondition>> noChangeMatchingMap
	= new HashMap<String, List<MatchingCondition>>();
	private LinkedHashMap<String, OrderDirection> orderMap
		= new LinkedHashMap<String, OrderDirection>();
	private Scope scope = Scope.ALL;
	private boolean isDarty = false;
	
	private static final long serialVersionUID = 1L;
}
