package net.sldt_team.gameEngine.util.misc;

import java.util.ArrayList;
import java.util.List;

public class StringUtilities {

    /**
     * Devides a long string into several other strings relative to max chars per lines (work as MS-Word word warp)
     */
    public static List<String> warp(String s, int maxChar) {
        String result = "";
        List<String> subLines = new ArrayList<String>();
        int lastdelimPos = 0;
        for (String token : s.split(" ", -1)) {
            if (result.length() - lastdelimPos + token.length() > maxChar) {
                result = result + "\n" + token;
                lastdelimPos = result.length() + 1;
            }
            else {
                result += (result.isEmpty() ? "" : " ") + token;
            }
        }

        for (String s1 : result.split("\n")){
            subLines.add(s1);
        }

        return subLines;
    }

    /**
     * Reverse a string
     */
    public static String reverse(String source) {
        int i, len = source.length();
        StringBuilder dest = new StringBuilder(len);

        for (i = (len - 1); i >= 0; i--){
            dest.append(source.charAt(i));
        }

        return dest.toString();
    }

    /**
     * Removes string last char
     */
    public static String removeLastChar(String s){
        return s.substring(0, s.length() - 1);
    }

    /**
     * Removes string first char
     */
    public static String removeFirstChar(String s){
        return s.substring(1);
    }
}
