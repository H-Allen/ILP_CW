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
}
