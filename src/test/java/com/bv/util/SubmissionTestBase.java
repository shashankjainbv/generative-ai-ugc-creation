package com.bv.util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;


import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.HashMap;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;





public class SubmissionTestBase extends TestBase {

    // maximum time to wait for submission processing
    private static final long maxWaitMilliSeconds = 10 * 60 * 1000;     // 10 minutes
    private static final int SLEEP_TIME = 2;
    protected static final String AUTHOR_SUBMISSION_TOKEN = "AuthorSubmissionToken";
    private final Queue<JSONObject> AUTHOR_QUEUE = new LinkedList<>();
    private final int DEFAULT_AUTH_NUM = 6;

    // This sample fingerprint was created on Steve O'Hara's Mac laptop in early 2012. It uses localhost instead of a Real IP
    // So it isn't very interesting. It is essentially the output from Iovation's snare.js script. Contains the computer name,
    // the IP address, Browser name, etc. All "encrypted" using some silly javascript. It uses their test version of snare.js
    protected static final String SAMPLE_FINGERPRINT = "0400/GNlSwBvsEsNf94lis1ztiT/BwFHxZwLskMqKRxYZWGeWAcbWeLSwtRoh5ph7y" +
            "VArp2Tu2ZaK/++XFpSIEb5Kknbtw2dg9JBTIBnmyVMxomohCbjy+F+TE1XuRd94Pmfm2m7iwQyd/xVah0xS2NUk6fTx+Mt/W4+ex1TLARybUq" +
            "vwFRqyepQW4stPHxOZiJVdLDs+1JC1RHkIyddAp8AtQkswC/wGkG+3/seKcOlvXqZUYPtoNLl7ThlP1OmGD7xeVaYJEF00M3FA0nRr0GSTmc9" +
            "q4JiXedOGGRJRrgh280xbsD2NmoHI9Egdcdhsez/0+1vm3fbD4ogZN3+U0UCvRXkYia2CTo14VChkAJuBS/dUqZEXLR7aItG7/EGOU7SKDGTT" +
            "iF4zGLz3wafI/WYHCDMnwUlr/77O9pqevtuRyNlr1aVueIq6eDDZnUp+uEjFsF83Kpm3K25LqBNk4kdSilCz8QbOZkvDlyhqok8FquCHqlvwO" +
            "uLWXiv1D0wB69K/pr71oD561o7wK7tuIQBsyfU5jDyVZH6V2qJsKeC6WuEKJ5sETrogn8if3oPJy1fCXaiWs2BQ09rfEp5mj8opT0XUsA1+E6" +
            "OOe0RxWZbWQzqNqtge7PenRkwfCqvl92YNxMVoVGGTL6vlcetxenoDaKD5h0rMYgkEMCy0s3hRLn08Th9FlNHDMJBqLj+Tz8r3SqFU/W+iqa7" +
            "Ass1MT2qnMUuEyz78glMXbI4Ftqyv7QaQ6QdHVwOB1QmHkWuaXrI7Al7P3rS0QjJ27v2hJ4MTbE7LH5wuIUSxIVrD45wNXBMXlJg36A8PAUIT" +
            "VdF834dE0FqXU7b6cPxpdsaS2lbG3A+k9XRlIy/7CZlP3tx2bvdCuUF/O6fLPe2Ce/EWeO0C7sxj10S10dUGD3kCDV76LGxn65R2wRJzWCkUm" +
            "/3GZjqEEPfKJCfyfR+ZtJR4yH4lU99UoH2xkkMardEACw5HPNnoQc78FVZF6cpAnuFU0qpWzW9d2tUz34vEEqOBv63FksBZ03Hp9YiLtKhR68" +
            "5GiBk3f5TRQK9FeRiJrYJOjXhUKGQAm4FL91SpkRctHtoi0bv8QY5TtLBnpJo1I/DFQ==";

    public static final String PARAM_ACTION = "Action";
    public static final String PARAM_LOCALE = "Locale";
    public static final String PARAM_USER_ID = "UserId";
    public static final String PARAM_AGREED_TO_TERMS_AND_CONDITIONS = "agreedtotermsandconditions";
    public static final String PARAM_USERNICKNAME = "usernickname";
    public static final String PARAM_USEREMAIL = "useremail";
    public static final String PARAM_LOCATION = "location";
    public static final String PARAM_CONTEXTDATAVALUE_GENDER = "contextdatavalue_gender";
    public static final String PARAM_CONTEXTDATAVALUE_AGE = "contextdatavalue_age";
    public static final String PARAM_CONTEXTDATAVALUE_PURCHASER_RANK = "contextdatavalue_purchaserRank";
    public static final String PARAM_ADDITIONALFIELD_INSPIRATIONS = "additionalfield_inspirations";
    public static final String PARAM_PHOTOCAPTION_1 = "photocaption_1";
    public static final String PARAM_AVATAR = "avatar";
    public static final String PARAM_QUESTION_ID = "questionid";
    public static final String PARAM_REVIEW_ID = "reviewid";
    public static final String PARAM_PRODUCT_ID = "productid";
    public static final String PARAM_CATEGORY_ID = "categoryid";
    public static final String PARAM_CALLBACK = "callback";
    public static final String PARAM_FINGERPRINT = "fp";
    public static final String PARAM_HOSTED_AUTH_EMAIL = "hostedauthentication_authenticationemail";
    public static final String PARAM_HOSTED_AUTH_CALLBACK_URL = "hostedauthentication_callbackurl";
    public static final String PARAM_PHOTOURL_1 = "photourl_1";
    public static final String PARAM_VIDEOURL_1 = "videourl_1";

    public static final String VALUE_USER_LOCATION = "austin";
    public static final String VALUE_GENDER = "male";
    public static final String VALUE_AGE = "21to34";
    public static final String VALUE_GENDER_CV2 = "Male";
    public static final String VALUE_AGE_CV2 = "18to24";
    public static final String VALUE_PURCHASER_RANK = "top10";
    public static final String VALUE_ADDITIONAL_FIELD_INSPIRATIONS = "Insane Clown Posse";
    public static final String VALUE_PHOTO_CAPTION = "family trip to the molassas factory";
    public static final String VALUE_ACTION_SUBMIT = "Submit";
    public static final String VALUE_ACTION_PREVIEW = "Preview";
    public static final String VALUE_VIDEOURL_YOUTUBE = "http://www.youtube.com/watch?v=SRH-Ywpz1_I";

    public static final String KEY_HAS_ERRORS = "HasErrors";
    public static final String KEY_FIELDS = "Fields";
    public static final String KEY_DATA = "Data";
    public static final String KEY_RESULTS = "Results";
    public static final String KEY_CONTEXT_DATA_VALUES = "ContextDataValues";
    public static final String KEY_ADDITIONAL_FIELDS = "AdditionalFields";
    public static final String KEY_AVATAR = "Avatar";
    public static final String KEY_PHOTOS = "Photos";
    public static final String KEY_USER_NICKNAME = "UserNickname";
    public static final String KEY_LOCATION = "Location";
    public static final String KEY_SECONDARY_RATINGS = "SecondaryRatings";
    public static final String KEY_VALUE = "Value";
    public static final String KEY_ID = "Id";
    public static final String KEY_SIZES = "Sizes";
    public static final String KEY_NORMAL = "normal";
    public static final String KEY_URL = "Url";
    public static final String KEY_TYPE = "Type";
    public static final String KEY_MIN_LENGTH = "MinLength";
    public static final String KEY_MAX_LENGTH = "MaxLength";


    // map content type to submission and query URIs
    protected static final Map<ContentType, String> SUBMIT_URI_JSON = new HashMap<ContentType, String>();
    protected static final Map<ContentType, String> QUERY_URI_JSON = new HashMap<ContentType, String>();
    protected static final Map<ContentType, String> SUBMIT_URI_XML = new HashMap<ContentType, String>();
    protected static final Map<ContentType, String> QUERY_URI_XML = new HashMap<ContentType, String>();

    // map content type to ability to submit anonymous content
    protected static final Map<ContentType, Boolean> ALLOWS_ANONYMOUS = new HashMap<ContentType, Boolean>();

    // map content type to ability to submit duplicate content
    protected static final Map<ContentType, Boolean> ALLOWS_DUPLICATE = new HashMap<ContentType, Boolean>();

    static {
        // initialize submission URI for JSON map
        SUBMIT_URI_JSON.put(ContentType.ANSWERS, SUBMIT_ANSWER_JSON_URI);
        SUBMIT_URI_JSON.put(ContentType.QUESTIONS, SUBMIT_QUESTION_JSON_URI);
        SUBMIT_URI_JSON.put(ContentType.REVIEWS, SUBMIT_REVIEW_JSON_URI);
        SUBMIT_URI_JSON.put(ContentType.REVIEWCOMMENTS, SUBMIT_REVIEWCOMMENT_JSON_URI);
        // initialize query URI for JSON map
        QUERY_URI_JSON.put(ContentType.ANSWERS, QUERY_ANSWERS_JSON_URI);
        QUERY_URI_JSON.put(ContentType.QUESTIONS, QUERY_QUESTIONS_JSON_URI);
        QUERY_URI_JSON.put(ContentType.REVIEWS, QUERY_REVIEWS_JSON_URI);
        QUERY_URI_JSON.put(ContentType.REVIEWCOMMENTS, QUERY_REVIEWCOMMENTS_JSON_URI);
        // initialize submission URI for XML map
        SUBMIT_URI_XML.put(ContentType.ANSWERS, SUBMIT_ANSWER_XML_URI);
        SUBMIT_URI_XML.put(ContentType.QUESTIONS, SUBMIT_QUESTION_XML_URI);
        SUBMIT_URI_XML.put(ContentType.REVIEWS, SUBMIT_REVIEW_XML_URI);
        SUBMIT_URI_XML.put(ContentType.REVIEWCOMMENTS, SUBMIT_REVIEWCOMMENT_XML_URI);
        // initialize query URI for XML map
        QUERY_URI_XML.put(ContentType.ANSWERS, QUERY_ANSWERS_XML_URI);
        QUERY_URI_XML.put(ContentType.QUESTIONS, QUERY_QUESTIONS_XML_URI);
        QUERY_URI_XML.put(ContentType.REVIEWS, QUERY_REVIEWS_XML_URI);
        QUERY_URI_XML.put(ContentType.REVIEWCOMMENTS, QUERY_REVIEWCOMMENTS_XML_URI);

        // initialize allows anonymous map
        // TODO - You will have to change this to match the configuration for the TestCustomer client
        ALLOWS_ANONYMOUS.put(ContentType.ANSWERS, false);
        ALLOWS_ANONYMOUS.put(ContentType.QUESTIONS, false);
        ALLOWS_ANONYMOUS.put(ContentType.REVIEWS, true);
        ALLOWS_ANONYMOUS.put(ContentType.REVIEWCOMMENTS, false);

        // initialize allows duplicate map
        // TODO - You will have to change this to match the configuration for the TestCustomer client
        ALLOWS_DUPLICATE.put(ContentType.ANSWERS, true);          // does not explicitly allow/disallow
        ALLOWS_DUPLICATE.put(ContentType.QUESTIONS, true);        // does not explicitly allow/disallow
        ALLOWS_DUPLICATE.put(ContentType.REVIEWS, true);          // explicitly allows
        ALLOWS_DUPLICATE.put(ContentType.REVIEWCOMMENTS, true);   // does not explicitly allow/disallow
    }




    /**
     * Method for authenticating user with authentication token - JSON version
     * @param authToken The authentication token
     * @return The user token
     * @throws JSONException On JSON parsing problems
     */
    protected String authenticateUserJSON(String authToken) throws JSONException {
        sleep(SLEEP_TIME);
        // create post data
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("PassKey", getPropPassKey()));
        formparams.add(new BasicNameValuePair("ApiVersion", getPropApiVersion()));
        formparams.add(new BasicNameValuePair("DisplayCode", getHostedAuthDisplayCode()));
        formparams.add(new BasicNameValuePair("AuthToken", authToken));
        // submit
        String url = getPropServer() + AUTH_USER_JSON_URI;
        JSONObject authObj = JSONHelper.parseJSON(doSubmit(url, formparams));
        boolean hasAuthError = JSONHelper.checkHasErrors(authObj);
        if(hasAuthError) {
            if("Authentication token is invalid.".equals(JSONHelper.getErrorMessage(authObj))) {
                //if we have called waitForAuthToken (and we always should) before calling this method,
                //then we should never be here. see VOL-847. If we are here, it is because of timing
                //(replication) differences between the test suite Db and the read slave. these should
                //not be very big, so try again
                authObj = JSONHelper.parseJSON(doSubmit(url, formparams));
                hasAuthError = JSONHelper.checkHasErrors(authObj);
            }
        }
        Assert.assertFalse(hasAuthError, "found errors in JSON response");
        // get user token
        return authObj.getJSONObject("Authentication").getString("User");
    }



    /**
     * Wait for submitted content to become available.  Waits until the specified request URL returns
     * a single piece of content and then returns that content as a JSONObject.
     * @param requestUrl The JSON request URL that will return a single piece of content.
     * @return JSONObject for content
     */
    protected JSONObject waitForSubmittedContent(String requestUrl) {
        JSONObject obj = null;
        long beginTime = System.currentTimeMillis();
        while (true) {
            String cachebuster = "&cachebust=" + String.valueOf(System.currentTimeMillis());
            obj = JSONHelper.parseJSON(doRequest(requestUrl + cachebuster));
            Assert.assertFalse(JSONHelper.checkHasErrors(obj), "unexpected errors in response");
            // break the loop if found
            if (JSONHelper.getInt(obj, "TotalResults") != 0) {
                break;
            }
            // check for timeout
            if (System.currentTimeMillis() - beginTime > maxWaitMilliSeconds) {
                Assert.fail("maximum wait time exceeded (ms): " + maxWaitMilliSeconds);
            }
            sleep(SLEEP_TIME * 2);
        }
        Assert.assertEquals(JSONHelper.getInt(obj, "TotalResults"), 1, "unexpected multiple results");
        JSONArray results = JSONHelper.getArray(obj, KEY_RESULTS);
        return JSONHelper.getJSONObjectAtIndex(results, 0);
    }


    /**
     * Utility method for sleeping that catches the exception and throws an assertion
     * @param seconds Number of seconds to sleep
     */
    protected void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ex) {
            Assert.fail("caught unexpected exception in the thread sleep method...\n" + ex.getMessage());
        }
    }

    /**
     * Submission tests that need an existing user should use this method to create a new author rather than getting a random existing author.
     * @return Author JSON object
     * @throws JSONException is thrown for any parsing exception
     */
    protected JSONObject getAuthorForSubmission() throws JSONException {
        JSONObject author = AUTHOR_QUEUE.poll();
        if (author != null) {
            return author;
        }
       // setUpAuthors();
        Assert.assertFalse(AUTHOR_QUEUE.isEmpty());
        author = AUTHOR_QUEUE.poll();
        Assert.assertNotNull(author);
        return author;
    }


    /**********************************************************************************************************
     * Helper Methods for generating submission data based on content type
     **********************************************************************************************************/


    /**
     * Get the submission form for the specified content type and generate random values for the fields in the form.
     * @param contentType The content type of the form
     * @param formUrl The JSON version of the URL for obtaining the submission form
     * @param requiredFieldsOnly Boolean to indicate that only the required fields are to be generated
     * @return AbstractSubmitFormFiller value with a list of field/value pairs for submission
     * @throws JSONException Thrown for JSON parsing errors
     */
    protected AbstractSubmitFormFiller getFilledFormJSON(ContentType contentType, String formUrl, boolean requiredFieldsOnly) throws JSONException {
        JSONObject formObj = JSONHelper.parseJSON(doRequest(formUrl));
        Assert.assertFalse(JSONHelper.checkHasErrors(formObj), "unexpected errors in Form response");
        return getFilledForm(contentType, formObj, requiredFieldsOnly);
    }

    /**
     * Get the submission form for the specified content type and generate random values for the fields in the form.
     * @param contentType The content type of the form
     * @param formUrl The XML version of the URL for obtaining the submission form
     * @param requiredFieldsOnly Boolean to indicate that only the required fields are to be generated
     * @return AbstractSubmitFormFiller value with a list of field/value pairs for submission
     * @throws JSONException Thrown for JSON parsing errors
     */
    protected AbstractSubmitFormFiller getFilledFormXML(ContentType contentType, String formUrl, boolean requiredFieldsOnly) throws JSONException {
        JSONObject formObj = JSONHelper.parseXML(doRequest(formUrl));
        Assert.assertFalse(JSONHelper.checkHasErrorsObject(formObj), "unexpected errors in Form response");
        return getFilledForm(contentType, formObj, requiredFieldsOnly);
    }

    /**
     * Generate random values for the fields in the specified submission form object.
     * @param contentType The content type of the form
     * @param formObj The JSON response to the request for the submission form
     * @param requiredFieldsOnly Boolean to indicate that only the required fields are to be generated
     * @return AbstractSubmitFormFiller value with a list of field/value pairs for submission
     * @throws JSONException Thrown for JSON parsing errors
     */
    protected AbstractSubmitFormFiller getFilledForm(ContentType contentType, JSONObject formObj, boolean requiredFieldsOnly) throws JSONException {
        AbstractSubmitFormFiller submitFormFiller = null;
        switch (contentType) {

            case QUESTIONS:
                submitFormFiller = new QuestionSubmitFormFiller(formObj, requiredFieldsOnly);
                break;
            case REVIEWS:
                submitFormFiller = new ReviewSubmitFormFiller(formObj, requiredFieldsOnly);
                break;
            default:
                Assert.fail("unknown content type: " + contentType);
        }
        return submitFormFiller;
    }

    /**********************************************************************************************************
    * Submission Token Verification Methods
    **********************************************************************************************************/

    /**
     * Verify that the author submission token is returned.  If a UAS is passed in, check that it matches
     */




    /**********************************************************************************************************
     * Preview Response Verification Methods
     **********************************************************************************************************/

    /**
     * Verify the response from a Action=Preview submission
     * @param responseObj The response from the submission as a JSON object
     * @param submitFormFiller The form filler used for the comparison
     * @throws JSONException Thrown on JSON parsing errors
     */
    protected void verifyPreviewResponseValues(JSONObject responseObj, AbstractSubmitFormFiller submitFormFiller) throws JSONException {
        JSONObject fields = responseObj.getJSONObject("Data").getJSONObject("Fields");
        for (NameValuePair pair : submitFormFiller.getFormParams()) {
            JSONObject field = fields.optJSONObject(pair.getName());
            if (field != null) {                                        // not all submission pairs are in the form!!!
                log("submitted field: " + pair.getName() + ", found field: " + field.getString("Id"));
                String fieldType = field.getString("Type");
                if (fieldType.equalsIgnoreCase("TextInput")) {
                    verifyPreviewTextInputValue(field, pair.getValue());
                }
                else if (fieldType.equalsIgnoreCase("TextAreaInput")) {
                    verifyPreviewTextAreaInputValue(field, pair.getValue());
                }
                else if (fieldType.equalsIgnoreCase("BooleanInput")) {
                    verifyPreviewTextInputValue(field, pair.getValue());
                }
                else if (fieldType.equalsIgnoreCase("IntegerInput")) {
                    verifyPreviewTextInputValue(field, pair.getValue());
                }
                else if (fieldType.equalsIgnoreCase("SelectInput")) {
                    verifyPreviewSelectInputValue(field, pair.getValue());
                }
                else {
                    Assert.fail("unknown field type: " + fieldType);
                }
            }
        }
    }

    /**
     * Verify the SelectInput value in the submission response matches the submitted value
     * @param field Field from the response
     * @param value Value in the submission
     * @throws JSONException Thrown on JSON parsing errors
     */
    private void verifyPreviewSelectInputValue(JSONObject field, String value) throws JSONException {
        JSONArray options = field.getJSONArray("Options");
        boolean found = false;
        for (int i=0; i<options.length(); i++) {
            JSONObject option = options.getJSONObject(i);
            if (option.getString("Value").equals(value) && option.getBoolean("Selected")) {
                found = true;
            }
        }
        Assert.assertTrue(found, "could not find value: " + value + " in field " + field.getString("Id"));
    }

    /**
     * Verify the TextInput value in the submission response matches the submitted value
     * @param field Field from the response
     * @param value Value in the submission
     * @throws JSONException Thrown on JSON parsing errors
     */
    private void verifyPreviewTextInputValue(JSONObject field, String value) throws JSONException {
        if (field.getString("Id").startsWith("tag_")) {               // special case
            value = value.toLowerCase();
            value = value.replaceAll("[,]", "");
        }
        Assert.assertEquals(field.getString("Value"), value, "unexpected difference in field: " + field.getString("Id"));
    }

    /**
     * Verify the TextAreaInput value in the submission response matches the submitted value
     * @param field Field from the response
     * @param value Value in the submission
     * @throws JSONException Thrown on JSON parsing errors
     */
    private void verifyPreviewTextAreaInputValue(JSONObject field, String value) throws JSONException {
        if (field.getString("Id").startsWith("additionalfield_")) {       // special case
            value = value.replaceAll("\n\n", " ");
        }
        Assert.assertEquals(field.getString("Value"), value, "unexpected difference in field: " + field.getString("Id"));
    }


    /**
     * Makes sure ResultSet matches map of expected results
     * @param keyColumn column in ResultSet that corresponds to keys in map
     * @param valueColumn column in ResultSet that corresponds to values in map
     * @param checkedMap map of expected results
     * @param results ResultSet from query
     */
    private void verifyResultsFromMap(String keyColumn, String valueColumn, Map<String, String> checkedMap, ResultSet results) {
        String dimension, expectedValue, actualValue;
        try {
            results.beforeFirst(); // this does nothing if the result set is empty
            while (results.next()) {  // this returns false if the result set is empty
                dimension = results.getString(keyColumn);
                expectedValue = checkedMap.get(dimension);
                expectedValue = expectedValue.replace("\n", " ");
                actualValue = results.getString(valueColumn);
                Assert.assertEquals(actualValue.replaceAll("\\s+", " "), expectedValue.replaceAll("\\s+", " "));
                checkedMap.remove(dimension);
            }
        } catch (SQLException ex) {
            Assert.fail("Exception reading results of query", ex);
        }
    }

    /**********************************************************************************************************
     * Author prefill for Content Submission
     **********************************************************************************************************/

    protected void verifyPrefilledAuthorFieldsForContentSubmission(JSONObject authorObj, JSONObject contentFormObj)
            throws JSONException {
        JSONObject data = contentFormObj.getJSONObject(KEY_DATA);
        if (authorObj.has(KEY_CONTEXT_DATA_VALUES)) {
            JSONObject contextDataValues = authorObj.getJSONObject(KEY_CONTEXT_DATA_VALUES);
            verifyContextDataValues(contextDataValues, data);
        }

        if (authorObj.has(KEY_ADDITIONAL_FIELDS)) {
            JSONObject additionalFields = authorObj.getJSONObject(KEY_ADDITIONAL_FIELDS);
            verifyAdditionalFields(additionalFields, data);
        }

        if (authorObj.has(KEY_AVATAR)) {
            verifyAvatar(authorObj.getString(KEY_AVATAR), data);
        }

        if (authorObj.has(KEY_PHOTOS)) {
            // TODO - No need to validate photos yet, but if you do, this will need to be updated.
//            verifyPhotos(authorObj.getJSONArray(KEY_PHOTOS), data);
        }

        if (authorObj.has(KEY_USER_NICKNAME)) {
            verifyUserNickname(authorObj.getString(KEY_USER_NICKNAME), data);
        }

        if (authorObj.has(KEY_LOCATION)) {
            verifyLocation(authorObj.getString(KEY_LOCATION), data);
        }

        if (authorObj.has(KEY_SECONDARY_RATINGS)) {
            // Secondary ratings aren't currently populated during Author submission, so it's hard to be sure that I've written this verify correctly...
            verifySecondaryRatings(authorObj.getJSONObject(KEY_SECONDARY_RATINGS), data);
        }
    }

    protected void verifyContextDataValues(JSONObject contextDataValues, JSONObject data)
            throws JSONException {
        JSONObject fields = data.getJSONObject(KEY_FIELDS);
        Iterator cdvKeys = contextDataValues.keys();
        while (cdvKeys.hasNext()) {
            JSONObject contextDataValue = contextDataValues.getJSONObject((String) cdvKeys.next());
            String contextDataValueFieldName = getContextDataValueFieldName(contextDataValue);
            if (fields.has(contextDataValueFieldName)) {
                verifySubmittedGroup(data, KEY_FIELDS, contextDataValueFieldName, contextDataValue.getString(KEY_VALUE));
            }
        }
    }

    protected String getContextDataValueFieldName(JSONObject contextDataValue)
            throws JSONException {
        return "contextdatavalue_" + contextDataValue.getString(KEY_ID);
    }

    protected void verifyAdditionalFields(JSONObject additionalFields, JSONObject data)
            throws JSONException {
        JSONObject fields = data.getJSONObject(KEY_FIELDS);
        Iterator additionalFieldsKeys = additionalFields.keys();
        while (additionalFieldsKeys.hasNext()) {
            JSONObject additionalField = additionalFields.getJSONObject((String) additionalFieldsKeys.next());
            String additionalFieldValueName = getAdditionalFieldValueName(additionalField);
            if (fields.has(additionalFieldValueName)) {
                verifySubmittedGroup(data, KEY_FIELDS, additionalFieldValueName, additionalField.getString(KEY_VALUE));
            }
        }
    }

    protected String getAdditionalFieldValueName(JSONObject additionalField)
            throws JSONException {
        return "additionalfield_" + additionalField.getString(KEY_ID);
    }

    protected void verifyAvatar(String avatarValue, JSONObject data)
            throws JSONException {
        JSONObject fields = data.getJSONObject(KEY_FIELDS);
        if (fields.has(PARAM_AVATAR)) {
            verifySubmittedGroup(data, KEY_FIELDS, PARAM_AVATAR, avatarValue);
        }
    }

    protected void verifyUserNickname(String nicknameValue, JSONObject data)
            throws JSONException {
        verifySubmittedGroup(data, KEY_FIELDS, PARAM_USERNICKNAME, nicknameValue);
    }

    protected void verifyLocation(String locationValue, JSONObject data)
            throws JSONException {
        JSONObject fields = data.getJSONObject(KEY_FIELDS);
        if (fields.has(PARAM_LOCATION)) {
            verifySubmittedGroup(data, KEY_FIELDS, PARAM_LOCATION, locationValue);
        }
    }

    protected void verifySecondaryRatings(JSONObject secondaryRatings, JSONObject data)
            throws JSONException {
        JSONObject fields = data.getJSONObject(KEY_FIELDS);
        Iterator secondaryRatingsKeys = secondaryRatings.keys();
        while (secondaryRatingsKeys.hasNext()) {
            JSONObject secondaryRating = secondaryRatings.getJSONObject((String) secondaryRatingsKeys.next());
            String secondaryRatingFieldName = getSecondaryRatingFieldName(secondaryRating);
            if (fields.has(secondaryRatingFieldName)) {
                verifySubmittedGroup(data, KEY_FIELDS, secondaryRatingFieldName, secondaryRating.getString(KEY_VALUE));
            }
        }
    }

    protected void verifySubmittedGroup(JSONObject obj, String blockName, String submittedInstanceName, String submittedValue) throws JSONException {
        Assert.assertTrue(obj.has(blockName), "group block is missing: " + blockName);
        Assert.assertTrue(obj.getJSONObject(blockName).has(submittedInstanceName), "group instance is missing: " + submittedInstanceName);
        String actualValue = obj.getJSONObject(blockName).getJSONObject(submittedInstanceName).getString(KEY_VALUE);
        String expectedValue = submittedValue.replaceAll("\n\n", " ");
        Assert.assertEquals(actualValue, expectedValue, "unexpected group instance value");
    }

    protected String getSecondaryRatingFieldName(JSONObject secondaryRating)
            throws JSONException {
        return "rating_" + secondaryRating.getString(KEY_ID);
    }


}
