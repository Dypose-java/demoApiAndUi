package ui.pages.gitHub;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class RepositoriesPage {
    private String value;
    private final String typeUrl = "type=repositories";

    public RepositoriesPage(String value) {
        this.value = value;
        webdriver().shouldHave(urlContaining(typeUrl));
    }

    @Step("Клик по первому репозиторию")
    public UserRepositoriesPage clickRep() {
        $$("[data-testid='results-list'] span").first().shouldBe(visible).click();
        return new UserRepositoriesPage(value);
    }

}
