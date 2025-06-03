package mobile.test;

import io.qameta.allure.*;
import mobile.page.WikipediaPage;
import mobile.test.setUp.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("MOBILE")
@Epic("Wikipedia Mobile App")
@Feature("Основная функциональность")
public class WikipediaTest extends BaseTest {
    @Test
    @DisplayName("Успешный поиск статьи через мобильное приложение")
    @Tag("Search")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Dypose")
    @Story("Поиск статей")
    @Description("""
            Проверяет полный сценарий поиска:
            1. Пропускает приветственное окно
            2. Вводит поисковый запрос
            3. Выбирает первый результат
            4. Проверяет заголовок статьи"""
    )
    void successfulSearchTest() {

        final String text = "java";
        final byte index = 0;

        WikipediaPage
                .wikipedia()
                .skipWelcomeScreen()
                .searchFor(text)
                .selectSearchResult(index)
                .verifyTitleContains(text);

    }

}
