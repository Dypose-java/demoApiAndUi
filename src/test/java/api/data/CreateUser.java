package api.data;

import com.github.javafaker.Faker;
import api.pojo.get.UserPojo;

import java.util.Locale;
import java.util.Random;

public class CreateUser {
    private static Faker fakerEn = new Faker(Locale.ENGLISH);
    private static Faker fakerRu = new Faker(new Locale("ru"));

    public static UserPojo generationUser() {
        return new UserPojo(new Random().nextLong(1_000_000_000_000_000_000L)
                , fakerRu.name().username()
                , fakerRu.name().firstName()
                , fakerRu.name().lastName()
                , fakerEn.internet().emailAddress()
                , fakerEn.internet().password()
                , fakerRu.phoneNumber().phoneNumber()
                , 200);
    }



}




