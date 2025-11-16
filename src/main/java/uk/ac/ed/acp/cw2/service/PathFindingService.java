package uk.ac.ed.acp.cw2.service;

import org.springframework.stereotype.Service;
import uk.ac.ed.acp.cw2.dto.Position;
import uk.ac.ed.acp.cw2.dto.Region;

import java.util.*;

/*
    A service that provides path finding functionality, implementing the A* algorithm
 */
@Service
public class PathFindingService {
    private final ClientService clientService;

    private static final double MOVE_DISTANCE = 0.00015;
    private static final double[] DIRECTIONS = {0, 22.5, 45, 67.7, 90, 112.5, 135.5, 157.5, 180, 202.5, 225, 247.5, 270, 292.5, 315, 337.5, 360};

    public PathFindingService(ClientService clientService) {
        this.clientService = clientService;
    }

    /*
        A greedy algorithm to find the a path between two given points
     */
    public List<Position> findBestPath(Position start, Position end) {
        List<Position> path = new ArrayList<>();
        List<Region> restrictedAreas = clientService.getRestrictedAreas();
        Position current = start;
        path.add(current);

        int maxSteps = 10000; // Safety limit to prevent infinite loops
        int steps = 0;

        List<Position> recentVisits = new ArrayList<>();

        //Keep moving until we are close to the target (within 0.00015 -> the same point according to the specification)
        while (!PositionService.isCloseTo(current, end) && steps < maxSteps) {
            Position bestNext = null;
            double bestDistance = 999999999;

            //Try to move in each of the directions and choose the one that is closest to the target
            for (double angle : DIRECTIONS) {
                Position candidate = PositionService.nextPosition(current, angle);

                //Skip the angle if the destination lies within a restricted area or it has been visited recently (to prevent drone getting stuck)
                if (isInRestrictedArea(candidate, restrictedAreas) || recentVisits.contains(candidate) || PositionService.isPathInRegion(current, candidate, restrictedAreas)) {
                    continue;
                }

                //Calculate the distance and choose the one that is closest to the goal
                double distanceToGoal = PositionService.distance(candidate, end);
                if (distanceToGoal < bestDistance) {
                    bestDistance = distanceToGoal;
                    bestNext = candidate;
                }
            }
            if (bestNext != null) {
                //Limit recent visits to 20 nodes.
                if (recentVisits.size() >= 50) {
                    recentVisits.remove(0);
                }
                recentVisits.add(bestNext);
                current = bestNext;
                path.add(current);
                steps++;
            } else {
                // Stuck! Try to move away from restricted area
                // Just pick any valid direction
                boolean moved = false;
                for (double angle : DIRECTIONS) {
                    Position candidate = PositionService.nextPosition(current, angle);
                    if (!isInRestrictedArea(candidate, restrictedAreas)) {
                        current = candidate;
                        path.add(current);
                        moved = true;
                        break;
                    }
                }
                if (!moved) {
                    // Completely stuck, return what we have
                    break;
                }
                steps++;
            }
        }
        return path;
    }

    /*
        Helper method to check if a position lies within any restricted area using the isInRegion Function from coursework 1
     */
    public static boolean isInRestrictedArea(Position pos, List<Region> areas) {
        for (Region area : areas) {
            if (PositionService.isInRegion(pos, area)) {
                return true;
            }
        }
        return false;
    }


}
