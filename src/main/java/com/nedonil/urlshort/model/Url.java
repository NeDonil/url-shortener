package com.nedonil.urlshort.model;

import com.yandex.ydb.table.result.ResultSetReader;

public class Url {

    private String urlId;
    private String path;
    private String key;

    public Url() {}

    public Url(String path, String key) {
        this.path = path;
        this.key = key;
    }

    public Url(String urlId, String path, String key) {
        this.urlId = urlId;
        this.path = path;
        this.key = key;
    }

    public String getUrlId() {
        return urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static Url fromResultSet(ResultSetReader resultSet) {
        var urlId = resultSet.getColumn("url_id").getUtf8();
        var path = resultSet.getColumn("path").getUtf8();
        var key = resultSet.getColumn("key").getUtf8();
        return new Url(urlId, path, key);
    }

}
