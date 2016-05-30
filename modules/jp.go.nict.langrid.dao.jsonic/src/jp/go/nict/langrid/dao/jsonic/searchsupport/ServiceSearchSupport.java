package jp.go.nict.langrid.dao.jsonic.searchsupport;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceContainerType;

public class ServiceSearchSupport extends SearchSupport<Service> {

	public ServiceSearchSupport(MatchingCondition[] conds, Order[] ord) {
		super(conds, ord);
	}

	@Override
	protected FieldDefs<Service> getDefinition(String fieldName) {
		return ServiceFieldDefs.valueOf(fieldName);
	}

	@Override
	protected Class<Service> getComponentType() {
		return Service.class;
	}

	protected enum ServiceFieldDefs implements FieldDefs<Service> {
		serviceId {
			@Override
			public int compare(Service left, Service right) {
				return left.getServiceId().compareTo(right.getServiceId());
			}
			@Override
			public boolean match(MatchingCondition cond, Service obj) {
				switch (cond.getMatchingMethod()) {
				case EQ:
				case COMPLETE:
					if (cond.getMatchingValue() == null) {
						return obj.getServiceId() == null;
					}
					return cond.getMatchingValue().toString()
							.equalsIgnoreCase(obj.getServiceId());
				default:
					throw new IllegalArgumentException(cond.getMatchingMethod() + " is not supported this field.");
				}
			}
		},

		serviceName {
			@Override
			public int compare(Service left, Service right) {
				return left.getServiceName().compareTo(right.getServiceName());
			}
		},

		serviceTypeId {
			@Override
			public boolean match(MatchingCondition cond, Service obj) {
				switch (cond.getMatchingMethod()) {
				case EQ:
				case COMPLETE:
					if (cond.getMatchingValue() == null) {
						return obj.getServiceTypeId() == null;
					}
					return cond.getMatchingValue().toString()
							.equalsIgnoreCase(obj.getServiceTypeId());
				default:
					throw new IllegalArgumentException(cond.getMatchingMethod() + " is not supported this field.");
				}
			}
		},
		
		active {
			@Override
			public boolean match(MatchingCondition cond, Service obj) {
				Boolean value = Boolean.valueOf(
						cond.getMatchingValue() == null ?
								null : cond.getMatchingValue().toString());
				switch (cond.getMatchingMethod()) {
				case EQ:
				case COMPLETE:
					return value.equals(obj.isActive());
				default:
					throw new IllegalArgumentException(cond.getMatchingMethod() + " is not supported this field.");
				}
			}
		},
		
		allowedAppProvision {
			@Override
			public boolean match(MatchingCondition cond, Service obj) {
				Set<String> values = new HashSet<String>();
				Collections.addAll(values, cond.getMatchingValue().toString().split(","));

				switch (cond.getMatchingMethod()) {
				case EQ:
				case COMPLETE:
					return values.containsAll(obj.getAllowedAppProvision())
							&& values.size() == obj.getAllowedAppProvision().size();
				case IN:
					return obj.getAllowedAppProvision().containsAll(values);
				default:
					throw new IllegalArgumentException(cond.getMatchingMethod() + " is not supported this field.");
				}
			}
		},
		
		containerType {
			@Override
			public boolean match(MatchingCondition cond, Service obj) {
				ServiceContainerType matchingValue = (ServiceContainerType) cond.getMatchingValue();

				switch (cond.getMatchingMethod()) {
				case EQ:
				case COMPLETE:
					return matchingValue.equals(obj.getContainerType());
				default:
					throw new IllegalArgumentException(cond.getMatchingMethod() + " is not supported this field.");
				}
			}
		},
		
		approved {
			@Override
			public boolean match(MatchingCondition cond, Service obj) {
				Boolean value = Boolean.valueOf(
						cond.getMatchingValue() == null ?
								null : cond.getMatchingValue().toString());
				switch (cond.getMatchingMethod()) {
				case EQ:
				case COMPLETE:
					return value.equals(obj.isApproved());
				default:
					throw new IllegalArgumentException(cond.getMatchingMethod() + " is not supported this field.");
				}
			}
		},
		
		membersOnly{
			@Override
			public boolean match(MatchingCondition cond, Service obj) {
				Boolean value = Boolean.valueOf(
						cond.getMatchingValue() == null ?
								null : cond.getMatchingValue().toString());
				switch (cond.getMatchingMethod()) {
				case EQ:
				case COMPLETE:
					return value.equals(obj.isMembersOnly());
				default:
					throw new IllegalArgumentException(cond.getMatchingMethod() + " is not supported this field.");
				}
			}
		}
		;

		@Override
		public boolean match(MatchingCondition cond, Service obj) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int compare(Service left, Service right) {
			throw new UnsupportedOperationException();
		}
	}
}
