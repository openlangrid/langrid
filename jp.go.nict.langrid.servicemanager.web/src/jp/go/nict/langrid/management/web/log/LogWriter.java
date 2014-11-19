package jp.go.nict.langrid.management.web.log;

import jp.go.nict.langrid.management.web.view.ServiceManagerApplication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class LogWriter{
	/**
	 * 
	 * 
	 */
	public static void writeError(String userId, Exception e, Class loggingClass){
		writeError(userId, e, loggingClass, "");
	}	
	/**
	 * 
	 * 
	 */
	public static void writeError(
			String userId, Exception e, Class loggingClass, String message, String... params)
	{
		writeError(systemLog, userId, e, loggingClass, message, params);
	}
	
	/**
	 * Langridに関するエラーレベルのログを出力する
	 * @param userId ログを出力するときのユーザID
	 * @param e 発生した例外オブジェクト
	 * @param loggingClass ログを実行する
	 * @param params 追加で出力するメッセージの配列 ex...["TestWrite:TestMessage"]
	 */
	public static void writeErrorFromLangrid(
			String userId, Exception e, Class loggingClass, String... params)
	{
		writeError(userId, e, loggingClass, "Service Manager can't complete Langrid operation. ", params);
	}

	/**
	 * 
	 * 
	 */
	public static void writeInfo(Log writeLog, String userId, String message
			, Class loggingClass, String[] params)
	{
		if(writeLog.isInfoEnabled()){
			StringBuilder sb = new StringBuilder();
			for(String param : params){
				sb.append(",");
				sb.append(param);
			}
			writeLog.info(message
					+ " [" + makeTreadIdLog()
					+ "," + makeUserIdLog(userId == null ? DefaultUserId : userId)
					+ "," + makeClassNameLog(loggingClass)
					+ sb.toString() + "]");
		}
	}

	/**
	 * インフォメーションレベルのログを出力する
	 * @param userId ログを出力する時のユーザID
	 * @param message ログメッセージ
	 * @param loggingClass ログを実行するクラス
	 * @param params 追加で出力するメッセージの配列 ex...["TestWrite:TestMessage"]
	 */
	public static void writeInfo(
			String userId, String message, Class loggingClass, String... params)
	{
		writeInfo(systemLog, userId, message, loggingClass, params);
	}

	/**
	 * 
	 * 
	 */
	public static void writeOutOfSystemError(
			String userId, Exception e, Class loggingClass, String message, String... params)
	{
		writeError(systemLog, userId, e, loggingClass, message, params);
	}

	/**
	 * ワーニングレベルのログを出力する
	 * @param userId ログを出力する時のユーザID
	 * @param message ログメッセージ
	 * @param loggingClass ログを実行する
	 * @param params 追加で出力するメッセージの配列 ex...["TestWrite:TestMessage"]
	 */
	public static void writeWarn(
			String userId, String message, Class loggingClass, String... params)
	{
		writeWarn(systemLog, userId, message, loggingClass, params);
	}

	private static String makeClassNameLog(Class loggingClass){
		return "ClassName:" + loggingClass.getSimpleName();
	}

	private static String makeTreadIdLog(){
		return "TreadID:" + Thread.currentThread().getId();
	}
	
	private static String makeUserIdLog(String userId){
		return "UserID:" + userId;
	}

	private static void writeError(Log writeLog, String userId, Exception e, Class loggingClass
			, String message, String[] params)
	{
		if(writeLog.isErrorEnabled()){
			StringBuffer sb = new StringBuffer();
			for(String param : params){
				sb.append(",");
				sb.append(param);
			}
			writeLog.error(message
					+ "[" + makeTreadIdLog()
					+ "," + makeUserIdLog(userId == null ? DefaultUserId : userId)
					+ "," + makeClassNameLog(loggingClass)
					+ sb.toString()
					+ "]"
					, e);
		}
	}

	private static void writeWarn(Log writeLog,
			String userId, String message, Class loggingClass, String[] params)
	{
		if(writeLog.isWarnEnabled()){
			StringBuilder sb = new StringBuilder();
			for(String param : params){
				sb.append(",");
				sb.append(param);
			}
			writeLog.warn(message
					+ " [" + makeTreadIdLog()
					+ "," + makeUserIdLog(userId == null ? DefaultUserId : userId)
					+ "," + makeClassNameLog(loggingClass)
					+ sb.toString() + "]");
		}
	}

	private static final String DefaultUserId = "System";
	private static final Log systemLog = LogFactory.getLog(ServiceManagerApplication.class);
}
