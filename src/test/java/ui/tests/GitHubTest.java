package ui.tests;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.*;
import ui.data.User;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ui.pages.gitHub.MainPage;
import ui.tests.setUp.BaseTest;

@Tag("UI")
public class GitHubTest extends BaseTest {
    static User USER;

    @BeforeAll
    static void setValue() {
        USER = ConfigFactory.create(User.class);
    }

@Disabled
    @Tag("ParameterizedTest")
    @CsvFileSource(resources = "/csv/banner_product.csv")
    @ParameterizedTest(name = "Проверка баннера {0} кликабельность {1}")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Dypose")
    void checkBannerTest(String banner, String nameValue) {
        Selenide.open(" ");
        new MainPage().banner(banner, nameValue);
    }

    @Test
    @DisplayName("Успешная аунтефикация пользователя")
    public void successfulLoginUser() {
        Selenide.open(" ");
        new MainPage().clickSignIn().successfulLoginUser(USER);
    }

    @Test
    @DisplayName("Неуспешная аунтефикация пользователя")
    public void failedLoginUser() {
        Selenide.open(" ");
        new MainPage().clickSignIn().failedLoginUser("log", "pass");
    }

    @Test
    @DisplayName("Поиск репозитория и переход к нему")
    void searchRep() {
        Selenide.open(" ");
        new MainPage().searchValue("Dypose-java").clickRep();
    }

    @Test
    @DisplayName("Скачиваем файл zip репозитория")
    void downloadZipRep() {
        Selenide.open(" ");
        new MainPage().searchValue("Dypose-java").clickRep().downloadZipRep();
    }

}
