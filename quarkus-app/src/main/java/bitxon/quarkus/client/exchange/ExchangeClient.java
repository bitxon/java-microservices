package bitxon.quarkus.client.exchange;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

import bitxon.common.api.thirdparty.exchange.model.ExchangeRate;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/exchanges")
@RegisterRestClient(configKey="exchange-client")
public interface ExchangeClient {

    @GET
    ExchangeRate getExchangeRate(@QueryParam("currency") String currency);

}
