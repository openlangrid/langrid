package jp.go.nict.langrid.dao.archive;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilenameUtil {
	public static String getIdFromFileName(File f){
		Matcher m = filenameToIdPattern.matcher(f.getName());
		m.matches();
		return m.group(1);
	}

	public static String getIdFromServiceFileName(File f){
		Matcher m = serviceFilenameToIdPattern.matcher(f.getName());
		m.matches();
		return m.group(1);
	}

	private static Pattern filenameToIdPattern = Pattern.compile("(.*)\\.json$");
	private static Pattern serviceFilenameToIdPattern = Pattern.compile("[ebw]s_(.*)\\.json$");
}
