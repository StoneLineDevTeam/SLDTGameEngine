package net.sldt_team.gameEngine.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtilities {
    public static List<String> stringWarp(String string, int maxChar) {

        List<String> subLines = new ArrayList<String>();

        int length = string.length();
        int start = 0;
        int end = maxChar;
        if (length > maxChar) {

            int noOfLines = (length / maxChar) + 1;

            int endOfStr[] = new int[noOfLines];

            for (int f = 0; f < noOfLines - 1; f++) {

                int end1 = maxChar;

                endOfStr[f] = end;

                if (string.charAt(end - 1) != ' ') {

                    if (string.charAt(end - 2) == ' ') {

                        subLines.add(string.substring(start, end - 1));
                        start = end - 1;
                        end = end - 1 + end1;

                    } else if (string.charAt(end - 2) != ' '
                            && string.charAt(end) == ' ') {

                        subLines.add(string.substring(start, end));
                        start = end;
                        end = end + end1;

                    } else if (string.charAt(end - 2) != ' ') {

                        subLines.add(string.substring(start, end) + "-");
                        start = end;
                        end = end + end1;

                    } else if (string.charAt(end + 2) == ' ') {
                        int lastSpaceIndex = string.substring(start, end)
                                .lastIndexOf("");
                        subLines.add(string.substring(start, lastSpaceIndex));

                        start = lastSpaceIndex;
                        end = lastSpaceIndex + end1;
                    }

                } else {

                    subLines.add(string.substring(start, end));
                    start = end;
                    end = end + end1;
                }

            }

            subLines.add(string.substring(endOfStr[noOfLines - 2], length));

        }

        return subLines;
    }
}
