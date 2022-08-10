package bitxon.dropwizard;

import static org.assertj.core.api.Assertions.assertThat;

import bitxon.api.model.Account;
import org.junit.jupiter.api.Test;

public class GetByIdDropwizardTest extends AbstractDropwizardTest {

    @Test
    void getById() throws Exception{
        var expectedId = 1L;

        var response = client()
            .target(String.format("http://localhost:%d/accounts/%s", appLocalPort(), expectedId))
            .request()
            .buildGet()
            .submit()
            .get(); // Future.get

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.readEntity(Account.class))
            .extracting(Account::getId)
            .isEqualTo(expectedId);
    }
}
