package ui.tests.setUp;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import ui.configs.BrowserConfig;
import ui.configs.BrowserConfigFactory;

import java.util.Map;
import java.util.Objects;

public class BaseTest {
    //если нужен запуск из идеи
    /*static {
        System.setProperty("tag","UI");
        System.setProperty("runIn","browser_local");
    }*/

    private static final BrowserConfig config = BrowserConfigFactory.PROP;

    @BeforeAll
    static void setUp() {
        configureBasicSettings();
        if (isSelenoidRun()){
            configureSelenoidSettings();
        }
    }

    private static void configureBasicSettings() {
        Configuration.baseUrl = config.uiUrl();
        Configuration.browser = config.browser();
        Configuration.browserSize = config.size();
        Configuration.timeout = config.timeOut();
        Configuration.pageLoadTimeout = config.pageLoadTimeout();
        Configuration.headless = config.headless();
    }
    private static boolean isSelenoidRun() {
        return "browser_selenoid".equals(config.runIn());
    }

    private static void configureSelenoidSettings() {
        if (isSelenoidRun()) {
            Configuration.remote = config.remote();
            Configuration.browserVersion = config.version();

            switch (config.browser().toLowerCase()) {
                case "chrome" -> setupChrome();
                case "firefox" -> setupFirefox();
                case "opera" -> setupOpera();
                default ->
                        throw new IllegalArgumentException("Unsupported browser: "
                                + config.browser());
            }
        }
    }

    private static void setupChrome() {
        Configuration.browserCapabilities = new ChromeOptions();
        setCommonSelenoidCapabilities();
    }

    private static void setupFirefox() {
        Configuration.browserCapabilities = new FirefoxOptions();
        setCommonSelenoidCapabilities();

    }

    private static void setupOpera() {// opera is not supported in selenide
        Configuration.browserCapabilities = new ChromeOptions();
        setCommonSelenoidCapabilities();
    }

    private static void setCommonSelenoidCapabilities() {
        Configuration.browserCapabilities.setCapability("selenoid:options", Map.of(
                "enableVNC", true,
                "enableVideo", true,
                "enableLog", true
        ));
    }

    @BeforeEach
    void setupAllureListener() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterEach
    void attachArtifacts() {
        Attach.makeScreenshot("Last screenshot");
        Attach.pageSource();
        if (!Objects.equals(config.browser(), "firefox")){
            Attach.browserConsoleLogs();
        }

        if (isSelenoidRun()) {
            Attach.addVideo();
        }
    }
}
