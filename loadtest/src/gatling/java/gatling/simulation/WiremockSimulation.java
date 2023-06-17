package gatling.simulation;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class WiremockSimulation extends Simulation {

    static final String BASE_URL = System.getProperty("baseUrl", "http://localhost:8888");
    static final int USERS = Integer.getInteger("users", 500);
    static final int REQUESTS_PER_USER = Integer.getInteger("requestsPerUser", 20);
    static final int DURATION = Integer.getInteger("duration", 20);

    //-----------------------------------------------------------------------------------------------------------------

    static FeederBuilder<String> feederCurrency = csv("feeders/currency.csv").random();

    //-----------------------------------------------------------------------------------------------------------------

    private static ChainBuilder getExchangeRate() {
        return exec(http("Get Exchange Rate")
            .get("/exchanges?currency=#{currency}")
            .check(status().is(200))
        );
    }

    //-----------------------------------------------------------------------------------------------------------------

    ScenarioBuilder scenarioGetOne = scenario("Get One - Scenario")
        .feed(feederCurrency)
        .exec(
            repeat(REQUESTS_PER_USER).on(
                getExchangeRate()
            )
        );

    //-----------------------------------------------------------------------------------------------------------------

    HttpProtocolBuilder httpProtocol = http.baseUrl(BASE_URL)
        .acceptHeader("application/json")
        .acceptLanguageHeader("en-US,en;q=0.5");

    {
        setUp(
            scenarioGetOne.injectOpen(
                rampUsers(USERS).during(DURATION)
            )
        ).protocols(httpProtocol);
    }

}
