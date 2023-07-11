package com.bv.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility methods for request generation.
 */
public class URLGenerationUtils {


    /********************************************************************************************
     * Encoding methods
     *********************************************************************************************/

    /**
     * URL Encoding method that only encodes the key/value pairs of the query string.
     * @param url The URL to be encoded
     * @return The encoded URL
     * @throws UnsupportedEncodingException
     */
    public static String encodeURL(String url) throws UnsupportedEncodingException {
        String[] urlParts = url.split("\\?", 2);
        StringBuilder builder = new StringBuilder(urlParts[0]);
        if (urlParts.length > 1) {
            builder.append("?");
            for (String param : urlParts[1].split("&")) {
                String pair[] = param.split("=");
                String key = URLEncoder.encode(pair[0], "UTF-8");
                String value = "";
                if (pair.length > 1) {
                    value = URLEncoder.encode(pair[1], "UTF-8");
                }
                if (builder.toString().endsWith("?")) {
                    builder.append(key + "=" + value);
                }
                else {
                    builder.append("&" + key + "=" + value);
                }
            }
        }
        return builder.toString();
    }

    /********************************************************************************************
     * The following methods are used to construct an encoded user token...
     *********************************************************************************************/

    public static String getUserToken(String userId, String encodingKey) {
        String userStr = "date=" + getDate() + "&userid=" + userId;

        return md5(encodingKey + userStr) + asciiToHex(userStr);
    }

    public static String getUserToken(Map<String, String> tokenMap, String encodingKey) {
        StringBuilder userStr = new StringBuilder();
        for (Map.Entry<String, String> token : tokenMap.entrySet()) {
            userStr.append(token.getKey()).append("=").append(token.getValue());
        }
        return md5(encodingKey + userStr) + asciiToHex(userStr.toString());
    }

    public static Map<String,String> decodeUserToken(String userToken) {
        String decoded = hexToAscii(userToken.substring(32));
        System.out.println("decoded user token = " + decoded);
        Map<String,String> nameValues = new HashMap<String, String>();
        String[] pairs = decoded.split("&");
        for (String pair : pairs) {
            String[] nameValue = pair.split("=");
            nameValues.put(nameValue[0], nameValue[1]);
        }
        return nameValues;
    }

    public static String getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(calendar.getTime());
    }

    //TO DO: md5 is out of date; we should be using SHA256
    private static String md5(String in) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  // To change body of catch statement use File | Settings | File Templates.
        }
        byte[] md5Digest = md.digest(in.getBytes());
        BigInteger md5Number = new BigInteger(1, md5Digest);
        StringBuilder resultBuilder = new StringBuilder(md5Number.toString(16));

        // This md5 digest must be 32 characters long, so make sure we pad the left end with zeros.
        while (resultBuilder.length() < 32) {
            resultBuilder.insert(0, "0");
        }

        return resultBuilder.toString();
    }

    private static String asciiToHex(String sourceText) {
        byte[] rawData = sourceText.getBytes();
        StringBuffer hexText = new StringBuffer();
        String initialHex = null;
        int initHexLength = 0;
        for (int i = 0; i < rawData.length; i++) {
            int positiveValue = rawData[i] & 0x000000FF;
            initialHex = Integer.toHexString(positiveValue);
            initHexLength = initialHex.length();
            while (initHexLength++ < 2) {
                hexText.append("0");
            }
            hexText.append(initialHex);
        }
        return hexText.toString();
    }

    private static String hexToAscii(String hex) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i < hex.length(); i+=2) {
            String output = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(output, 16);
            sb.append((char)decimal);
        }
        return sb.toString();
    }

}
