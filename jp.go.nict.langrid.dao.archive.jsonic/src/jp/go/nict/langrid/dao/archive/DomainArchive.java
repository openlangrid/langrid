package jp.go.nict.langrid.dao.archive;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.util.ListUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.entity.Domain;
import jp.go.nict.langrid.dao.entity.Protocol;
import jp.go.nict.langrid.dao.entity.ResourceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ResourceType;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceType;
import jp.go.nict.langrid.dao.entity.ServiceTypePK;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

/**
 * 2-passでドメインアーカイブを読み込む。
 * 最初のパスで*.binファイルを読み込み、次のパスで
 * @author nakaguchi
 *
 */
public class DomainArchive {
	public void read1pass(ZipInputStream is)
	throws IOException{
		for(ZipEntry entry; (entry = is.getNextEntry()) != null;){
			if(entry.getName().endsWith(".bin")){
				binaries.put(entry.getName(), StreamUtil.readAsBytes(is));
			}
		}
	}
	private Map<String, byte[]> binaries = new HashMap<String, byte[]>();

	public void read2pass(ZipInputStream is, DaoFactory factory){
		
	}
	
	public List<Protocol> getProtocols(){
		return protocols;
	}

	public List<Domain> getDomains(){
		return domains;
	}

	@SuppressWarnings("unchecked")
	public List<ResourceMetaAttribute> getResourceMetaAttributes(String domainId){
		return (List<ResourceMetaAttribute>)resourceMetaAttributes.get(domainId);
	}

	@SuppressWarnings("unchecked")
	public List<ResourceType> getResourceTypes(String domainId){
		return (List<ResourceType>)resourceTypes.get(domainId);
	}

	@SuppressWarnings("unchecked")
	public List<ServiceMetaAttribute> getServiceMetaAttributes(String domainId){
		return (List<ServiceMetaAttribute>)serviceMetaAttributes.get(domainId);
	}

	@SuppressWarnings("unchecked")
	public List<ServiceType> getServiceTypes(String domainId){
		return (List<ServiceType>)serviceTypes.get(domainId);
	}

	// <domainId, serviceTypeId, definition>, protocolName
	private void parse(DaoContext dc, ZipInputStream zis)
	throws IOException, DaoException, SQLException{
		List<Pair<Pattern, Map<String, List<?>>>> l = 
				new ArrayList<Pair<Pattern, Map<String,List<?>>>>();
		l.add(Pair.create(rmjPattern, resourceMetaAttributes));
		l.add(Pair.create(rtjPattern, resourceTypes));
		l.add(Pair.create(smjPattern, serviceMetaAttributes));
		l.add(Pair.create(stjPattern, serviceTypes));

		ZipEntry entry = null;
		while((entry = zis.getNextEntry()) != null){
			String name = entry.getName();
			System.out.println(name);
			if(name.matches("domains\\/[^/]+\\.json")){
				parseDomain(zis);
				continue;
			}
			if(name.matches("protocols\\/[^/]+\\.json")){
				parseProtocol(zis);
				continue;
			}
			if(name.matches("domains\\/[^/]+\\/serviceTypes\\/[^/]+\\.wsdl")){
				Trio<String, String, byte[]> r = parseServiceTypeWsdl(zis, name);
				ServiceType st = dc.loadEntity(
						ServiceType.class, new ServiceTypePK(r.getFirst(), r.getSecond())
						);
				
				continue;
			}

			for(Pair<Pattern, Map<String, List<?>>> e : l){
				Matcher m = e.getFirst().matcher(name);
				if(m.matches()){
					parseAndAdd(m.group(1), zis, e.getSecond());
					break;
				}
			}
		}
	}

	private void parseDomain(ZipInputStream zis) throws IOException{
		domains.add(new LangridJSON().parse(zis, Domain.class));
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void parseAndAdd(String domainId, ZipInputStream zis, Map<String, List<?>> values)
	throws IOException{
		Domain v = JSON.decode(zis, Domain.class);
		List ret = values.get(domainId);
		if(ret == null){
			ret = ListUtil.newArrayList();
			values.put(domainId, ret);
		}
		ret.add(v);
	}

	private Trio<String, String, byte[]> parseServiceTypeWsdl(ZipInputStream zis, String name)
	throws IOException, SQLException{
		Matcher m = stwPattern.matcher(name);
		if(!m.matches()) throw new RuntimeException("pattern not match");
		String domainId = m.group(1);
		String serviceTypeId = m.group(2);
		return Trio.create(domainId, serviceTypeId, StreamUtil.readAsBytes(zis));
	}

	private void parseProtocol(ZipInputStream zis) throws IOException{
		protocols.add(JSON.decode(
				zis, Protocol.class
				));
	}

	private List<Protocol> protocols = new ArrayList<Protocol>();
	private List<Domain> domains = new ArrayList<Domain>();
	private Map<String, List<?>> resourceMetaAttributes = new HashMap<String, List<?>>();
	private Map<String, List<?>> resourceTypes = new HashMap<String, List<?>>();
	private Map<String, List<?>> serviceMetaAttributes = new HashMap<String, List<?>>();
	private Map<String, List<?>> serviceTypes = new HashMap<String, List<?>>();

	private static Pattern rmjPattern = Pattern.compile("domains\\/([^/]+)\\/resourceMetaAttributes\\/(.*)\\.json");
	private static Pattern rtjPattern = Pattern.compile("domains\\/([^/]+)\\/resourceTypes\\/(.*)\\.json");
	private static Pattern smjPattern = Pattern.compile("domains\\/([^/]+)\\/serviceMetaAttributes\\/(.*)\\.json");
	private static Pattern stwPattern = Pattern.compile("domains\\/([^/]+)\\/serviceTypes\\/(.*)\\.wsdl");
	private static Pattern stjPattern = Pattern.compile("domains\\/([^/]+)\\/serviceTypes\\/(.*)\\.json");
}
