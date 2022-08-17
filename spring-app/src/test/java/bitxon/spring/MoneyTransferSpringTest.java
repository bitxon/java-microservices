package bitxon.spring;

import static bitxon.api.constant.Constants.DIRTY_TRICK_HEADER;
import static bitxon.api.constant.Constants.DirtyTrick.FAIL_TRANSFER;
import static org.assertj.core.api.Assertions.assertThat;

import bitxon.api.model.Account;
import bitxon.api.model.MoneyTransfer;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

class MoneyTransferSpringTest extends AbstractSpringTest {

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

        var response = client()
            .postForEntity("/accounts/transfers", requestBody, Void.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        assertThat(retrieveUserMoneyAmount(senderId)).as("Check sender result balance")
            .isEqualTo(senderMoneyAmountResult);
        assertThat(retrieveUserMoneyAmount(recipientId)).as("Check recipient result balance")
            .isEqualTo(recipientMoneyAmountResult);
    }

    @Test
    void transferFromGBPToUSD() throws Exception {
        var transferAmount = 40;
        var exchangeRate = 2.0d;
        // Sender
        var senderId = 6L;
        var senderMoneyAmountOriginal = 80; // GPB
        var senderMoneyAmountResult = senderMoneyAmountOriginal - transferAmount;
        // Recipient
        var recipientId = 5L;
        var recipientMoneyAmountOriginal = 100; // USD
        var recipientMoneyAmountResult = recipientMoneyAmountOriginal + (int)(transferAmount * exchangeRate);

        var requestBody = MoneyTransfer.builder()
            .senderId(senderId)
            .recipientId(recipientId)
            .moneyAmount(transferAmount)
            .build();

        var response = client()
            .postForEntity("/accounts/transfers", requestBody, Void.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        assertThat(retrieveUserMoneyAmount(senderId)).isEqualTo(senderMoneyAmountResult);
        assertThat(retrieveUserMoneyAmount(recipientId)).isEqualTo(recipientMoneyAmountResult);
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


        var headers = new HttpHeaders();
        headers.set(DIRTY_TRICK_HEADER, FAIL_TRANSFER);
        var request = new HttpEntity<MoneyTransfer>(requestBody, headers);

        var response = client()
            .postForEntity("/accounts/transfers", request, Void.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(500);
        assertThat(retrieveUserMoneyAmount(senderId)).as("Check sender result balance")
            .isEqualTo(senderMoneyAmountOriginal);
        assertThat(retrieveUserMoneyAmount(recipientId)).as("Check recipient result balance")
            .isEqualTo(recipientMoneyAmountOriginal);

    }

    @Test
    void transferWithoutRequiredField() {
        var requestBody = MoneyTransfer.builder()
            .senderId(1L)
            .recipientId(2L)
            .moneyAmount(null) // Required field is null
            .build();


        var headers = new HttpHeaders();
        headers.set(DIRTY_TRICK_HEADER, FAIL_TRANSFER);
        var request = new HttpEntity<MoneyTransfer>(requestBody, headers);

        var response = client()
            .postForEntity("/accounts/transfers", request, Void.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    private int retrieveUserMoneyAmount(Long id) {
        var response = client()
            .exchange("/accounts/" + id, HttpMethod.GET, null, Account.class);

        if (response.getStatusCodeValue() != 200) {
            throw new RuntimeException("Unable to retrieve account to verify result money amount");
        }
        return response.getBody().getMoneyAmount();
    }
}
