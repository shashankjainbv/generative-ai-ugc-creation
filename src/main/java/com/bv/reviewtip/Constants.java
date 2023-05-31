package com.bv.reviewtip;

public class Constants {
    public static final String PROMPT_INPUT = "Act as a product reviews expert and influencer.Your instructions are to " +
            "give me tips and ideas to write a helpful and thorough product review. You can only give " +
            "me up to 5 simple two sentence tips in bullet format. You will use the tones - brief, crisp " +
            "and succinct. Make sure that the reviews are honest.Do not return any explanations or long paragraphs." +
            " Do not give tips on comparison with competitors or mention any retailers. Do not use any leading " +
            "language and make sure to keep the tips unbiased.The product name is %s The brand name is %s.";

    public static final String MODEL = "gpt-3.5-turbo";

    public static final int MAX_TOKENS = 1000;

    public static final String ROLE = "user";
    public static final String ACCEPT = "Accept";
    public static final String APPLICATION_JSON = "application/json";
    public static final String CONTENT_TYPE = "content-type";
    public static final String PRODUCTS_API_URL = "%s://%s/data/products.json?passkey=%s&apiversion=5.4&filter=id:%s";
    public static final String OPENAPI_URL = "https://api.openai.com/v1/chat/completions";
    public static final String OPENAPI_KEY = "openapi-key";
}
