package jp.go.nict.langrid.dao.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
@Entity
@IdClass(UserAttributePK.class)
public class UserAttribute
extends Attribute
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public UserAttribute(){
	}

	/**
	 * 
	 * 
	 */
	public UserAttribute(String gridId, String userId, String name, String value){
		super(name, value);
		this.gridId = gridId;
		this.userId = userId;
	}

	/**
	 * 
	 * 
	 */
	public String getGridId() {
		return gridId;
	}

	/**
	 * 
	 * 
	 */
	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	/**
	 * 
	 * 
	 */
	public String getUserId(){
		return userId;
	}

	/**
	 * 
	 * 
	 */
	public void setUserId(String userId){
		this.userId = userId;
	}

	@Id
	public String getName() {
		return super.getName();
	}

	@Id
	private String gridId;
	@Id
	private String userId;
	private static final long serialVersionUID = 6963600458056879071L;
}
