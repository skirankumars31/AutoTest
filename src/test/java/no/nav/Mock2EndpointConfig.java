package no.nav;

import com.consol.citrus.dsl.endpoint.CitrusEndpoints;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.xml.namespace.NamespaceContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Collections;

/**
 * @author Christoph Deppisch
 */
@Configuration
public class Mock2EndpointConfig {

    @Bean
    public HttpClient anotherclient() {
        return CitrusEndpoints.http()
                .client()
                .requestUrl("http://localhost:8080")
                .build();
    }

    @Bean
    public NamespaceContextBuilder namespaceContextBuilder() {
        NamespaceContextBuilder namespaceContextBuilder = new NamespaceContextBuilder();
        namespaceContextBuilder.setNamespaceMappings(Collections.singletonMap("xh", "http://www.w3.org/1999/xhtml"));
        return namespaceContextBuilder;
    }
}