package net.sldt_team.gameEngine.format.datafile;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.util.MathUtilities;
import net.sldt_team.gameEngine.exception.GameException;
import net.sldt_team.gameEngine.exception.code.ErrorCode002;

import java.util.Random;
import java.util.StringTokenizer;

/**
 * @exclude
 */
public class GameDataFileEncoder {
    private int[] currentEncodingKey;
    private boolean needToGenerateNewKey;

    public GameDataFileEncoder(boolean needNewKey) {
        currentEncodingKey = new int[16]; // 16-char encoding key
        needToGenerateNewKey = false;
        if (needNewKey) {
            generateNewEncodingKey();
            needToGenerateNewKey = true;
        }
    }

    /**
     * Generates a new random key for starting encoding/decoding
     */
    private void generateNewEncodingKey() {
        Random r = new Random();
        for (int i = 0; i < 16; i++) {
            int j = MathUtilities.generateRandomInteger(-1, 10, r);
            if (j <= 9 && j >= 0) {
                currentEncodingKey[i] = j;
            } else {
                i--;
            }
        }
    }

    /**
     * Returns the current encoding key (used when let the program auto-generate the key)
     */
    public String getCurrentEncodingKey() {
        String result = "";
        for (int i = 0; i < currentEncodingKey.length; i++) {
            int var1 = currentEncodingKey[i];
            if (i != 16) {
                String currentChar = var1 + "-";
                result += currentChar;
            } else {
                result += var1;
            }
        }
        return result;
    }

    /**
     * Sets the encoding key of the current Security System from a simple string
     */
    public void loadEncodingKeyFromString(String encodingKey) {
        if ((encodingKey.length() / 2) != 16) {
            GameApplication.log.severe("FATAL ERROR : Encoding key need to make 16 characters ; not : " + encodingKey.length() / 2);
            GameApplication.log.severe("IF_FAILED");
            throw new GameException(new ErrorCode002());
        }
        GameApplication.log.info("IF_JUMPED");
        if (!needToGenerateNewKey) {
            int i = 0;
            StringTokenizer tokenizer = new StringTokenizer(encodingKey, "-");
            while (tokenizer.hasMoreTokens()) {
                String s = tokenizer.nextToken();
                if (isNumeric(s)) {
                    currentEncodingKey[i] = Integer.valueOf(s);
                }
                i++;
            }
        } else {
            GameApplication.log.warning("WARNING : Security System is in gen mode. Security System can't load key in the current Mode.");
            GameApplication.log.severe("FATAL ERROR : Unable to load key");
            throw new GameException(new ErrorCode002());
        }
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    private boolean containsKeyChar(int par1) {
        for (int i : currentEncodingKey) {
            if (i == par1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the encoded form of a string
     */
    public String encodeString(String toEncode) {
        String codedPass = "";
        int l = -1;
        for (int i = 0; i < toEncode.length(); i++) {
            l++;
            if (l > 15) {
                l = 0;
            }
            int c = toEncode.charAt(i) << currentEncodingKey[l];
            codedPass += (char) c;
        }
        return codedPass;
    }

    /**
     * Returns the decoded form of a string
     */
    public String decodeString(String toDecode) {
        String decodedPass = "";
        int l = -1;
        for (int i = 0; i < toDecode.length(); i++) {
            l++;
            if (l > 15) {
                l = 0;
            }
            int c = toDecode.charAt(i) >> currentEncodingKey[l];
            decodedPass += (char) c;
        }
        return decodedPass;
    }
}