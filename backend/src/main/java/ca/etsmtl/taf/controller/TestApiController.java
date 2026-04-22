package ca.etsmtl.taf.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ca.etsmtl.taf.payload.request.TestApiRequest;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.springframework.beans.factory.annotation.Value;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/testapi")
public class TestApiController {
    @Value("${taf.app.testAPI_url}")
    String Test_API_microservice_url;

    @Value("${taf.app.testAPI_port}")
    String Test_API_microservice_port;

    @PostMapping("/checkApi")
        public ResponseEntity<Map<String, Object>> testApi(@Valid @RequestBody TestApiRequest testApiRequest) throws URISyntaxException, IOException, InterruptedException {
                URI uri;
                if (testApiRequest.getApiUrl() != null && !testApiRequest.getApiUrl().isBlank()) {
                        uri = new URI(testApiRequest.getApiUrl().trim());
                } else {
                        uri = new URI(Test_API_microservice_url + ":" + Test_API_microservice_port + "/microservice/testapi/checkApi");
                }

        ObjectMapper objectMapper = new ObjectMapper();

        String requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(testApiRequest);

        HttpClient client = HttpClient.newHttpClient();

                String method = testApiRequest.getMethod() == null ? "POST" : testApiRequest.getMethod().trim().toUpperCase(Locale.ROOT);

                HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(uri)
                                .header("Content-Type", "application/json");

                switch (method) {
                        case "GET":
                                requestBuilder.GET();
                                break;
                        case "DELETE":
                                requestBuilder.DELETE();
                                break;
                        case "PUT":
                                requestBuilder.PUT(BodyPublishers.ofString(requestBody));
                                break;
                        case "POST":
                        default:
                                requestBuilder.POST(BodyPublishers.ofString(requestBody));
                                break;
                }

                HttpRequest request = requestBuilder.build();

        HttpResponse<String> response =
                client.send(request, BodyHandlers.ofString());

                Map<String, Object> result = new HashMap<>();
                result.put("id", 0);
                result.put("stutsCode", response.statusCode());
                result.put("output", response.body());
                result.put("fieldAnswer", null);
                result.put("answer", response.statusCode() == testApiRequest.getStatusCode());
                result.put("messages", new ArrayList<>());

                return ResponseEntity.ok(result);
    }
}
