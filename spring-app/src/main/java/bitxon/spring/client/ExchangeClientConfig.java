package bitxon.spring.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ExchangeClientConfig {

    @Bean
    public ExchangeClient exchangeClient(@Value("${http.exchange-client.url}") String url) {
        var webClient = WebClient.create(url);
        var factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient)).build();
        var serviceClient = factory.createClient(ExchangeClient.class);
        return serviceClient;
    }
}
