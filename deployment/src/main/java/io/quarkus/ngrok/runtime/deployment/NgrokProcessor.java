package io.quarkus.ngrok.runtime.deployment;

import static io.quarkus.deployment.annotations.ExecutionTime.RUNTIME_INIT;

import org.eclipse.microprofile.config.ConfigProvider;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.ServiceStartBuildItem;
import io.quarkus.ngrok.runtime.NgrokRecorder;

class NgrokProcessor {

    @BuildStep
    @Record(RUNTIME_INIT)
    public void build(NgrokRecorder recorder,
            BuildProducer<ServiceStartBuildItem> serviceStart) {
        recorder.init(getPort());
        serviceStart.produce(new ServiceStartBuildItem("ngrok"));
    }

    private String getPort() {
        return ConfigProvider.getConfig().getOptionalValue("quarkus.http.port", String.class).orElse("8080");
    }
}
