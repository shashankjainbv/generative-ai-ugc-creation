package com.bv.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public abstract class AbstractSubmitFormFiller {

    private String _textPrefix = "";
    private static Random _random = new Random();
    private List<NameValuePair> _formparams = new ArrayList<NameValuePair>();

    protected final static String DEFAULT_TEXT_PREFIX = "TEST API >> ";

    /**
     * Constructor - Creates name/value pairs for each of the fields discovered in the input Submission For object.
     * @param formObj Input submission form
     * @param requiredFieldsOnly If true, then only provide name/value pairs for Required fields
     * @param prefix String to prefix text input fields
     * @throws JSONException Exceptions thrown by the org.json package
     */
    protected AbstractSubmitFormFiller(JSONObject formObj, boolean requiredFieldsOnly, String prefix) throws JSONException {
        if (prefix != null) _textPrefix = prefix;
        JSONObject data = formObj.getJSONObject("Data");
        JSONObject groups = data.getJSONObject("Groups");
        JSONObject fields = data.getJSONObject("Fields");

        String formLocale = formObj.getString("Locale");
        if (!StringUtils.isEmpty(formLocale)) {
            setNameAndValue("Locale", formLocale);
        }

        // check for form object format (JSON or converted XML)
        if (fields.has("Field")) {
            // generate field values for converted XML submission form response
            JSONArray fieldArray = fields.getJSONArray("Field");
            for (int i=0; i<fieldArray.length(); i++) {
                JSONObject field = fieldArray.getJSONObject(i);
                String fieldName = field.getString("id");

                // skip if not required
                if (requiredFieldsOnly && !field.getBoolean("required")) continue;

                // generate
                String fieldValue = getValueForField(field);
                if (fieldValue != null  && !Objects.equals(fieldValue, "")) _formparams.add(new BasicNameValuePair(fieldName, fieldValue));
            }
        }
        else {
            // verify Required field is non-null in Groups
            Assert.assertTrue(data.has("GroupsOrder"), "cannot continue: GroupsOrder array is missing");
            JSONArray groupsOrder = data.getJSONArray("GroupsOrder");
            Assert.assertTrue(data.has("Groups"), "cannot continue: Groups object is missing");
            for (int i=0; i<groupsOrder.length(); i++) {
                String groupName = groupsOrder.getString(i);
                JSONObject group = groups.getJSONObject(groupName);
                String val = group.getString("Required");
                Assert.assertNotEquals(val, "null", "found unexpected null value");
            }

            // generate field values for JSON submission form response
            JSONArray fieldsOrder = data.getJSONArray("FieldsOrder");
            for (int i = 0; i < fieldsOrder.length(); i++) {
                String fieldName = fieldsOrder.getString(i);
                JSONObject field = fields.getJSONObject(fieldName);

                // skip if not required
                if (requiredFieldsOnly && !field.getBoolean("Required")) continue;

                // generate
                String fieldValue = getValueForField(field);
                if (fieldValue != null && !Objects.equals(fieldValue, "")) _formparams.add(new BasicNameValuePair(fieldName, fieldValue));
            }
        }
    }

    /**
     * Get a generated value for the specified field
     * @param field The specified field object
     * @return String value suitable for submission
     * @throws JSONException Thrown for JSON parsing errors
     */
    private String getValueForField(JSONObject field) throws JSONException {
        String fieldType = field.optString("Type");
        if (fieldType == null || fieldType.isEmpty()) fieldType = field.getString("type");
        String value = "";
        if (fieldType.equalsIgnoreCase("TextInput")) {
            value = getTextInput(field);
        }
        else if (fieldType.equalsIgnoreCase("TextAreaInput")) {
            value = getTextAreaInput(field);
        }
        else if (fieldType.equalsIgnoreCase("BooleanInput")) {
            value = getBooleanInput(field);
        }
        else if (fieldType.equalsIgnoreCase("IntegerInput")) {
            value = getIntegerInput(field);
        }
        else if (fieldType.equalsIgnoreCase("SelectInput")) {
            value = getSelectInput(field);
        }
        else {
            Assert.fail("unknown field type: " + fieldType);
        }
        return value;
    }

    protected abstract String getTextInput(JSONObject field) throws JSONException;

    protected String getTextInput(String id, int minLength, int maxLength) {
        TextInputGenerator textInputGenerator = new TextInputGenerator(id, minLength, maxLength, _textPrefix);
        return textInputGenerator.doGenerate().trim();
    }

    protected abstract String getTextAreaInput(JSONObject field) throws JSONException;

    protected String getTextAreaInput(String id, int minLength, int maxLength) {
        TextAreaInputGenerator textAreaInputGenerator = new TextAreaInputGenerator(id, minLength, maxLength, _textPrefix);
        return textAreaInputGenerator.doGenerate().trim();
    }

    /**
     * Generate a random value for BooleanInput fields
     * @param field The JSON object representing the field
     * @return Boolean value as a String
     */
    protected String getBooleanInput(JSONObject field) throws JSONException {
        Boolean value = true;
        if (getId(field).contains("tagid_")) {
            int selector = _random.nextInt(5);
            value = selector == 3 ? false : true;  // Tag fields are true more often
        }
        else {
            value = _random.nextBoolean();
        }
        return value.toString();
    }

    /**
     * Generate a random value for IntegerInput fields
     * @param field The JSON object representing the field
     * @return Integer value as a String
     */
    protected String getIntegerInput(JSONObject field) {
        Integer value = _random.nextInt(5) + 1;
        return value.toString();
    }

    /**
     * Select a random value for SelectInput fields using the Options in the JSON object
     * @param field The JSON object representing the field (ex: "contextdatavalue_age")
     * @return Selected value for the field as a String
     */
    protected String getSelectInput(JSONObject field) throws JSONException {
        String result = null;
        String id = field.optString("Id");
        if (id == null || id.isEmpty()) id = field.getString("id");
        Assert.assertTrue(field.has("Options"), "found SelectInput without options: " + id);
        // check for form object format (JSON or converted XML)
        if (field.has("type")) {
            JSONArray options = field.getJSONObject("Options").getJSONArray("Option");
            Assert.assertTrue(options.length() > 0, "found SelectInput with zero options: " + id);
            int index = _random.nextInt(options.length());
            String value = options.getJSONObject(index).getString("value");
            if (!value.isEmpty()) result = value;
        }
        else {
            JSONArray options = field.getJSONArray("Options");
            Assert.assertTrue(options.length() > 0, "found SelectInput with zero options: " + id);
            int index = _random.nextInt(options.length());
            String value = options.getJSONObject(index).getString("Value");
            //The CV2 client is configured to error on COPPA violations, so for most tests we will avoid setting
            //the "Age" CDV to a value that will be identified as a minor

            if (!value.isEmpty()) result = value;
        }
        return result;
    }

    // ******************************************************************************************************
    //    Common methods for getting values from a field
    // ******************************************************************************************************

    /**
     * Get MinLength from field object.  Object could be from JSON or converted XML.
     * @param field The JSON field object
     * @return The int value
     * @throws JSONException Thrown for JSON parsing errors
     */
    protected int getMaxLength(JSONObject field) throws JSONException {
        Integer maxLength = 50;
        if (field.has("MaxLength") && !field.isNull("MaxLength")) maxLength = field.getInt("MaxLength");
        if (field.has("maxLength")) maxLength = field.getInt("maxLength");
        if (maxLength == null) maxLength = 50;
        if (maxLength > 500) maxLength = 500;   // this is a reasonable cap
        return maxLength;
    }

    /**
     * Get MaxLength from field object.  Object could be from JSON or converted XML.
     * @param field The JSON field object
     * @return The int value
     * @throws JSONException Thrown for JSON parsing errors
     */
    protected int getMinLength(JSONObject field) throws JSONException {
        Integer minLength = 12;
        if (field.has("MinLength") && !field.isNull("MinLength")) minLength = field.optInt("MinLength");
        if (field.has("minLength")) minLength = field.optInt("minLength");
        if (minLength == null) minLength = 12;
        return minLength;
    }

    /**
     * Get Id from field object.  Object could be from JSON or converted XML.
     * @param field The JSON field object
     * @return The int value
     * @throws JSONException Thrown for JSON parsing errors
     */
    protected String getId(JSONObject field) throws JSONException {
        String id = field.optString("Id");
        if (id == null || id.isEmpty()) id = field.getString("id");
        return id;
    }

    // ******************************************************************************************************
    //    Accessors for Form Parameter name/value pairs
    // ******************************************************************************************************

    /**
     * Get the entire form parameters list
     * @return The entire form parameters list
     */
    public List<NameValuePair> getFormParams() {
        return _formparams;
    }

    /**
     * Add a new name/value pair to the form parameters list
     * @param name Name of the form parameter
     * @param value Value of the form parameter
     */
    public void setNameAndValue(String name, String value) {
        if (containsName(name)) {
            removeByName(name);
        }
        _formparams.add(new BasicNameValuePair(name, value));
    }

    /**
     * Search for a NameValuePair in the list with the specified name and return the associated value.
     * Search is case-insensitive.
     * @param name The name to search for
     * @return The associated value if found, otherwise null
     */
    public String getValueByName(String name) {
        String result = null;
        for (int i=0; i<_formparams.size(); i++) {
            NameValuePair pair = _formparams.get(i);
            if (pair.getName().equalsIgnoreCase(name)) {
                result = pair.getValue();
            }
        }
        return result;
    }

    /**
     * Search for a NameValuePair in the list with the specified name and return indication if found.
     * Search is case-insensitive.
     * @param name The name to search for
     * @return true if found, otherwise false
     */
    public boolean containsName(String name) {
        boolean result = false;
        for (int i=0; i<_formparams.size(); i++) {
            NameValuePair pair = _formparams.get(i);
            if (pair.getName().equalsIgnoreCase(name)) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Search for a NameValuePair in the list with the specified name and remove it.
     * Search is case-insensitive.
     * @param name The name to search for
     */
    public void removeByName(String name) {
        for (int i=0; i<_formparams.size(); i++) {
            NameValuePair pair = _formparams.get(i);
            if (pair.getName().equalsIgnoreCase(name)) {
                _formparams.remove(i);
            }
        }
    }

    // This is only used for debug to print out what is contained in the forms set in a test
    public void printFormToStdout () {

        List thisForm = this.getFormParams();

        for (int i = 0; i < thisForm.size(); i++) {
            System.out.println("List Item " + i + " = " + thisForm.get(i).toString());
        }
    }
}
