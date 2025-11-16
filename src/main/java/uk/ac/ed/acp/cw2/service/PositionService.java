package uk.ac.ed.acp.cw2.service;

import org.springframework.stereotype.Service;
import uk.ac.ed.acp.cw2.dto.Position;
import uk.ac.ed.acp.cw2.dto.Region;

import java.util.List;

//A service class that provides the core logic for position-based operations used by the controller

@Service
public class PositionService {
    //calculate Euclidean distance
    public static double distance(Position position1, Position position2) {
        double dx = position1.getLng() - position2.getLng();
        double dy = position1.getLat() - position2.getLat();
        return Math.sqrt(dx * dx + dy * dy);
    }

    //calculate if position1 is close to position2 (distance < 0.00015)
    public static boolean isCloseTo(Position position1, Position position2) {
        return (distance(position1, position2) < 0.00015);
    }

    //calculate the next position based on angle and start position
    public static Position nextPosition(Position position, double angle) {
        //declare the step size as per the spec + angle
        final double step = 0.00015;
        double angleInRads = Math.toRadians(angle);

        //calculate the next position
        double next_lng = position.getLng() + step * Math.cos(angleInRads);
        double next_lat = position.getLat() + step * Math.sin(angleInRads);

        //create the next position object and return the position as this
        Position nextPosition = new Position();
        nextPosition.setLng(next_lng);
        nextPosition.setLat(next_lat);
        return nextPosition;
    }

    public static boolean isInRegion(Position point, Region region) {
        List<Position> vertices = region.getVertices();
        int size = vertices.size();

        boolean isInside = false;

        for (int i = 0, j = size - 1; i < size; j = i++) {
            //get the current and previous vertex coordinates
            double currentX = vertices.get(i).getLng();
            double currentY = vertices.get(i).getLat();
            double previousX = vertices.get(j).getLng();
            double previousY = vertices.get(j).getLat();

            //check if the point lies vertically between the two points
            boolean isBetween = (currentY > point.getLat() != previousY > point.getLat());

            //if it does lie between vertically calculate the longitude of the intersection
            if (isBetween) {
                //y = mx + c
                double gradient = (currentX - previousX) / (currentY - previousY);
                double longitudeIntersection = previousX + gradient * (point.getLat() - previousY);

                if (longitudeIntersection > point.getLng()) {
                    isInside = !isInside;
                }
            }
        }
        return isInside;
    }

    public static boolean isPathInRegion(Position pathStart, Position pathEnd, List<Region> regions) {
        int intermediatePoints = 20;
        for (int i = 0; i <= intermediatePoints; i++) {
            double fraction = (double) i / intermediatePoints;
            Position intermediate = interpolate(pathStart, pathEnd, fraction);

            if (PathFindingService.isInRestrictedArea(intermediate, regions)) {
                return true;  // Found a restricted area on the path
            }
        }
        return false;
    }

    public static Position interpolate(Position start, Position end, double fraction) {
        double lat = start.getLat() + (end.getLat() - start.getLat()) * fraction;
        double lng = start.getLng() + (end.getLng() - start.getLng()) * fraction;
        Position position = new Position();
        position.setLat(lat);
        position.setLng(lng);
        return position; // Assuming Position has a constructor accepting lat, lon
    }

}
