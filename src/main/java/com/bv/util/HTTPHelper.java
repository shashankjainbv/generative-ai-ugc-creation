package com.bv.util;

import com.bazaarvoice.jolt.JsonUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTPHelper {

    private static CloseableHttpClient client = null;

    /**
     * Get a HTTP client that can be used with HTTPS connections and Proxy servers
     * @param ps The settings to be used for the Proxy server connection
     * @return The HTTP client
     */
    private static CloseableHttpClient getHttpClient(ProxySettings ps) {
        if (client == null) {
            try {
                // trust all certificates
                SSLContextBuilder contextBuilder = new SSLContextBuilder();
                contextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(contextBuilder.build());

                // Create global request configuration - can be overridden later
                RequestConfig defaultRequestConfig = RequestConfig.custom()
                        .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                        .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                        .build();

                // build the client
                HttpClientBuilder clientBuilder = HttpClients.custom();
                clientBuilder.setSSLSocketFactory(sslsf);
                clientBuilder.setDefaultRequestConfig(defaultRequestConfig);
                if (ps != null) {
                    // Use custom credentials provider
                    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    credentialsProvider.setCredentials(
                            new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM, "basic"),
                            new UsernamePasswordCredentials(ps.getProxyUsername(), ps.getProxyPassword()));

                    // set proxy server and credentials on client
                    clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    clientBuilder.setProxy(new HttpHost(ps.getProxyHost(), Integer.parseInt(ps.getProxyPort())));
                }

                client = clientBuilder.build();
            } catch (Exception ex) {
                return null;
            }
        }
        return client;
    }

    /**
     * Perform a HTTP GET method with the specified URL
     * @param strURL The URL for the HTTP GET method
     * @return String containing the response
     * @throws IOException Exception thrown by the IO operations
     */
    public static String request(String strURL) throws IOException {
        return request(null, strURL, null).getResponse();
    }

    /**
     * Perform a HTTP GET method with the specified URL
     * @param strURL The URL for the HTTP GET method
     * @return HttpResponseTuple containing the response
     * @throws IOException Exception thrown by the IO operations
     */
    public static HttpResponseTuple requestTuple(String strURL) throws IOException {
        return request(null, strURL, null);
    }

    /**
     * Perform a HTTP GET method with the specified URL using the specified Proxy settings.
     * @param ps ProxySettings object
     * @param strURL The URL for the HTTP GET method
     * @return HttpResponseTuple containing the response and response code
     * @throws IOException Exception thrown by the IO operations
     */
    public static HttpResponseTuple request(ProxySettings ps, String strURL) throws IOException {
        return request(ps, strURL, null);
    }

    /**
     * Perform a HTTP GET method with the specified URL using the specified Proxy settings and headers.
     * @param ps ProxySettings object
     * @param strURL The URL for the HTTP GET method
     * @param headers Map of headers to send in the request
     * @return HttpResponseTuple containing the response and response code
     * @throws IOException Exception thrown by the IO operations
     */
    public static HttpResponseTuple request(ProxySettings ps, String strURL, Map<String,String> headers) throws IOException {
        String result = "";
        CloseableHttpClient httpclient = getHttpClient(ps);
        HttpGet httpget = new HttpGet(strURL);
        if (headers != null) {
            for (String key : headers.keySet()) {
                httpget.addHeader(key, headers.get(key));
            }
        }

        // execute the request
        CloseableHttpResponse response = httpclient.execute(httpget);
        int responseCode = response.getStatusLine().getStatusCode();
        Header[] responseHeaders = response.getAllHeaders();
        // get the response
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
                    StringBuilder resBldr = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        resBldr.append(line);
                    }
                    result = resBldr.toString();
                } finally {
                    instream.close();
                }
            }
            EntityUtils.consume(entity);
        }
        finally {
            response.close();
        }
        return new HttpResponseTuple(result, responseCode, responseHeaders);
    }

    public static HttpResponseTuple request(String strURL, Map<String,String> headers) throws IOException {
        String result = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(strURL);
        if (headers != null) {
            for (String key : headers.keySet()) {
                httpget.addHeader(key, headers.get(key));
            }
        }

        // execute the request
        CloseableHttpResponse response = httpclient.execute(httpget);
        int responseCode = response.getStatusLine().getStatusCode();
        Header[] responseHeaders = response.getAllHeaders();
        // get the response
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
                    StringBuilder resBldr = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        resBldr.append(line);
                    }
                    result = resBldr.toString();
                } finally {
                    instream.close();
                }
            }
            EntityUtils.consume(entity);
        }
        finally {
            response.close();
        }
        return new HttpResponseTuple(result, responseCode, responseHeaders);
    }
    /**
     * Perform a HTTP POST method with the specified URL and form data.
     * Uses an encoded form entity for the POST method.
     * @param strURL The URL for the HTTP POST method
     * @param formparams The list containing the form data as name/value pairs
     * @return String containing the response
     * @throws IOException Exception thrown by the IO operations
     */


    /**
     * Perform a HTTP POST method with the specified URL and form data using the Proxy settings
     * Uses an encoded form entity for the POST method.
     * @param ps ProxySettings object
     * @param strURL The URL for the HTTP POST method
     * @param data The list containing the form data as name/value pairs
     * @param headers The request headers
     * @return HttpResponseTuple containing the response and response code
     * @throws IOException Exception thrown by the IO operations
     */
    public static HttpResponseTuple submit(ProxySettings ps, String strURL, List<NameValuePair> data, Map<String,String> headers) throws IOException {
        String result = "";
        CloseableHttpClient httpclient = getHttpClient(ps);
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(data, "UTF-8");
        HttpPost httppost = new HttpPost(strURL);
        httppost.setEntity(formEntity);
        if (headers != null) {
            for (String key : headers.keySet()) {
                httppost.addHeader(key, headers.get(key));
            }
        }

        // execute the request
        CloseableHttpResponse response = httpclient.execute(httppost);
        int responseCode = response.getStatusLine().getStatusCode();
        Header[] responseHeaders = response.getAllHeaders();
        // get the response
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
                    StringBuilder resBldr = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        resBldr.append(line);
                    }
                    result = resBldr.toString();
                } finally {
                    instream.close();
                }
            }
            EntityUtils.consume(entity);
        }
        finally {
            response.close();
        }
        return new HttpResponseTuple(result, responseCode, responseHeaders);
    }

    public static HttpResponseTuple submit(String strURL, List<NameValuePair> data, Map<String,String> headers) throws IOException {
        String result = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(data, "UTF-8");
        HttpPost httppost = new HttpPost(strURL);
        httppost.setEntity(formEntity);
        if (headers != null) {
            for (String key : headers.keySet()) {
                httppost.addHeader(key, headers.get(key));
            }
        }

        // execute the request
        CloseableHttpResponse response=null;
        try {
             response = httpclient.execute(httppost);
        }
        catch(Exception e){
            System.out.println("Request failed");
        }
        int responseCode = response.getStatusLine().getStatusCode();

        HttpEntity responseEntity = response.getEntity();
                String responseBody = EntityUtils.toString(responseEntity);
                Map<String, Object> responseJson = JsonUtils.jsonToMap(responseBody);
        Header[] responseHeaders = response.getAllHeaders();

       if(responseCode==200 && !responseJson.isEmpty())
           System.out.println("Submitted object "+ responseJson.toString());
        // get the response
        try {
            if (responseEntity != null) {
                InputStream instream = responseEntity.getContent();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
                    StringBuilder resBldr = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        resBldr.append(line);
                    }
                    result = resBldr.toString();
                } finally {
                 //   instream.close();
                }
            }
          //  EntityUtils.consume(responseEntity);
        }
        finally {
           // response.close();
        }
        return new HttpResponseTuple(result, responseCode, responseHeaders);
    }

    /**
     * Perform a HTTP POST method to upload a photo or video file using the Proxy settings.
     * Uses an multi-part encoded form entity for the POST method and uploads the
     * file specified in the pathname.
     * @param ps ProxySettings object
     * @param strURL The URL for the HTTP POST method
     * @param data The list containing the form data as name/value pairs
     * @param binType Specify either "photo" or "video"
     * @param pathname Specify the full path to the file to be uploaded
     * @param mimeType Specify a MIME type ("image/png" or "image/jpeg")
     * @return String containing the response
     * @throws IOException Exception thrown by the IO operations
     */
    public static String upload(ProxySettings ps, String strURL, HashMap<String, String> data, String binType,
                                String pathname, String mimeType) throws IOException {
        return upload(ps, strURL, data, binType, String.valueOf(new File(pathname)), mimeType);
    }

//    public static String upload(ProxySettings ps, String strURL, HashMap<String, String> data, String binType,
//                                File resource, String mimeType) throws IOException {
//        String result = "";
//        CloseableHttpClient httpclient = getHttpClient(ps);
//        HttpPost httppost = new HttpPost(strURL);
//
//        // build up multi-part request entity
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        builder.addBinaryBody(binType, resource, ContentType.create(mimeType), resource.getName());
//        for (Map.Entry<String, String> entry : data.entrySet()) {
//            builder.addTextBody(entry.getKey(), entry.getValue(), ContentType.TEXT_PLAIN);
//        }
//        HttpEntity reqEntity = builder.build();
//        httppost.setEntity(reqEntity);
//
//        // execute the request
//        CloseableHttpResponse response = httpclient.execute(httppost);
//        // get the response
//        try {
//            HttpEntity entity = response.getEntity();
//            if (entity != null) {
//                InputStream instream = entity.getContent();
//                try {
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
//                    StringBuilder resBldr = new StringBuilder();
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        resBldr.append(line);
//                    }
//                    result = resBldr.toString();
//                } finally {
//                    instream.close();
//                }
//            }
//            EntityUtils.consume(entity);
//        }
//        finally {
//            response.close();
//        }
//        return result;
//    }

    public static void download(String strURL, String filename) throws Exception {
        download(null, strURL, filename);
    }

    public static void download(ProxySettings ps, String strURL, String filename) throws Exception {
        CloseableHttpClient httpclient = getHttpClient(ps);
        HttpGet httpget = new HttpGet(strURL);

        // execute the request
        CloseableHttpResponse response = httpclient.execute(httpget);
        int status = response.getStatusLine().getStatusCode();
        if (status != 200) {
            throw new Exception(response.getStatusLine().toString());
        }
            
        // get the response
        try {
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                InputStream inputStream = null;
                OutputStream out = null;
                try {
                    inputStream = resEntity.getContent();
                    out = new FileOutputStream(filename);

                    int read;
                    byte[] bytes = new byte[1024];

                    while ((read = inputStream.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                }
            }
            EntityUtils.consume(resEntity);
        }
        finally {
            response.close();
        }
    }
}

