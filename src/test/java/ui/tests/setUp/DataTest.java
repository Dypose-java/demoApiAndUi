package ui.tests.setUp;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;

public class DataTest {
    private static Faker faker = new Faker();
    public static final String REPOSITORY ="Dypose-java";
    public static String FAIL_LOGIN = faker.internet().emailAddress();
    public static String FAIL_PASSWORD = faker.internet().password();

}
