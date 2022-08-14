package bitxon.micronaut;

import static org.assertj.core.api.Assertions.assertThat;

import bitxon.api.model.Account;
import io.micronaut.http.HttpRequest;
import org.junit.jupiter.api.Test;


class GetAccountByIdMicronautTest extends AbstractMicronautTest {

    @Test
    public void getById() {
        var expectedId = 1L;
        var request = HttpRequest.GET("/" + expectedId);
        var response = client().toBlocking().exchange(request, Account.class);

        assertThat(response.getStatus().getCode()).isEqualTo(200);
        assertThat(response.getBody())
            .map(Account::getId)
            .contains(expectedId);
    }

}
