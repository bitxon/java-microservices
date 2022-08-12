package bitxon.spring.client;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "exchange-client-config")
public class ExchangeClientProperties {
    @NotBlank
    String basePath;
}
