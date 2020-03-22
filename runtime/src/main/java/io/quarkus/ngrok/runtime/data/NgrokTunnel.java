package io.quarkus.ngrok.runtime.data;

public class NgrokTunnel {
    private String public_url;

    private String proto;

    public String getPublic_url() {
        return public_url;
    }

    public void setPublic_url(String public_url) {
        this.public_url = public_url;
    }

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }
}
