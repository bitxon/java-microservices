package bitxon.quarkus.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class AccountResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
            .when()
                .get("/accounts")
            .then()
                .statusCode(200);
    }

}