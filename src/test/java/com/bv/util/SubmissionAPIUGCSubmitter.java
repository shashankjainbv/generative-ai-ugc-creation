package com.bv.util;


import com.bazaarvoice.jolt.JsonUtils;
import com.bv.textcompletion.GPTResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;


public class SubmissionAPIUGCSubmitter extends SubmissionTestBase {
    private static final String PASSKEY = "caBZoE5X0dmsywGCMoQmo6OPymWLQFnY37VernuC3SGkY";
    private static final String PRODUCT_ID = "6K648UA_BVEP_ABA";
    private static final String LOCALE = "en_US";
    private static final int NUMBER_OF_REVIEWS_TO_SUBMIT = 5;
    private static final int NUMBER_OF_QUESTIONS_TO_SUBMIT = 1;

    public void submitReview() throws IOException, JSONException {
        //  CloseableHttpResponse httpResponse = null;
        //replace productID and passkey
//        String aiText = getAIUGCText(PASSKEY, PRODUCT_ID, "review");


        String[] reviewText = getAIUGCText(PASSKEY, PRODUCT_ID, "review").split("-");
        String[] reviewTitle = getAIUGCText(PASSKEY, PRODUCT_ID, "reviewtitle").split("-");


        for (int i = 0; i < Math.min(NUMBER_OF_REVIEWS_TO_SUBMIT, reviewText.length/3); i++) {
            String reviewTextPerSubmission = "";
            String reviewTitlePerSubmission = "";

            String wordsReviewText[] = (reviewText[i] + reviewText[i + 1]+reviewText[i + 2].replaceAll("\\n[0-9]]", "").replaceAll("\\n","")).split(" ");
            String wordsReviewTitle[] = (reviewTitle[i] + reviewTitle[i + 1] + reviewTitle[i + 2].replaceAll("\\n[0-9]]", "").replaceAll("\\n","")).split(" ");
            for (int j = 0; j <Math.min(wordsReviewText.length,15); j++) {
                reviewTextPerSubmission =reviewTextPerSubmission+ wordsReviewText[j] + " ";
            }
            for (int j = 0; j < Math.min(wordsReviewTitle.length,4); j++) {
                reviewTitlePerSubmission =reviewTitlePerSubmission+ wordsReviewTitle[j] + " ";
            }
//
            //  String userId = StringHelper.randomString(8);
            String userNickname = StringHelper.randomString(6);

            // generate submission parameters
            String formUrl = "http://api.bazaarvoice.com/data/submitreview.json?passkey=" + "ik43j1r1xnamanp2xn48pg7jf" + "&ApiVersion=5.4&productid=" + "XYZ123-prod-9-3-ExternalId";
            AbstractSubmitFormFiller submitFormFiller = getFilledFormJSON(ContentType.REVIEWS, formUrl, true);
            submitFormFiller.setNameAndValue("passkey", "ik43j1r1xnamanp2xn48pg7jf");
            submitFormFiller.setNameAndValue("apiversion", "5.4");
            submitFormFiller.setNameAndValue("Action", "submit");
            submitFormFiller.setNameAndValue("Locale", "en_US");
            // submitFormFiller.setNameAndValue("UserId", userId);
            submitFormFiller.setNameAndValue("usernickname", userNickname);
            submitFormFiller.setNameAndValue("productid", "XYZ123-prod-9-3-ExternalId");
            // submitFormFiller.setNameAndValue("reviewtext", reviewText[i]);
            submitFormFiller.setNameAndValue("reviewtext", reviewTextPerSubmission);
//            submitFormFiller.setNameAndValue("reviewtext","test multiple things that we want to test for testing purpose");
            submitFormFiller.setNameAndValue("rating", "4");
            submitFormFiller.setNameAndValue("contextdatavalue_LengthOfOwnership", "3months");
            submitFormFiller.setNameAndValue("rating_Design", "4");
            submitFormFiller.setNameAndValue("contextdatavalue_Age", "18to24");
            submitFormFiller.setNameAndValue("isRecommended", "true");
            submitFormFiller.setNameAndValue("title", reviewTitlePerSubmission);
//            submitFormFiller.setNameAndValue("title", "this is a test title");

            // preview
            JSONObject obj = JSONHelper.parseJSON(doSubmit("http://api.bazaarvoice.com/data/submitreview.json", submitFormFiller.getFormParams()));
            //   System.out.println("Submitted JSON object"+obj.toString());
            // verifyErrorsInResponseJSON(obj, false);

        }
    }

    public void submitQuestion() throws IOException, JSONException {
        String[] questionText = getAIUGCText(PASSKEY, PRODUCT_ID, "question").split("\n");
        String[] questionSummary = getAIUGCText(PASSKEY, PRODUCT_ID, "question").split("\n");

        for (int i = 0; i < Math.min(NUMBER_OF_QUESTIONS_TO_SUBMIT, questionText.length/2); i++) {
            String questionTextPerSubmission = "";
            String questionSummaryPerSubmission = "";


            String wordsQuestionText[] = (questionText[i]+questionText[i+1].replaceAll("\\n[0-9]]", "").replaceAll("\\n","").replaceAll("-","")).split(" ");
            String wordsQuestionSummary[] = (questionSummary[i].replaceAll("\\n[0-9]]", "").replaceAll("\\n","").replaceAll("-","")).split(" ");
            for (int j = 0; j <Math.min(wordsQuestionText.length,25); j++) {
                questionTextPerSubmission =questionTextPerSubmission+ wordsQuestionText[j] + " ";
            }
            for (int j = 0; j <Math.min(wordsQuestionSummary.length,25); j++) {
                questionSummaryPerSubmission =questionSummaryPerSubmission+ wordsQuestionSummary[j] + " ";
            }
//
            //  String userId = StringHelper.randomString(8);
            String userNickname = StringHelper.randomString(6);

            // generate submission parameters
            String formUrl = "http://api.bazaarvoice.com/data/submitquestion.json?passkey=" + "ik43j1r1xnamanp2xn48pg7jf" + "&ApiVersion=5.4&productid=" + "XYZ123-prod-9-3-ExternalId";
            AbstractSubmitFormFiller submitFormFiller = getFilledFormJSON(ContentType.QUESTIONS, formUrl, true);
            submitFormFiller.setNameAndValue("passkey", "ik43j1r1xnamanp2xn48pg7jf");
            submitFormFiller.setNameAndValue("apiversion", "5.4");
            submitFormFiller.setNameAndValue("Action", "submit");
            submitFormFiller.setNameAndValue("Locale", "en_US");
            // submitFormFiller.setNameAndValue("UserId", userId);
            submitFormFiller.setNameAndValue("usernickname", userNickname);
            submitFormFiller.setNameAndValue("productid", "XYZ123-prod-9-3-ExternalId");
            // submitFormFiller.setNameAndValue("reviewtext", reviewText[i]);
            submitFormFiller.setNameAndValue("questiontext", questionTextPerSubmission);
//            submitFormFiller.setNameAndValue("reviewtext","test multiple things that we want to test for testing purpose");
           // submitFormFiller.setNameAndValue("rating", "4");
           // submitFormFiller.setNameAndValue("contextdatavalue_LengthOfOwnership", "3months");
          //  submitFormFiller.setNameAndValue("rating_Design", "4");
            submitFormFiller.setNameAndValue("displayasanonymous", "false");
            submitFormFiller.setNameAndValue("agreedtotermsandconditions", "true");
            submitFormFiller.setNameAndValue("questionsummary", questionSummaryPerSubmission);
//            submitFormFiller.setNameAndValue("title", "this is a test title");

            // preview
            JSONObject obj = JSONHelper.parseJSON(doSubmit("http://api.bazaarvoice.com/data/submitquestion.json", submitFormFiller.getFormParams()));
            //   System.out.println("Submitted JSON object"+obj.toString());
            // verifyErrorsInResponseJSON(obj, false);

        }
    }

    private String getAIUGCText(String passkey, String productId, String ugc) throws IOException {
        GPTResponse response = null;
        String URI = "http://localhost:8080/data/" + ugc + "tip?passkey=" + passkey + "&productid=" + productId;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(URI);

        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        if (httpResponse.getStatusLine().getStatusCode() == 401) {
            response = new GPTResponse("OpenApi key is not authorized to perform this action.", "", 401);
            return response.getError();
        }

        try {
            HttpEntity responseEntity = httpResponse.getEntity();
            String responseBody = EntityUtils.toString(responseEntity);
            Map<String, Object> responseJson = JsonUtils.jsonToMap(responseBody);
            String data = (String) responseJson.get("data");
            return data;
        } catch (Exception e) {
            response = new GPTResponse("Something went wrong while getting the response from openApi", "", 500);
            return response.getError();
        } finally {
            httpResponse.close();
        }
    }


    public static void main(String[] args) throws IOException, JSONException {
        SubmissionAPIUGCSubmitter submissionAPIUGCSubmitter = new SubmissionAPIUGCSubmitter();
        //submissionAPIUGCSubmitter.submitReview();
        submissionAPIUGCSubmitter.submitQuestion();
    }

}
