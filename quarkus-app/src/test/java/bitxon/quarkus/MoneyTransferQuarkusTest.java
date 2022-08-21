package bitxon.quarkus;

import static bitxon.api.constant.Constants.DIRTY_TRICK_HEADER;
import static bitxon.api.constant.Constants.DirtyTrick.FAIL_TRANSFER;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import bitxon.api.model.MoneyTransfer;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class MoneyTransferQuarkusTest extends AbstractQuarkusTest {

    @Test
    void transfer() {
        var transferAmount = 40;
        // Sender
        var senderId = 1L;
        var senderMoneyAmountOriginal = 340;
        var senderMoneyAmountResult = senderMoneyAmountOriginal - transferAmount;
        // Recipient
        var recipientId = 2L;
        var recipientMoneyAmountOriginal = 573;
        var recipientMoneyAmountResult = recipientMoneyAmountOriginal + transferAmount;

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


        get("/accounts/" + senderId).then()
            .assertThat().body("moneyAmount", equalTo(senderMoneyAmountResult));
        get("/accounts/" + recipientId).then().assertThat()
            .body("moneyAmount", equalTo(recipientMoneyAmountResult));
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


        get("/accounts/" + senderId).then()
            .assertThat().body("moneyAmount", equalTo(senderMoneyAmountOriginal));
        get("/accounts/" + recipientId).then().assertThat()
            .body("moneyAmount", equalTo(recipientMoneyAmountOriginal));
        //@formatter:on
    }

}