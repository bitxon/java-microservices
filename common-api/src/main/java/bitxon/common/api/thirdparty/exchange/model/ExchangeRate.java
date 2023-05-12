package bitxon.common.api.thirdparty.exchange.model;

import lombok.Builder;

import java.util.Map;

@Builder
public record ExchangeRate(
    String base,
    Map<String, Double> rates
) {
}
