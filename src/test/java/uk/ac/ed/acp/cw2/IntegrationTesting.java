package uk.ac.ed.acp.cw2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//Class for integration testing. Unit testing complete and works.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTesting {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpEntity<String> jsonEntity(String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    //Testing actuator/health exists
    @Test
    void actuatorHealthExists() {
        String url = "http://localhost:" + port + "/actuator/health";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    //Testing uid exists and returns my student number
    @Test
    void uidExists() {
        String url = "http://localhost:" + port + "/api/v1/uid";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("s2524342");

    }

    //Testing distanceTo endpoint returns correct distance for correct JSON
    @Test
    void distanceTo_correctJSON() {
        String url = "http://localhost:" + port + "/api/v1/distanceTo";
        String json = """
                {
                  "position1": {
                    "lng": -3.192473,
                    "lat": 55.946233
                  },
                  "position2": {
                    "lng": -3.192473,
                    "lat": 55.942617
                  }
                }
                """;
        ResponseEntity<Double> response = restTemplate.postForEntity(url, jsonEntity(json), Double.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isGreaterThan(0.0);
    }

    //Testing distanceTo with typos in parameters
    @Test
    void distanceTo_incorrectJSON() {
        String url = "http://localhost:" + port + "/api/v1/distanceTo";
        String json = """
                {
                  "position1": {
                    "lng": -3.192473,
                    "lt": 55.946233
                  },
                  "poston2": {
                    "lng": -3.192473,
                    "lat": 55.942617
                  }
                }
                """;
        ResponseEntity<Double> response = restTemplate.postForEntity(url, jsonEntity(json), Double.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    //testing distance to with missing parameters
    @Test
    void distanceTo_incorrectJSON2() {
        String url = "http://localhost:" + port + "/api/v1/distanceTo";
        String json = """
                {
                  "position1": {
                    "lng": -3.192473,
                    "lat": 55.946233
                  },
                  "position2": {
                    "lng": -3.192473
                  }
                }
                """;
        ResponseEntity<Double> response = restTemplate.postForEntity(url, jsonEntity(json), Double.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    //Unit testing isCloseTo with correct JSON and should be close
    @Test
    void isCloseTo_correctJSONAndIsClose() {
        String url = "http://localhost:" + port + "/api/v1/isCloseTo";
        String json = """
                {
                  "position1": {
                    "lng": 2,
                    "lat": 2
                  },
                  "position2": {
                    "lng": 2,
                    "lat": 2
                  }
                }
                """;
        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, jsonEntity(json), Boolean.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isTrue();
    }

    //Unit testing isCloseTo with correct JSON and shouldn't be close
    @Test
    void isCloseTo_correctJSONAndIsFar() {
        String url = "http://localhost:" + port + "/api/v1/isCloseTo";
        String json = """
                {
                  "position1": {
                    "lng": 2,
                    "lat": 2
                  },
                  "position2": {
                    "lng": 4,
                    "lat": 4
                  }
                }
                """;
        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, jsonEntity(json), Boolean.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isFalse();
    }

    //Unit testing isCloseTo with bad JSON
    @Test
    void isCloseTo_badJSON() {
        String url = "http://localhost:" + port + "/api/v1/isCloseTo";
        String json = """
                {
                  "position1": {
                    "lng": 2,
                    "lat": 2
                  },
                  "position2": {
                    "lng": 4
                  }
                }
                """;
        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, jsonEntity(json), Boolean.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    //Testing nextPosition with correct JSON
    @Test
    void nextPosition_correctJSON() {
        String url = "http://localhost:" + port + "/api/v1/nextPosition";
        String json = """
                {
                  "start": { "lng": -3.192473, "lat": 3},
                  "angle": 45
                }
                """;
        ResponseEntity<String> response = restTemplate.postForEntity(url, jsonEntity(json), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("lat").contains("lng");
    }

    //Testing nextPosition with semantically incorrect JSON
    @Test
    void nextPosition_semanticallyBadJSON() {
        String url = "http://localhost:" + port + "/api/v1/nextPosition";
        String json = """
                {
                  "start": { "lng": -3.192473, "lat": 3},
                  "angle": 40
                }
                """;
        ResponseEntity<Double> response = restTemplate.postForEntity(url, jsonEntity(json), Double.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    //Testing nextPosition with syntactically incorrect JSON
    @Test
    void nextPosition_syntacticallyBadJSON() {
        String url = "http://localhost:" + port + "/api/v1/nextPosition";
        String json = """
                {
                  "start": { "lng": -3.192473, "lat": 3},
                  "angle":
                }
                """;
        ResponseEntity<String> response = restTemplate.postForEntity(url, jsonEntity(json), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    //Testing isInRegion with correct JSON and should return true
    @Test
    void isInRegion_correctJSONAndIsInRegion() {
        String url = "http://localhost:" + port + "/api/v1/isInRegion";
        String json = """
                {
                  "position": { "lng": 0.5, "lat": 0.5 },
                  "region": {
                    "name": "test",
                    "vertices": [
                      {"lng": 0, "lat": 0},
                      {"lng": 4, "lat": 0},
                      {"lng": 4, "lat": 4},
                      {"lng": 0, "lat": 4},
                      {"lng": 0, "lat": 0}
                    ]
                  }
                }
                """;
        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, jsonEntity(json), Boolean.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isTrue();
    }

    //Testing isInRegion with syntactically bad JSON (misspelled name)
    @Test
    void isInRegion_syntacticallyBadJSON() {
        String url = "http://localhost:" + port + "/api/v1/isInRegion";
        String json = """
                {
                  "position": { "lng": 0.5, "lat": 0.5 },
                  "region": {
                    "nam": "test",
                    "vertices": [
                      {"lng": 0, "lat": 0},
                      {"lng": 4, "lat": 0},
                      {"lng": 4, "lat": 4},
                      {"lng": 4, "lat": 0},
                      {"lng": 0, "lat": 0}
                    ]
                  }
                }
                """;
        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, jsonEntity(json), Boolean.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    //Testing isInRegion with semantically bad JSON (region is open)
    @Test
    void isInRegion_semanticallyBadJSON() {
        String url = "http://localhost:" + port + "/api/v1/isInRegion";
        String json = """
                {
                  "position": { "lng": 0.5, "lat": 0.5 },
                  "region": {
                    "name": "test",
                    "vertices": [
                      {"lng": 0, "lat": 0},
                      {"lng": 4, "lat": 0},
                      {"lng": 4, "lat": 4},
                      {"lng": 4, "lat": 0},
                      {"lng": 0, "lat": 1}
                    ]
                  }
                }
                """;
        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, jsonEntity(json), Boolean.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
