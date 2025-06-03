package ui.tests;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.*;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ui.data.User;
import ui.pages.gitHub.MainPage;
import ui.tests.setUp.BaseTest;

import static ui.tests.setUp.DataTest.*;

@Tag("UI")
@Epic("GitHub")
@Feature("Основная функциональность GitHub")
public class GitHubTest extends BaseTest {
    static User USER;

    @BeforeAll
    static void setUpConfig() {
        USER = ConfigFactory.create(User.class);
    }

    @BeforeEach
    void openBrowser() {
        Selenide.open(" ");

    }

    @Disabled("Временно отключен для доработки")
    @Tag("ParameterizedTest")
    @ParameterizedTest(name = "Проверка баннера {0} кликабельность {1}")
    @CsvFileSource(resources = "/csv/banner_product.csv")
    @Severity(SeverityLevel.NORMAL)
    @Story("Проверка баннеров на главной странице")
    @Owner("Dypose")
    @Description("Параметризованный тест проверки кликабельности баннеров с различными параметрами")
    void checkBannerTest(String banner, String nameValue) {
        Selenide.open(" ");
        new MainPage()
                .banner(banner, nameValue);
    }

    @Test
    @DisplayName("Успешная авторизация пользователя")
    @Tag("Auth")
    @Severity(SeverityLevel.TRIVIAL)
    @Story("Авторизация пользователя")
    @Description("Проверка успешного входа в систему с валидными учетными данными")
    @Owner("Dypose")

    public void successFulLoginUserTest() {
        new MainPage()
                .clickSignIn()
                .successfulLoginUser(USER);
    }

    @Test
    @DisplayName("Неуспешная авторизация пользователя")
    @Tag("Auth")
    @Severity(SeverityLevel.TRIVIAL)
    @Owner("Dypose")
    @Story("Авторизация пользователя")
    @Description("Проверка отображения ошибки при вводе неверных учетных данных")
    public void failedLoginUserTest() {
        new MainPage()
                .clickSignIn()
                .failedLoginUser(FAIL_LOGIN, FAIL_PASSWORD);
    }

    @Test
    @DisplayName("Поиск репозитория и переход к нему")
    @Tag("Search")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Dypose")
    @Story("Работа с репозиториями")
    @Description("Проверка поиска репозитория и перехода на его страницу")
    @Issue("GH-123")
    public void searchRepositoryTest() {
        new MainPage()
                .searchValue(REPOSITORY)
                .clickRep();
    }

    @Test
    @DisplayName("Скачиваем файл zip репозитория")
    @Tag("Download")
    @Severity(SeverityLevel.NORMAL)
    @Story("Работа с репозиториями")
    @Description("Проверка возможности скачивания репозитория в формате ZIP")
    void downloadRepositoryAsZipTest() {
        new MainPage()
                .searchValue(REPOSITORY)
                .clickRep()
                .downloadZipRep();
    }

}
