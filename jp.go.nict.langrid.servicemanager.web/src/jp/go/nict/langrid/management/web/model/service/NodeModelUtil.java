package jp.go.nict.langrid.management.web.model.service;

import java.net.MalformedURLException;
import java.net.URL;

import jp.go.nict.langrid.dao.entity.Node;
import jp.go.nict.langrid.management.web.model.NodeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class NodeModelUtil {
	public static NodeModel makeModel(Node entity) throws ServiceManagerException {
		NodeModel model = new NodeModel();
		// IDs
		model.setGridId(entity.getGridId());
		model.setOwnerUserId(entity.getOwnerUserId());
		model.setNodeId(entity.getNodeId());
		// profile
		model.setNodeName(entity.getNodeName());
		model.setUrl(entity.getUrl() == null ? "" : entity.getUrl().toString());
		model.setOwnerUserOrganization(entity.getOwnerUserOrganization());
		// optional
		model.setCpu(entity.getCpu());
		model.setMemory(entity.getMemory());
		model.setOs(entity.getOs());
		model.setSpecialNotes(entity.getSpecialNotes());
		// status
		model.setCreatedDateTime(entity.getCreatedDateTime());
		model.setUpdatedDateTime(entity.getUpdatedDateTime());
		model.setActive(entity.isActive());
		return model;
	}

	public static Node setProperty(NodeModel model, Node entity)
	throws ServiceManagerException
	{
		entity.setOwnerUserId(model.getOwnerUserId());
		entity.setNodeName(model.getNodeName());
		entity.setCpu(model.getCpu());
		entity.setMemory(model.getMemory());
		entity.setOs(model.getOs());
		entity.setSpecialNotes(model.getSpecialNotes());
		try {
			entity.setUrl(new URL(model.getUrl()));
		} catch(MalformedURLException e) {
			throw new ServiceManagerException(e);
		}
		return entity;
	}
}
