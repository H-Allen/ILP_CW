package uk.ac.ed.acp.cw2.dto.request;

import lombok.Getter;
import lombok.Setter;
import uk.ac.ed.acp.cw2.dto.Position;

//DTO for a nextPosition request
//Defined by a Position DTO and an angle you want to move through

@Setter
@Getter
public class NextPositionRequest {
    private Position start;
    private Double angle;

    //Constructor
    public NextPositionRequest() {
    }

}
