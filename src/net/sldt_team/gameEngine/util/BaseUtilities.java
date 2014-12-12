package net.sldt_team.gameEngine.util;

public class BaseUtilities {

    public static final int BASE_2_ENCODING_DIVISOR = 2;

    public static String encodeNumberIntoMachineCode(int numberToEncode){
        int quotient = numberToEncode;
        String encodedNumber = "";
        while (quotient > 0){
            int rest = quotient % BASE_2_ENCODING_DIVISOR;
            quotient = MathUtilities.getIntegerPart(quotient / BASE_2_ENCODING_DIVISOR);
            encodedNumber += rest;
        }
        return StringUtilities.stringReverse(encodedNumber);
    }
}
