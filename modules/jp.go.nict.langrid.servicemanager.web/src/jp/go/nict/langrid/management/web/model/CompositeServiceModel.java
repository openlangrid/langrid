package jp.go.nict.langrid.management.web.model;

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.dao.entity.ServiceContainerType;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class CompositeServiceModel extends ServiceModel {
	/**
	 * 
	 * 
	 */
	public CompositeServiceModel() {
		setContainerType(ServiceContainerType.COMPOSITE.name());
	}

	/**
	 * 
	 * 
	 */
	public List<InvocationModel> getInvocations() {
		return invocations;
	}

	/**
	 * 
	 * 
	 */
	public void setInvocations(List<InvocationModel> invocations) {
		this.invocations = invocations;
	}

	private List<InvocationModel> invocations = new ArrayList<InvocationModel>();

	private static final long serialVersionUID = 7817675060573304720L;
}
