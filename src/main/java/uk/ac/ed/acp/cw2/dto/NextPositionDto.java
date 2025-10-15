package uk.ac.ed.acp.cw2.dto;

public class NextPositionDto {
    private PositionDto start;
    private Double angle;

    //Constructor
    public NextPositionDto() {
    }

    //Getters and Setters
    public PositionDto getStart() {
        return start;
    }

    public void setStart(PositionDto start) {
        this.start = start;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }
}
