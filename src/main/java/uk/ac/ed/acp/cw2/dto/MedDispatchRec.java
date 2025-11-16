package uk.ac.ed.acp.cw2.dto;

import lombok.Getter;
import lombok.Setter;
/*
    Java class representing a MedDispatchRec as specified in the CW2 spec
    (amended with a delivery position as per Piazza)
 */
@Getter
@Setter
public class MedDispatchRec {
    int id;
    String date;
    String time;
    Requirements requirements;
    Position delivery;

    public MedDispatchRec() {}

    @Getter
    @Setter
    public static class Requirements{
        Double capacity;
        boolean cooling;
        boolean heating;
        Double maxCost;

        public Requirements() {};
    }
}