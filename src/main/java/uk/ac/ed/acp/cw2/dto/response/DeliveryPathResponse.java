package uk.ac.ed.acp.cw2.dto.response;

import lombok.Getter;
import lombok.Setter;
import uk.ac.ed.acp.cw2.dto.Position;

import java.util.List;

/*
    Java DTO class that represents the response to the calcDeliveryPath endpoint, according to the specification for CW2
 */
@Getter
@Setter
public class DeliveryPathResponse {
    private Double totalCost;
    private int totalMoves;
    private List<DronePath> dronePaths;

    public DeliveryPathResponse() {}

    @Getter
    @Setter
    public static class DronePath {
        private String droneId;
        private List<Delivery> deliveries;

        public DronePath() {}

        @Getter
        @Setter
        public static class Delivery {
            private int deliveryId;
            private List<Position> flightPath;

            public Delivery() {}
        }
    }
}
