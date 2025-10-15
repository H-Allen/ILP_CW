package uk.ac.ed.acp.cw2.service;

import org.springframework.stereotype.Service;
import uk.ac.ed.acp.cw2.dto.*;

//class used for checking validity of DTO objects (JSON requests)
@Service
public class CheckValidService {
    //check validity of PositionDto
    public static boolean checkValidPosition(PositionDto position) {
        if (position == null || position.getLat() == null || position.getLng() == null) {
            return false;
        }
        return true;
    }

    //check validity of PositionsDto
    public static boolean checkValidPositions(PositionsDto positions) {
        if (positions == null || positions.getPosition1() == null || positions.getPosition2() == null || !checkValidPosition(positions.getPosition1()) || !checkValidPosition(positions.getPosition2())) {
            return false;
        }
        return true;
    }

    //check validity of NextPositionDto
    public static boolean checkValidNextPosition(NextPositionDto nextPosition) {
        if (nextPosition == null || nextPosition.getStart() == null || !checkValidPosition(nextPosition.getStart())) {
            return false;
        }
        return true;
    }

    //check validity of RegionDto
    public static boolean checkValidRegion(RegionDto region) {
        //check name and vertex exist and vertex has at least 4 members
        if (region == null || region.getName() == null || region.getVertices() == null || region.getVertices().size() < 4) {
            return false;
        }
        //go through each vertex and check the PositionDto is valid
        for (int i  = 0; i < region.getVertices().size(); i++) {
            if (!checkValidPosition(region.getVertices().get(i))){
                return false;
            }
        }

        PositionDto first = region.getVertices().getFirst();
        PositionDto last = region.getVertices().getLast();

        return (Math.abs(first.getLat()- last.getLat()) < 1e-7 && Math.abs(first.getLng()- last.getLng()) < 1e-7);
    }

    public static boolean checkValidIsInRegion(IsInRegionDto isInRegion) {
        if(isInRegion == null || !checkValidRegion(isInRegion.getRegion()) || !checkValidPosition(isInRegion.getPosition())) {
            return false;
        }
        return true;
    }
}
