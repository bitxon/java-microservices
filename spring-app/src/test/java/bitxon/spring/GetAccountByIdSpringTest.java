package bitxon.spring;

import static org.assertj.core.api.Assertions.assertThat;

import bitxon.api.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

class GetAccountByIdSpringTest extends AbstractSpringTest {

    @Test
    void getById() {
        var expectedId = 1L;
        var response = client()
            .exchange("/accounts/" + expectedId, HttpMethod.GET, null, Account.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody())
            .extracting(Account::getId)
            .isEqualTo(expectedId);
    }
}
