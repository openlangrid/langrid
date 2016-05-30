package jp.go.nict.langrid.management.web.view.model;

import java.util.HashMap;

import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;

import org.apache.wicket.IClusterable;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public abstract class ServiceSortOrder implements IClusterable{
	protected ServiceSortOrder(){
		setDefaultOrder();
	}

	/**
	 * 
	 * 
	 */
	public void deleteOrder(String key){
		orderList.remove(key);
	}

	/**
	 * 
	 * 
	 */
	public Order[] getOrder(){
		if(hasMakedOrder()){
			return orders;
		}
		if(!hasOrderParam()){
			return new Order[]{};
		}
		orders = new Order[orderList.size()];
		int i = 0;
		for(String key : orderList.keySet()){
			orders[i++] = new Order(key, orderList.get(key));
		}
		return orders;
	}

	/**
	 * 
	 * 
	 */
	public String getRegisteredDateId(){
		return registeredDateId;
	}

	/**
	 * 
	 * 
	 */
	public String getServiceNameId(){
		return serviceNameId;
	}

	/**
	 * 
	 * 
	 */
	public String getServiceTypeId(){
		return serviceTypeId;
	}

	/**
	 * 
	 * 
	 */
	public void initialize(){
		orderList = new HashMap<String, OrderDirection>();
		setDefaultOrder();
	}

	/**
	 * 
	 * 
	 */
	public boolean isAscendant(String id){
		if(orderList.get(id) == null){
			return false;
		}
		return orderList.get(id).equals(OrderDirection.ASCENDANT);
	}

	/**
	 * 
	 * 
	 */
	public void setOrReplaceOrder(String orderFieldName, OrderDirection direction){
		if(hasMakedOrder()){
			orders = null;
		}
		orderList.put(orderFieldName, direction);
	}

	protected abstract void setDefaultOrder();

	private boolean hasMakedOrder(){
		return orders != null;
	}

	private boolean hasOrderParam(){
		return orderList != null && orderList.size() != 0;
	}

	private HashMap<String, OrderDirection> orderList = new HashMap<String, OrderDirection>();
	private Order[] orders;
	private static final String registeredDateId = "createdDateTime";
	private static final long serialVersionUID = 1L;
	private static final String serviceNameId = "serviceName";
	private static final String serviceTypeId = "serviceType";
}
