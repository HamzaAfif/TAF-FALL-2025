package ca.etsmtl.taf.controller;

import ca.etsmtl.taf.payload.request.TestApiRequest;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("TestApiController - Unit")
class TestApiControllerTest {

    @Test
    @DisplayName("testApi should return downstream response body when request is valid")
    void testApiShouldReturnSuccessResponseBody() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/microservice/testapi/checkApi", exchange -> {
            String responseJson = "{\"result\":\"ok\"}";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, responseJson.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseJson.getBytes());
            }
        });
        server.start();

        try {
            int port = server.getAddress().getPort();

            TestApiController controller = new TestApiController();

            TestApiRequest request = new TestApiRequest();
            request.setMethod("GET");
            request.setApiUrl("http://localhost:" + port + "/microservice/testapi/checkApi");
            request.setStatusCode(200);
            request.setInput("{}");
            request.setExpectedOutput("ok");

            ResponseEntity<Map<String, Object>> response = controller.testApi(request);

            assertEquals(200, response.getStatusCode().value());
            assertEquals("{\"result\":\"ok\"}", response.getBody().get("output"));
            assertEquals(true, response.getBody().get("answer"));
        } finally {
            server.stop(0);
        }
    }

    @Test
    @DisplayName("testApi should throw URISyntaxException when URL is invalid")
    void testApiShouldThrowForInvalidUri() {
        TestApiController controller = new TestApiController();

        TestApiRequest request = new TestApiRequest();
        request.setMethod("GET");
        request.setApiUrl("http://bad host");
        request.setStatusCode(200);
        request.setInput("{}");
        request.setExpectedOutput("ok");

        assertThrows(URISyntaxException.class, () -> controller.testApi(request));
    }
}
