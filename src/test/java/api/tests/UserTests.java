package api.tests;

import api.config.ApiConfig;
import api.data.CreateUser;
import api.pojo.get.UserPojo;
import api.pojo.post.ResponsesUserPojo;
import api.steps.UserApiSteps;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Epic("API Тесты")
@Tag("API")
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
    @DisplayName("Получаем пользователя")
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
    @DisplayName("Изменяем пользователя")
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
    @DisplayName("Удаляем пользователя")
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
    @DisplayName("Добавляем лист пользователей")
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
