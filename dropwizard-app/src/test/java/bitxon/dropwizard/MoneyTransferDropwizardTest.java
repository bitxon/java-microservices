package bitxon.dropwizard;

import static bitxon.api.constant.Constants.DIRTY_TRICK_HEADER;
import static bitxon.api.constant.Constants.DirtyTrick.FAIL_TRANSFER;
import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.client.Entity;

import bitxon.api.model.Account;
import bitxon.api.model.MoneyTransfer;
import org.junit.jupiter.api.Test;

public class MoneyTransferDropwizardTest extends AbstractDropwizardTest {

    @Test
    void transfer() throws Exception {
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
            .target(String.format("http://localhost:%d/accounts/transfers", appLocalPort()))
            .request()
            .buildPost(Entity.entity(requestBody, "application/json"))
            .submit()
            .get();

        assertThat(response.getStatus()).isEqualTo(204);
        assertThat(retrieveUserMoneyAmount(senderId)).isEqualTo(senderMoneyAmountResult);
        assertThat(retrieveUserMoneyAmount(recipientId)).isEqualTo(recipientMoneyAmountResult);
    }

    @Test
    void transferWithServerProblemDuringTransfer() throws Exception {
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

        var response = client()
            .target(String.format("http://localhost:%d/accounts/transfers", appLocalPort()))
            .request()
            .header(DIRTY_TRICK_HEADER, FAIL_TRANSFER)
            .buildPost(Entity.entity(requestBody, "application/json"))
            .submit()
            .get();

        assertThat(response.getStatus()).isEqualTo(500);
        assertThat(retrieveUserMoneyAmount(senderId)).isEqualTo(senderMoneyAmountOriginal);
        assertThat(retrieveUserMoneyAmount(recipientId)).isEqualTo(recipientMoneyAmountOriginal);
    }

    private int retrieveUserMoneyAmount(Long id) {
        var response = client()
            .target(String.format("http://localhost:%d/accounts/%d", appLocalPort(), id))
            .request()
            .get();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Unable to retrieve account to verify result money amount");
        }
        var account = response.readEntity(Account.class);
        return account.getMoneyAmount();
    }
}
