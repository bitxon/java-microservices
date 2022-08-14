package bitxon.micronaut;

import static bitxon.api.constant.Constants.DIRTY_TRICK_HEADER;
import static bitxon.api.constant.Constants.DirtyTrick.FAIL_TRANSFER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import bitxon.api.model.Account;
import bitxon.api.model.MoneyTransfer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;


@MicronautTest(rebuildContext = true)
class MoneyTransferMicronautTest extends AbstractMicronautTest {

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

        var request = HttpRequest.POST("/transfers", requestBody);
        var response = client().toBlocking().exchange(request);

        assertThat(response.getStatus().getCode()).isEqualTo(204);
        assertThat(retrieveUserMoneyAmount(senderId)).as("Check sender result balance")
            .isEqualTo(senderMoneyAmountResult);
        assertThat(retrieveUserMoneyAmount(recipientId)).as("Check recipient result balance")
            .isEqualTo(recipientMoneyAmountResult);

    }

    @Test
    void transferWithError() {
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

        var request = HttpRequest.POST("/transfers", requestBody)
            .header(DIRTY_TRICK_HEADER, FAIL_TRANSFER);
        var exception = catchThrowableOfType(
            () -> client().toBlocking().exchange(request, Account.class),
            HttpClientResponseException.class
        );

        assertThat(exception).as("Check response error/status")
            .extracting(HttpClientResponseException::getResponse)
            .extracting(HttpResponse::getStatus)
            .extracting(HttpStatus::getCode)
            .isEqualTo(500);
        assertThat(retrieveUserMoneyAmount(senderId)).as("Check sender result balance")
            .isEqualTo(senderMoneyAmountOriginal);
        assertThat(retrieveUserMoneyAmount(recipientId)).as("Check recipient result balance")
            .isEqualTo(recipientMoneyAmountOriginal);

    }

    private int retrieveUserMoneyAmount(Long id) {
        var request = HttpRequest.GET("/" + id);
        var response = client().toBlocking().exchange(request, Account.class);
        return response.getBody().get().getMoneyAmount();
    }

}