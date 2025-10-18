package uk.ac.ed.acp.cw2.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import uk.ac.ed.acp.cw2.dto.Position;

//DTO for a distance or isClose request
//Defined by a Position DTO pair

@Setter
@Getter
public class PositionRequest {
    private Position position1;
    private Position position2;

    //constructor
    public PositionRequest() {
    }

}
