package uk.ac.ed.acp.cw2.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import uk.ac.ed.acp.cw2.dto.Drone;
import uk.ac.ed.acp.cw2.dto.DronesForServicePoint;
import uk.ac.ed.acp.cw2.dto.Region;
import uk.ac.ed.acp.cw2.dto.ServicePoint;

import java.util.List;

/*
    A service that accesses each specific page of the ILP_ENDPOINT
 */
@Service
public class ClientService {

    private final WebClient webClient;

    public ClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    //Fetches the list of drones
    public List<Drone> getDroneList() {
        return webClient.get().uri("/drones").retrieve().bodyToFlux(Drone.class).collectList().block();
    }

    //Fetches the list of service points
    public List<ServicePoint> getServicePointList() {
        return webClient.get().uri("/service-points").retrieve().bodyToFlux(ServicePoint.class).collectList().block();
    }

    //Fetches the drones and what service points they're located at
    public List<DronesForServicePoint> getDronesForServicePoints() {
        return webClient.get().uri("/drones-for-service-points").retrieve().bodyToFlux(DronesForServicePoint.class).collectList().block();
    }

    public List<Region> getRestrictedAreas() {
        return webClient.get().uri("/restricted-areas").retrieve().bodyToFlux(Region.class).collectList().block();
    }
}
