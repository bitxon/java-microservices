package bitxon.quarkus.test;

import static io.restassured.RestAssured.given;

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


}