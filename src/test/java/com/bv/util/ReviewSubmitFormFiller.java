package com.bv.util;

import org.json.JSONException;
import org.json.JSONObject;

public class ReviewSubmitFormFiller extends AbstractSubmitFormFiller {

    public ReviewSubmitFormFiller(JSONObject formObj, boolean requiredFieldsOnly) throws JSONException {
        this(formObj, requiredFieldsOnly, DEFAULT_TEXT_PREFIX);
    }

    public ReviewSubmitFormFiller(JSONObject formObj, boolean requiredFieldsOnly, String prefix) throws JSONException {
        super(formObj, requiredFieldsOnly, prefix);
    }

    public String getReviewText() {
        return getValueByName("reviewtext");
    }

    public String getTitle() {
        return getValueByName("title");
    }

    public String getRating() {
        return getValueByName("rating");
    }

    @Override
    protected String getTextInput(JSONObject field) throws JSONException {
        // special cases...
        String id = getId(field);
        int minLength = getMinLength(field);
        if (id.contains("usernickname")) return StringHelper.randomString(6);
        if (id.contains("url")) return null;
        if (id.contains("productrecommendation")) return null;
        if (id.contains("title")) minLength = 10;
        if (id.contains("tag_")) return null;
        return getTextInput(id, minLength, getMaxLength(field));
    }

    @Override
    protected String getTextAreaInput(JSONObject field) throws JSONException {
        // special cases...
        String id = getId(field);
        return getTextAreaInput(id, getMinLength(field), getMaxLength(field));
    }
}
