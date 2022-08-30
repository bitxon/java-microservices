package bitxon.dropwizard.test;

import static io.restassured.RestAssured.when;

import org.junit.jupiter.api.Test;

class FindAccountsDropwizardTest extends AbstractDropwizardTest {

    @Test
    void getAll() {
        //@formatter:off
        when()
            .get("/accounts")
        .then()
            .statusCode(200);
        //@formatter:on
    }
}
