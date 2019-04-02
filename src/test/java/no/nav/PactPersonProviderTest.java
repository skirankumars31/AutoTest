package no.nav;

import au.com.dius.pact.consumer.PactFolder;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.loader.PactBrokerAuth;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import com.atlassian.oai.validator.wiremock.OpenApiValidationListener;
import com.atlassian.ta.wiremockpactgenerator.WireMockPactGenerator;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import java.lang.annotation.Target;
import java.util.Properties;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@Provider("PersonProvider")
@PactBroker(host = "skirankumars.pact.dius.com.au",
        authentication = @PactBrokerAuth(username = "4eaf1ke5os9i7g5nq2a9vg", password = "tkdxgo5poanc7ddpsfdiv8"))
public class PactPersonProviderTest {

    private WireMockServer wireMockServer;

    Properties prop = System.getProperties();


    @State("Pact for Person")
    public void someProviderState() {
        System.out.println("Person is present in the system");
    }


    @BeforeEach
    void configureSystemUnderTest(PactVerificationContext context) {
        //Connecting to remote server
        WireMock.configureFor("localhost", 8080);

        //This is for Pact
        context.setTarget(new HttpTestTarget("localhost", 8080, "/"));
    }


    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {

        prop.put("pact.verifier.publishResults", "false");
        System.setProperties(prop);

        // Returns JSON from File
        WireMock.stubFor(get(urlEqualTo("/rest/docker/api/fetchPerson"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("svp/Person.json"))
        );

        context.verifyInteraction();
    }


}
