package no.nav;

import com.atlassian.oai.validator.wiremock.OpenApiValidationListener;
import com.consol.citrus.annotations.CitrusEndpoint;
import com.consol.citrus.http.client.HttpClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.get;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * An example test that uses the {@link OpenApiValidationListener} to validate WireMock interactions
 * against a Swagger API specification.
 * <p>
 * This allows developers to have confidence that the mocks you are setting up in your tests reflect reality. It also
 * gives early (unit-test level) feedback if a breaking change is made to a provider's API, allowing you to
 * respond accordingly.
 *
 * @see ValidatedWireMockRuleTestExample
 * @see <a href="http://wiremock.org/">WireMock</a>
 */
public class ValidatedWireMockListenerTestExample {

    private static final String SWAGGER_JSON_URL = "http://petstore.swagger.io/v2/swagger.json";
    private static final int PORT = 8080;
    private static final String WIREMOCK_URL = "http://localhost:" + PORT;

    private WireMockServer wireMockServer;
    private final OpenApiValidationListener validationListener;

    @CitrusEndpoint
    private HttpClient todoClient;


    public ValidatedWireMockListenerTestExample() {
        this.wireMockServer = new WireMockServer(wireMockConfig().usingFilesUnderDirectory("C:\\Wiremock").port(8080));
        wireMockServer.start();
        validationListener = new OpenApiValidationListener("http://petstore.swagger.io/v2/swagger.json");
        wireMockServer.addMockServiceRequestListener(validationListener);
    }


    @After
    public void teardown() {
        validationListener.reset();
    }

    /**
     * Test a GET with a valid request/response expectation.
     * <p>
     * This test will pass both the (contrived) business logic tests and the Swagger validation.
     */
    @Test
    public void testGetValidPet() {
        wireMockServer.stubFor(
                WireMock.get(urlEqualTo("/pet/1"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("content-type", "application/json")
                                .withBody("{\"name\":\"fido\", \"photoUrls\":[]}")));

        final Response response = get(WIREMOCK_URL + "/pet/1");
        assertThat(response.getStatusCode(), is(200));
        validationListener.assertValidationPassed();
    }

    /**
     * Test a GET with an invalid request/response expectation.
     * <p>
     * This test will pass the business logic tests, but will fail because the expectations encoded
     * in the WireMock stubs do not match the API specification defined in the Swagger spec.
     */
    @Test
    public void testGetInvalidPet() {

        wireMockServer.stubFor(
                WireMock.get(urlEqualTo("/pet/1"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("content-type", "application/json")
                                .withBody("{\"name\":\"fido\"}"))); // Missing required 'photoUrls' field

        //final Response response = get(WIREMOCK_URL + "/pet/1");
        //assertThat(response.getStatusCode(), is(200));
        validationListener.assertValidationPassed();
    }

}