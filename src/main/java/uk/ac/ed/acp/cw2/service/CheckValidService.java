package uk.ac.ed.acp.cw2.service;

import org.springframework.stereotype.Service;
import uk.ac.ed.acp.cw2.dto.NextPositionDto;
import uk.ac.ed.acp.cw2.dto.PositionDto;
import uk.ac.ed.acp.cw2.dto.PositionsDto;

//class used for checking validity of DTO objects (JSON requests)
@Service
public class CheckValidService {
    //check validity of instantiated position DTO
    public static boolean checkValidPosition(PositionDto position) {
        if (position.getLat() == null || position.getLng() == null) {
            return false;
        }
        return true;
    }

    //check validity of instantiated positions DTO
    public static boolean checkValidPositions(PositionsDto positions) {
        if (positions.getPosition1() == null || positions.getPosition2() == null || !checkValidPosition(positions.getPosition1()) || !checkValidPosition(positions.getPosition2())) {
            return false;
        }
        return true;
    }

    //check validity of NextPositionDto
    public static boolean checkValidNextPosition(NextPositionDto nextPosition) {
        if (nextPosition.getStart() == null || nextPosition.getStart() == null || !checkValidPosition(nextPosition.getStart())) {
            return false;
        }
        return true;
    }
}
