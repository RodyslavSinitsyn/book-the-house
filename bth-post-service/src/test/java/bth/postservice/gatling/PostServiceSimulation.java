package bth.postservice.gatling;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import org.springframework.http.MediaType;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class PostServiceSimulation extends Simulation {

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8081")
            .acceptHeader(MediaType.APPLICATION_JSON_VALUE)
            .contentTypeHeader(MediaType.APPLICATION_JSON_VALUE)
            .userAgentHeader("Gatling");

    private final ScenarioBuilder postsScenario = scenario("Load posts simulation")
            .exec(http("posts")
                    .post("/graphql")
                    .body(StringBody("""
                            {"query":"query {\\n  posts(page: 0, filter:{}) {\\n    id\\n  }\\n}"}
                            """))
                    .check(status().is(200)));

    {
        setUp(
                postsScenario.injectOpen(
                        rampUsers(50).during(30),
                        constantUsersPerSec(10).during(30),
                        stressPeakUsers(200).during(10)
                )
        ).protocols(httpProtocol);
    }
}
