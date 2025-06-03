package mobile.test.setUp;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import mobile.config.MobileDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

public class BaseTest {
    @BeforeAll
    static void beforeAll(){
        Configuration.browser= MobileDriverManager.class.getName();
        Configuration.browserSize = null;

    }
    @BeforeEach
    void addListener() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        open();
    }
    @AfterEach
    void afterEach() {
        Attach.pageSource();

        closeWebDriver();
    }
}
