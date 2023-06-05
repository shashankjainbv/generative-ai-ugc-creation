package com.bv.reviewtip;

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

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bazaarvoice.jolt.utils.JoltUtils.navigate;
import static com.bazaarvoice.jolt.utils.JoltUtils.navigateOrDefault;
import static com.bv.reviewtip.Constants.*;

@Service
@Slf4j
public class ResourceService {
    @Autowired
    AppProperties appProperties;

    public ReviewTipResponse getReviewTipsBased(String passkey, String id) throws IOException {
        Map<String, String> productInfoMap = getProductInfo(passkey, id);
        return getGPTResponse(productInfoMap.get("productName"), productInfoMap.get("brandName"));
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

            productInfoMap.put("productName", navigateOrDefault("", results.get(0), "Name"));
            productInfoMap.put("brandName", navigateOrDefault("", results.get(0), "Brand", "Name"));

            return productInfoMap;
        } catch (Exception exception) {
            log.error("Error occurred during products api call.", exception);
        }
        return productInfoMap;
    }

    private ReviewTipResponse getGPTResponse(String productName, String brandName) throws IOException {
        ReviewTipResponse response = null;

        if (productName.isEmpty()) {
            log.error("Product name is not present in the api response.");
            response = new ReviewTipResponse("Product name is not present in the api response.", "", 400);
            return response;
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(OPENAPI_URL);
        String secretValue = SecretManager.getSecretValue(appProperties.getSecretName(), appProperties.getProfile(), appProperties.getRegion(), appProperties.getIsLocal());

        String inputText = String.format(PROMPT_INPUT, productName, brandName);

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
            response = new ReviewTipResponse("OpenApi key is not authorized to perform this action.", "", 401);
            return response;
        }

        try {
            HttpEntity responseEntity = httpResponse.getEntity();
            String responseBody = EntityUtils.toString(responseEntity);
            Map<String, Object> responseJson = JsonUtils.jsonToMap(responseBody);
            List<Object> choices = (List<Object>) responseJson.get("choices");
            for (Object choice : choices) {
                Map<String, Object> message = (Map<String, Object>)((Map<String, Object>)choice).get("message");
                response = new ReviewTipResponse("", (String) message.get("content"), httpResponse.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new ReviewTipResponse("Something went wrong while getting the response from openApi", "", 500);
            return response;
        } finally {
            httpResponse.close();
        }
        return response;
    }
}
