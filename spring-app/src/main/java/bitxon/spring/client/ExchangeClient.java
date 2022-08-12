package bitxon.spring.client;

import bitxon.api.thirdparty.exchange.model.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ExchangeClient {

    private final RestTemplate restTemplate;
    private final ExchangeClientProperties exchangeClientProperties;


    public ExchangeRate getExchangeRate(String currency) {
        var response = restTemplate.getForObject(
            exchangeClientProperties.getBasePath() + currency,
            ExchangeRate.class
        );
        return response;
    }
}
