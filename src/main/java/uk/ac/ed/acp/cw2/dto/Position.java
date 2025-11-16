package uk.ac.ed.acp.cw2.dto;

import lombok.Data;

//DTO for a position
//Defined by the longitude and latitude of that position

@Data
public class Position {
    private Double lng;
    private Double lat;

    //Constructor
    public Position() {
    }

}
