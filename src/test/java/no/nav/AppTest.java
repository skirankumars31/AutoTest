package no.nav;

import au.com.dius.pact.consumer.PactFolder;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.loader.PactBrokerAuth;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import com.atlassian.oai.validator.wiremock.OpenApiValidationListener;
import com.atlassian.oai.validator.wiremock.ValidatedWireMockRule;
import com.atlassian.ta.wiremockpactgenerator.WireMockPactGenerator;
import com.consol.citrus.annotations.CitrusEndpoint;
import com.consol.citrus.dsl.design.TestDesigner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.junit.jupiter.CitrusExtension;
import com.consol.citrus.http.client.HttpClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * Unit test for simple App.
 */

@ExtendWith(CitrusExtension.class)
public class AppTest
{
    private WireMockServer wireMockServer;
    private OpenApiValidationListener validationListener;

    @CitrusEndpoint
    private HttpClient todoClient;

    @State("Pact for Issue 313")
    public void someProviderState() {
        System.out.println("Issue 313 is present in the system");
    }


    @BeforeEach
    void configureSystemUnderTest() {

        //this will start a server here
        this.wireMockServer = new WireMockServer(wireMockConfig().usingFilesUnderDirectory("C:\\Wiremock").port(8080));
        wireMockServer.start();

        //Connecting to remote server
        //WireMock.configureFor("localhost", 8080);

        wireMockServer.addMockServiceRequestListener(
                WireMockPactGenerator
                        .builder("the-consumer", "the-provider")
                        .withStrictApplicationJson(false)
                        .build()
        );

        this.validationListener = new OpenApiValidationListener("http://localhost:8082/v2/api-docs");
        wireMockServer.addMockServiceRequestListener(validationListener);

    }

    @Test
    @CitrusTest
    public void exampleTest(@CitrusResource TestDesigner designer) {

        //Returns body XML on get request
        /*wireMockServer.stubFor(get(urlEqualTo("/my/resource"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Some content</response>"))
        );
*/
        //Returns body XML on post request
        /*wireMockServer.stubFor(post(urlEqualTo("/my/resource"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<PostResponse>Some content</PostResponse>"))
        );*/


        // Returns XML from File
        /*WireMock.stubFor(get(urlEqualTo("/my/resource"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBodyFile("svp/resource.xml"))
        );*/

        // Returns JSON from File
        wireMockServer.stubFor(get(urlEqualTo("/my/resource"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("response.json"))
        );


        designer.http()
                .client(todoClient)
                .send()
                .get("/my/resource");

        //Post Request
        /*designer.http()
                .client(todoClient)
                .send()
                .post("/my/resource")
                .payload("<ID>1</ID>");*/


        //Validates the full XML
        /*designer.http()
                .client(todoClient)
                .receive()
                .response(HttpStatus.OK).payload("<response>Some content</response>");*/

        //Validates the value in XML Nodes in the post response
        /*designer.http()
                .client(todoClient)
                .receive()
                .response(HttpStatus.OK)
                .validate("PostResponse", "Some content");*/


        //Validates the value in XML Nodes in the get response
        /*designer.http()
                .client(todoClient)
                .receive()
                .response(HttpStatus.OK)
                .validate("response", "Some content");*/

        //Validates the value in Json Nodes
        designer.http()
        .client(todoClient)
                .receive()
                .response(HttpStatus.OK)
                .validate("$.description", "Sample Description");


        validationListener.assertValidationPassed();

}


    @Test
    @CitrusTest
    public void validateSwaggerApi(@CitrusResource TestDesigner designer) {

        //Returns body XML on get request
        /*wireMockServer.stubFor(get(urlEqualTo("/my/resource"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Some content</response>"))
        );
*/
        //Returns body XML on post request
        /*wireMockServer.stubFor(post(urlEqualTo("/my/resource"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<PostResponse>Some content</PostResponse>"))
        );*/


        // Returns XML from File
        /*WireMock.stubFor(get(urlEqualTo("/my/resource"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBodyFile("svp/resource.xml"))
        );*/

        // Returns JSON from File
        wireMockServer.stubFor(get(urlEqualTo("/rest/docker/hello"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/xml")
                        .withBody("<response>Some content</response>"))
        );


        designer.http()
                .client(todoClient)
                .send()
                .get("/rest/docker/hello");

        //Post Request
        /*designer.http()
                .client(todoClient)
                .send()
                .post("/my/resource")
                .payload("<ID>1</ID>");*/


        //Validates the full XML
        /*designer.http()
                .client(todoClient)
                .receive()
                .response(HttpStatus.OK).payload("<response>Some content</response>");*/

        //Validates the value in XML Nodes in the post response
        /*designer.http()
                .client(todoClient)
                .receive()
                .response(HttpStatus.OK)
                .validate("PostResponse", "Some content");*/


        //Validates the value in XML Nodes in the get response
        /*designer.http()
                .client(todoClient)
                .receive()
                .response(HttpStatus.OK)
                .validate("response", "Some content");*/

        //Validates the value in Json Nodes
        designer.http()
                .client(todoClient)
                .receive()
                .response(HttpStatus.OK)
                .payload("<response>Some content</response>");

        validationListener.assertValidationPassed();

    }


    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {


        // Returns JSON from File
        WireMock.stubFor(get(urlEqualTo("/articles.json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("svp/resource.json"))
        );

        context.verifyInteraction();
    }


    @AfterEach
    void stopWireMockServer() {
        this.wireMockServer.stop();
        this.validationListener.reset();
    }
}
