package com.bv.util;

/**
 * Created by IntelliJ IDEA.
 * User: robert.moody
 * Date: 1/2/12
 * Time: 6:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProxySettings {

    private static String _proxyHost = null;
    private static String _proxyPort = null;
    private static String _proxyUsername = null;
    private static String _proxyPassword = null;

    public void setProxyHost(String value) {
        _proxyHost = value;
    }

    public void setProxyPort(String value) {
        _proxyPort = value;
    }

    public void setProxyUsername(String value) {
        _proxyUsername = value;
    }

    public void setProxyPassword(String value) {
        _proxyPassword = value;
    }

    public String getProxyHost() {
        return _proxyHost;
    }

    public String getProxyPort() {
        return _proxyPort;
    }

    public String getProxyUsername() {
        return _proxyUsername;
    }

    public String getProxyPassword() {
        return _proxyPassword;
    }
}
