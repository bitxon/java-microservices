package bitxon.dropwizard;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FindAccountsDropwizardTest extends AbstractDropwizardTest {

    @Test
    void getAll() throws Exception{
        var response = client()
            .target(String.format("http://localhost:%d/accounts", appLocalPort()))
            .request()
            .buildGet()
            .submit()
            .get(); // Future.get

        assertThat(response.getStatus()).isEqualTo(200);
    }
}
