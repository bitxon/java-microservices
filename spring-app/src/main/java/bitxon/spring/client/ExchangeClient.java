package bitxon.spring.client;

import bitxon.api.thirdparty.exchange.model.ExchangeRate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "exchange-client", url = "${feign.client.config.exchange-client.url}")
public interface ExchangeClient {

    @GetMapping("/exchanges")
    ExchangeRate getExchangeRate(@RequestParam("currency") String currency);
}
