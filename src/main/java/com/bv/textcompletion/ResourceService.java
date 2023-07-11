package com.bv.textcompletion;

import com.bazaarvoice.jolt.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bazaarvoice.jolt.utils.JoltUtils.navigate;
import static com.bazaarvoice.jolt.utils.JoltUtils.navigateOrDefault;
import static com.bv.textcompletion.Constants.*;

@Service
@Slf4j
public class ResourceService {
    @Autowired
    AppProperties appProperties;

    public GPTResponse getReviewTipsBased(String passkey, String id) throws IOException {
        Map<String, String> productInfoMap = getProductInfo(passkey, id);
        if (productInfoMap.isEmpty()) {
            log.error("Requested product id is not present or doesnt have any information. Please check if the product id is valid.");
            return new GPTResponse("Requested product id is not present or doesnt have any information. Please check if the product id is valid.", "", 400);
        }
        return getGPTResponse(productInfoMap.get("productName"), productInfoMap.get("brandName"),"review");
    }

    public GPTResponse getQuestionTipsBased(String passkey, String id) throws IOException {
        Map<String, String> productInfoMap = getProductInfo(passkey, id);
        if (productInfoMap.isEmpty()) {
            log.error("Requested product id is not present or doesnt have any information. Please check if the product id is valid.");
            return new GPTResponse("Requested product id is not present or doesnt have any information. Please check if the product id is valid.", "", 400);
        }
        return getGPTResponse(productInfoMap.get("productName"), productInfoMap.get("brandName"),"question");
    }

    private Map<String, String> getProductInfo(String passkey, String id) {
        Map<String, String> productInfoMap = new HashMap<>();
        try {
            String url = String.format(PRODUCTS_API_URL, appProperties.getProtocol(), appProperties.getHost(), passkey, id);
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader(CONTENT_TYPE, APPLICATION_JSON);
            httpGet.addHeader(ACCEPT, APPLICATION_JSON);

            HttpClientBuilder clientBuilder = HttpClientBuilder.create();
            HttpClient client = clientBuilder.build();
            HttpResponse response = client.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();
            String responseBody = EntityUtils.toString(responseEntity);
            Map<String, Object> apiResponse = JsonUtils.jsonToMap(responseBody);
            List<Object> results = navigate(apiResponse, "Results");

            if (results.size() > 0) {
                productInfoMap.put("productName", navigateOrDefault("", results.get(0), "Name"));
                productInfoMap.put("brandName", navigateOrDefault("", results.get(0), "Brand", "Name"));
            }

            return productInfoMap;
        } catch (Exception exception) {
            log.error("Error occurred during products api call.", exception);
        }
        return productInfoMap;
    }

    private GPTResponse getGPTResponse(String productName, String brandName,String ugc) throws IOException {
        GPTResponse response = null;

        if (StringUtils.isEmpty(productName)) {
            log.error("Product name is not present in the api response.");
            response = new GPTResponse("Product name is not present in the api response.", "", 400);
            return response;
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(OPENAPI_URL);
        String secretValue = SecretManager.getSecretValue(appProperties.getSecretName(), appProperties.getProfile(), appProperties.getRegion(), appProperties.getIsLocal());

        String chatInput="";
        if(ugc.equalsIgnoreCase("review")){
            chatInput=PROMPT_INPUT_REVIEW;
        }
        if(ugc.equalsIgnoreCase("question")){
            chatInput=PROMPT_INPUT_QUESTION;
        }

        String inputText = String.format(chatInput, productName, brandName);

        httpPost.addHeader(CONTENT_TYPE, APPLICATION_JSON);
        httpPost.addHeader("Authorization", "Bearer " + JsonUtils.jsonToMap(secretValue).get(OPENAPI_KEY));
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("role", ROLE);
        inputMap.put("content", inputText);
        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("model", MODEL);
        requestBodyMap.put("max_tokens", MAX_TOKENS);
        requestBodyMap.put("messages", Collections.singletonList(inputMap));

        StringEntity entity = new StringEntity(JsonUtils.toJsonString(requestBodyMap));
        httpPost.setEntity(entity);
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        if (httpResponse.getStatusLine().getStatusCode() == 401) {
            response = new GPTResponse("OpenApi key is not authorized to perform this action.", "", 401);
            return response;
        }

        try {
            HttpEntity responseEntity = httpResponse.getEntity();
            String responseBody = EntityUtils.toString(responseEntity);
            Map<String, Object> responseJson = JsonUtils.jsonToMap(responseBody);
            List<Object> choices = (List<Object>) responseJson.get("choices");
            for (Object choice : choices) {
                Map<String, Object> message = (Map<String, Object>)((Map<String, Object>)choice).get("message");
                response = new GPTResponse("", (String) message.get("content"), httpResponse.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new GPTResponse("Something went wrong while getting the response from openApi", "", 500);
            return response;
        } finally {
            httpResponse.close();
        }
        return response;
    }
}
