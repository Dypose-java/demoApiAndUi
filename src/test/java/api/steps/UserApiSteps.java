package api.steps;

import api.config.ApiConfig;
import api.pojo.get.UserPojo;
import api.pojo.post.ResponsesUserPojo;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;

import java.util.List;

import static io.restassured.RestAssured.given;

public class UserApiSteps {
    @Step("Получаем пользователя по имени:{userName}")
    public static UserPojo getUser(String userName) {
        return given()
                .spec(ApiConfig.mainRequestSpec(ContentType.JSON))
                .pathParam("username", userName)
                .get("/user/{username}")
                .then().spec(ApiConfig.mainResponseSpec(200)).extract().as(UserPojo.class);
    }

    @Step("Добавляем пользователя:{userPojo}")
    public static ResponsesUserPojo addUser(UserPojo userPojo) {
        return given()
                .spec(ApiConfig.mainRequestSpec(ContentType.JSON))
                .body(userPojo)
                .post("/user")
                .then().spec(ApiConfig.mainResponseSpec(200)).extract().as(ResponsesUserPojo.class);
    }

    @Step("Изменяем значение:{bodyUser}")
    public static ResponsesUserPojo updateUser(UserPojo bodyUser) {
        return given().spec(ApiConfig.mainRequestSpec(ContentType.JSON))
                .pathParam("username", bodyUser.getUserName())
                .body(bodyUser)
                .put("/user/{username}")
                .then().spec(ApiConfig.mainResponseSpec(200)).extract().as(ResponsesUserPojo.class);
    }

    @Step("Удаление пользователя:{nameUser}")
    public static ResponsesUserPojo deleteUser(String nameUser) {
        return given()
                .spec(ApiConfig.mainRequestSpec(ContentType.JSON))
                .pathParam("username", nameUser)
                .delete("/user/{username}").then().spec(ApiConfig.mainResponseSpec(200))
                .extract().as(ResponsesUserPojo.class);
    }

    @Step("Добавляем лист пользователей")
    public static ResponsesUserPojo addUsers(List<UserPojo> listUsers) {
        return given()
                .spec(ApiConfig.mainRequestSpec(ContentType.JSON))
                .body(listUsers)
                .post("/user/createWithArray")
                .then().spec(ApiConfig.mainResponseSpec(200)).extract().as(ResponsesUserPojo.class);
    }
}
