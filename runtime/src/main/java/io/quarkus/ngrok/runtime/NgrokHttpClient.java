package io.quarkus.ngrok.runtime;

import javax.enterprise.context.Dependent;
import javax.ws.rs.*;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.quarkus.ngrok.runtime.data.NgrokTunnelResponse;

/**
 * Used to test default @{@link Dependent} scope defined on interface
 */
@RegisterRestClient(baseUri = "http://localhost:4040/api/tunnels")
@Path("/")
public interface NgrokHttpClient {

    @GET
    @Consumes("application/json")
    @Produces("application/json")
    NgrokTunnelResponse get();

}
