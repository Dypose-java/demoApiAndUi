package mobile.config;

import com.codeborne.selenide.WebDriverProvider;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static io.appium.java_client.remote.AutomationName.ANDROID_UIAUTOMATOR2;
import static org.apache.commons.io.FileUtils.copyInputStreamToFile;

public class MobileDriverManager implements WebDriverProvider {
    private final MobileConfig config = ConfigFactory.create(MobileConfig.class);

    @Override
    public WebDriver createDriver(Capabilities capabilities) {
        UiAutomator2Options options = new UiAutomator2Options();
        options.merge(capabilities);

        options.setAutomationName(ANDROID_UIAUTOMATOR2)
                .setPlatformName(config.mobilePlatform())
                .setDeviceName(config.mobileDevice())
                .setPlatformVersion(config.mobilePlatformVersion())
                .setApp(getAppPath())
                .setAppPackage(config.mobileAppPackage())
                .setAppActivity(config.mobileAppActivity());

        return new AndroidDriver(getAppiumServerUrl(), options);
    }

    private URL getAppiumServerUrl() {
        try {
            return new URL(config.mobileUrl());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium server URL", e);
        }
    }

    private String getAppPath() {
        String appPath = "src/test/resources/apps/" + extractAppNameFromUrl(config.mobileAppUrl());
        File app = new File(appPath);

        if (!app.exists()) {
            downloadApp(config.mobileAppUrl(), app);
        }
        return app.getAbsolutePath();
    }

    private String extractAppNameFromUrl(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    private void downloadApp(String appUrl, File targetFile) {
        try (InputStream in = new URL(appUrl).openStream()) {
            copyInputStreamToFile(in, targetFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to download application from: " + appUrl, e);
        }
    }
}
