package quarkus.extension.ngrok.ngrok;

import io.quarkus.runtime.StartupEvent;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;
import quarkus.extension.ngrok.configuration.NgrokConfiguration;
import quarkus.extension.ngrok.data.NgrokTunnelResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@ApplicationScoped
public class NgrokRunner {
    private static final Logger log = Logger.getLogger(NgrokRunner.class);

    @Inject
    private NgrokAutoDownload ngrokAutoDownload;

    @Inject
    private NgrokHttpClient httpClient;

    @Inject
    private NgrokConfiguration ngrokConfiguration;


    public void start(@Observes StartupEvent ev) {
        if (ngrokConfiguration.enabled()) {
            Thread thread = new Thread(() -> {
                if (!checkNgrokRunning(ngrokConfiguration.url())) {
                    if (needToDownloadNgrok()) {
                        ngrokAutoDownload.downloadAndExtractNgrokTo(getNgrokDirectoryOrDefault());
                        addPermissionsIfNeeded();
                    }
                    startupNgrok(getPort());
                    try {
                        Thread.sleep(ngrokConfiguration.waitTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    logNgrokResult(ngrokConfiguration.url());

                }

            }, "ngrok-thread");
            thread.start();
        } else {
            log.info("Ngrok disabled");
        }
    }

    private void startupNgrok(String port) {
        String command = getNgrokExecutablePath() + " http " + port;
        execute(command);
    }

    private void execute(String command) {

        log.infof("Starting process command : %s", command);

        try {
            Runtime.getRuntime().exec(command);
        } catch (Exception e) {
            log.error("Error occurred when execute ngrok", e);
        }

    }

    private void addPermissionsIfNeeded() {
        if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC) {
            String chmod = "chmod +x ".concat(getNgrokExecutablePath());

            log.info("Running: " + chmod);

            execute(chmod);
        }
    }

    private void logNgrokResult(String baseUrl) {
        Response response;

        String tunnelEndpoint = baseUrl + "/api/tunnels";
        response = httpClient.getClient().target(tunnelEndpoint)
                .request()
                .accept("application/json")
                .get();
        NgrokTunnelResponse ngrokTunnelResponse = response.readEntity(NgrokTunnelResponse.class);
        ngrokTunnelResponse.getTunnels().forEach(t -> log.infof("Remote url (%s) -> %s", t.getProto(), t.getPublicUrl()));
        response.close();
    }

    private boolean checkNgrokRunning(String baseUrl) {
        Response response = null;
        try {
            response = httpClient.getClient().target(baseUrl)
                    .request()
                    .accept("application/json")
                    .get();
        } catch (Exception e) {
            log.warn("ngrok not running");
        }

        if (Objects.nonNull(response) && 302 == response.getStatus()) {
            response.close();
            return true;
        }

        return false;
    }

    private boolean needToDownloadNgrok() {
        return !Files.isExecutable(Paths.get(getNgrokExecutablePath()));
    }

    private String getNgrokExecutablePath() {
        String executable = SystemUtils.IS_OS_WINDOWS ? "ngrok.exe" : "ngrok";

        return getNgrokDirectoryOrDefault() + File.separator + executable;
    }

    private String getNgrokDirectoryOrDefault() {
        String cs = ngrokConfiguration.customBinary().orElse("");
        return StringUtils.isNotBlank(cs) ? cs : getDefaultNgrokDirectory();
    }

    private String getDefaultNgrokDirectory() {
        return FilenameUtils.concat(FileUtils.getUserDirectory().getPath(), ".ngrok2");
    }

    private String getPort() {
        return ConfigProvider.getConfig().getOptionalValue("quarkus.http.port", String.class).orElse("8080");
    }
}
