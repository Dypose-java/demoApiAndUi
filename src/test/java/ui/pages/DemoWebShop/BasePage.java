package ui.pages.DemoWebShop;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class BasePage {
    @Step("Проверка главного тайтла")
    public BasePage assertTitle() {
        $(".header .header-logo img").shouldHave(Condition.visible);
        return this;
    }
    @Step("Устанавлием значение в поиске {item} и проверяем количество товаров {countItem} ")
    public SearchItem setSearchStoreAndAssertCountItem(String item, long countItem) {
        $("#small-searchterms").setValue(item).pressEnter();
        Assertions.assertEquals(countItem, $$(".search-results .item-box").stream().count());
        return new SearchItem();
    }
}
