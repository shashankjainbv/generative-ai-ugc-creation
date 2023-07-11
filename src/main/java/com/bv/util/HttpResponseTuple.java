package com.bv.util;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseTuple {

    private final String _response;
    private final int _responseCode;
    private final Map<String,String> _headers = new HashMap<String, String>();

    HttpResponseTuple(String response, int responseCode, Header[] responseHeaders) {
        _response = response;
        _responseCode = responseCode;
        for (Header header : responseHeaders) {
            _headers.put(header.getName(), header.getValue());
        }
    }

    public int getResponseCode() {return _responseCode;}
    public String getResponse() {return _response;}
    public Map<String, String> getHeaders() { return _headers; }
}
