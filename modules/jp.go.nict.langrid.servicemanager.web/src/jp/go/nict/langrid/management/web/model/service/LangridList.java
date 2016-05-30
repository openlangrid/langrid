package jp.go.nict.langrid.management.web.model.service;

import java.util.ArrayList;

public class LangridList<E> extends ArrayList<E> {
	public void setTotalCount(int count) {
		totalCount = count;
	}
	
	public int getTotalCount() {
		return totalCount;
	}
	
	private int totalCount;

	private static final long serialVersionUID = -9100597791221311193L;
}
