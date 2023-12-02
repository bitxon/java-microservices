package bitxon.spring.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

@Configuration
public class ExchangeClientConfig {

    @Bean
    public ExchangeClient exchangeClient(@Value("${http.exchange-client.url}") String url,
                                         @Value("${http.exchange-client.read-timeout}") Duration readTimeout,
                                         RestClient.Builder restClientBuilder) {
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(readTimeout);

        var restClient = restClientBuilder
            .baseUrl(url)
            .requestFactory(requestFactory)
            .build();

        var factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
        return factory.createClient(ExchangeClient.class);
    }
}
