package bitxon.quarkus.test;

import static io.restassured.RestAssured.given;

import java.time.LocalDate;

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
            .dateOfBirth(LocalDate.of(1990, 1, 1))
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

    @Test
    void createWithMissedEmail() {
        var account = Account.builder()
            .email(null) // Required field is null
            .firstName("MyName")
            .lastName("FamilyName")
            .dateOfBirth(LocalDate.of(1990, 1, 1))
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
            .statusCode(400);
        //@formatter:off
    }


}