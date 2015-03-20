package jp.go.nict.langrid.client.soap.test;

import java.util.Calendar;

import jp.go.nict.langrid.commons.rpc.intf.Parameter;

public interface Axis14EchoService{
	boolean funcString(@Parameter(name="v") String v);
	boolean funcBooleanP(@Parameter(name="v") boolean v);
	short funcShortP(@Parameter(name="v") short v);
	int funcIntP(@Parameter(name="v") int v);
	long funcLongP(@Parameter(name="v") long v);
	float funcFloatP(@Parameter(name="v") float v);
	double funcDoubleP(@Parameter(name="v") double v);
	Calendar funcCalendar(@Parameter(name="v") Calendar v);
}
