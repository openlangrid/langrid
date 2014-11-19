package jp.go.nict.langrid.commons.beanutils;

import java.lang.reflect.Array;

import jp.go.nict.langrid.commons.util.LapTimer;
import jp.go.nict.langrid.commons.util.function.Consumer;

public class ArrayAccessPerformance {
	public static void main(String[] args) throws Exception{
		LapTimer lt = new LapTimer();
		Integer[] array = {1, 2, 3};
		Consumer<Long> print = new Consumer<Long>() {
			public void accept(Long value) {
				System.out.println(String.format("%d millis.", value));
			}
		};
		int n = array.length;
		@SuppressWarnings("unused")
		int sum = 0;
		lt.lapMillis();
		for(int k = 0; k < 10000000; k++){
			for(int i = 0; i < n; i++){
				sum += array[i];
			}
		}
		lt.lapMillis(print);
		for(int k = 0; k < 10000000; k++){
			for(int i = 0; i < n; i++){
				sum += (Integer)Array.get(array, i);
			}
		}
		lt.lapMillis(print);
	}
}
