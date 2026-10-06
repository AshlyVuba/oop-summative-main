package za.co.wethinkcode.ridehub.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

class BikeApiTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();
    private Javalin app;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        app = new RideHubApp(new BikeRepository()).create().start(0);
        baseUrl = "http://127.0.0.1:" + app.port();
    }

    @AfterEach
    void tearDown() {
        app.stop();
    }

    // ---- GET /api/bikes (worked example) --------------------------------

    @Test
    void listBikes_returns200AndEveryBike() throws Exception {
        HttpResponse<String> response = get("/api/bikes");

        assertThat(response.statusCode()).isEqualTo(200);
        JsonNode bikes = mapper.readTree(response.body());
        assertThat(bikes.isArray()).isTrue();
        assertThat(bikes.size()).isEqualTo(3);
    }

    // ---- GET /api/bikes/{serialNo} --------------------------------------

    @Test
    void getBike_returns200AndTheBikeWhenItExists() throws Exception {
        HttpResponse<String> response = get("/api/bikes/EB-001");

        assertThat(response.statusCode()).isEqualTo(200);
        JsonNode bike = mapper.readTree(response.body());
        assertThat(bike.get("serialNo").asText()).isEqualTo("EB-001");
        assertThat(bike.get("type").asText()).isEqualTo("ELECTRIC");
        assertThat(bike.get("available").asBoolean()).isTrue();
    }

    @Test
    void getBike_returns404WithAnErrorMessageWhenItDoesNotExist() throws Exception {
        HttpResponse<String> response = get("/api/bikes/XX-999");

        assertThat(response.statusCode()).isEqualTo(404);
        assertThat(mapper.readTree(response.body()).get("error").asText()).isNotBlank();
    }

    // ---- POST /api/rentals ----------------------------------------------

    @Test
    void createRental_returns201AndTheRentalDetails() throws Exception {
        HttpResponse<String> response = post("/api/rentals",
                "{\"riderName\":\"Thandiwe Nkosi\",\"serialNo\":\"SB-001\"}");

        assertThat(response.statusCode()).isEqualTo(201);
        JsonNode rental = mapper.readTree(response.body());
        assertThat(rental.get("riderName").asText()).isEqualTo("Thandiwe Nkosi");
        assertThat(rental.get("serialNo").asText()).isEqualTo("SB-001");
        assertThat(rental.get("unlockFee").asDouble()).isEqualTo(5.0);
    }

    @Test
    void createRental_marksTheBikeAsUnavailable() throws Exception {
        post("/api/rentals", "{\"riderName\":\"Thandiwe Nkosi\",\"serialNo\":\"SB-001\"}");

        JsonNode bike = mapper.readTree(get("/api/bikes/SB-001").body());
        assertThat(bike.get("available").asBoolean()).isFalse();
    }

    @Test
    void createRental_returns409WhenTheBikeIsAlreadyRented() throws Exception {
        HttpResponse<String> response = post("/api/rentals",
                "{\"riderName\":\"Sipho Dlamini\",\"serialNo\":\"SB-002\"}");

        assertThat(response.statusCode()).isEqualTo(409);
    }

    @Test
    void createRental_returns409WhenTheSameBikeIsRentedTwice() throws Exception {
        post("/api/rentals", "{\"riderName\":\"Thandiwe Nkosi\",\"serialNo\":\"SB-001\"}");
        HttpResponse<String> second = post("/api/rentals",
                "{\"riderName\":\"Sipho Dlamini\",\"serialNo\":\"SB-001\"}");

        assertThat(second.statusCode()).isEqualTo(409);
    }

    @Test
    void createRental_returns404WhenTheBikeDoesNotExist() throws Exception {
        HttpResponse<String> response = post("/api/rentals",
                "{\"riderName\":\"Sipho Dlamini\",\"serialNo\":\"XX-999\"}");

        assertThat(response.statusCode()).isEqualTo(404);
    }

    @Test
    void createRental_returns400WhenTheBodyIsNotValidJson() throws Exception {
        HttpResponse<String> response = post("/api/rentals", "this is not json");

        assertThat(response.statusCode()).isEqualTo(400);
    }

    @Test
    void createRental_returns400WhenTheRiderNameIsMissing() throws Exception {
        HttpResponse<String> response = post("/api/rentals", "{\"serialNo\":\"SB-001\"}");

        assertThat(response.statusCode()).isEqualTo(400);
    }

    // ---- helpers --------------------------------------------------------

    private HttpResponse<String> get(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + path)).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> post(String path, String json) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
