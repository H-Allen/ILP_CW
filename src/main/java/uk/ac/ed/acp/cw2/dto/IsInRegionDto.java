package uk.ac.ed.acp.cw2.dto;

public class IsInRegionDto {
    private PositionDto position;
    private RegionDto region;

    public IsInRegionDto() {
    }

    public PositionDto getPosition() {
        return position;
    }

    public void setPosition(PositionDto position) {
        this.position = position;
    }

    public RegionDto getRegion() {
        return region;
    }

    public void setRegion(RegionDto region) {
        this.region = region;
    }
}
