package uk.ac.ed.acp.cw2.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

//DTO for a region
//Defined by a name and a list of vertices forming the regions edges

@Setter
@Getter
public class Region {
    private String name;
    private List<Position> vertices;

    public Region() {
    }

}
