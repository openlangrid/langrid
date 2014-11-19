package jp.go.nict.langrid.dao.archive;

import java.util.List;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.DomainDao;
import jp.go.nict.langrid.dao.GridDao;
import jp.go.nict.langrid.dao.ProtocolDao;
import jp.go.nict.langrid.dao.ResourceTypeDao;
import jp.go.nict.langrid.dao.ServiceTypeDao;
import jp.go.nict.langrid.dao.entity.Domain;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.dao.entity.Protocol;
import jp.go.nict.langrid.dao.entity.ResourceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ResourceType;
import jp.go.nict.langrid.dao.entity.ServiceInterfaceDefinition;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceType;

public class Importer {
	public Importer(DaoFactory factory)
	throws DaoException{
		gdao = factory.createGridDao();
		pdao = factory.createProtocolDao();
		ddao = factory.createDomainDao();
		rtdao = factory.createResourceTypeDao();
		stdao = factory.createServiceTypeDao();
	}

	public void clearDomains() throws DaoException{
		for(Grid g : gdao.listAllGrids()){
			g.getSupportedDomains().clear();
		}
		stdao.clear();
		rtdao.clear();
		pdao.clear();
		ddao.clear();
	}

	public void importDomains(DomainArchive archive) throws DaoException{
		importProtocols(archive.getProtocols());
		importDomains(archive.getDomains());
		for(Domain d : archive.getDomains()){
			String did = d.getDomainId();
			importResourceMetaAttributes(did, archive.getResourceMetaAttributes(did));
			importResourceTypes(did, archive.getResourceTypes(did));
			importServiceMetaAttributes(did, archive.getServiceMetaAttributes(did));
			importServiceTypes(did, archive.getServiceTypes(did));
		}
	}

	private void importProtocols(List<Protocol> values)
	throws DaoException{
		for(Protocol p : values){
			pdao.addProtocol(p);
		}
	}

	private void importDomains(List<Domain> values)
	throws DaoException{
		for(Domain d : values){
			ddao.addDomain(d);
		}
	}

	private void importResourceMetaAttributes(String domainId, List<ResourceMetaAttribute> values)
	throws DaoException{
		for(ResourceMetaAttribute rma : values){
			rma.setDomainId(domainId);
			rtdao.addResourceMetaAttribute(rma);
		}
	}

	private void importResourceTypes(String domainId, List<ResourceType> values)
	throws DaoException{
		for(ResourceType rt : values){
			rt.setDomainId(domainId);
			for(ResourceMetaAttribute m : rt.getMetaAttributes().values()){
				m.setDomainId(domainId);
			}
			rtdao.addResourceType(rt);
		}
	}

	private void importServiceMetaAttributes(String domainId, List<ServiceMetaAttribute> values)
	throws DaoException{
		for(ServiceMetaAttribute sma : values){
			sma.setDomainId(domainId);
			stdao.addServiceMetaAttribute(sma);
		}
	}

	private void importServiceTypes(String domainId, List<ServiceType> values)
	throws DaoException{
		for(ServiceType st : values){
			st.setDomainId(domainId);
			for(ServiceMetaAttribute m : st.getMetaAttributes().values()){
				m.setDomainId(domainId);
			}
			for(ServiceInterfaceDefinition def : st.getInterfaceDefinitions().values()){
				def.setServiceType(st);
			}
			stdao.addServiceType(st);
		}
	}

	private ProtocolDao pdao;
	private GridDao gdao;
	private DomainDao ddao;
	private ResourceTypeDao rtdao;
	private ServiceTypeDao stdao;
}
