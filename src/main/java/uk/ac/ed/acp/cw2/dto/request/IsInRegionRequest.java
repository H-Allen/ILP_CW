package uk.ac.ed.acp.cw2.dto.request;

import lombok.Getter;
import lombok.Setter;
import uk.ac.ed.acp.cw2.dto.Position;
import uk.ac.ed.acp.cw2.dto.Region;

//DTO constructed for an IsInRegion request
//Defined by a Position DTO and Region DTO

@Setter
@Getter
public class IsInRegionRequest {
    private Position position;
    private Region region;

    public IsInRegionRequest() {
    }

}
