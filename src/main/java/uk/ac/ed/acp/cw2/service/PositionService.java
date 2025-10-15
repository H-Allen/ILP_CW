package uk.ac.ed.acp.cw2.service;

import org.springframework.stereotype.Service;
import uk.ac.ed.acp.cw2.dto.PositionDto;
import uk.ac.ed.acp.cw2.dto.RegionDto;

import java.util.List;

@Service
public class PositionService {
    //calculate euclidian distance
    public static double distance(PositionDto position1, PositionDto position2) {
        double dx = position1.getLng() - position2.getLng();
        double dy = position1.getLat() - position2.getLat();
        return Math.sqrt(dx * dx + dy * dy);
    }

    //calculate if position1 is close to position2 (distance < 0.00015)
    public static boolean isCloseTo(PositionDto position1, PositionDto position2) {
        return (distance(position1, position2) < 0.00015);
    }

    //calculate the next position based on angle and start position
    public static PositionDto nextPosition(PositionDto position, double angle) {
        //declare the step size as per the spec + angle
        final double step = 0.00015;
        double angleInRads = Math.toRadians(angle);

        //calculate the next position
        double next_lng = position.getLng() + step * Math.cos(angleInRads);
        double next_lat = position.getLat() + step * Math.sin(angleInRads);

        //create the next position object and return the position as this
        PositionDto nextPosition = new PositionDto();
        nextPosition.setLng(next_lng);
        nextPosition.setLat(next_lat);
        return nextPosition;
    }

    public static boolean isInRegion(PositionDto point, RegionDto region) {
        List<PositionDto> vertices = region.getVertices();
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
}
