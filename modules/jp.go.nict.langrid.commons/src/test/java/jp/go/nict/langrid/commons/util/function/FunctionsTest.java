package jp.go.nict.langrid.commons.util.function;

public class FunctionsTest {
/*	@Test
	public void test_tunnelingExec() throws Throwable{
		try{
			Functions.tunnelingExecute(
					Arrays.asList(1, 2, 3)::forEach,
					new Consumer<Integer>() {
						@Override
						public void accept(Object value) {
							throw new IOException();
						}
					});
			Assert.fail();
		} catch(IOException e){
		}
	}


	@Test
	public void test_tunnelingExecute() throws Throwable{
		try{
			tunnelingExecute(
					Arrays.asList(1, 2, 3)::forEach,
					v -> {throw new IOException();}
					);
			Assert.fail();
		} catch(IOException e){
		}
	}

	@Test
	public void test_tunnelingExecute2() throws Throwable{
		new ExpStr<Integer>(Arrays.asList(1, 2, 3).stream())
			.then(c -> c::forEach, v -> {throw new IOException();})
			.fail(e -> {})
			;
	}
	
	public static class ExpStr<T>{
		public ExpStr(Stream<T> st){
			this.stream = st;
		}
		public <E extends Throwable> ExpStr<T> then(
				Function<Stream<T>, Consumer<Consumer<T>>> conscons,
				ConsumerWithException<T, E> cons){
			if(exception != null) return this;
			conscons.apply(stream).accept(p -> {
				try{
					cons.accept(p);
				} catch(RuntimeException | Error e){
					throw e;
				} catch(Throwable e){
					exception = e;
				}
			});
			return this;
		}
		public void fail(Consumer<Throwable> c){
			if(exception != null){
				c.accept(exception);
			}
		}
		private Stream<T> stream;
		private Throwable exception;
	}

	public static interface ConsumerWithException<T, E extends Throwable>{
		void accept(T value) throws E;
	}
	@SuppressWarnings("serial")
	public static class SoftenedException extends RuntimeException {
		public SoftenedException(Throwable cause) {
			super(cause);
		}
	}
	@SuppressWarnings("unchecked")
	public static <P, E extends Throwable> void tunnelingExecute(
			Consumer<Consumer<P>> term, ConsumerWithException<P, E> proc)
	throws E{
		try{
			term.accept(pr -> {
				try{
					proc.accept(pr);
				} catch(Error | RuntimeException e){
					throw e;
				} catch(Throwable e){
					throw new SoftenedException(e);
				}
			});
		} catch(SoftenedException e){
			throw (E)e.getCause();
		}
	}
*/
}
