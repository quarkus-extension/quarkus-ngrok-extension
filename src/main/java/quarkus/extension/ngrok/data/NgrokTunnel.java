package quarkus.extension.ngrok.data;


import javax.json.bind.annotation.JsonbProperty;

public class NgrokTunnel {
    @JsonbProperty("public_url")
    private String publicUrl;
    private String proto;

    public String getPublicUrl() {
        return publicUrl;
    }

    public void setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
    }

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }
}
