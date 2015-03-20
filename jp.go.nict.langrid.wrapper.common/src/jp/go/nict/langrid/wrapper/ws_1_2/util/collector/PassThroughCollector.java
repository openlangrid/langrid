package jp.go.nict.langrid.wrapper.ws_1_2.util.collector;

import java.util.ArrayList;
import java.util.Collection;

public class PassThroughCollector<T>
implements Collector<T>{
	public PassThroughCollector(int maxCount){
		this.maxCount = maxCount;
	}

	@Override
	public void collect(T element) {
		if(results.size() == maxCount) return;
		results.add(element);
	}

	@Override
	public Collection<T> getCollection() {
		return results;
	}

	private int maxCount;
	private Collection<T> results = new ArrayList<T>(); 
}
