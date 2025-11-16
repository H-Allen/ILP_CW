package uk.ac.ed.acp.cw2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Service;
import uk.ac.ed.acp.cw2.dto.*;
import uk.ac.ed.acp.cw2.dto.DronesForServicePoint.Availability;
import uk.ac.ed.acp.cw2.dto.request.QueryRequest;
import uk.ac.ed.acp.cw2.dto.response.DeliveryPathResponse;
import uk.ac.ed.acp.cw2.dto.response.GeoJsonResponse;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    A service class that provides functionality for the CW2 endpoints
 */
@Service
public class DroneService {
    private final ClientService clientService;

    public DroneService(ClientService clientService) {
        this.clientService = clientService;
    }

    /*
        Function called by endpoint 2a that returns a list of Drone IDs which have cooling
     */
    public List<String> getDronesWithCooling(boolean state) {
        return clientService.getDroneList().stream().filter(d -> d.getCapability().isCooling() == state).map(Drone::getId).toList();
    }

    /*
        Function called by endpoint 2b that returns a drone object for a specified ID
     */
    public Drone getDroneById(String id) {
        return clientService.getDroneList().stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);
    }

    /*
        Function called by endpoint 3a. It returns a list of Drone IDs that matches a single specified attribute and corresponding value
     */
    public List<String> getDroneByAttribute(String attribute, String value) {
        List<Drone> drones = clientService.getDroneList();
        List<String> ids = new ArrayList<>();

        for (Drone drone : drones) {
            switch (attribute) {
                case "name":
                    if (drone.getName().equals(value)) {
                        ids.add(drone.getId());
                    }
                    break;
                case "id":
                    if  (drone.getId().equals(value)) {
                        ids.add(drone.getId());
                    }
                    break;
                case "cooling":
                    if (drone.getCapability().isCooling() == Boolean.parseBoolean(value)) {
                        ids.add(drone.getId());
                    }
                    break;
                case "heating":
                    if (drone.getCapability().isHeating() == Boolean.parseBoolean(value)) {
                        ids.add(drone.getId());
                    }
                    break;
                case "capacity":
                    if (drone.getCapability().getCapacity() == Double.parseDouble(value)) {
                        ids.add(drone.getId());
                    }
                    break;
                case "maxMoves":
                    if (drone.getCapability().getMaxMoves() == Integer.parseInt(value)) {
                        ids.add(drone.getId());
                    }
                    break;
                case "costPerMove":
                    if (drone.getCapability().getCostPerMove() == Double.parseDouble(value)) {
                        ids.add(drone.getId());
                    }
                    break;
                case "costInitial":
                    if (drone.getCapability().getCostInitial() == Double.parseDouble(value)) {
                        ids.add(drone.getId());
                    }
                    break;
                case "costFinal":
                    if (drone.getCapability().getCostFinal() == Double.parseDouble(value)) {
                        ids.add(drone.getId());
                    }
                    break;
                default:
                    break;
            }
        }
        return ids;
    }

    /*
        Function called by endpoint 3b of the specification, that allows the user to return a list of DroneIDs matching a
     */
    public List<String> getDroneByQuery(List<QueryRequest> queries) {
        List<Drone> drones = clientService.getDroneList();
        List<String> ids = new ArrayList<>();
        for (Drone drone : drones) {
            boolean matchesAllAttributes = true;
            for (QueryRequest query : queries) {
                String attribute = query.getAttribute().toLowerCase();
                String operator = query.getOperator();
                String value = query.getValue();
                boolean matchFound = false;

                switch (attribute) {
                    case "name":
                        if (drone.getName().toLowerCase().equals(value)) {
                            matchFound = true;
                        }
                        break;
                    case "id":
                        if (drone.getId().equals(value)) {
                            matchFound = true;
                        }
                    case "cooling":
                        if (drone.getCapability().isCooling() == Boolean.parseBoolean(value)) {
                            matchFound = true;
                        }
                        break;
                    case "heating":
                        if (drone.getCapability().isHeating() == Boolean.parseBoolean(value)) {
                            matchFound = true;
                        }
                        break;
                    case "capacity":
                        if (MathService.compareDouble(drone.getCapability().getCapacity(), Double.parseDouble(value),  operator)) {
                            matchFound = true;
                        }
                        break;
                    case "maxmoves":
                        if (MathService.compareInt(drone.getCapability().getMaxMoves(), Integer.parseInt(value), operator)) {
                            matchFound = true;
                        }
                        break;
                    case "costpermove":
                        if (MathService.compareDouble(drone.getCapability().getCostPerMove(), Double.parseDouble(value), operator)) {
                            matchFound = true;
                        }
                        break;
                    case "costinitial":
                        if (MathService.compareDouble(drone.getCapability().getCostInitial(), Double.parseDouble(value), operator)) {
                            matchFound = true;
                        }
                        break;
                    case "costfinal":
                        if (MathService.compareDouble(drone.getCapability().getCostFinal(), Double.parseDouble(value), operator)) {
                            matchFound = true;
                        }
                        break;
                    default:
                        break;
                }
                if (!matchFound) {
                    matchesAllAttributes = false;
                    break;
                }
            }
            if (matchesAllAttributes) {
                ids.add(drone.getId());
            }
        }
        return ids;
    }

    /*
        Function called by the endpoint 4a, which returns a list of all the Drone IDs that are able to fulfill a list of MedDispatchRecs.
        The drone must be able to fulfill the requirements listed in each single record.
     */
    public List<String> getAvailableDrones(List<MedDispatchRec> medDispatchRecs) {
        List<Drone> drones = clientService.getDroneList();
        List<String> ids = new ArrayList<>();

        for (Drone drone : drones) {
            boolean matchesAllAttributes = true;
            Double distanceTotal = 0.0;
            for (MedDispatchRec dispatchRecord : medDispatchRecs) {
                //Check the drone's capacity is enough for the dispatch
                if (drone.getCapability().getCapacity() < dispatchRecord.getRequirements().getCapacity()) {
                    matchesAllAttributes = false;
                    break;
                }
                //Check for heating
                if (dispatchRecord.getRequirements().isCooling() && !drone.getCapability().isCooling()) {
                    matchesAllAttributes = false;
                    break;
                }
                //Check for cooling
                if (dispatchRecord.getRequirements().isHeating() && !drone.getCapability().isHeating()) {
                    matchesAllAttributes = false;
                    break;
                }
                //Check time-availability
                if (!isDroneAvailable(drone.getId(), dispatchRecord.getDate(), dispatchRecord.getTime())) {
                    matchesAllAttributes = false;
                    break;
                }

                //Check the drone doesn't exceed the max cost if it's present -> heuristically uses only euclidian distance
                if (dispatchRecord.getRequirements().getMaxCost() != null) {
                    Position serviceLocation = findServicePointForDroneId(drone.getId());
                    Position deliveryLocation = dispatchRecord.getDelivery();

                    Double estimatedCost = (PositionService.distance(serviceLocation, deliveryLocation) / 0.00015 * drone.getCapability().getCostPerMove() + drone.getCapability().getCostInitial() + drone.getCapability().getCostFinal());

                    if (estimatedCost > dispatchRecord.getRequirements().getMaxCost()) {
                        matchesAllAttributes = false;
                    }
                }
            }

            //If a drone matches all required attributes then add it to the list of possible drones.
            if (matchesAllAttributes) {
                ids.add(drone.getId());
            }
        }
        return ids;
    }

    /*
        Function called by endpoint 4b in the specification that calculates all the paths for each drone to meet the requirements of the
        medDispatchRecs. It returns the data as a DeliveryPathResponse, which is a response DTO matching the structure of that in the specifications
     */
    public DeliveryPathResponse calcDeliveryPath(List<MedDispatchRec> medDispatchRecs) {
        PathFindingService pathFindingService = new PathFindingService(clientService);
        DeliveryPathResponse response = new DeliveryPathResponse();
        response.setDronePaths(new ArrayList<>());

        //Initialise the total cost and move variables for each flight
        double totalCost = 0.0;
        int totalMoves = 0;

        //Assign each of the medDispatchRecs to each drone
        Map<String, List<MedDispatchRec>> assignments = assignDeliveries(medDispatchRecs);


        //For each drone, a route might look like: ServicePoint -> Drop off A (+ hover) -> Drop off B (+ hover) -> Service Point
        for (Map.Entry<String, List<MedDispatchRec>> entry : assignments.entrySet()) {
            //Parse the drone ID and the dispatch records
            String droneID = entry.getKey();
            List<MedDispatchRec> deliveries = entry.getValue();

            //Get the drone and the Position of the servicePoint for that drone
            Drone drone = getDroneById(droneID);
            Position servicePointPosition = findServicePointForDroneId(droneID);

            //Instantiate a new dronePath for the delivery response. This is the full drone path for a specified drone.
            DeliveryPathResponse.DronePath dronePath = new DeliveryPathResponse.DronePath();
            dronePath.setDroneId(droneID);
            dronePath.setDeliveries(new ArrayList<>());

            //Set the initial position to the position of the servicePoint.
            Position currentPos = servicePointPosition;

            for (MedDispatchRec delivery : deliveries) {
                DeliveryPathResponse.DronePath.Delivery tempDelivery = new DeliveryPathResponse.DronePath.Delivery();

                //Calculate the path
                List<Position> path = pathFindingService.findBestPath(currentPos, delivery.getDelivery());

                //Set the ID and path of the tempDelivery and add it to the drone deliveries
                tempDelivery.setDeliveryId(delivery.getId());
                tempDelivery.setFlightPath(path);
                dronePath.getDeliveries().add(tempDelivery);

                //Calculate the cost of the flight
                totalMoves += path.size();
                totalCost += drone.getCapability().getCostPerMove() * path.size() + drone.getCapability().getCostInitial() +  drone.getCapability().getCostFinal();

                //Set the current position to this delivery
                currentPos = delivery.getDelivery();
            }

            //Add the path to return to service point with the same logic as before
            List<Position> path = pathFindingService.findBestPath(currentPos, servicePointPosition);
            DeliveryPathResponse.DronePath.Delivery tempDelivery = new DeliveryPathResponse.DronePath.Delivery();
            tempDelivery.setFlightPath(path);
            dronePath.getDeliveries().add(tempDelivery);
            totalMoves += path.size();
            totalCost += drone.getCapability().getCostPerMove() * path.size() + drone.getCapability().getCostInitial() +  drone.getCapability().getCostFinal();

            //add the full dronePath object and then do the same for the next drone
            response.getDronePaths().add(dronePath);

        }

        //Set the total cost of the flight
        response.setTotalCost(totalCost);
        response.setTotalMoves(totalMoves);

        //Return the full deliveryPathResponse to the endpoints
        return response;
    }

    /*
        Function that returns the path of a single drone in GeoJSON LineString format. The input guarantees a single drone can complete
        all dispatches.
     */
    public GeoJsonResponse calcGeoJsonPath(List<MedDispatchRec> medDispatchRecs) {
        //Just used to show the restricted areas when true - useful for debugging
        boolean debug = true;

        PathFindingService pathFindingService = new PathFindingService(clientService);
        GeoJsonResponse response = new GeoJsonResponse();
        response.setFeatures(new ArrayList<>());

        String droneID = getAvailableDrones(medDispatchRecs).get(0);
        Drone drone = getDroneById(droneID);
        Position servicePointPosition = findServicePointForDroneId(droneID);

        GeoJsonResponse.Feature feature = new GeoJsonResponse.Feature();

        //Set the properties
        GeoJsonResponse.Feature.Properties properties = new GeoJsonResponse.Feature.Properties();
        properties.setDroneId(droneID);
        feature.setProperties(properties);

        //Set up the geometry list
        GeoJsonResponse.Feature.Geometry geometry = new GeoJsonResponse.Feature.Geometry();
        List<List<Double>> coordinates = new ArrayList<>();

        //Set the current position
        Position currentPos = servicePointPosition;

        //Go through each delivery:
        for(MedDispatchRec medDispatchRec : medDispatchRecs) {
            List<Position> path = pathFindingService.findBestPath(currentPos, medDispatchRec.getDelivery());

            //Add each position:
            for(Position position : path) {
                coordinates.add(List.of(position.getLng(), position.getLat()));
            }

            currentPos = medDispatchRec.getDelivery();
        }

        List<Position> returnPath = pathFindingService.findBestPath(currentPos, servicePointPosition);
        for (int i = 1; i < returnPath.size(); i++) {
            Position pos = returnPath.get(i);
            coordinates.add(List.of(pos.getLng(), pos.getLat()));
        }

        List<Region> restrictedAreas = clientService.getRestrictedAreas();

        for (Region area : restrictedAreas) {
            GeoJsonResponse.Feature restrictedFeature = new GeoJsonResponse.Feature();

            // Set properties with area name
            GeoJsonResponse.Feature.Properties restrictedProps = new GeoJsonResponse.Feature.Properties();
            restrictedProps.setDroneId(area.getName()); // Reuse droneId field for area name
            restrictedFeature.setProperties(restrictedProps);

            if (debug) {
                GeoJsonResponse.Feature.Geometry restrictedGeometry = new GeoJsonResponse.Feature.Geometry();
                List<List<Double>> areaCoordinates = new ArrayList<>();

                for (Position vertex : area.getVertices()) {
                    areaCoordinates.add(List.of(vertex.getLng(), vertex.getLat()));
                }

                restrictedGeometry.setCoordinates(areaCoordinates);
                restrictedFeature.setGeometry(restrictedGeometry);
            }

            response.getFeatures().add(restrictedFeature);
        }

        geometry.setCoordinates(coordinates);
        feature.setGeometry(geometry);

        response.getFeatures().add(feature);
        return response;
    }
    /*
        Function called by getAvailableDrones to calculate the time-availability of a drone, given a droneID, date and time, it calculates
        availability based on the dronesForServicePoints
     */
    private boolean isDroneAvailable(String droneId, String date, String time) {
        if (date == null || time == null) return true;
        //Fetch the dronesForServicePoints
        List<DronesForServicePoint> dronesForServicePoints = clientService.getDronesForServicePoints();
        //Parse the date and time, and find the corresponding day of the week
        LocalDate localDate = LocalDate.parse(date);
        LocalTime localTime = LocalTime.parse(time);
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();

        //Find the correct done in the service points
        for (DronesForServicePoint servicePoint : dronesForServicePoints) {
            for (DronesForServicePoint.AvailableDrones availableDrone : servicePoint.getDrones()) {
                if (availableDrone.getId().equals(droneId)) {
                    //Check for availability
                    for (Availability availability : availableDrone.getAvailability()) {
                        //Check the day is the same
                        if (availability.getDayOfWeek().equals(dayOfWeek.toString())) {
                            LocalTime from = LocalTime.parse(availability.getFrom());
                            LocalTime until = LocalTime.parse(availability.getUntil());

                            //Check the time lies between specified times in the dronesForServicePoints.
                            if (!localTime.isBefore(from) && !localTime.isAfter(until)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        //If the drone isn't available return false.
        return false;
    }

    /*
        Function used by the getAvailableDrones function that allows you to find the location of the service point for a specific drone, given that drone's ID
     */
    private Position findServicePointForDroneId(String droneId) {
        //Fetch the dronesForServicePoints
        List<DronesForServicePoint> servicePoints = clientService.getDronesForServicePoints();

        //Iterate through the servicePoints and available drones at those service points
        for (DronesForServicePoint servicePoint : servicePoints) {
            for (DronesForServicePoint.AvailableDrones availableDrone : servicePoint.getDrones()) {
                //if the drone ID matches that in the dronesForServicePoints then return the service point ID
                if (availableDrone.getId().equals(droneId)) {
                    ServicePoint location = clientService.getServicePointList().stream().filter(p -> p.getId() == servicePoint.getServicePointId()).findFirst().get();
                    return location.getLocation();
                }
            }
        }
        return null;
    }

    /*
        A private class used by the calcDeliveryPath function to construct a hashmap of droneIDs and the deliveries that they take. It is
        currently a greedy algorithm and just assigns the deliveries on a first-come first-serve basis.
        (can be optimised)
     */
    private Map<String, List<MedDispatchRec>> assignDeliveries(List<MedDispatchRec> dispatches) {
        Map<String, List<MedDispatchRec>> droneAssignments = new HashMap<>();
        for (MedDispatchRec medDispatchRec : dispatches) {
            List<String> availableDrones = getAvailableDrones(List.of(medDispatchRec));
            if (!availableDrones.isEmpty()) {
                String droneId = availableDrones.get(0);
                droneAssignments.computeIfAbsent(droneId, k -> new ArrayList<>()).add(medDispatchRec);
            }
        }
        return droneAssignments;
    }
}
