package quarkus.extension.ngrok.configuration;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Optional;

@ConfigProperties
public interface NgrokConfiguration {
    @ConfigProperty(name = "enabled", defaultValue = "true")
    Boolean enabled();

    @ConfigProperty(name = "windowsBinaryUrl", defaultValue = "https://bin.equinox.io/c/4VmDzA7iaHb/ngrok-stable-windows-386.zip")
    String windowsBinaryUrl();

    @ConfigProperty(name = "linuxBinaryUrl", defaultValue = "https://bin.equinox.io/c/4VmDzA7iaHb/ngrok-stable-linux-386.zip")
    String linuxBinaryUrl();

    @ConfigProperty(name = "osxBinaryUrl", defaultValue = "https://bin.equinox.io/c/4VmDzA7iaHb/ngrok-stable-darwin-386.zip")
    String osxBinaryUrl();

    @ConfigProperty(name = "windows64BinaryUrl", defaultValue = "https://bin.equinox.io/c/4VmDzA7iaHb/ngrok-stable-windows-amd64.zip")
    String windows64BinaryUrl();

    @ConfigProperty(name = "linux64BinaryUrl", defaultValue = "https://bin.equinox.io/c/4VmDzA7iaHb/ngrok-stable-linux-amd64.zip")
    String linux64BinaryUrl();

    @ConfigProperty(name = "osx64BinaryUrl", defaultValue = "https://bin.equinox.io/c/4VmDzA7iaHb/ngrok-stable-darwin-amd64.zip")
    String osx64BinaryUrl();

    @ConfigProperty(name = "binaryCustom")
    Optional<String> customBinary();

    @ConfigProperty(name = "directory")
    Optional<String> ngrokDirectory();

    @ConfigProperty(name = "http.url", defaultValue = "http://localhost:4040")
    String url();

    @ConfigProperty(name = "wait.time", defaultValue = "1000")
    Integer waitTime();
}
