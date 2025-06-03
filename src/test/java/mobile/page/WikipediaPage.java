package mobile.page;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static io.appium.java_client.AppiumBy.className;
import static io.appium.java_client.AppiumBy.id;

public class WikipediaPage {

    public static Wikipedia wikipedia() {
        return new Wikipedia();
    }


    public static class Wikipedia {
        @Step("Пропускаем приветственное окно")
        public MainPage skipWelcomeScreen() {
            $(id("org.wikipedia.alpha:id/fragment_onboarding_skip_button")).click();
            return new MainPage();
        }
    }

    public static class MainPage {
        @Step("Выполняем поиск по значению: '{value}'")
        public SearchResultsPage searchFor(String value) {
            $(id("org.wikipedia.alpha:id/search_container")).click();
            $(id("org.wikipedia.alpha:id/search_src_text")).sendKeys(value);
            return new SearchResultsPage();
        }
    }

    public static class SearchResultsPage {
        @Step("Выбираем результат с индексом {index}")
        public ArticlePage selectSearchResult(int index) {
            $$(id("org.wikipedia.alpha:id/page_list_item_title")).get(index).click();
            return new ArticlePage();
        }
    }

    public static class ArticlePage {
        public ArticlePage() {
            $(id("org.wikipedia.alpha:id/closeButton")).click();
        }

        @Step("Проверяем, что заголовок статьи содержит текст: '{expectedText}'")
        public ArticlePage verifyTitleContains(String expectedText) {

            $$(className("android.webkit.WebView")).get(0)
                    .shouldHave(Condition.text(expectedText));
            return this;
        }
    }

}



