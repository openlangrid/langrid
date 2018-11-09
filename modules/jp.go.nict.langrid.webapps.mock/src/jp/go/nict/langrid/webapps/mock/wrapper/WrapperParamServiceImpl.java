package jp.go.nict.langrid.webapps.mock.wrapper;

public class WrapperParamServiceImpl implements WrapperParamService{

	@Override
	public Byte byteFunc(Byte v) {
		return v;
	}

	@Override
	public Byte[] byteArrayFunc(Byte[] v) {
		return v;
	}

	@Override
	public Boolean booleanFunc(Boolean v) {
		return v;
	}

	@Override
	public Boolean[] booleanArrayFunc(Boolean[] v) {
		return v;
	}

	@Override
	public Character charFunc(Character v) {
		return v;
	}

	@Override
	public Character[] charArrayFunc(Character[] v) {
		return v;
	}
}
