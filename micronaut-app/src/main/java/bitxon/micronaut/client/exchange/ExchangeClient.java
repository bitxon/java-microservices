package bitxon.micronaut.client.exchange;

import bitxon.common.api.thirdparty.exchange.model.ExchangeRate;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;

@Client("exchange-client")
public interface ExchangeClient {

    @Get("/exchanges{?currency}")
    ExchangeRate getExchangeRate(@QueryValue String currency);
}
