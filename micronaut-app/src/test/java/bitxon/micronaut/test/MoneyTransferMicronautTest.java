package bitxon.micronaut.test;

import static bitxon.common.api.constant.Constants.DIRTY_TRICK_HEADER;
import static bitxon.common.api.constant.Constants.DirtyTrick.FAIL_TRANSFER;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import bitxon.common.api.model.MoneyTransfer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


class MoneyTransferMicronautTest extends AbstractMicronautTest {

    @ParameterizedTest
    @CsvSource({
        "USD -> USD, 1, 2, 40, 300, 613",
        "GBP -> USD, 6, 5, 25, 55,  150"
    })
    void transfer(String description, long senderId, long recipientId, int transferAmount,
                  int expectedSenderMoney, int expectedRecipientMoney) {

        var requestBody = MoneyTransfer.builder()
            .senderId(senderId)
            .recipientId(recipientId)
            .moneyAmount(transferAmount)
            .build();

        //@formatter:off
        given()
            .body(requestBody)
            .contentType(ContentType.JSON)
        .when()
            .post("/accounts/transfers")
        .then()
            .statusCode(204);


        RestAssured.get("/accounts/" + senderId).then()
            .assertThat().body("moneyAmount", equalTo(expectedSenderMoney));
        RestAssured.get("/accounts/" + recipientId).then().assertThat()
            .body("moneyAmount", equalTo(expectedRecipientMoney));
        //@formatter:on
    }

    @Test
    void transferWithServerProblemDuringTransfer() {
        var transferAmount = 40;
        // Sender
        var senderId = 3L;
        var senderMoneyAmountOriginal = 79;
        // Recipient
        var recipientId = 4L;
        var recipientMoneyAmountOriginal = 33;

        var requestBody = MoneyTransfer.builder()
            .senderId(senderId)
            .recipientId(recipientId)
            .moneyAmount(transferAmount)
            .build();

        //@formatter:off
        given()
            .body(requestBody)
            .header(DIRTY_TRICK_HEADER, FAIL_TRANSFER)
            .contentType(ContentType.JSON)
        .when()
            .post("/accounts/transfers")
        .then()
            .statusCode(500);
        //@formatter:on

        RestAssured.get("/accounts/" + senderId).then()
            .body("moneyAmount", is(senderMoneyAmountOriginal));
        RestAssured.get("/accounts/" + recipientId).then()
            .body("moneyAmount", is(recipientMoneyAmountOriginal));
    }

    @Test
    void transferWithoutRequiredField() {

        var requestBody = MoneyTransfer.builder()
            .senderId(1L)
            .recipientId(2L)
            .moneyAmount(null) // Required field is null
            .build();

        //@formatter:off
        given()
            .body(requestBody)
            .contentType(ContentType.JSON)
        .when()
            .post("/accounts/transfers")
        .then()
            .statusCode(422)
            .body("errors", hasItem("'moneyAmount' must not be null"));
        //@formatter:on
    }

}
