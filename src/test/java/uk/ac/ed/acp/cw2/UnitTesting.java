package uk.ac.ed.acp.cw2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import uk.ac.ed.acp.cw2.dto.*;
import uk.ac.ed.acp.cw2.service.*;
import java.util.List;

public class UnitTesting {

    private Position setPosition(double lng, double lat) {
        Position aPosition = new Position();
        aPosition.setLng(lng);
        aPosition.setLat(lat);
        return aPosition;
    }

    //Test the distance caluclation works to accuracy of a double
    @Test
    public void testDistance() {
        Position position1 = setPosition(0.0, 0.0);
        Position position2 = setPosition(0.0, 0.00015);

        double result = PositionService.distance(position1, position2);

        assertEquals(0.00015, result, 1e-9, "Distance calculation failed");
    }

    //Test isCloseTo where the point should be close
    @Test
    public void testIsCloseTo_InThreshold() {
        Position position1 = setPosition(0.0, 0.0);
        Position position2 = setPosition(0.0, 0.00014);

        boolean result = PositionService.isCloseTo(position1, position2);
        assertEquals(true, result, "IsClose returned a false negative");
    }

    //Test isCloseTo where the point isn't close
    @Test
    public void testIsCloseTo_OutOfThreshold() {
        Position position1 = setPosition(0.0, 0.0);
        Position position2 = setPosition(0.0, 1.0);

        boolean result = PositionService.isCloseTo(position1, position2);
        assertEquals(false, result, "IsClose returned a false positive");

    }

    //Test isCloseTo edge case where is should not be considered close
    @Test
    public void testIsCloseTo_EdgeCase() {
        Position position1 = setPosition(0.0, 0.0);
        Position position2 = setPosition(0.0, 0.00015);

        boolean result = PositionService.isCloseTo(position1, position2);
        assertEquals(false, result, "IsClose returned a false positive");

    }

    //Test a move that changes the longitude
    @Test
    public void testNextPosition_EastwardMove() {
        Position start = setPosition(0.0, 0.0);
        Position next = PositionService.nextPosition(start, 0.0);

        assertEquals(next.getLng(), start.getLng() + 0.00015, "Longitude should increase by 0.00015 when moving east");
        assertEquals(start.getLat(), next.getLat(), 1e-9, "Latitude should remain same when moving east");
    }

    //Test a move that changes the latitude
    @Test
    public void testNextPosition_NorthwardMove() {
        Position start = setPosition(0.0, 0.0);
        Position next = PositionService.nextPosition(start, 90);

        assertEquals(next.getLat(), start.getLat() + 0.00015, "Latitude should increase by 0.00015 when moving east");
        assertEquals(start.getLng(), next.getLng(), 1e-9, "Longitude should remain same when moving east");
    }

    //Test the isInRegion where the point is inside the region
    @Test
    public void testIsInRegion_InsideRegion() {
        Region region = new Region();
        region.setVertices(List.of(
                setPosition(0, 0),
                setPosition(0, 4),
                setPosition(4, 4),
                setPosition(4, 0),
                setPosition(0, 0)
        ));
        Position position = setPosition(2, 2);
        assertTrue(PositionService.isInRegion(position, region), "Point should be inside region");
    }

    //Test the isInRegion where the point is outside the region
    @Test
    public void testIsInRegion_OutsideRegion() {
        Region region = new Region();
        region.setVertices(List.of(
                setPosition(0, 0),
                setPosition(0, 4),
                setPosition(4, 4),
                setPosition(4, 0),
                setPosition(0, 0)
        ));
        Position position = setPosition(2, 6);
        assertFalse(PositionService.isInRegion(position, region), "Point should be inside region");
    }
}
