package gatling.simulation;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class AppSimulation extends Simulation {

    static final String BASE_URL = System.getProperty("baseUrl", "http://localhost:8080");
    static final int USERS = Integer.getInteger("users", 500);
    static final int REQUESTS_PER_USER = Integer.getInteger("requestsPerUser", 5); // TODO align with WiremockSimulation value
    static final int DURATION = Integer.getInteger("duration", 20);

    //-----------------------------------------------------------------------------------------------------------------

    static Iterator<Map<String, Object>> feederPost = Stream.generate((Supplier<Map<String, Object>>) () ->
        Map.of(
            "email", UUID.randomUUID() + "@mail.com",
            "firstName", UUID.randomUUID().toString().replace("-", ""),
            "lastName", UUID.randomUUID().toString().replace("-", ""),
            "dateOfBirth", LocalDate.of(1990, 1, 1),
            "currency", "USD",
            "moneyAmount", 340
        )
    ).iterator();

    static Iterator<Map<String, Object>> feederTransfer = Stream.generate((Supplier<Map<String, Object>>) () ->
        Map.of(
            "moneyAmount", new Random().nextInt(100)
        )
    ).iterator();

    static FeederBuilder<String> feederInvalidPost = csv("feeders/post-account-invalid-body.csv").random();


    //-----------------------------------------------------------------------------------------------------------------

    private static ChainBuilder postAccount(String sessionFieldNameForId) {
        return exec().feed(feederPost).exec(http("Create")
            .post("/accounts")
            .header("Content-Type", "application/json")
            .body(StringBody("""
                {
                    "email": "#{email}",
                    "firstName": "#{firstName}",
                    "lastName": "#{lastName}",
                    "dateOfBirth": "#{dateOfBirth}",
                    "currency": "#{currency}",
                    "moneyAmount": #{moneyAmount}
                }
                """))
            .check(status().is(200))
            .check(jsonPath("$.id").saveAs(sessionFieldNameForId))
        );
    }

    private static ChainBuilder postInvalidAccount() {
        return exec().feed(feederInvalidPost).exec(http("Create (400,422)")
            .post("/accounts")
            .header("Content-Type", "application/json")
            .body(StringBody("""
                {
                    "email": "#{email}",
                    "firstName": "#{firstName}",
                    "lastName": "#{lastName}",
                    "dateOfBirth": "#{dateOfBirth}",
                    "currency": "#{currency}",
                    "moneyAmount": #{moneyAmount}
                }
                """))
            .check(status().in(400, 422))
        );
    }

    private static ChainBuilder getOneAccountById() {
        return exec(http("Get One")
            .get("/accounts/#{id}")
            .check(status().is(200))
        );
    }

    private static ChainBuilder getAllAccounts() {
        return exec(http("Get All")
            .get("/accounts")
            .check(status().is(200))
        );
    }

    private static ChainBuilder postTransfer() {
        return exec().feed(feederTransfer).exec(http("Transfer")
            .post("/accounts/transfers")
            .header("Content-Type", "application/json")
            .body(StringBody("""
                {
                    "senderId": "#{senderId}",
                    "recipientId": "#{recipientId}",
                    "moneyAmount": #{moneyAmount}
                }
                """))
            .check(status().is(204))
        );
    }

    //-----------------------------------------------------------------------------------------------------------------

    ScenarioBuilder scenarioGetAll = scenario("Get All - Scenario").exec(
        getAllAccounts()
    );

    ScenarioBuilder scenarioGetOne = scenario("Get One - Scenario").exec(
        postAccount("id").exitHereIfFailed(),
        getOneAccountById()
    );

    ScenarioBuilder scenarioTransfer = scenario("Transfer - Scenario").exec(
        repeat(REQUESTS_PER_USER).on(exec(
            postAccount("senderId").exitHereIfFailed(),
            postAccount("recipientId").exitHereIfFailed(),
            postTransfer()
        ))
    );

    ScenarioBuilder scenarioValidation = scenario("Validation 4xx - Scenario").exec(
        postInvalidAccount()
    );

    //-----------------------------------------------------------------------------------------------------------------

    HttpProtocolBuilder httpProtocol = http.baseUrl(BASE_URL)
        .acceptHeader("application/json")
        .acceptLanguageHeader("en-US,en;q=0.5");

    {
        setUp(
            scenarioGetAll.injectOpen(
                constantUsersPerSec(2).during(DURATION)
            ),
            scenarioGetOne.injectOpen(
                constantUsersPerSec(2).during(DURATION)
            ),
            scenarioTransfer.injectOpen(
                // Transfer is the most I/O intensive operation in application
                rampUsers(USERS).during(DURATION)
            ),
            scenarioValidation.injectOpen(
                constantUsersPerSec(2).during(DURATION)
            )
        ).protocols(httpProtocol);
    }
}
