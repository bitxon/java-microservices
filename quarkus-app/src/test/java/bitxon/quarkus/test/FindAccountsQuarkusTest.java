package bitxon.quarkus.test;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class FindAccountsQuarkusTest extends AbstractQuarkusTest {

    @Test
    public void getAll() {
        //@formatter:off
        given()
        .when()
            .get("/accounts")
        .then()
            .statusCode(200);
        //@formatter:off
    }


}