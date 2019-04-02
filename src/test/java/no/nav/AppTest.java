package no.nav;

import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import com.atlassian.oai.validator.wiremock.OpenApiValidationListener;
import com.atlassian.ta.wiremockpactgenerator.WireMockPactGenerator;
import com.consol.citrus.annotations.CitrusEndpoint;
import com.consol.citrus.dsl.design.TestDesigner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.junit.jupiter.CitrusExtension;
import com.consol.citrus.http.client.HttpClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

/**
 * Unit test for simple App.
 */

@ExtendWith(CitrusExtension.class)
public class AppTest
{
    private WireMockServer wireMockServer;
    private OpenApiValidationListener validationListener;
    private static final int PORT = 8080;
    private static final String WIREMOCK_URL = "http://localhost:" + PORT;

    @CitrusEndpoint
    private HttpClient todoClient;
    private HttpClient anotherclient;

    @State("Pact for Issue 313")
    public void someProviderState() {
        System.out.println("Issue 313 is present in the system");
    }

    @BeforeEach
    void configureSystemUnderTest() {

        //this will start a server here
        this.wireMockServer = new WireMockServer(wireMockConfig().usingFilesUnderDirectory("C:\\Wiremock").port(8080));
        //Connecting to remote server
        //WireMock.configureFor("localhost", 8080);

     /*   wireMockServer.addMockServiceRequestListener(
                WireMockPactGenerator
                        .builder("the-consumer", "the-provider")
                        .withStrictApplicationJson(false)
                        .build()
        );*/

        this.validationListener = new OpenApiValidationListener("http://petstore.swagger.io/v2/swagger.json");
        wireMockServer.addMockServiceRequestListener(validationListener);
        wireMockServer.start();

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
        wireMockServer.stubFor(get(urlEqualTo("/rest/docker/api/fetchPerson"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/xml")
                        .withBodyFile("svp/Person.json"))
        );


        designer.http()
                .client(todoClient)
                .send()
                .get("/rest/docker/api/fetchPerson");

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
                .validate("$.super", "kiran");

        validationListener.assertValidationPassed();

    }

    @Test
    @CitrusTest
    private void testGetInvalidPet(@CitrusResource TestDesigner designer) {
        wireMockServer.stubFor(
                WireMock.get(urlEqualTo("/pet/1"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("content-type", "application/json")
                                .withBody("{\"name\":\"fido\"}"))); // Missing required 'photoUrls' field


        // This is done to validate the response model with swagger
        final Response response = RestAssured.get(WIREMOCK_URL + "/pet/1");

        designer.http()
                .client(todoClient)
                .send()
                .get("/pet/1");


        designer.http()
                .client(todoClient)
                .receive()
                .response(HttpStatus.OK)
                .validate("$.name", "fido");


        validationListener.assertValidationPassed();
    }

    @Test
    @CitrusTest
    public void testPostOrder(@CitrusResource TestDesigner designer) {
        wireMockServer.stubFor(
                WireMock.post(urlEqualTo("/store/order"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("content-type", "application/json")
                                .withBody("{\"id\":0}"))); // Missing required 'photoUrls' field


        // This is done to validate the response model with swagger
        final Response response = given()
                .contentType(ContentType.JSON)
                .body("{\"id\":0}")
                .post("/store/order")
                .then()
                .statusCode(200)
                .extract()
                .response();

        //dynamic http endpoints
        designer.http()
                .client("http://localhost:8080")
                .send()
                .post("/store/order").payload("{\"id\":0}");


        designer.http()
                .client("http://localhost:8080")
                .receive()
                .response(HttpStatus.OK)
                .validate("$.id", 0);


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
