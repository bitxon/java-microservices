package bitxon.spring.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

@Configuration
public class ExchangeClientConfig {

    @Bean
    public ExchangeClient exchangeClient(@Value("${http.exchange-client.url}") String url,
                                         @Value("${http.exchange-client.block-timeout}") Duration blockTimeout,
                                         WebClient.Builder webClientBuilder) {
        var webClient = webClientBuilder.baseUrl(url).build();
        var factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient))
            .blockTimeout(blockTimeout)
            .build();
        return factory.createClient(ExchangeClient.class);
    }
}
