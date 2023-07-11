package com.bv.util;

import org.json.JSONException;
import org.json.JSONObject;

public class QuestionSubmitFormFiller extends AbstractSubmitFormFiller {

    public QuestionSubmitFormFiller(JSONObject formObj, boolean requiredFieldsOnly) throws JSONException {
        this(formObj, requiredFieldsOnly, DEFAULT_TEXT_PREFIX);
    }

    public QuestionSubmitFormFiller(JSONObject formObj, boolean requiredFieldsOnly, String prefix) throws JSONException {
        super(formObj, requiredFieldsOnly, prefix);
    }

    public String getQuestionSummary() {
        return getValueByName("questionsummary");
    }

    @Override
    protected String getTextInput(JSONObject field) throws JSONException {
        // special cases...
        String id = getId(field);
        int maxLength = getMaxLength(field);
        if (id.contains("usernickname")) return StringHelper.randomString(6);
        if (id.contains("url")) return null;
        if (id.contains("tag_")) return null;
        if (id.contains("questionsummary")) maxLength = 150;
        return getTextInput(id, getMinLength(field), maxLength);
    }

    @Override
    protected String getTextAreaInput(JSONObject field) throws JSONException {
        // special cases...
        String id = getId(field);
        return getTextAreaInput(id, getMinLength(field), getMaxLength(field));
    }
}
