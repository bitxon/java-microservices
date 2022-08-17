package bitxon.spring;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import bitxon.api.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

class FindAccountsSpringTest extends AbstractSpringTest {

    @Test
    void getAll() {
        var response = client()
            .exchange("/accounts", HttpMethod.GET, null, new ParameterizedTypeReference<List<Account>>() {});

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }
}
