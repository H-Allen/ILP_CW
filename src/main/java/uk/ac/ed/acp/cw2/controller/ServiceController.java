package uk.ac.ed.acp.cw2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.ed.acp.cw2.dto.*;
import uk.ac.ed.acp.cw2.service.*;

import java.net.URL;

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
    public ResponseEntity<Double> distanceTo(@RequestBody PositionsDto request) {
        //catch an empty request
        if (!CheckValidService.checkValidPositions(request)) {
            return ResponseEntity.badRequest().build();
        }

        //catch bad arguments
        PositionDto position1 = request.getPosition1();
        PositionDto position2 = request.getPosition2();

        //calculate and return the Euclidean distance between the two positions
        double distance = PositionService.distance(position1, position2);
        return ResponseEntity.ok(distance);
    }

    @PostMapping("/isCloseTo")
    public ResponseEntity<Boolean> isCloseTo(@RequestBody PositionsDto request) {
        //catch an empty request
        if (!CheckValidService.checkValidPositions(request)) {
            return ResponseEntity.badRequest().build();
        }

        //catch bad arguments
        PositionDto position1 = request.getPosition1();
        PositionDto position2 = request.getPosition2();

        //calculate and return if the two distances are close to each other (distance < 0.00015)
        boolean isClose = PositionService.isCloseTo(position1, position2);
        return ResponseEntity.ok(isClose);
    }

    @PostMapping("/nextPosition")
    public ResponseEntity<PositionDto> nextPosition(@RequestBody NextPositionDto request) {
        if(!CheckValidService.checkValidNextPosition(request)) {
            return ResponseEntity.badRequest().build();
        }

        PositionDto nextPosition = PositionService.nextPosition(request.getStart(), request.getAngle());
        return ResponseEntity.ok(nextPosition);
    }
}
