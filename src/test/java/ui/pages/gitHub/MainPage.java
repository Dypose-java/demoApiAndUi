package ui.pages.gitHub;


import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    static SelenideElement banner;

    static {
        banner = $(".HeaderMktg");
    }


    @Step("Наводим на {nameBanner} и нажимаем на {nameValue}")
    public MainPage banner(String nameBanner, String nameValue) {
        if (!nameValue.equals("null")) {
            banner.$(byText(nameBanner)).hover();
            $(byText(nameValue)).click();
        } else {
            banner.$(byText(nameBanner)).click();

        }
        return this;
    }
    @Step("клик на signIn")
    public LoginPage clickSignIn(){
        $(".HeaderMenu-link--sign-in").click();
        return new LoginPage();
    }
    @Step("ввод значения в поиск:{value}")
    public RepositoriesPage searchValue(String value){
        $(".search-input").click();
        $("#query-builder-test").setValue(value).pressEnter();
        return new RepositoriesPage(value);
    }
}


