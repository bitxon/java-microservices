package bitxon.quarkus.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class GetAccountByIdQuarkusTest extends AbstractQuarkusTest {

    @Test
    public void getById() {
        var expectedId = 1;

        //@formatter:off
        given()
            .pathParam("id", expectedId)
        .when()
            .get("/accounts/{id}")
        .then()
            .statusCode(200)
            .body("id", is(expectedId))
            .body("dateOfBirth", is("1991-01-21"));
        //@formatter:on
    }

    @Test
    void getByIdNotFound() {
        //@formatter:off
        given()
            .pathParam("id", Long.MAX_VALUE)
        .when()
            .get("/accounts/{id}")
        .then()
            .statusCode(404)
            .body("errors", hasItem("Resource not found"));
        //@formatter:on
    }


}