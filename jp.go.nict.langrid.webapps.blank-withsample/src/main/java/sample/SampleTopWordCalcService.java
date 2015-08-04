package sample;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SampleTopWordCalcService implements TopWordCalcService{
	@Override
	public String calculate(String text) {
		int topCount = 0;
		String topCountWord = "";
		Map<String, Integer> counts = new HashMap<String, Integer>();
		Scanner s = new Scanner(text);
		while(s.hasNext()){
			String w = s.next();
			Integer c = counts.get(w);
			if(c == null){
				c = 0;
			}
			c = c + 1;
			if(topCount < c){
				topCount = c;
				topCountWord = w;
			}
			counts.put(w, c);
		}
		return topCountWord;
	}
}
