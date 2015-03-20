package jp.go.nict.langrid.custominvoke.workflowsupport.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageDecision {
    public static void main(String[] args) {
        String text = "hoge";
//        Pattern p = Pattern.compile(
//            "(\\p{InBasicLatin}+|" +
//            " \\p{InHiragana}+|" +
//            " \\p{InKatakana}+|" +
//            " \\p{InCJKUnifiedIdeographs}+)", Pattern.COMMENTS);
        Pattern p = Pattern.compile("(\\p{InBasicLatin}+)", 
        		Pattern.COMMENTS);
        Matcher m = p.matcher(text);
        System.out.println(m.matches());
        while (m.find()) {
            String chunk = m.group(1);
            System.out.println(chunk);
        }
    }
    /**
     * ラテン文字かどうかを判断し、返します。
     * @param str
     * @return
     */
    public static boolean isBasicLatin(String str) {
        Pattern p = Pattern.compile("(\\p{InBasicLatin}+)", Pattern.COMMENTS);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}