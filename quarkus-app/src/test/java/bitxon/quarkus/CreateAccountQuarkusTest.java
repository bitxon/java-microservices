package bitxon.quarkus;

import static io.restassured.RestAssured.given;

import bitxon.api.model.Account;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class CreateAccountQuarkusTest extends AbstractQuarkusTest {

    @Test
    public void create() {
        var account = Account.builder()
            .email("some-email@example.com")
            .firstName("MyName")
            .lastName("FamilyName")
            .currency("USD")
            .moneyAmount(893)
            .build();

        //@formatter:off
        given()
            .body(account)
            .contentType(ContentType.JSON)
        .when()
            .post("/accounts")
        .then()
            .statusCode(200);
        //@formatter:off
    }

}