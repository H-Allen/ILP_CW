package uk.ac.ed.acp.cw2.service;

import org.springframework.stereotype.Service;
import uk.ac.ed.acp.cw2.dto.*;
import uk.ac.ed.acp.cw2.dto.request.IsInRegionRequest;
import uk.ac.ed.acp.cw2.dto.request.NextPositionRequest;
import uk.ac.ed.acp.cw2.dto.request.PositionRequest;

//A Service class used for checking validity of cosntructed DTO objects
@Service
public class CheckValidService {
    //check validity of Position
    public static boolean checkValidPosition(Position position) {
        if (position == null || position.getLat() == null || position.getLng() == null) {
            return false;
        }

        //A position must follow the rules of a longitude and latitude pair being -180 < lng < 180 and -90 < lat < 90
        if (position.getLng() < -180 || position.getLng() > 180 || position.getLat() < -90 || position.getLat() > 90) {
            return false;
        }
        return true;
    }

    //check validity of PositionRequest
    public static boolean checkValidPositions(PositionRequest positions) {
        if (positions == null || positions.getPosition1() == null || positions.getPosition2() == null || !checkValidPosition(positions.getPosition1()) || !checkValidPosition(positions.getPosition2())) {
            return false;
        }
        return true;
    }

    //check validity of NextPositionRequest
    public static boolean checkValidNextPosition(NextPositionRequest nextPosition) {
        if (nextPosition == null || nextPosition.getStart() == null || !checkValidPosition(nextPosition.getStart())) {
            return false;
        }

        //A movement angle must lie between 0 and 360 degrees, as well as being a multiple of 22.5 degrees otherwise it's invalid
        if (nextPosition.getAngle() < 0 || nextPosition.getAngle() > 360 || nextPosition.getAngle() % 22.5 != 0) {
            return false;
        }
        return true;
    }

    //check validity of Region
    public static boolean checkValidRegion(Region region) {
        //check name and vertex exist and vertex has at least 4 members
        if (region == null || region.getName() == null || region.getVertices() == null || region.getVertices().size() < 4) {
            return false;
        }

        //go through each vertex and check the Position is valid
        for (int i  = 0; i < region.getVertices().size(); i++) {
            if (!checkValidPosition(region.getVertices().get(i))){
                return false;
            }
        }

        //The first and last position of a region must be the same to within the error of a double
        Position first = region.getVertices().getFirst();
        Position last = region.getVertices().getLast();
        boolean isSame = (Math.abs(first.getLat()- last.getLat()) < 1e-9 && Math.abs(first.getLng()- last.getLng()) < 1e-9);

        return isSame;
    }

    //Check vailidity of isInRegionRequest DTO
    public static boolean checkValidIsInRegion(IsInRegionRequest isInRegion) {
        if(isInRegion == null || !checkValidRegion(isInRegion.getRegion()) || !checkValidPosition(isInRegion.getPosition())) {
            return false;
        }
        return true;
    }
}
