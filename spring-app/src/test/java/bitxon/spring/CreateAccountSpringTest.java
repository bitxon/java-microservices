package bitxon.spring;

import static org.assertj.core.api.Assertions.assertThat;

import bitxon.api.model.Account;
import org.junit.jupiter.api.Test;

public class CreateAccountSpringTest extends AbstractSpringTest {

    @Test
    void create() {
        var account = Account.builder()
            .email("some-email@example.com")
            .firstName("MyName")
            .lastName("FamilyName")
            .currency("USD")
            .moneyAmount(893)
            .build();

        var response = client()
            .postForEntity("/accounts", account, Account.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void createWithMissedEmail() {
        var account = Account.builder()
            .email(null)
            .firstName("MyName")
            .lastName("FamilyName")
            .currency("USD")
            .moneyAmount(893)
            .build();

        var response = client()
            .postForEntity("/accounts", account, Account.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(400); // TODO should be 422
    }
}
