package no.nav;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import au.com.dius.pact.model.generators.Category;
import au.com.dius.pact.model.generators.RandomIntGenerator;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@ExtendWith(PactConsumerTestExt.class)
//@PactTestFor(providerName = "ArticlesProvider",port = "9095")
@PactTestFor(port = "9095")
public class PactTest {

    private Map<String, String> headers = MapUtils.putAll(new HashMap<>(), new String[] {
            "Content-Type", "application/json"
    });

    @Pact(provider = "ArticlesProvider", consumer = "ArticlesConsumer")
    public RequestResponsePact articles(PactDslWithProvider builder) {

        final DslPart body = new PactDslJsonBody()
                .stringType("name")
                .booleanType("isWorking")
                .booleanType("medlemskap")
                .stringMatcher("position", "staff|contactor","staff")
                .stringMatcher("FNR","\\d{11}","19048634263")
                .numberType("Age")
                .numberType("Salary");

        body.getGenerators()
                .addGenerator(Category.BODY, ".Age", new RandomIntGenerator(0, 100));

        final DslPart personbody = new PactDslJsonBody()
                .stringType("name")
                .stringMatcher("FNR","\\d{11}","19048634263");

        return builder
                .given("Pact for Issue 313")
                .uponReceiving("retrieving article data for person with salary")
                .path("/articles.json")
                .method("GET")
                .willRespondWith()
                .headers(headers)
                .status(200)
                .body(body)
                .given("Pact for Issue 314")
                .uponReceiving("retrieving article data for person without salary")
                .path("/getarticles")
                .method("GET")
                .willRespondWith()
                .headers(headers)
                .status(200)
                .body(personbody)
                .toPact();
    }


    /*@Pact(provider = "ArticlesProvider", consumer = "ArticlesConsumer")
    public RequestResponsePact person(PactDslWithProvider builder) {

        final DslPart body = new PactDslJsonBody()
                .stringType("name")
                .booleanType("isWorking")
                .booleanType("medlemskap")
                .stringMatcher("position", "staff|contactor","staff")
                .stringMatcher("FNR","\\d{11}","19048634263")
                .numberType("Age")
                .numberType("Salary");

        body.getGenerators()
                .addGenerator(Category.BODY, ".Age", new RandomIntGenerator(0, 100));

        return builder
                .given("Pact for Issue 313")
                .uponReceiving("retrieving article data")
                .path("/person2.json")
                .method("GET")
                .willRespondWith()
                .headers(headers)
                .status(200)
                .body(body)
                .toPact();
    }*/

    @Pact(provider = "PersonProvider", consumer = "PersonConsumer")
    public RequestResponsePact person(PactDslWithProvider builder) {

        final DslPart personbody = new PactDslJsonBody()
                .stringType("name")
                .stringMatcher("FNR","\\d{11}","19048634263");

        return builder
                .given("Pact for Person")
                .uponReceiving("retrieving person data")
                .path("/rest/docker/api/fetchPerson")
                .method("GET")
                .willRespondWith()
                .headers(headers)
                .status(200)
                .body(personbody)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "articles")
    void testArticles(MockServer mockServer) throws IOException {
        HttpResponse httpResponse = Request.Get(mockServer.getUrl() + "/articles.json").execute().returnResponse();
        assertThat(httpResponse.getStatusLine().getStatusCode(), is(equalTo(200)));

        httpResponse = Request.Get(mockServer.getUrl() + "/getarticles").execute().returnResponse();
        assertThat(httpResponse.getStatusLine().getStatusCode(), is(equalTo(200)));
    }


   @Test
   @PactTestFor(pactMethod = "person")
    void testPerson(MockServer mockServer) throws IOException {

       HttpResponse httpResponse = Request.Get(mockServer.getUrl() + "/rest/docker/api/fetchPerson").execute().returnResponse();
       assertThat(httpResponse.getStatusLine().getStatusCode(), is(equalTo(200)));

    }
}
