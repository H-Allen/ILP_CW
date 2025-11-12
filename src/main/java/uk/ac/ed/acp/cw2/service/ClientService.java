package uk.ac.ed.acp.cw2.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import uk.ac.ed.acp.cw2.dto.Drone;

import java.util.List;

@Service
public class ClientService {

    private final WebClient webClient;

    public ClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<Drone> getDroneList() {
        return webClient.get().uri("/drones").retrieve().bodyToFlux(Drone.class).collectList().block();
    }
}
