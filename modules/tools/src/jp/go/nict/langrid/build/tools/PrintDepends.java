package jp.go.nict.langrid.build.tools;

import java.io.File;

import jp.go.nict.langrid.commons.lang.block.BlockP;

/**
 * 依存プロジェクトを出力する。
 * 依存プロジェクトの列挙には、ListDependsクラスを使用する。
 * @author Takao Nakaguchi
 */
public class PrintDepends {
	public static void main(String[] args) throws Exception{
		if(args.length == 0){
			System.out.println("please specify target directory.");
			return;
		}
		System.out.println("print depends of " + args[0]);
		go(new File(args[0]));
	}

	public static void go(File base) throws Exception{
		ListProjectDepends.go(base, new BlockP<String>() {
			@Override
			public void execute(String p1) {
				System.out.println(p1);
			}
		});
	}
}
