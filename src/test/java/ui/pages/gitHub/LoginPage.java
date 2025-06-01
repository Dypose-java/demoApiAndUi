package ui.pages.gitHub;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ui.data.User;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    SelenideElement login=$("#login_field");
    SelenideElement password=$("#password");

    @Step("Успешная аунтефикация пользователя")
    public void successfulLoginUser(User user) {
        login.setValue(user.login());
        password.setValue(user.password()).pressEnter();
        $("#dashboard .my-2").shouldHave(text("Home"));

    }
    @Step("Неуспешная аунтефикация пользователя")
    public void failedLoginUser(String log,String pass) {
        login.setValue(log);
        password.setValue(pass).pressEnter();
        $(".js-flash-alert").shouldHave(Condition.text("Incorrect username or password."));

    }

}
