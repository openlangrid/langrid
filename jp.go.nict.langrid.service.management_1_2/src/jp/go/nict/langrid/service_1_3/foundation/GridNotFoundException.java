package jp.go.nict.langrid.service_1_3.foundation;

import jp.go.nict.langrid.service_1_2.LangridException;

public class GridNotFoundException
extends LangridException{
	public GridNotFoundException(String gridId, String description){
		super(description);
		this.gridId = gridId;
	}

	public String getGridId(){
		return gridId;
	}

	private String gridId;

	private static final long serialVersionUID = -8943137594288178125L;
}
