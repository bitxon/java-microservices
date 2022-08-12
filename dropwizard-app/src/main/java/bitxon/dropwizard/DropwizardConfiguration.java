package bitxon.dropwizard;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;
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
