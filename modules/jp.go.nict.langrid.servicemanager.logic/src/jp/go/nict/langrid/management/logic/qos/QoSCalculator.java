package jp.go.nict.langrid.management.logic.qos;

import jp.go.nict.langrid.dao.entity.AccessLog;
import jp.go.nict.langrid.management.logic.QoSResult;

public interface QoSCalculator {
	void addLog(AccessLog log);
	QoSResult getResult();
}
