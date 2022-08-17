package bitxon.dropwizard;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.client.Entity;

import bitxon.api.model.Account;
import org.junit.jupiter.api.Test;

class CreateAccountDropwizardTest extends AbstractDropwizardTest {

    @Test
    void create() throws Exception{
        var account = Account.builder()
            .email("some-email@example.com")
            .firstName("MyName")
            .lastName("FamilyName")
            .currency("USD")
            .moneyAmount(893)
            .build();

        var response = client()
            .target(String.format("http://localhost:%d/accounts", appLocalPort()))
            .request()
            .buildPost(Entity.entity(account,"application/json"))
            .submit()
            .get(); // Future.get

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void createWithMissedEmail() throws Exception{
        var account = Account.builder()
            .email(null)  // Required field is null
            .firstName("MyName")
            .lastName("FamilyName")
            .currency("USD")
            .moneyAmount(893)
            .build();

        var response = client()
            .target(String.format("http://localhost:%d/accounts", appLocalPort()))
            .request()
            .buildPost(Entity.entity(account,"application/json"))
            .submit()
            .get(); // Future.get

        assertThat(response.getStatus()).isEqualTo(422);
    }
}
