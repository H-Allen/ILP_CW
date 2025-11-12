package uk.ac.ed.acp.cw2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Drone {
    private int id;
    private String name;
    private Capability capability;

    public Drone() {}
}
