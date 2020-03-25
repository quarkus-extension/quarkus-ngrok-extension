package quarkus.extension.ngrok;

import quarkus.extension.ngrok.configuration.NgrokConfiguration;
import quarkus.extension.ngrok.data.NgrokTunnelResponse;
import quarkus.extension.ngrok.ngrok.NgrokAutoDownload;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@ApplicationScoped
public class NgrokRunner {
    private static final Logger log = Logger.getLogger(NgrokRunner.class);
    private Process process;

    @Inject
    private NgrokAutoDownload ngrokAutoDownload;

    @Inject
    private NgrokConfiguration ngrokConfiguration;


    public void start(@Observes StartupEvent ev) {
        System.out.println(ngrokConfiguration.enabled());
        if (ngrokConfiguration.enabled()) {
            Thread thread = new Thread(() -> {
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
            }, "ngrok-thread");
            thread.start();
        }
        log.info("Ngrok disabled");

    }

    void onStop(@Observes ShutdownEvent ev) {
        if (Objects.nonNull(process)) {
            log.info("Stopping ngrok");
            process.destroy();
        }
    }

    private void startupNgrok(String port) {
        String command = getNgrokExecutablePath() + " http " + port;
        execute(command);
    }

    private void execute(String command) {
        log.infof("Starting process command : %s", command);
        try {
            process = Runtime.getRuntime().exec(command);
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

        System.out.println(baseUrl);
        String tunnelEndpoint = baseUrl + "/api/tunnels";
        Client client = ClientBuilder.newClient();
        NgrokTunnelResponse response = client.target(tunnelEndpoint)
                .request()
                .accept("application/json")
                .get(NgrokTunnelResponse.class);
        response.getTunnels().forEach(t -> log.infof("Remote url (%s) -> %s", t.getProto(), t.getPublicUrl()));
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
