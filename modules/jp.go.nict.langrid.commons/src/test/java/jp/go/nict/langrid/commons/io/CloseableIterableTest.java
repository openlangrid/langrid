package jp.go.nict.langrid.commons.io;


public class CloseableIterableTest {
/* temporary disabled
	@Test
	public void test() throws Exception{
		closeCalled = false;
		try(CloseableIterator<String> it = get()){
			while(it.hasNext()){
				Assert.assertEquals("hello", it.next());
				break;
			}
		}
		Assert.assertTrue(closeCalled);
	}

	private CloseableIterator<String> get(){
		Iterator<String> it = Arrays.asList("hello", "world").iterator();
		return new CloseableIteratorAdapter<String>(it){
			public void close() throws IOException{
				closeCalled = true;
			}
		};
	}

	private boolean closeCalled;
*/
}
