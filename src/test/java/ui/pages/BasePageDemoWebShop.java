package ui.pages;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import ui.pages.DemoWebShop.SearchItem;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class BasePageDemoWebShop {
    @Step("Проверка главного тайтла")
    public BasePageDemoWebShop assertTitle() {
        $(".header .header-logo img").shouldHave(Condition.visible);
        return this;
    }

    @Step("Устанавлием значение в поиске {item} и проверяем количество товаров {countItem} ")
    public SearchItem setSearchStoreAndAssertCountItem(String item, long countItem) {
        $("#small-searchterms").setValue(item).pressEnter();
        Assertions.assertEquals(countItem, $$(".search-results .item-box").stream().count());
        return new SearchItem();
    }

    /*public BasePageDemoWebShop clickItem(String item) {
        $(byText(item)).click();
        $(".product-name h1").shouldHave(text(item));
        return this;
    }*/

    public BasePageDemoWebShop register(String firstName, String lastName, String gender, String email, String password) {
        $(".ico-register").click();
        $("#gender-male").click();
        $("#FirstName").setValue(firstName);
        $("#LastName").setValue(lastName);
        $("#Email").setValue(email);
        $("#Password").setValue(password);
        $("#ConfirmPassword").setValue(password);
        $("#register-button").click();
        $(".result").shouldHave(text("Your registration completed"));
        return this;
    }

}
