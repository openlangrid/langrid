package jp.go.nict.langrid.management.logic.qos;

import jp.go.nict.langrid.dao.entity.AccessLog;
import jp.go.nict.langrid.management.logic.QoSResult;
import jp.go.nict.langrid.management.logic.QoSType;

public class AvailabilityCalculator
implements QoSCalculator{
	@Override
	public void addLog(AccessLog log) {
		if(log.getResponseCode() == 200 ||
				log.getFaultString().contains("InvalidParameterException") ||
				log.getFaultString().contains("UnsupportLanguageException") ||
				log.getFaultString().contains("UnsupportLanguagePairException") ||
				log.getFaultString().contains("AccessLimitExceededException")) {
			available++;
		}
		total++;
	}

	@Override
	public QoSResult getResult() {
		return new QoSResult(QoSType.AVAILABILITY, total, 1.0 * available / total);
	}

	private int available;
	private int total;
}
