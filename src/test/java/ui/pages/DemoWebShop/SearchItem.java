package ui.pages.DemoWebShop;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class SearchItem {
    public SearchItem clickItem(String item){
        $(byText(item)).click();
        $(".product-name h1").shouldHave(text(item));
        return this;
    }

}
