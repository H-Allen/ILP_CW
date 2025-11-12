package uk.ac.ed.acp.cw2.service;

import org.springframework.stereotype.Service;
import uk.ac.ed.acp.cw2.dto.Drone;
import uk.ac.ed.acp.cw2.dto.request.Query;

import java.util.ArrayList;
import java.util.List;

@Service
public class DroneService {
    private final ClientService clientService;

    public DroneService(ClientService clientService) {
        this.clientService = clientService;
    }

    public List<Integer> getDronesWithCooling(boolean state) {
        return clientService.getDroneList().stream().filter(d -> d.getCapability().isCooling() == state).map(Drone::getId).toList();
    }

    public Drone getDroneById(int id) {
        return clientService.getDroneList().stream().filter(d -> d.getId() == id).findFirst().orElse(null);
    }

    public List<Integer> getDroneByAttribute(String attribute, String value) {
        List<Drone> drones = clientService.getDroneList();
        List<Integer> ids = new ArrayList<>();
        for (Drone drone : drones) {
            switch (attribute) {
                case "name":
                    if (drone.getName().equals(value)) {
                        ids.add(drone.getId());
                    }
                    break;
                case "id":
                    if  (drone.getId() == Integer.parseInt(value)) {
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

    public List<Integer> getDroneByQuery(List<Query> queries) {
        List<Drone> drones = clientService.getDroneList();
        List<Integer> ids = new ArrayList<>();
        for (Drone drone : drones) {
            boolean matchesAllAttributes = true;
            for (Query query : queries) {
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
                        if (MathService.compareInt(drone.getId(), Integer.parseInt(value), operator)) {
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
}
