package gatling.simulation;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

public class CommonSimulation extends Simulation {
    static Iterator<Map<String, Object>> feederPost = Stream.generate((Supplier<Map<String, Object>>) () ->
        Map.of(
            "email", UUID.randomUUID() + "@mail.com",
            "currency", "USD",
            "moneyAmount", 340
        )
    ).iterator();

    static Iterator<Map<String, Object>> feederTransfer = Stream.generate((Supplier<Map<String, Object>>) () ->
        Map.of(
            "moneyAmount", new Random().nextInt(100)
        )
    ).iterator();


    //-----------------------------------------------------------------------------------------------------------------

    private static ChainBuilder postAccount(String sessionFieldNameForId) {
        return exec().feed(feederPost).exec(http("Create One")
            .post("/")
            .header("Content-Type", "application/json")
            .body(StringBody("{\"email\": \"#{email}\",\"currency\": \"#{currency}\",\"moneyAmount\": #{moneyAmount}}"))
            .check(jsonPath("$.id").saveAs(sessionFieldNameForId))
        );
    }

    private static ChainBuilder getOneAccountById() {
        return exec(http("Get One by Id").get("/#{id}"));
    }

    private static ChainBuilder getAllAccounts() {
        return exec(http("Get All").get("/"));
    }

    private static ChainBuilder postTransfer() {
        return exec().feed(feederTransfer).exec(http("Transfer")
            .post("/transfers")
            .header("Content-Type", "application/json")
            .body(StringBody("""
                {
                    "senderId": "#{senderId}",
                    "recipientId": "#{recipientId}",
                    "moneyAmount": #{moneyAmount}
                }
                """))
        );
    }

    //-----------------------------------------------------------------------------------------------------------------

    HttpProtocolBuilder httpProtocol = http.baseUrl("http://localhost:8080/accounts")
        .acceptHeader("application/json")
        .acceptLanguageHeader("en-US,en;q=0.5")
        .acceptEncodingHeader("gzip, deflate")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0");

    ScenarioBuilder scenarioGetAll = scenario("Get All - Scenario").exec(
        getAllAccounts()
    );

    ScenarioBuilder scenarioGetOne = scenario("Get One - Scenario").exec(
        postAccount("id"),
        getOneAccountById()
    );

    ScenarioBuilder scenarioTransfer = scenario("Transfer - Scenario").exec(
        postAccount("senderId"),
        postAccount("recipientId"),
        postTransfer()
    );

    //-----------------------------------------------------------------------------------------------------------------

    {
        setUp(
            scenarioGetAll.injectOpen(
                constantUsersPerSec(4).during(90)
            ),
            scenarioGetOne.injectOpen(
                nothingFor(4), // Wait
                atOnceUsers(10), // 10
                rampUsers(10).during(5), // 10 -> 20
                constantUsersPerSec(20).during(15), // 20
                constantUsersPerSec(20).during(15).randomized(), // 20
                rampUsersPerSec(10).to(20).during(10), // 10 -> 20
                rampUsersPerSec(10).to(20).during(10).randomized(), // 10 -> 20
                stressPeakUsers(400).during(20) // 8
            ),
            scenarioTransfer.injectOpen(
                incrementUsersPerSec(3)
                    .times(6)
                    .eachLevelLasting(8)
                    .separatedByRampsLasting(8)
                    .startingFrom(8)
            )
        ).protocols(httpProtocol);
    }
}
