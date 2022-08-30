package bitxon.micronaut.test;

import static io.restassured.RestAssured.when;

import org.junit.jupiter.api.Test;


class FindAccountsMicronautTest extends AbstractMicronautTest {

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
