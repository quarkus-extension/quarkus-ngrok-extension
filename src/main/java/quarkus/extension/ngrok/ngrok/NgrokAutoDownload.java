package quarkus.extension.ngrok.ngrok;

import quarkus.extension.ngrok.NgrokFileExtractUtils;
import quarkus.extension.ngrok.configuration.NgrokConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;


@ApplicationScoped
public class NgrokAutoDownload {

    @Inject
    NgrokConfiguration ngrokConfiguration;

    private static final Logger log = LoggerFactory.getLogger(NgrokAutoDownload.class);

    public void downloadAndExtractNgrokTo(String destinationPath) {
        String downloadedFilePath = downloadNgrokTo(destinationPath);
        NgrokFileExtractUtils.extractArchive(downloadedFilePath, destinationPath);
    }

    public String downloadNgrokTo(String destinationPath) {
        String zipFileName = getFileNameFromUrl(getBinaryUrl());
        String destinationFile = FilenameUtils.concat(destinationPath, zipFileName);

        if (Files.exists(Paths.get(destinationFile))) {
            log.info("Skipping downloading, cached archive available at {}", destinationFile);

            return destinationFile;
        }

        log.info("Downloading ngrok from {} to {}", getBinaryUrl(), destinationFile);

        File targetFile = new File(destinationFile);
        long downloadStartTime = System.currentTimeMillis();

        try {
            FileUtils.copyURLToFile(new URL(getBinaryUrl()), targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long downloadFinishTime = System.currentTimeMillis();

        log.info("Downloaded {} kb in {} ms. It will be cached in {} for the next usage.",
                FileUtils.sizeOf(targetFile) / 1024,
                downloadFinishTime - downloadStartTime,
                destinationPath);

        return destinationFile;
    }

    private String getFileNameFromUrl(String url) {
        return url.substring(getBinaryUrl().lastIndexOf("/") + 1);
    }

    public String getBinaryUrl() {
        String customBinaryUrl = ngrokConfiguration.customBinary().orElse("");
        if (!customBinaryUrl.isEmpty()) {
            return customBinaryUrl;
        }

        if (SystemUtils.IS_OS_WINDOWS) {
            return is64bitOS() ? ngrokConfiguration.windows64BinaryUrl() : ngrokConfiguration.windowsBinaryUrl();
        }

        if (SystemUtils.IS_OS_MAC) {
            return is64bitOS() ? ngrokConfiguration.osx64BinaryUrl() : ngrokConfiguration.osxBinaryUrl();
        }

        if (SystemUtils.IS_OS_LINUX) {
            return is64bitOS() ? ngrokConfiguration.linux64BinaryUrl() : ngrokConfiguration.linuxBinaryUrl();
        }

        throw new UnsupportedOperationException("Unsupported OS");
    }

    private boolean is64bitOS() {
        return SystemUtils.OS_ARCH.contains("64");
    }
}
