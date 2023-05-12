package bitxon.dropwizard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.core.Configuration;
import io.dropwizard.db.DataSourceFactory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class DropwizardConfiguration extends Configuration {

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    private DropwizardConfiguration.ExchangeClientConfig exchangeClientConfig = new ExchangeClientConfig();


    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class ExchangeClientConfig extends JerseyClientConfiguration {

        @NotBlank
        @JsonProperty("basePath")
        String basePath;

    }
}
