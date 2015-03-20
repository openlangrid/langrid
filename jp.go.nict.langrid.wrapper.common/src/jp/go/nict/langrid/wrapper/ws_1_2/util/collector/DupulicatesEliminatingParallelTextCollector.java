package jp.go.nict.langrid.wrapper.ws_1_2.util.collector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jp.go.nict.langrid.service_1_2.paralleltext.ParallelText;

public class DupulicatesEliminatingParallelTextCollector
implements Collector<ParallelText>{
	public DupulicatesEliminatingParallelTextCollector(int maxCount){
		this.maxCount = maxCount;
	}

	@Override
	public void collect(ParallelText p) {
		if(result.size() == maxCount) return;
		String source = p.getSource();
		String target = p.getTarget();
		Set<String> targets = texts.get(source);
		if(targets != null){
			if(targets.contains(target)) return;
		} else{
			targets = new HashSet<String>();
			texts.put(source, targets);
		}
		targets.add(target);
		result.add(p);
	}

	@Override
	public Collection<ParallelText> getCollection() {
		return result;
	}

	private int maxCount;
	private Map<String, Set<String>> texts = new HashMap<String, Set<String>>();
	private Collection<ParallelText> result = new ArrayList<ParallelText>(); 
}
