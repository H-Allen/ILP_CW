package uk.ac.ed.acp.cw2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Capability {

    private boolean cooling;
    private boolean heating;
    private Double capacity;
    private Integer maxMoves;
    private Double costPerMove;
    private Double costInitial;
    private Double costFinal;

    public Capability() {}
}
