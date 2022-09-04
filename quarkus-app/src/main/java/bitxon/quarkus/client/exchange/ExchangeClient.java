package bitxon.quarkus.client.exchange;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import bitxon.common.api.thirdparty.exchange.model.ExchangeRate;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/exchanges")
@RegisterRestClient(configKey="exchange-client")
public interface ExchangeClient {

    @GET
    ExchangeRate getExchangeRate(@QueryParam("currency") String currency);

}
