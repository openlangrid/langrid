package jp.go.nict.langrid.dao.archive;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.sql.Blob;
import java.sql.Clob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.dao.util.LobUtil;
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.language.util.LanguagePathUtil;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

public class LangridJSON extends JSON{
	public LangridJSON(){
		setPrettyPrint(true);
		setSuppressNull(true);
	}

	public LangridJSON(File baseDir, String prefix){
		this.baseDir = baseDir;
		this.prefix = prefix;
		setPrettyPrint(true);
		setSuppressNull(true);
	}

	public void setBaseDirAndPrefix(File baseDir, String prefix){
		this.baseDir = baseDir;
		this.prefix = prefix;
	}

	@Override
	protected Object preformat(Context context, Object value)
			throws Exception {
		if(value == null) return null;
		if(context.getKey().equals("serviceType")
				&& context.getKey(1).equals("interfaceDefinitions")
				&& context.getLevel() == 3) return null;
		if(value instanceof Clob){
			if(baseDir == null || prefix == null) throw new RuntimeException();
			String fname = prefix + getKeys(context) + ".bin";
			FileOutputStream os = new FileOutputStream(new File(baseDir, fname));
			OutputStreamWriter w = new OutputStreamWriter(os, "UTF-8");
			try{
				StreamUtil.transfer(((Clob)value).getCharacterStream(), w); 
				return fname;
			} finally{
				os.close();
			}
		} else if(value instanceof Blob){
			if(baseDir == null || prefix == null) throw new RuntimeException();
			String fname = prefix + getKeys(context) + ".bin";
			FileOutputStream os = new FileOutputStream(new File(baseDir, fname));
			try{
				StreamUtil.transfer(((Blob)value).getBinaryStream(), os); 
				return fname;
			} finally{
				os.close();
			}
		} else if(value instanceof InputStream){
			if(baseDir == null || prefix == null) throw new RuntimeException();
			String fname = prefix + getKeys(context) + ".bin";
			FileOutputStream os = new FileOutputStream(new File(baseDir, fname));
			try{
				StreamUtil.transfer((InputStream)value, os); 
				return fname;
			} finally{
				os.close();
			}
		} else if(value instanceof LanguagePath){
			return LanguagePathUtil.encodeLanguagePath((LanguagePath)value);
		} else if(value instanceof Calendar){
			return format.format(((Calendar)value).getTime());
		}
		return super.preformat(context, value);
	}

	@Override
	protected <T> T postparse(Context context, Object value,
			Class<? extends T> cls, Type type) throws Exception {
		if(Clob.class.isAssignableFrom(cls)){
			if(baseDir == null || prefix == null) throw new RuntimeException();
			FileInputStream is = new FileInputStream(new File(baseDir, value.toString()));
			try{
				return cls.cast(LobUtil.createClob(new InputStreamReader(is, "UTF-8")));
			} finally{
				is.close();
			}
		} else if(Blob.class.isAssignableFrom(cls)){
			if(baseDir == null || prefix == null) throw new RuntimeException();
			FileInputStream is = new FileInputStream(new File(baseDir, value.toString()));
			try{
				if(context.getKey().equals("definition")){
					ByteArrayOutputStream o = new ByteArrayOutputStream();
					ZipOutputStream zos = new ZipOutputStream(o);
					zos.putNextEntry(new ZipEntry(value.toString()));
					StreamUtil.transfer(is, zos);
					zos.finish();
					return cls.cast(LobUtil.createBlob(new ByteArrayInputStream(o.toByteArray())));
				} else{
					return cls.cast(LobUtil.createBlob(is));
				}
			} finally{
				is.close();
			}
		} else if(InputStream.class.isAssignableFrom(cls)){
			if(baseDir == null || prefix == null) throw new RuntimeException();
			FileInputStream is = new FileInputStream(new File(baseDir, value.toString()));
			try{
				return cls.cast(new ByteArrayInputStream(StreamUtil.readAsBytes(is)));
			} finally{
				is.close();
			}
		} else if(LanguagePath.class.isAssignableFrom(cls)){
			return cls.cast(LanguagePathUtil.decodeLanguagePath(value.toString()));
		} else if(Calendar.class.isAssignableFrom(cls)){
			Calendar c = Calendar.getInstance();
			c.setTime(format.parse(value.toString()));
			return cls.cast(c);
		}
		return super.postparse(context, value, cls, type);
	}

	private static String getKeys(Context c){
		StringBuilder b = new StringBuilder();
		for(int i = 1; i <= c.getLevel(); i++){
			b.append('$').append(c.getKey(i));
		}
		return b.toString();
	}

	private File baseDir;
	private String prefix;
	private DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS z");
}
