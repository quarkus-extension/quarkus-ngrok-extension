package quarkus.extension.ngrok.ngrok;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class NgrokHttpClient {
    Client client = new ResteasyClientBuilderImpl()
            .connectTimeout(100, TimeUnit.MILLISECONDS)
            .readTimeout(100, TimeUnit.MILLISECONDS)
            .build();

    public Client getClient() {
        return client;
    }
}
