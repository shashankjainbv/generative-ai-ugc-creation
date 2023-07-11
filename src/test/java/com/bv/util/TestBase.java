package com.bv.util;


import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.xml.sax.InputSource;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;


public class TestBase {

    public static final String QUERY_REVIEWS_JSON_URI = "/data/reviews.json";
    public static final String QUERY_QUESTIONS_JSON_URI = "/data/questions.json";
    public static final String QUERY_ANSWERS_JSON_URI = "/data/answers.json";
    public static final String QUERY_STORIES_JSON_URI = "/data/stories.json";
    public static final String QUERY_REVIEWCOMMENTS_JSON_URI = "/data/reviewcomments.json";
    public static final String QUERY_STORYCOMMENTS_JSON_URI = "/data/storycomments.json";
    public static final String QUERY_CATEGORIES_JSON_URI = "/data/categories.json";
    public static final String QUERY_PRODUCTS_JSON_URI = "/data/products.json";
    public static final String QUERY_AUTHORS_JSON_URI = "/data/authors.json";
    public static final String SUBMIT_REVIEW_JSON_URI = "/data/submitreview.json";
    public static final String SUBMIT_QUESTION_JSON_URI = "/data/submitquestion.json";
    public static final String SUBMIT_ANSWER_JSON_URI = "/data/submitanswer.json";
    public static final String SUBMIT_STORY_JSON_URI = "/data/submitstory.json";
    public static final String SUBMIT_REVIEWCOMMENT_JSON_URI = "/data/submitreviewcomment.json";
    public static final String SUBMIT_STORYCOMMENT_JSON_URI = "/data/submitstorycomment.json";
    public static final String SUBMIT_FEEDBACK_JSON_URI = "/data/submitfeedback.json";
    public static final String SUBMIT_AUTHOR_JSON_URI = "/data/submitauthor.json";
    public static final String UPLOAD_PHOTO_JSON_URI = "/data/uploadphoto.json";
    public static final String UPLOAD_VIDEO_JSON_URI = "/data/uploadvideo.json";
    public static final String BOGUS_JSON_URI = "/data/pancakes";
    public static final String AUTH_USER_JSON_URI = "/data/authenticateuser.json";
    public static final String JANRAIN_JSON_URI = "/data/janrain.json";

    public static final String QUERY_REVIEWS_XML_URI = "/data/reviews.xml";
    public static final String QUERY_QUESTIONS_XML_URI = "/data/questions.xml";
    public static final String QUERY_ANSWERS_XML_URI = "/data/answers.xml";
    public static final String QUERY_STORIES_XML_URI = "/data/stories.xml";
    public static final String QUERY_REVIEWCOMMENTS_XML_URI = "/data/reviewcomments.xml";
    public static final String QUERY_STORYCOMMENTS_XML_URI = "/data/storycomments.xml";
    public static final String SUBMIT_REVIEW_XML_URI = "/data/submitreview.xml";
    public static final String SUBMIT_QUESTION_XML_URI = "/data/submitquestion.xml";
    public static final String SUBMIT_ANSWER_XML_URI = "/data/submitanswer.xml";
    public static final String SUBMIT_STORY_XML_URI = "/data/submitstory.xml";
    public static final String SUBMIT_REVIEWCOMMENT_XML_URI = "/data/submitreviewcomment.xml";
    public static final String SUBMIT_STORYCOMMENT_XML_URI = "/data/submitstorycomment.xml";
    public static final String SUBMIT_FEEDBACK_XML_URI = "/data/submitfeedback.xml";
    public static final String SUBMIT_AUTHOR_XML_URI = "/data/submitauthor.xml";
    public static final String UPLOAD_PHOTO_XML_URI = "/data/uploadphoto.xml";
    public static final String UPLOAD_VIDEO_XML_URI = "/data/uploadvideo.xml";

    public static String RPC_URI = "/data/rpcfile";

    public static final String DYNAMIC_PROPERTY_JSON_URI = "/DynamicProperty.json";

    public static final String PARAM_PASS_KEY = "passkey";
    public static final String PARAM_API_VERSION = "ApiVersion";
    public static final String PARAM_USER = "User";
    public static final String PARAM_DISPLAY_CODE = "DisplayCode";

    protected final String sampleMediaDirectory = "sample.media/";

    protected static final int MQ_SLEEP_TIME = 10;
    private static final String CREDENTIALS_PROPERTIES_FILE = "/credentials.properties";

    /**
     * ContentType holds the name of the content used in queries, the names of the
     * filtered and unfiltered stats blocks returned in the responses as well as the
     * name of the content count tag inside the stats blocks.
     */
    public enum ContentType {

        REVIEWS("Reviews", "Review"),
        QUESTIONS("Questions", "Question"),
        ANSWERS("Answers", "Answer"),
        REVIEWCOMMENTS("Review Comments", "Review_Comment", "Comment"),
        AUTHORS("Authors", null);

        private String _name;
        private String _fieldName;
        private String _shortFieldName;

        // Default the shortFieldName to the fieldName (e.g., Review -> Review)
        private ContentType(String name, String fieldName) {
            this(name, fieldName, fieldName);
        }

        private ContentType(String name, String fieldName, String shortFieldName) {
            _name = name;
            _fieldName = fieldName;
            _shortFieldName = shortFieldName;
        }

        public String getName() {
            return _name;
        }

        public String getFieldName() {
            return _fieldName;
        }

        public String getShortFieldName() {
            return _shortFieldName;
        }
    }

    /**
     * TestEnvType is the target test environment type
     */
    public enum TestEnvType {
        LOCAL("local"),
        DEV("dev"),
        QA("qa");

        private String _name;

        TestEnvType(String name) {
            _name = name;
        }

        public String getName() {
            return _name;
        }
    }

    // Proxy Settings for the HTTP methods
    private ProxySettings _proxySettings = null;

    // variables for holding the Host, Version, and API Key from the properties file
    private String _Server = null;
    private String _ServerHttps = null;
    private String _OriginServer = null;
    private String _OriginServerHttps = null;
    private String _ApiVersion = null;
    private String _PassKey = null;
    private String _EncodingKey = null;
    private Boolean _legacyApi = null;

    private String _hostedAuthDisplayCode = null;

    private static String _clientName = null;
    private static long _clientID = -1;

    // variables for holding database connection information from the properties file
    private String _databaseHostname = null;
    private String _databaseUsername = null;
    private String _databasePassword = null;

    // variables used for submissions only
    private static String strValidEmail = "bvspambox@gmail.com";

    // variables for holding responses from "Display" requests for Questions, Reviews, and Stories
    //  - requested once and then used several times for submissions
    private List<String> _productList = null;
    private List<String> _categoryList = null;
    private List<String> _authorList = null;
    private List<String> _approvedAuthorList = null;
    private List<Long> _questionList = null;
    private List<Long> _questionAboutCategoryList = null;
    private List<Long> _questionAboutProductList = null;
    private List<Long> _answerList = null;
    private List<Long> _reviewList = null;
    private List<Long> _reviewCommentList = null;

    protected static boolean isCV2Test = false;
    protected static String contentTypeProvider = "SubmissionContentTypes";

    // _random number generator
    protected static Random _random = new Random();

    // test environment selection
    private TestEnvType _testEnv = null;

    /**
     * Default constructor loads the properties from the top level properties file
     */
    protected TestBase() {
//        getTestEnvironment();
//
//        if (loadBooleanProperty("HTTP_PROXY_ENABLED")) {
//            _proxySettings = new ProxySettings();
//            _proxySettings.setProxyHost(loadStringProperty("HTTP_PROXY_HOST"));
//            _proxySettings.setProxyPort(loadStringProperty("HTTP_PROXY_PORT"));
//            _proxySettings.setProxyUsername(loadStringProperty("HTTP_PROXY_USERNAME"));
//            _proxySettings.setProxyPassword(loadStringProperty("HTTP_PROXY_PASSWORD"));
//        }
//        _Server = loadStringProperty("DEVAPI_CLIENT_HOST");
//        _ServerHttps = _Server.replace("http", "https");
//        _OriginServer = loadStringProperty("DEVAPI_CLIENT_ORIGIN_HOST");
//        _OriginServerHttps = _OriginServer.replace("http", "https");
//        _ApiVersion = loadStringProperty("DEVAPI_CLIENT_VERSION");
//        _legacyApi = Boolean.parseBoolean(loadStringProperty("DEVAPI_LEGACY_API"));
//
//        if ("CV2".equals(System.getProperty("ClientType"))) {
//            _clientName = loadStringProperty("CV2_CLIENT_NAME");
//            _PassKey = loadStringProperty("CV2_CLIENT_KEY");
//            _hostedAuthDisplayCode = loadStringProperty("CV2_HOSTED_AUTH_DISPLAY_CODE");
//            _EncodingKey = loadStringProperty("DEVAPI_CLIENT_ENCODING_KEY_CV2");
//            isCV2Test = true;
//            log("Running tests with CV2 client");
//        } else {
//            _clientName = loadStringProperty("DEVAPI_CLIENT_NAME");
//            _PassKey = loadStringProperty("DEVAPI_CLIENT_KEY");
//            _hostedAuthDisplayCode = loadStringProperty("DEVAPI_HOSTED_AUTH_DISPLAY_CODE");
//            _EncodingKey = loadStringProperty("DEVAPI_CLIENT_ENCODING_KEY");
//            log("Running tests with Agrippa client");
//        }
//
//        _databaseHostname = loadStringProperty("DATABASE_HOSTNAME");
//       // _databaseUsername = getCredentialSettings().databaseUsername;
//      //  _databasePassword = getCredentialSettings().databasePassword;
//        initXPath();
    }

    /**
     * Getter
     *
     * @return isCV2Test
     */
    public static boolean isCV2Test() {
        return isCV2Test;
    }

    private void getTestEnvironment() {
        String value = System.getProperty("devapi.testenv");
        if (value == null) {
            _testEnv = TestEnvType.QA;           // default
        } else if (value.equalsIgnoreCase(TestEnvType.LOCAL.getName())) {
            _testEnv = TestEnvType.LOCAL;
        } else if (value.equalsIgnoreCase(TestEnvType.DEV.getName())) {
            _testEnv = TestEnvType.DEV;
        } else if (value.equalsIgnoreCase(TestEnvType.QA.getName())) {
            _testEnv = TestEnvType.QA;
        } else {
            Assert.fail("unknown test environment specified: " + value);
        }
    }



    protected String loadStringProperty(String property) {
        return getProperties().getProperty(property);
    }

    protected Boolean loadBooleanProperty(String property) {
        return Boolean.parseBoolean(getProperties().getProperty(property));
    }

    private Properties getProperties() {
        Assert.assertNotNull(_testEnv, "test environment is not set");
        String propertyFile = null;
        if (_testEnv == TestEnvType.LOCAL) {
            propertyFile = "/devapi.functional.local.properties";
        } else if (_testEnv == TestEnvType.DEV) {
            propertyFile = "/devapi.functional.dev.properties";
        } else if (_testEnv == TestEnvType.QA) {
            propertyFile = "/devapi.functional.qa.properties";
        }
        Properties prop = new Properties();
//        try {
//          //  prop.load(getClass().getResourceAsStream(propertyFile));
//        } catch (FileNotFoundException e) {
//            Assert.fail("loadProperties caught FileNotFoundException...\n" + e.getMessage());
//        } catch (IOException e) {
//            Assert.fail("loadProperties caught IOException...\n" + e.getMessage());
//        }
        return prop;
    }



    /**
     * Send logging output to the Reporter class and optionally to stdout.
     * Put SUBMISSION_VERBOSE_OUTPUT=true in the test.default.properties file to see test case output in Intellij IDEA.
     *
     * @param output The string to send to the log
     */
    protected void log(String output) {
        if (loadBooleanProperty("SUBMISSION_VERBOSE_OUTPUT")) {
            System.out.println(output);
        }
        Reporter.log(output);
    }


    /********************************************************************************************
     * Helper methods for HTTP GET and POST methods
     *********************************************************************************************/

    protected String doRequest(String url) {
        return doRequestWithHeaders(url, null);
    }

    protected String doRequestWithHeaders(String url, Map<String, String> headers) {
        String result = "";

      //  log("REQUEST...\n" + url);
        try {
            result = HTTPHelper.request(URLGenerationUtils.encodeURL(url), headers).getResponse();
        } catch (IOException ex) {
            Assert.fail("doRequest caught IOException...\n" + ex.getMessage());
        }
        // Do not send result to System.out - too much data
        return result;
    }

    protected HttpResponseTuple doRequestWithResponseCode(String url) {
        HttpResponseTuple result = null;

        log("REQUEST...\n" + url);
        try {
            result = HTTPHelper.request(_proxySettings, URLGenerationUtils.encodeURL(url));
        } catch (IOException ex) {
            Assert.fail("doRequest caught IOException...\n" + ex.getMessage());
        }
        // Do not send result to System.out - too much data
        return result;
    }

    protected String doSubmit(String url, List<NameValuePair> formparams) {
        return doSubmitWithHeaders(url, formparams, null);
    }

    protected String doSubmitWithHeaders(String url, List<NameValuePair> formparams, Map<String, String> headers) {
        String result = "";

        StringBuilder urlParameters = new StringBuilder("?");

        for (NameValuePair param : formparams) {
            urlParameters.append(param.getName() + "=" + param.getValue() + "&");
        }
        urlParameters.deleteCharAt(urlParameters.length() - 1);
       // log("REQUEST...\n" + url + urlParameters.toString());
        try {
            HttpResponseTuple tuple = HTTPHelper.submit(URLGenerationUtils.encodeURL(url), formparams, headers);
            Assert.assertTrue(tuple.getHeaders().containsKey("Content-Type"), "response did not contain Content-Type header");
            if (tuple.getHeaders().get("Content-Type").equals("text/html")) {
                Assert.fail("doSubmitWithHeaders response contains HTML...\n" + tuple.getResponse());
            }
            result = tuple.getResponse();
        } catch (IOException ex) {
           // Assert.fail("doSubmitWithHeaders caught IOException...\n" + ex.getMessage());
        } finally {
           // log("RESPONSE...\n" + result);
        }


        return result;
    }

    protected String doUpload(String url, HashMap<String, String> data, String binType, File resource, String mimeType) {
        String result = "";


        log("REQUEST...\n" + url);
        log("UPLOAD FILE...\n" + resource.getName());
        //  result = HTTPHelper.upload(_proxySettings, URLGenerationUtils.encodeURL(url), data, binType, resource, mimeType);

        log("RESPONSE...\n" + result);
        return result;
    }







    /********************************************************************************************
     * Getter methods for obtaining a random piece of content...
     *********************************************************************************************/

    /**
     * Get a random ID from the results for the specified client with an optional filter
     *
     * @param fullUrl The target URL (host + URI + passkey + apiversion)
     * @param filter  Optional filter string or empty string ("") for none
     * @return The randomly selected ID value
     */
    protected String getRandomIdWithHeaders(String fullUrl, String filter, String name, Map<String, String> headers) {
        JSONObject obj = JSONHelper.parseJSON(doRequestWithHeaders(fullUrl + filter, headers));
        Assert.assertFalse(JSONHelper.checkHasErrors(obj));
        JSONArray results = JSONHelper.getArray(obj, "Results");
        Assert.assertTrue(results.length() > 0, "No valid " + name + " results found");
        int i = _random.nextInt(results.length());
        JSONObject resultobj = JSONHelper.getJSONObjectAtIndex(results, i);
        return JSONHelper.getString(resultobj, "Id");
    }

    /**
     * Get a random ID from the results for the specified client with an optional filter AND
     * use ONLY NATIVE results
     *
     * @param fullUrl The target URL (host + URI + passkey + apiversion)
     * @param filter  Optional filter string or empty string ("") for none
     * @return The randomly selected ID value
     */
    protected String getRandomNativeIdWithHeaders(String fullUrl, String filter, String name, Map<String, String> headers) {
        boolean isNative = false;
        JSONObject resultobj;
        JSONObject obj = JSONHelper.parseJSON(doRequestWithHeaders(fullUrl + filter, headers));
        Assert.assertFalse(JSONHelper.checkHasErrors(obj));
        JSONArray results = JSONHelper.getArray(obj, "Results");
        Assert.assertTrue(results.length() > 0, "No valid " + name + " results found");
        do {
            int i = _random.nextInt(results.length());
            resultobj = JSONHelper.getJSONObjectAtIndex(results, i);

            isNative = !JSONHelper.getBoolean(resultobj, "IsSyndicated");  // if isSyndicated = true, then isNative = false

        } while (!isNative);  // continue to look for content that is NOT syndicated

        return JSONHelper.getString(resultobj, "Id");

    }

    /********************************************************************************************
     * Methods for setting and verifying URLs
     *********************************************************************************************/

    protected String setUrlProtocol(String urlStr, String protocol) {
        // verify correctly formed url
        try {
            URL url = new URL(urlStr);
        } catch (MalformedURLException e) {
            Assert.fail("malformed url: " + urlStr);
        }
        // return url with specified protocol
        return protocol + urlStr.substring(urlStr.indexOf(":"));
    }

    protected void verifyUrlProtocol(String urlStr, String protocol) {
        if (urlStr.equals("null")) {
            return;   // ignore null
        }
        // verify correctly formed url
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            Assert.fail("malformed url: " + urlStr);
        }
        if (url.getHost().contains("brightcove.com")) {
            return;
        }
        Assert.assertEquals(url.getProtocol(), protocol, "unexpected protocol in url: " + urlStr);
    }

    /*********************************************************************************************
     * Getter Methods for config file properties
     *********************************************************************************************/

    /**
     * Getter method for the configuration file property DEVAPI_CLIENT_HOST
     *
     * @return value of property
     */
    public String getPropServer() {
        return _Server;
    }

    /**
     * Getter method for the configuration file property DEVAPI_CLIENT_HOST
     * Property is changed to https from http
     *
     * @return value of property
     */
    public String getPropServerHttps() {
        return _ServerHttps;
    }

    /**
     * Getter method for the configuration file property DEVAPI_CLIENT_ORIGIN_HOST
     *
     * @return value of property
     */
    public String getPropOriginServer() {
        return _OriginServer;
    }

    /**
     * Getter method for the configuration file property DEVAPI_CLIENT_ORIGIN_HOST
     * Property is changed to https from http
     *
     * @return value of property
     */
    public String getPropOriginServerHttps() {
        return _OriginServerHttps;
    }

    /**
     * Getter method for the configuration file property DEVAPI_CLIENT_VERSION
     *
     * @return value of property
     */
    public String getPropApiVersion() {
        return _ApiVersion;
    }

    /**
     * Getter method for the configuration file property DEVAPI_CLIENT_KEY
     *
     * @return value of property
     */
    public String getPropPassKey() {
        return _PassKey;
    }

    /**
     * Getter method for the configuration file property DEVAPI_CLIENT_ENCODING_KEY
     *
     * @return value of property
     */
    public String getPropEncodingKey() {
        return _EncodingKey;
    }

    /**
     * Getter method for the configuration file property DEVAPI_LEGACY_API
     *
     * @return value of property
     */
    public Boolean getPropLegacyApi() {
        return _legacyApi;
    }

    /**
     * Getter method for the configuration file property DEVAPI_CLIENT_NAME
     *
     * @return value of the property
     */
    public String getClientName() {
        return _clientName;
    }

    /**
     * Getter method for the configuration file property DEVAPI_HOSTED_AUTH_DISPLAY_CODE
     *
     * @return value of the property
     */
    public String getHostedAuthDisplayCode() {
        return _hostedAuthDisplayCode;
    }

    /**
     * Getter method for the configuration file property DATABASE_HOSTNAME
     *
     * @return value of property
     */
    public String getDatabaseHostname() {
        return _databaseHostname;
    }

    /**
     * Getter method for the configuration file property DATABASE_USERNAME
     *
     * @return value of property
     */
    public String getDatabaseUsername() {
        return _databaseUsername;
    }

    /**
     * Getter method for the configuration file property DATABASE_PASSWORD
     *
     * @return value of property
     */
    public String getDatabasePassword() {
        return _databasePassword;
    }

    public String getApiVersionUrlParameter() {
        return getApiVersionUrlParameter(getPropApiVersion());
    }

    public String getApiVersionUrlParameter(String version) {
        if (getPropLegacyApi()) {
            return "&ApiVersion=" + version;
        } else {
            return "";
        }
    }

    public String getPasskeyUrlParameter() {
        return String.format("&%s=%s", PARAM_PASS_KEY, getPropPassKey());
    }

    public String getPasskeyInitialUrlParameter() {
        return String.format("?%s=%s", PARAM_PASS_KEY, getPropPassKey());
    }



    public String getUserUrlParameterFromUserToken(String userToken) {
        return String.format("&%s=%s", PARAM_USER, userToken);
    }

    public String getHostedAuthDisplayCodeUrlParameter() {
        return String.format("&%s=%s", PARAM_DISPLAY_CODE, getHostedAuthDisplayCode());
    }

    /*********************************************************************************************
     * Methods for verification
     *********************************************************************************************/

    /**
     * Verify the expected Field Error in the Form response - JSON version
     *
     * @param obj             The response JSON object
     * @param expectedField   The name of the field with an error
     * @param expectedMessage The expected error message
     * @param expectedCode    The expected error code
     * @throws JSONException thrown by JSON methods
     */
    protected void verifyFieldErrorsJSON(JSONObject obj, String expectedField, String expectedMessage, String expectedCode)
            throws JSONException {
        boolean found = false;
        StringBuilder failMsg = new StringBuilder("Expected error message/code not found. Expected Code: " + expectedCode + ", Expected message: " + expectedMessage + "\nActual errors were:");
        if (obj.has("FormErrors")) {
            JSONArray fieldErrorsOrder = obj.getJSONObject("FormErrors").optJSONArray("FieldErrorsOrder");
            if (fieldErrorsOrder != null && fieldErrorsOrder.length() > 0) {
                for (int i = 0; i < fieldErrorsOrder.length(); i++) {
                    String fieldName = fieldErrorsOrder.getString(i);
                    JSONObject error = obj.getJSONObject("FormErrors").getJSONObject("FieldErrors").getJSONObject(fieldName);
                    String actualField = error.getString("Field");
                    String actualMessage = error.getString("Message");
                    String actualCode = error.getString("Code");
                    if (StringUtils.equals(expectedField, actualField) && StringUtils.equals(expectedMessage, actualMessage) && StringUtils.equals(expectedCode, actualCode)) {
                        found = true;
                    }
                    failMsg.append("\nCode: " + actualCode + ", Message: " + actualMessage);
                }
            }
        }
        Assert.assertTrue(found, failMsg.toString());
    }

    /**
     * Log errors, field errors, and group errors and verify expected error
     *
     * @param obj      The response JSON object
     * @param expected Boolean value to indicated whether errors are expected are not
     * @throws JSONException Thrown for JSON parsing errors
     */
    protected void verifyErrorsInResponseJSON(JSONObject obj, boolean expected)
            throws JSONException {
        // verify errors
        if (obj.has("Errors") && obj.getJSONArray("Errors").length() > 0) {
            log("\nErrors...");
            JSONArray errors = obj.getJSONArray("Errors");
            for (int i = 0; i < errors.length(); i++) {
                JSONObject error = errors.getJSONObject(i);
                log("Message: " + error.getString("Message"));
                log("Code   : " + error.getString("Code"));
            }
            log("\n");
        }
        // verify form errors
        if (obj.has("FormErrors")) {
            JSONArray groupErrorsOrder = obj.getJSONObject("FormErrors").optJSONArray("GroupErrorsOrder");
            if (groupErrorsOrder != null && groupErrorsOrder.length() > 0) {
                log("\nGroupErrors...");
                for (int i = 0; i < groupErrorsOrder.length(); i++) {
                    String groupName = groupErrorsOrder.getString(i);
                    JSONObject error = obj.getJSONObject("FormErrors").getJSONObject("GroupErrors").getJSONObject(groupName);
                    log("Group  : " + error.getString("Group"));
                    log("Message: " + error.getString("Message"));
                    log("Code   : " + error.getString("Code"));
                }
            }
            JSONArray fieldErrorsOrder = obj.getJSONObject("FormErrors").optJSONArray("FieldErrorsOrder");
            if (fieldErrorsOrder != null && fieldErrorsOrder.length() > 0) {
                log("\nFieldErrors...");
                for (int i = 0; i < fieldErrorsOrder.length(); i++) {
                    String fieldName = fieldErrorsOrder.getString(i);
                    JSONObject error = obj.getJSONObject("FormErrors").getJSONObject("FieldErrors").getJSONObject(fieldName);
                    log("Field  : " + error.getString("Field"));
                    log("Message: " + error.getString("Message"));
                    log("Code   : " + error.getString("Code"));
                }
            }
            log("\n");
        }
        // verify HasErrors value
        Assert.assertEquals(obj.getBoolean("HasErrors"), expected, "unexpected errors in Submission response");
    }

    /**
     * Verify the expected Field Error in the Form response - XML version
     *
     * @param obj             The response JSON object converted from XML
     * @param expectedField   The name of  the field with an error
     * @param expectedMessage The expected error message
     * @param expectedCode    The expected error code
     * @throws JSONException thrown by JSON methods
     */
    protected void verifyFieldErrorsXML(JSONObject obj, String expectedField, String expectedMessage, String expectedCode)
            throws JSONException {
        boolean found = false;
        StringBuilder failMsg = new StringBuilder("Expected error message/code not found. Expected Code: " + expectedCode + ", Expected message: " + expectedMessage + "\nActual errors were:");
        if (obj.has("FormErrors")) {
            JSONObject formErrors = obj.getJSONObject("FormErrors");
            if (formErrors.has("FieldErrors")) {
                JSONObject fieldErrors = formErrors.optJSONObject("FieldErrors");
                JSONArray fieldErrorArray = fieldErrors.optJSONArray("FieldError");
                if (fieldErrorArray != null) {
                    for (int i = 0; i < fieldErrorArray.length(); i++) {
                        JSONObject error = fieldErrorArray.getJSONObject(i);
                        String actualField = error.getString("field");
                        String actualMessage = error.getString("message");
                        String actualCode = error.getString("code");
                        if (StringUtils.equals(expectedField, actualField) && StringUtils.equals(expectedMessage, actualMessage) && StringUtils.equals(expectedCode, actualCode)) {
                            found = true;
                        }
                        failMsg.append("\nCode: " + actualCode + ", Message: " + actualMessage);
                    }
                } else {
                    JSONObject error = fieldErrors.getJSONObject("FieldError");
                    String actualField = error.getString("field");
                    String actualMessage = error.getString("message");
                    String actualCode = error.getString("code");
                    if (StringUtils.equals(expectedField, actualField) && StringUtils.equals(expectedMessage, actualMessage) && StringUtils.equals(expectedCode, actualCode)) {
                        found = true;
                    }
                    failMsg.append("\nCode: " + actualCode + ", Message: " + actualMessage);
                }
            }
        }
        Assert.assertTrue(found, failMsg.toString());
    }

    /**
     * Log errors, field errors, and group errors and verify expected error
     *
     * @param obj      The response JSON object converted from XML
     * @param expected Boolean value to indicated whether errors are expected are not
     * @throws JSONException Thrown for JSON parsing errors
     */
    protected void verifyErrorsInResponseXML(JSONObject obj, boolean expected)
            throws JSONException {
        // verify errors
        if (obj.has("Errors")) {
            log("\nErrors...");
            JSONArray errorsArray = obj.optJSONArray("Errors");
            if (errorsArray != null) {
                for (int i = 0; i < errorsArray.length(); i++) {
                    JSONObject error = errorsArray.getJSONObject(i);
                    log("Message: " + error.getString("message"));
                    log("Code   : " + error.getString("code"));
                }
            } else {
                JSONObject error = obj.getJSONObject("Errors").getJSONObject("Error");
                log("Message: " + error.getString("message"));
                log("Code   : " + error.getString("code"));
            }
            log("\n");
        }
        if (obj.has("FormErrors")) {
            JSONObject fieldErrors = obj.getJSONObject("FormErrors").optJSONObject("FieldErrors");
            if (fieldErrors != null) {
                log("\nFieldErrors...");
                JSONArray fieldErrorArray = fieldErrors.optJSONArray("FieldError");
                if (fieldErrorArray != null) {
                    for (int i = 0; i < fieldErrorArray.length(); i++) {
                        JSONObject error = fieldErrorArray.getJSONObject(i);
                        log("Message: " + error.getString("message"));
                        log("Code   : " + error.getString("code"));
                    }
                } else {
                    JSONObject error = fieldErrors.getJSONObject("FieldError");
                    log("Message: " + error.getString("message"));
                    log("Code   : " + error.getString("code"));
                }
            }
            JSONObject groupErrors = obj.getJSONObject("FormErrors").optJSONObject("GroupErrors");
            if (groupErrors != null) {
                log("\nGroupErrors...");
                JSONArray groupErrorArray = fieldErrors.optJSONArray("GroupError");
                if (groupErrorArray != null) {
                    for (int i = 0; i < groupErrorArray.length(); i++) {
                        JSONObject error = groupErrorArray.getJSONObject(i);
                        log("Message: " + error.getString("message"));
                        log("Code   : " + error.getString("code"));
                    }
                } else {
                    JSONObject error = fieldErrors.getJSONObject("GroupError");
                    log("Message: " + error.getString("message"));
                    log("Code   : " + error.getString("code"));
                }
            }
        }
        // verify HasErrors value
        Assert.assertEquals(obj.getBoolean("HasErrors"), expected, "unexpected errors in Submission response");
    }

    protected JSONObject verifyCallback(String response, String methodName) {
        Assert.assertTrue(response.startsWith("<script>" + methodName + "("));
        Assert.assertTrue(response.endsWith(");</script>"));
        int substringOffset = response.indexOf(methodName + "(") + methodName.length() + 1;
        JSONObject obj = JSONHelper.parseJSON(response.substring(substringOffset, response.lastIndexOf(");")));
        Assert.assertNotNull(obj);
        Assert.assertFalse(JSONHelper.checkHasErrors(obj), "found unexpected error in callback response");
        return obj;
    }

    /**
     * This method gets the text value of the specified element via XPath
     *
     * @param xml         The xml String
     * @param elementName The name of the element
     * @return The {@link String} value of the Element; the empty string ("") if element is missing
     * @throws Exception
     */
    protected String getElementTextValue(String xml, String elementName)
            throws Exception {
        InputSource source = new InputSource(new StringReader(xml));
        XPathExpression xPathExpression = xPath.compile("//bv:" + elementName + "/text()");
        return xPathExpression.evaluate(source);
    }

    /**
     * When the value of an element is null, that element in
     * the xml has the xsi:nil attribute set to true.  This method will
     * find the value of the xsi:nil attributed on the given element and
     * return true if it exists and is set to true.
     *
     * @param xml         The xml string
     * @param elementName The name of the element
     * @return boolean Returns true if element has xsi:nil set to true
     * @throws Exception
     */
    protected boolean isElementXsiNil(String xml, String elementName)
            throws Exception {
        InputSource source = new InputSource(new StringReader(xml));
        XPathExpression xPathExpression = xPath.compile("//bv:" + elementName + "/@xsi:nil");
        String attrResult = xPathExpression.evaluate(source);
        return attrResult.equals("true");
    }

    /**
     * In order for XPath to find the appropriate data, it is necessary
     * to set the namespace contexts found in the xml.  This static
     * block creates a {@link NamespaceContext} and sets it on the
     * static {@link XPath} object.
     */

    private NamespaceContext ctx;
    private final XPathFactory factory = XPathFactory.newInstance();
    private final XPath xPath = factory.newXPath();

    private void initXPath() {
        ctx = new NamespaceContext() {
            public String getNamespaceURI(String prefix) {
                String uri;
                if (prefix.equals("bv")) {
                    uri = "http://www.bazaarvoice.com/xs/DataApiSubmit/" + getPropApiVersion();
                } else if (prefix.equals("xsi")) {
                    uri = "http://www.w3.org/2001/XMLSchema-instance";
                } else {
                    uri = null;
                }
                return uri;
            }

            // Dummy implementation - not used!
            public Iterator getPrefixes(String val) {
                return null;
            }

            // Dummy implementation - not used!
            public String getPrefix(String uri) {
                return null;
            }
        };
        xPath.setNamespaceContext(ctx);
    }

    /**
     * Verifies that the response element contains exactly the elements in expectedElements
     * i.e. - Contains all elements in expected elements and no extra elements
     *
     * @param response         The JSON response object
     * @param expectedElements The list of expected elements that should make up the reponse
     */
    protected void verifyResponseElements(JSONObject response, List<String> expectedElements) {
        Iterator responseElementKeys = response.keys();
        int count = 0;
        while (responseElementKeys.hasNext()) {
            count++;
            Assert.assertTrue(expectedElements.contains((String) responseElementKeys.next()));
        }
        Assert.assertEquals(count, response.length());
    }
}
