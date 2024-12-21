package com.own.filemanager.backend.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("blob")
public class BlobProperties {
    private String connectionStr = null;
    private String endpointString = null;
    

    public String getConnectionStr() {
        return this.connectionStr;
    }

    public void setConnectionStr(String connString) {
        this.connectionStr = connString;
    }

    public String getUrlPrefix() {
        return this.endpointString;
    }

    public void setUrlPrefix() {
        this.endpointString = "https://thisisnottaken.blob.core.windows.net/";
    }
}
