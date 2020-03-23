package io.quarkus.ngrok.runtime.deployment;

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
}
