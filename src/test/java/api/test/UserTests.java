package api.test;

import api.config.ApiConfig;
import api.data.CreateUser;
import api.pojo.get.UserPojo;
import api.pojo.post.ResponsesUserPojo;
import api.step.UserApiSteps;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Epic("API Тесты пользователей")
@Tag("API")
@Feature("Управление пользователями")
public class UserTests extends ApiConfig {
    private static UserPojo originalUser;
    private UserPojo copyUser;


    @BeforeAll
    static public void setUp() {
        originalUser = CreateUser.generationUser();
        UserApiSteps.addUser(originalUser);
    }

    @BeforeEach
    void prepareTest() {
        copyUser = new UserPojo(originalUser);
    }

    @Test
    @DisplayName("Получение данных пользователя")
    @Description("Проверка корректного получения данных пользователя по username")
    @Severity(SeverityLevel.NORMAL)
    @Feature("User")
    @Story("Get")
    @Owner("Dypose")
    void GetUser() {
        UserPojo user = UserApiSteps.getUser(copyUser.getUserName());
        assertThat(user).isNotNull();
        assertThat(copyUser.getUserName()).isEqualTo(user.getUserName());
    }

    @Test
    @DisplayName("Обновление данных пользователя")
    @Description("Проверка успешного обновления данных пользователя")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("User")
    @Story("Put")
    @Owner("Dypose")
    void updateUserPut() {
        ResponsesUserPojo responsesUserPojo = UserApiSteps.updateUser(copyUser);
        assertThat(responsesUserPojo).isNotNull();
        assertThat(responsesUserPojo.getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("Удаление пользователя")
    @Description("Проверка успешного удаления пользователя")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("User")
    @Story("Delete")
    @Owner("Dypose")
    void deleteUser() {
        ResponsesUserPojo responsesUserPojo = UserApiSteps.deleteUser(copyUser.getUserName());
        assertThat(responsesUserPojo).isNotNull();
        assertThat(responsesUserPojo.getCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("Добавление списка пользователей")
    @Description("Проверка создания списка пользователей")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("User")
    @Story("Post")
    @Owner("Dypose")
    void addUsers() {
        List<UserPojo> list = IntStream.rangeClosed(0, 5).mapToObj(el -> CreateUser.generationUser()).toList();
        ResponsesUserPojo responsesUserPojo = UserApiSteps.addUsers(list);
        assertThat(responsesUserPojo).isNotNull();
        assertThat(responsesUserPojo.getMessage()).isEqualTo("ok");
        assertThat(responsesUserPojo.getCode()).isEqualTo(200);
    }


}
