package api.config;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.with;

public class ApiConfig {
    private final static AllureRestAssured ALLURE_REST_ASSURED = new AllureRestAssured();

    public static AllureRestAssured withCustomTemplate() {
        ALLURE_REST_ASSURED.setRequestTemplate("request.ftl");
        ALLURE_REST_ASSURED.setResponseTemplate("response.ftl");
        return ALLURE_REST_ASSURED;
    }
    @BeforeAll
    static void setUp(){
        RestAssured.baseURI="https://petstore.swagger.io/v2/";
    }
    public static RequestSpecification mainRequestSpec(ContentType contentType) {
        return with()
                .log().uri()
                .log().headers()
                .log().body()
                .filter(withCustomTemplate())
                .contentType(contentType);
    }
    public static ResponseSpecification mainResponseSpec(int statusCode){
        return new ResponseSpecBuilder()
                .log(LogDetail.BODY)
                .log(LogDetail.STATUS)
                .log(LogDetail.HEADERS)
                .expectStatusCode(statusCode)
                .build();
    }

}


