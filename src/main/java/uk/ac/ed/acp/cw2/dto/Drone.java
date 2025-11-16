package uk.ac.ed.acp.cw2.dto;

import lombok.Getter;
import lombok.Setter;

/*
    A DTO representing the structure of a Drone and it's capabilities
 */

@Getter
@Setter
public class Drone {
    private String id;
    private String name;
    private Capability capability;

    public Drone() {}

    @Getter
    @Setter
    public static class Capability {

        private boolean cooling;
        private boolean heating;
        private Double capacity;
        private Integer maxMoves;
        private Double costPerMove;
        private Double costInitial;
        private Double costFinal;

        public Capability() {}
    }
}
