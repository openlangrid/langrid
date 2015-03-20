package jp.go.nict.langrid.commons.lang.test;

public class AnonymousRunnableFactory{
	public static Runnable creatRunnable(){
		return new Runnable() {
			public void run() {
			}
		};
	}
}
