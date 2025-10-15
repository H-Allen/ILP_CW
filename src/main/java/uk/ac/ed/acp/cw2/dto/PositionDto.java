package uk.ac.ed.acp.cw2.dto;

public class PositionDto {
    private Double lng;
    private Double lat;

    //Constructor
    public PositionDto() {
    }

    //getters and setters
    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}
