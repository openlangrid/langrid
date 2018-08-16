package jp.go.nict.langrid.management.logic.qos;

import jp.go.nict.langrid.dao.entity.AccessLog;
import jp.go.nict.langrid.management.logic.QoSResult;
import jp.go.nict.langrid.management.logic.QoSType;

public class SuccessRateCalculator
implements QoSCalculator{
	@Override
	public void addLog(AccessLog log) {
		if(log.getResponseCode() == 200) {
			success++;
			total++;
		} else if(
				log.getFaultString().contains("InvalidParameterException") ||
				log.getFaultString().contains("UnsupportLanguageException") ||
				log.getFaultString().contains("UnsupportLanguagePairException") ||
				log.getFaultString().contains("AccessLimitExceededException")) {
		} else {
			total++;
		}
	}

	@Override
	public QoSResult getResult() {
		return new QoSResult(QoSType.SUCCESS_RATE, total, 1.0 * success / total);
	}

	private int success;
	private int total;
}
