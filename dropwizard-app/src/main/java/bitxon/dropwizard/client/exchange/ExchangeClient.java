package bitxon.dropwizard.client.exchange;

import jakarta.ws.rs.client.Client;

import bitxon.common.api.thirdparty.exchange.model.ExchangeRate;
import bitxon.dropwizard.DropwizardConfiguration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExchangeClient {

    private final Client client;
    private final DropwizardConfiguration.ExchangeClientConfig config;

    public ExchangeRate getExchangeRate(String currency) {
        var response = client.target(config.getBasePath() + currency)
            .request()
            .get(ExchangeRate.class);
        return response;
    }
}
