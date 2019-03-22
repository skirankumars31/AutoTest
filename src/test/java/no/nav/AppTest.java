package no.nav;

import com.consol.citrus.annotations.CitrusEndpoint;
import com.consol.citrus.dsl.design.TestDesigner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.junit.jupiter.CitrusExtension;
import com.consol.citrus.http.client.HttpClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @CitrusEndpoint
    private HttpClient todoClient;

    @BeforeEach
    void configureSystemUnderTest() {
        this.wireMockServer = new WireMockServer(wireMockConfig().port(8080));
        wireMockServer.start();
    }

    @Test
    @CitrusTest
    public void exampleTest(@CitrusResource TestDesigner designer) {

        System.out.println("Server should be running now");

        wireMockServer.stubFor(get(urlEqualTo("/my/resource"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Some content</response>")));

        System.out.println("Server is now started");

        designer.http()
                .client(todoClient)
                .send()
                .get("/my/resource");

        designer.http()
                .client(todoClient)
                .receive()
                .response(HttpStatus.OK).payload("<response>Some content</response>");



    }

    @AfterEach
    void stopWireMockServer() {
        this.wireMockServer.stop();
    }
}
