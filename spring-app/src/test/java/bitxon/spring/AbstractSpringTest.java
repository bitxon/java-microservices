package bitxon.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(classes = SpringAppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AbstractSpringTest {

    @Autowired
    private TestRestTemplate client;

    protected TestRestTemplate client() {
        return client;
    }

}
