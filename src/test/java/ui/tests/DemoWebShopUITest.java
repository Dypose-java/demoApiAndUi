package ui.tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ui.pages.DemoWebShop.BasePage;
import ui.tests.setUp.BaseTest;

import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DemoWebShopUITest extends BaseTest {
    private final Faker fakerRu = new Faker(new Locale("ru"));
    private final Faker fakerEn = new Faker(Locale.ENGLISH);

    @Tag("UI")
    @Test
    void checkMainTitle() {
        Selenide.open("/");
        $(".header .header-logo img").shouldHave(Condition.visible);
        System.out.println();
    }

    @Tag("UI")
    @Test
    void searchStore() {
        Selenide.open("/");
        $("#small-searchterms").setValue("Virtual Gift Card").pressEnter();
        Assertions.assertEquals(2, $$(".search-results .item-box").stream().count());//сумма товаров
        $(byText("$25 Virtual Gift Card")).click();
        $(".product-name h1").shouldHave(text("$25 Virtual Gift Card"));
    }

    @Tag("UI")
    @Test
    void register() {
        String firstName = fakerRu.name().firstName(),
                lastName = fakerRu.name().lastName(),
                email = fakerEn.internet().emailAddress(),
                pass = fakerEn.internet().password();
        Selenide.open(" ");
        $(".ico-register").click();
        $("#gender-male").click();
        $("#FirstName").setValue(firstName);
        $("#LastName").setValue(lastName);
        $("#Email").setValue(email);
        $("#Password").setValue(pass);
        $("#ConfirmPassword").setValue(pass);
        $("#register-button").click();
        $(".result").shouldHave(text("Your registration completed"));

    }

    @Tag("UI")
    @Test
    void checkPageObj() {
        Selenide.open(" ");
        final String item = "Virtual Gift Card";
        new BasePage().assertTitle().setSearchStoreAndAssertCountItem(item, 2).clickItem(item);
    }
}
