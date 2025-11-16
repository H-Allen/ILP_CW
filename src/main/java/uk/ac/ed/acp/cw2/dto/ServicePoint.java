package uk.ac.ed.acp.cw2.dto;

import lombok.Getter;
import lombok.Setter;

/*
    A java DTO representing a service point as per the endpoint JSON data
 */
@Getter
@Setter
public class ServicePoint {
    private String name;
    private int id;
    private Position location;

    public ServicePoint() {};
}
