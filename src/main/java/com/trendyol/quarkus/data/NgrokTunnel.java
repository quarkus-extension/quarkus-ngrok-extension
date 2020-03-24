package com.trendyol.quarkus.data;

import javax.json.bind.annotation.JsonbProperty;

public class NgrokTunnel {
    @JsonbProperty("public_url")
    private String publicUrl;

    private String proto;

    public String getPublicUrl() {
        return publicUrl ;
    }

    public void setPublicUrl(String public_url) {
        this.publicUrl= public_url;
    }

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }
}
