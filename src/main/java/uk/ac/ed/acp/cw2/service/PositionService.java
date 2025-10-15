package uk.ac.ed.acp.cw2.service;

import org.springframework.stereotype.Service;
import uk.ac.ed.acp.cw2.dto.PositionDto;

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
}
