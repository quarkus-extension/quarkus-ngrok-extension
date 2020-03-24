package io.quarkus.ngrok.runtime;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.jboss.logging.Logger;

import io.quarkus.ngrok.runtime.data.NgrokTunnelResponse;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class NgrokRecorder {

    private static final Logger log = Logger.getLogger(NgrokRecorder.class);

    public void init(NgrokBuildTimeConfig ngrokBuildTimeConfig, String port) {

        if (ngrokBuildTimeConfig.enabled) {
            Thread thread = new Thread(() -> {
                String command = "ngrok http " + port;
                execute(command);
                try {
                    Thread.sleep(1000);
                    logNgrokResult(ngrokBuildTimeConfig.ngrokHttpUrl);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "ngrok-thread");

            thread.start();
        }

    }

    private void execute(String command) {
        log.info("Starting ngrok");
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            log.error("Error occured when execute ngrok", e);
        }
    }

    private void logNgrokResult(String baseUrl) {
        String tunnelEndpoint = baseUrl + "/api/tunnels";
        Client client = ClientBuilder.newClient();
        NgrokTunnelResponse response = client.target(tunnelEndpoint)
                .request()
                .accept("application/json")
                .get(NgrokTunnelResponse.class);
        response.getTunnels().forEach(t -> log.infof("Remote url (%s) -> %s", t.getProto(), t.getPublic_url()));
        client.close();

    }
}
