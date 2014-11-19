/*
 * $Id: ProcessFacade.java 507 2012-05-24 04:34:29Z t-nakaguchi $
 *
 * This is a program to wrap language resources as Web services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 2.1 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.wrapper.common.process;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 507 $
 */
public class ProcessFacade {
	/**
	 * コンストラクタ。
	 * @param args 実行するコマンド文字列
	 */
	public ProcessFacade(File directory, String... args){
		builder = new ProcessBuilder(args);
		builder.directory(directory);
	}

	/**
	 * コンストラクタ。
	 * @param args 実行するコマンド文字列
	 */
	public ProcessFacade(String... args){
		builder = new ProcessBuilder(args);
	}

	/**
	 * コンストラクタ。
	 */
	public ProcessFacade(){
		builder = new ProcessBuilder();
	}

	/**
	 * プロセスビルダを返す。
	 * @return プロセスビルダ
	 */
	public ProcessBuilder builder(){
		return builder;
	}

	/**
	 * プロセスを開始する。
	 * @throws IOException プロセスの開始に失敗した
	 */
	public void start()
	throws IOException{
		process = builder.start();
	}

	/**
	 * 入力ストリームを取得する。
	 * プロセスが開始されていない場合、IllegalStateException(非チェック例外)が発生する。
	 * @return 入力ストリーム
	 * @throws IllegalStateException プロセスが開始されていない
	 */
	public InputStream getInputStream()
	throws IllegalStateException
	{
		checkProcessValid();
		if(inputStream == null){
			inputStream = process.getInputStream();
		}
		return inputStream;
	}

	/**
	 * 入力ストリームを取得する。
	 * プロセスが開始されていない場合、IllegalStateException(非チェック例外)が発生する。
	 * @return 入力ストリーム
	 * @throws IllegalStateException プロセスが開始されていない
	 */
	public InputStream getErrorStream()
	throws IllegalStateException
	{
		checkProcessValid();
		if(errorStream == null){
			errorStream = process.getErrorStream();
		}
		return errorStream;
	}

	/**
	 * 出力ストリームを取得する。
	 * プロセスが開始されていない場合、IllegalStateException(非チェック例外)が発生する。
	 * @return 出力ストリーム
	 * @throws IllegalStateException プロセスが開始されていない
	 */
	public OutputStream getOutputStream(){
		checkProcessValid();
		if(outputStream == null){
			outputStream = process.getOutputStream();
		}
		return outputStream;
	}

	/**
	 * 出力ストリーム、入力ストリームの順でcloseメソッドを呼び出す。
	 * 出力ストリームのcloseでIOExceptionが生じても、入力ストリームのcloseを呼び出す。
	 * @throws IOException ストリームのclose呼び出しに失敗した
	 */
	public void closeStreams()
	throws IOException
	{
		IOException ex = null;
		if(outputStream != null){
			try{
				outputStream.close();
			} catch(IOException e){
				ex = e;
			}
		}
		if(inputStream != null){
			try{
				inputStream.close();
			} catch(IOException e){
				if(ex != null){
					logger.log(Level.WARNING
							, "failed to close both output and input stream"
							+ " (log exception for output stream"
							+ " and throw exception for input stream)"
							, ex
							);
				}
				ex = e;
			}
		}
		if(errorStream != null){
			try{
				errorStream.close();
			} catch(IOException e){
				if(ex != null){
					logger.log(Level.WARNING
							, "failed to close both output and input stream"
							+ " (log exception for output stream"
							+ " and throw exception for input stream)"
							, ex
							);
				}
				ex = e;
			}
		}
		if(ex != null) throw ex;
	}

	/**
	 * 起動したプロセスの終了を待機する。
	 * プロセスが開始されていない場合は何もしない。
	 * @throws InterruptedException 待機操作が中断された
	 */
	public void waitFor()
	throws InterruptedException
	{
		if(process == null) return;
		process.waitFor();
	}

	public int exitValue(){
		return process.exitValue();
	}

	/**
	 * 起動したプロセスの終了を待機する。
	 * プロセスが開始されていない場合は何もしない。
	 * @param timeoutMillis 待機するミリ秒数
	 * @throws InterruptedException 待機操作が中断された
	 * @throws TimeoutException 待機時間を経過してもプロセスが終了しなかった
	 */
	public void waitFor(int timeoutMillis)
	throws InterruptedException, TimeoutException
	{
		if(process == null) return;
		Thread t = new Thread(new Runnable(){
			public void run() {
				try{
					process.waitFor();
				} catch(InterruptedException e){
				}
			}
		});
		try{
			t.start();
			t.join(timeoutMillis);
			if(t.isAlive()){
				t.interrupt();
				throw new TimeoutException();
			}
		} finally{
			if(t.isAlive()){
				t.interrupt();
			}
		}
	}

	/**
	 * リソースを解放しプロセスを終了させる(終了を待機する)。
	 * プロセスが開始されていない場合は何もしない。
	 * @throws InterruptedException 待機操作が中断された
	 * @throws IOException ストリームのクローズに失敗した
	 */
	public void close()
	throws InterruptedException, IOException{
		if(process == null) return;
		try{
			closeStreams();
			process.waitFor();
		} finally{
			process.destroy();
		}
	}

	/**
	 * リソースを解放しプロセスを終了させる(終了を待機する)。
	 * プロセスが開始されていない場合は何もしない。
	 * @param timeoutMillis 待機するミリ秒数
	 * @throws InterruptedException 待機操作が中断された
	 * @throws IOException ストリームのクローズに失敗した
	 * @throws TimeoutException 待機時間を経過してもプロセスが終了しなかった
	 */
	public void close(int timeoutMillis)
	throws InterruptedException, IOException, TimeoutException{
		if(process == null) return;
		try{
			closeStreams();
			waitFor(timeoutMillis);
		} finally{
			process.destroy();
		}
	}

	private void checkProcessValid()
	throws IllegalStateException{
		if(process == null){
			throw new IllegalStateException(
					"Process already closed or not started.");
		}
	}

	private ProcessBuilder builder;
	private Process process;
	private InputStream inputStream;
	private InputStream errorStream;
	private OutputStream outputStream;

	private static Logger logger = Logger.getLogger(
			ProcessFacade.class.getName());
}
