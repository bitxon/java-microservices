package bitxon.spring.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

class GetAccountByIdSpringTest extends AbstractSpringTest {

    @Test
    void getById() {
        var expectedId = 1;

        //@formatter:off
        given()
            .pathParam("id", expectedId)
            .when()
            .get("/accounts/{id}")
            .then()
            .statusCode(200)
            .body("id", is(expectedId));
        //@formatter:on
    }
}
