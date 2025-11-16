package uk.ac.ed.acp.cw2.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/*
    A java DTO class representing the structure of the DronesForServicePoints as defined by the endpoint
 */

@Getter
@Setter
public class DronesForServicePoint {
    private int servicePointId;
    private List<AvailableDrones> drones;

    public DronesForServicePoint() {}

    @Getter
    @Setter
    public static class AvailableDrones {
        String id;
        List<Availability> availability;

        public AvailableDrones() {}
    }

    @Getter
    @Setter
    public static class Availability {
        private String dayOfWeek;
        private String from;
        private String until;

        public Availability() {}
    }
}
