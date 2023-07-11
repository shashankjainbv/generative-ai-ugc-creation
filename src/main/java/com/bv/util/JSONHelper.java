package com.bv.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.testng.Assert;
import org.testng.Reporter;

public class JSONHelper {

    /**
     * Parse the JSON formatted input string
     * @param str The input string in JSON format
     * @return JSONObject
     */
    public static JSONObject parseJSON(String str) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(str);

        } catch (JSONException ex) {
         //   Assert.fail("failed to parse JSON input...\n" + str);
        }
        return obj;
    }

    /**
     * Parse the XML formatted input string
     * @param str The input string in XML format
     * @return JSONObject
     */
    private static JSONObject sharedParseXML(String str, String apiResponse) {
        JSONObject obj = null;
        try {
            obj = XML.toJSONObject(str).getJSONObject(apiResponse);
        } catch (JSONException ex) {
            Assert.fail("failed to parse JSON input");
        }
        return obj;
    }

    public static JSONObject parseXML(String str) {
        return sharedParseXML(str, "DataApiResponse");
    }

    public static JSONObject parseBatchXML(String str) {
        return sharedParseXML(str, "BatchApiResponse");
    }

    /**
     * Get the value of the AuthorSubmissionToken from the
     * submission response
     */
    public static String getAuthorSubmissionToken(JSONObject obj) {
        return obj.optString("AuthorSubmissionToken");
    }

    /**
     * Get the value of "HasErrors" from the JSON object
     * @param obj The JSONObject
     * @return The value of the HasErrors field
     */
    public static boolean checkHasErrors(JSONObject obj) {
        boolean found = true;
        try {
            found = obj.getBoolean("HasErrors");
            if (found) {
                Reporter.log("\nErrors...");
                JSONArray errorsArray = obj.getJSONArray("Errors");
                for (int i = 0; i < errorsArray.length(); i++) {
                    JSONObject error = errorsArray.getJSONObject(i);
                    Reporter.log("Message: " + error.getString("Message"));
                    Reporter.log("Code   : " + error.getString("Code"));
                }
                Reporter.log("");
            }
        } catch (JSONException ex) {
            Assert.fail("unexpected error checking HasErrors");
        }
        return found;
    }

    /**
     * Get the value of "HasErrors" from the JSON object.
     * This version is used for a JSONObject from converted XML.
     * @param obj The JSONObject
     * @return The value of the HasErrors field
     */
    public static boolean checkHasErrorsObject(JSONObject obj) {
        boolean found = true;
        try {
            found = obj.getBoolean("HasErrors");
            if (found) {
                Reporter.log("\nErrors...");
                JSONObject errorsObject = obj.getJSONObject("Errors");
                if (errorsObject != null) {
                    JSONObject error = errorsObject.getJSONObject("Error");
                    Reporter.log("Message: " + error.getString("message"));
                    Reporter.log("Code   : " + error.getString("code"));
                }
                Reporter.log("");
            }
        } catch (JSONException ex) {
            Assert.fail("unexpected error checking HasErrors");
        }
        return found;
    }

    /**
     * Gets the first errors message from "HasErrors" in the JSON object.
     * This version is used for a JSONObject from converted XML.
     * @param obj The JSONObject
     * @return The value of the message, or empty string if none
     */
    public static String getErrorMessage(JSONObject obj) {
        try {
            boolean found = obj.getBoolean("HasErrors");
            if (found) {
                JSONObject errorsObject = obj.getJSONObject("Errors");
                if (errorsObject != null) {
                    JSONObject error = errorsObject.getJSONObject("Error");
                    return error.getString("message");
                }
            }
        } catch (JSONException ex) {
            Assert.fail("unexpected error checking HasErrors");
        }
        return "";
    }


    /**
     * Search for a specific error code in the set of errors in the JSONObject
     * @param obj The input JSONObject
     * @param code The error code string to search for
     * @return Returns true if found, otherwise returns false
     */
    public static boolean findErrorCode(JSONObject obj, String code) {
        boolean result = false;
        if (obj.isNull("Errors") == false) {
            try {
                JSONArray errors = obj.getJSONArray("Errors");
                for (int i = 0; i < errors.length(); i++) {
                    JSONObject error = errors.getJSONObject(i);
                    if (error.getString("Code").equalsIgnoreCase(code)) {
                        result = true;
                        break;
                    }
                }
            } catch (JSONException ex) {
                Assert.fail("unexpected error finding error message");
            }
        }
        return result;
    }

    /**
     * Search for a specific error code in the set of errors in the JSONObject.
     * This version is used for a JSONObject from converted XML.
     * @param obj The input JSONObject
     * @param code The error code string to search for
     * @return Returns true if found, otherwise returns false
     */
    public static boolean findErrorCodeObject(JSONObject obj, String code) {
        boolean result = false;
        if (obj.isNull("Errors") == false) {
            try {
                JSONObject errors = obj.getJSONObject("Errors");
                JSONObject error = errors.getJSONObject("Error");
                if (error.getString("code").equalsIgnoreCase(code)) {
                    result = true;
                }
            } catch (JSONException ex) {
                Assert.fail("unexpected error finding error message");
            }
        }
        return result;
    }

    /**
     * Search for a specific error code in in the response from a content submission.
     * @param obj The input JSONObject
     * @param title The error code string to search for
     * @return The value for the specified error code field
     */
    public static String findErrorCodeInFieldErrors(JSONObject obj, String title) {
        String result = null;
        if (obj.isNull("FormErrors") == false) {
            try {
                JSONObject fieldErrors = obj.getJSONObject("FormErrors").getJSONObject("FieldErrors");
                if (fieldErrors.has(title)) {
                    result = fieldErrors.getJSONObject(title).getString("Code");
                }
            } catch (JSONException ex) {
                Assert.fail("unexpected error finding error message");
            }
        }
        return result;
    }

    /**
     * Get a JSONObject from within a JSONObject. Throws a TestNG assertion on failure.
     * @param obj JSONObject
     * @param str The name of the JSONObject to get
     * @return The JSONObject
     */
    public static JSONObject getJSONObject(JSONObject obj, String str) {
        JSONObject result = null;
        if (obj.isNull(str) == false) {
            try {
                result = obj.getJSONObject(str);
            } catch (JSONException ex) {
                Assert.fail("unexpected error getting JSONObject named: " + str);
            }
        }
        return result;
    }

    /**
     * Get the value of a boolean field from within a JSONObject. Throws a TestNG assertion on failure.
     * @param obj JSONObject
     * @param str The name of the boolean field
     * @return The value of the boolean field
     */
    public static boolean getBoolean(JSONObject obj, String str) {
        boolean result = false;
        if (obj.isNull(str) == false) {
            try {
                result = obj.getBoolean(str);
            } catch (JSONException ex) {
                Assert.fail("unexpected error getting boolean value: " + str);
            }
        }
        return result;
    }

    /**
     * Get the value of a integer field from within a JSONObject. Throws a TestNG assertion on failure.
     * @param obj JSONObject
     * @param str The name of the integer field
     * @return The value of the integer field
     */
    public static int getInt(JSONObject obj, String str) {
        int result = -1;
        if (obj.isNull(str) == false) {
            try {
                result = obj.getInt(str);
            } catch (JSONException ex) {
                Assert.fail("unexpected error getting integer value: " + str);
            }
        }
        return result;
    }

    /**
     * Get the value of a double field from within a JSONObject. Throws a TestNG assertion on failure.
     * @param obj JSONObject
     * @param str The name of the double field
     * @return The value of the double field
     */
    public static double getDouble(JSONObject obj, String str) {
        double result = -1.0;
        if (obj.isNull(str) == false) {
            try {
                result = obj.getDouble(str);
            } catch (JSONException ex) {
                Assert.fail("unexpected error getting double value: " + str);
            }
        }
        return result;
    }

    /**
     * Get the value of a string field from within a JSONObject. Throws a TestNG assertion on failure.
     * @param obj JSONObject
     * @param str The name of the string field
     * @return The value of the string field
     */
    public static String getString(JSONObject obj, String str) {
        String result = null;
        if (obj.isNull(str) == false) {
            try {
                result = obj.getString(str);
            } catch (JSONException ex) {
                Assert.fail("unexpected error getting string value: " + str);
            }
        }
        return result;
    }

    /**
     * Get the value of a JSONArray from within a JSONObject. Throws a TestNG assertion on failure.
     * @param obj JSONObject
     * @param str The name of the JSONArray
     * @return The value of the JSONArray
     */
    public static JSONArray getArray(JSONObject obj, String str) {
        JSONArray result = null;
        if (obj.isNull(str) == false) {
            try {
                result = obj.getJSONArray(str);
            } catch (JSONException ex) {
                Assert.fail("unexpected error getting array: " + str);
            }
        }
        return result;
    }

    /**
     * Get the JSONObject from a JSONArray at the specified index.  Throws a TestNG assertion on failure.
     * @param array JSONArray
     * @param index The index of the desired JSONObject
     * @return The JSONObject
     */
    public static JSONObject getJSONObjectAtIndex(JSONArray array, int index) {
        JSONObject result = null;
        try {
            result = array.getJSONObject(index);
        } catch (JSONException ex) {
            Assert.fail("cannot find JSONObject at index = " + index);
        }
        return result;
    }

    /**
     * Get the String from a JSONArray at the specified index.  Throws a TestNG assertion on failure.
     * @param array JSONArray
     * @param index The index of the desired JSONObject
     * @return The String
     */
    public static String getStringAtIndex(JSONArray array, int index) {
        String result = null;
        try {
            result = array.getString(index);
        } catch (JSONException ex) {
            Assert.fail("cannot find String at index = " + index);
        }
        return result;
    }
}
