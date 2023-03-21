package bitxon.spring.client;

import bitxon.common.api.thirdparty.exchange.model.ExchangeRate;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface ExchangeClient {

    @GetExchange("/exchanges")
    ExchangeRate getExchangeRate(@RequestParam("currency") String currency);
}
