package jp.go.nict.langrid.management.logic.qos;

import jp.go.nict.langrid.dao.entity.AccessLog;
import jp.go.nict.langrid.management.logic.QoSResult;
import jp.go.nict.langrid.management.logic.QoSType;

public class ThroughputCalculator implements QoSCalculator{
	@Override
	public void addLog(AccessLog log) {
		if(log.getResponseCode() != 200) return;
		sum += 1000.0 / log.getResponseMillis();
		total++;
	}
	
	@Override
	public QoSResult getResult() {
		return new QoSResult(QoSType.THROUGHPUT, total, sum / total);
	}

	private double sum;
	private int total;
}
