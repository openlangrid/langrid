package crlf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Adjust {
	public static void main(String[] args) throws Exception{
		new Adjust().go(new File("src.stubs"));
	}

	private void go(File dir) throws Exception{
		for(File f : dir.listFiles()){
			if(f.isDirectory()){
				go(f);
			} else{
				adjust(f);
			}
		}
	}

	private void adjust(File file) throws Exception{
		if(file.toString().endsWith(".tmp")) return;
		InputStream is = new FileInputStream(file);
		File dest = new File(file.getAbsolutePath() + ".tmp");
		try{
			BufferedReader r = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			OutputStream os = new FileOutputStream(dest);
			try{
				BufferedWriter w = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
				String line = null;
				while((line = r.readLine()) != null){
					w.write(line);
					w.write("\r\n");
				}
				w.flush();
			} finally{
				os.close();
			}
		} finally{
			is.close();
		}
		file.delete();
		dest.renameTo(file.getAbsoluteFile());
	}
}
