package jp.go.nict.langrid.dao.util;

import java.lang.reflect.Type;
import java.sql.Blob;

import jp.go.nict.langrid.commons.transformer.ByteArrayToBlobTransformer;

public class JSON extends jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON{
	@Override
	protected Object preformat(Context context, Object value) throws Exception {
		if(value.getClass().getSimpleName().equals("JavassistLazyInitializer")) return null;
		if(value instanceof Blob){
			Blob b = (Blob)value;
			return b.getBytes(1, (int)b.length());
		}
		return super.preformat(context, value);
	}

	@SuppressWarnings("unchecked")
	protected <T> T postparse(Context context, Object value, Class<? extends T> cls, Type type) throws Exception {
		if(value == null) return null;
		if(Blob.class.isAssignableFrom(cls)){
			byte[] b = (byte[])this.convert(value, byte[].class);
			return (T)new ByteArrayToBlobTransformer().transform(b);
		}
		return super.postparse(context, value, cls, type);
	};
}
