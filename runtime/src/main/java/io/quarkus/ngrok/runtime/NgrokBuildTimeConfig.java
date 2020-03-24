package io.quarkus.ngrok.runtime;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "ngrok", phase = ConfigPhase.BUILD_TIME)
public class NgrokBuildTimeConfig {

    /**
     * Determine whether to enable the ngrok enabled
     */
    @ConfigItem(defaultValue = "true")
    public boolean enabled;

    /**
     * Ngrok http api url
     */
    @ConfigItem(defaultValue = "http://localhost:4040/")
    public String ngrokHttpUrl;
}
