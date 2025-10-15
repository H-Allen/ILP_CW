package uk.ac.ed.acp.cw2.dto;

import java.util.List;

public class RegionDto {
    private String name;
    private List<PositionDto> vertices;

    public RegionDto() {
    }

    //Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PositionDto> getVertices() {
        return vertices;
    }

    public void setVertices(List<PositionDto> vertices) {
        this.vertices = vertices;
    }
}
