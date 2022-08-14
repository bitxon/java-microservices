package bitxon.micronaut;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.http.HttpRequest;
import org.junit.jupiter.api.Test;


class FindAccountsMicronautTest extends AbstractMicronautTest {

    @Test
    public void getAll() {
        var request = HttpRequest.GET("");
        var response = client().toBlocking().exchange(request);

        assertThat(response.getStatus().getCode()).isEqualTo(200);
    }

}
