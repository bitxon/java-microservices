package bitxon.quarkus.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class GetAccountByIdQuarkusTest extends AbstractQuarkusTest {

    @Test
    public void getById() {
        //@formatter:off
        given()
        .when()
            .get("/accounts/1")
        .then()
            .statusCode(200);
        //@formatter:off
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