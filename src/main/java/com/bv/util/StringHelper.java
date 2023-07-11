package com.bv.util;

import org.apache.commons.lang3.RandomStringUtils;

public class StringHelper {

    private static java.util.Random _random = new java.util.Random();

    /**
     * Gets a random length string
     * @param count The maximum permissible length of the string
     * @return The string
     */
    public static String randomString(int count) {
        return RandomStringUtils.randomAlphanumeric(count);
    }

    /**
     * Gets <b>count</b> number of random length words.
     * @param count The number of words desired
     * @return The words separated by a space character
     */
    public static String randomWords(int count) {
        String result = "";
        for (int i=0; i<count; i++) {
            int wordsize = _random.nextInt(10) + 1;
            result += RandomStringUtils.randomAlphanumeric(wordsize) + " ";
        }
        return result.trim();
    }

    /**
     * Get a paragraph of random words where the total length of the paragraph is maxLength or less.
     * @param maxLength The maximum permissible length of the paragraph
     * @return The paragraph as a String
     */
    public static String randomParagraph(int maxLength) {
        String result = "";
        int finalLength = _random.nextInt(maxLength);
        while (true) {
            if (!result.isEmpty()) result += " ";
            result += RandomStringUtils.randomAlphanumeric(_random.nextInt(6) + 1);
            if (result.length() > finalLength) break;
        }
        return result.substring(0, finalLength);
    }

    /**
     * Get a paragraph of random words where the total length of the paragraph (in characters) is
     * at least minLength and less than maxLength.
     * @param prefix String that paragraph begins with
     * @param minLength The minimum permissible length of the paragraph
     * @param maxLength The maximum permissible length of the paragraph
     * @return The paragraph as a String
     */
    public static String randomParagraph(String prefix, int minLength, int maxLength) {
        String result = "";
        if (prefix != null) result = prefix;
        int finalLength = _random.nextInt(maxLength);
        if (finalLength < minLength) finalLength = minLength;   // floor
        while (true) {
            if (!result.isEmpty()) result += " ";
            result += RandomStringUtils.randomAlphanumeric(_random.nextInt(6) + 1);
            if (result.length() > finalLength) break;
        }
        return result.substring(0, finalLength);
    }
}
