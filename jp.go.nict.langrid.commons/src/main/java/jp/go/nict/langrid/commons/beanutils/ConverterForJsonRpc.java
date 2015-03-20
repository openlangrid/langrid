package jp.go.nict.langrid.commons.beanutils;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

public class ConverterForJsonRpc extends Converter{
	@SuppressWarnings("unchecked")
	@Override
	public <T> T convert(Object value, Class<T> target)
			throws ConversionException {
		if(value instanceof String && value.equals("[]") && target.isArray()){
			return (T)Array.newInstance(target.getComponentType(), 0);
		}
		try{
			return super.convert(value, target);
		} catch(ConversionException e){
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public ConverterForJsonRpc() {
		addTransformerConversion(Map.class, String.class, new Transformer<Map, String>(){
			@Override
			public String transform(Map value) throws TransformationException {
				return JSON.encode(value);
			}
		});
		addTransformerConversion(String.class, Calendar.class, new Transformer<String, Calendar>(){
			public Calendar transform(String value) throws TransformationException {
				if(value == null) return null;
				if(value.length() == 0) return null;
				Calendar c = Calendar.getInstance();
				try{
					long t = Long.valueOf(value);
					c.setTimeInMillis(t);
					return c;
				} catch(NumberFormatException e){
				}
				try{
					c.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(value));
					return c;
				} catch(ParseException e){
				}
				try{
					c.setTime(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss.SSS z").parse(value));
					return c;
				} catch(ParseException e){
				}
				try{
					c.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(value));
					return c;
				} catch(ParseException e){
				}
				throw new TransformationException("failed to convert string:\"" + value + "\" to Calendar.");
			};
		});
	}
}
