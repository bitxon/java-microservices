package bitxon.micronaut;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import bitxon.api.model.Account;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import org.junit.jupiter.api.Test;


class CreateAccountByIdMicronautTest extends AbstractMicronautTest {

    @Test
    public void create() {
        var account = Account.builder()
            .email("some-email@example.com")
            .currency("USD")
            .moneyAmount(893)
            .build();

        var request = HttpRequest.POST("", account);
        var response = client().toBlocking().exchange(request, Account.class);

        assertThat(response.getStatus().getCode()).isEqualTo(200);
    }

    @Test
    void createWithMissedEmail() {
        var account = Account.builder()
            .email(null)
            .currency("USD")
            .moneyAmount(893)
            .build();

        var request = HttpRequest.POST("", account);

        var exception = catchThrowableOfType(
            () -> client().toBlocking().exchange(request, Account.class),
            HttpClientResponseException.class
        );

        assertThat(exception)
            .extracting(HttpClientResponseException::getResponse)
            .extracting(HttpResponse::getStatus)
            .extracting(HttpStatus::getCode)
            .isEqualTo(400);
    }

}
