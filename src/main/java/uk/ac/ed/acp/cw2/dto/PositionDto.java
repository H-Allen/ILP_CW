package uk.ac.ed.acp.cw2.dto;

public class PositionDto {
    private double lng;
    private double lat;

    //Constructor
    public PositionDto() {}

    //getters
    public double getLng() {return lng;}
    public double getLat() {return lat;}

    //setters
    public void setLng(double lng) {this.lng = lng;}
    public void setLat(double lat) {this.lat = lat;}
}
