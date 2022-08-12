package bitxon.api.thirdparty.exchange.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {

    private String base;
    private Map<String, Double> rates;
}
