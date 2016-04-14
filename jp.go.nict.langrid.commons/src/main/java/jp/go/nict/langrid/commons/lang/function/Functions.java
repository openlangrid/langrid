package jp.go.nict.langrid.commons.lang.function;

public class Functions {
	public interface RunnableE<E extends Exception>{
		void run() throws E;
	}
	public static <E extends Exception> Runnable soften(final RunnableE<E> r){
		return new Runnable(){
			@Override
			public void run() {
				try{
					r.run();
				} catch(Exception e){
					throw new SoftException(e);
				}
			}
		};
	}
}
