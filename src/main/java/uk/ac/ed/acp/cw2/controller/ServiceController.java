package uk.ac.ed.acp.cw2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.ed.acp.cw2.dto.*;
import uk.ac.ed.acp.cw2.dto.request.IsInRegionRequest;
import uk.ac.ed.acp.cw2.dto.request.NextPositionRequest;
import uk.ac.ed.acp.cw2.dto.request.PositionRequest;
import uk.ac.ed.acp.cw2.dto.request.Query;
import uk.ac.ed.acp.cw2.service.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class that handles various HTTP endpoints for the application.
 * Provides functionality for serving the index page, retrieving a static UUID,
 * and managing key-value pairs through POST requests.
 */
@RestController()
@RequestMapping("/api/v1")
public class ServiceController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);

    @Value("${ilp.service.url}")
    public URL serviceUrl;


    @GetMapping("/")
    public String index() {
        return "<html><body>" +
                "<h1>Welcome from ILP</h1>" +
                "<h4>ILP-REST-Service-URL:</h4> <a href=\"" + serviceUrl + "\" target=\"_blank\"> " + serviceUrl + " </a>" +
                "</body></html>";
    }

    @GetMapping("/uid")
    public String uid() {
        return "s2524342";
    }

    @PostMapping("/distanceTo")
    public ResponseEntity<Double> distanceTo(@RequestBody PositionRequest request) {
        //catch an empty request
        if (!CheckValidService.checkValidPositions(request)) {
            return ResponseEntity.badRequest().build();
        }

        //catch bad arguments
        Position position1 = request.getPosition1();
        Position position2 = request.getPosition2();

        //calculate and return the Euclidean distance between the two positions
        double distance = PositionService.distance(position1, position2);
        return ResponseEntity.ok(distance);
    }

    @PostMapping("/isCloseTo")
    public ResponseEntity<Boolean> isCloseTo(@RequestBody PositionRequest request) {
        //Validate the JSON request
        if (!CheckValidService.checkValidPositions(request)) {
            return ResponseEntity.badRequest().build();
        }

        //If ok get the positions and return true if the distances are close to each other (<0.00015)
        Position position1 = request.getPosition1();
        Position position2 = request.getPosition2();

        boolean isClose = PositionService.isCloseTo(position1, position2);
        return ResponseEntity.ok(isClose);
    }

    @PostMapping("/nextPosition")
    public ResponseEntity<Position> nextPosition(@RequestBody NextPositionRequest request) {
        //Validate the JSON request
        if(!CheckValidService.checkValidNextPosition(request)) {
            return ResponseEntity.badRequest().build();
        }

        //If ok return the calculated next position
        Position nextPosition = PositionService.nextPosition(request.getStart(), request.getAngle());
        return ResponseEntity.ok(nextPosition);
    }

    @PostMapping("/isInRegion")
    public ResponseEntity<Boolean> isInRegion(@RequestBody IsInRegionRequest request) {
        //Validate the JSON request
        if(!CheckValidService.checkValidIsInRegion(request)) {
            return ResponseEntity.badRequest().build();
        }

        //If ok calculate if the position lies within the requested region and return
        Position position = request.getPosition();
        Region region = request.getRegion();

        boolean isInRegion = PositionService.isInRegion(position, region);
        return ResponseEntity.ok(isInRegion);
    }

    //Start of Coursework 2 in the Service Controller

    private final DroneService droneService;
    public ServiceController(DroneService droneService) {
        this.droneService = droneService;
    }

    @GetMapping("dronesWithCooling/{state}")
    public ResponseEntity<List<Integer>> getDronesWithCooling(@PathVariable boolean state) {
        List<Integer> dronesWithCooling = droneService.getDronesWithCooling(state);
        return ResponseEntity.ok(dronesWithCooling);
    }

    @GetMapping("droneDetails/{id}")
    public ResponseEntity<Drone> getDroneDetails(@PathVariable int id) {
        Drone drone = droneService.getDroneById(id);
        if (drone == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(drone);
    }

    @GetMapping("/queryAsPath/{attribute}/{value}")
    public ResponseEntity<List<Integer>> getQueryAsPath(@PathVariable String attribute, @PathVariable String value) {
        List<Integer> result = droneService.getDroneByAttribute(attribute, value);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/query")
    public ResponseEntity<List<Integer>> query(@RequestBody List<Query> queries) {
        List<Integer> result = droneService.getDroneByQuery(queries);
        return ResponseEntity.ok(result);
    }
}
