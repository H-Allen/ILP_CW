package uk.ac.ed.acp.cw2.dto;

import lombok.Getter;
import lombok.Setter;

//DTO for a position
//Defined by the longitude and latitude of that position

@Setter
@Getter
public class Position {
    private Double lng;
    private Double lat;

    //Constructor
    public Position() {
    }

}
