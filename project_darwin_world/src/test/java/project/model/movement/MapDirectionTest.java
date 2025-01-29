package project.model.movement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapDirectionTest {

    public static final MapDirection DIRECTION_NORTH = MapDirection.NORTH;
    public static final MapDirection DIRECTION_NORTH_EAST = MapDirection.NORTH_EAST;
    public static final MapDirection DIRECTION_EAST = MapDirection.EAST;
    public static final MapDirection DIRECTION_SOUTH_EAST = MapDirection.SOUTH_EAST;
    public static final MapDirection DIRECTION_SOUTH = MapDirection.SOUTH;
    public static final MapDirection DIRECTION_SOUTH_WEST = MapDirection.SOUTH_WEST;
    public static final MapDirection DIRECTION_WEST = MapDirection.WEST;
    public static final MapDirection DIRECTION_NORTH_WEST = MapDirection.NORTH_WEST;

    @Test
    public void rotateTest(){
        MapDirection directionNorth = DIRECTION_NORTH;
        MapDirection test1 = directionNorth.rotate(2);
        assertEquals(DIRECTION_EAST, test1);

        MapDirection directionNorthEast = DIRECTION_NORTH_EAST;
        MapDirection test2 = directionNorthEast.rotate(3);
        assertEquals(DIRECTION_SOUTH, test2);

        MapDirection directionEast = DIRECTION_EAST;
        MapDirection test3 = directionEast.rotate(4);
        assertEquals(DIRECTION_WEST, test3);

        MapDirection directionSouthEast = DIRECTION_SOUTH_EAST;
        MapDirection test4 = directionSouthEast.rotate(5);
        assertEquals(DIRECTION_NORTH, test4);

        MapDirection directionSouth = DIRECTION_SOUTH;
        MapDirection test5 = directionSouth.rotate(6);
        assertEquals(DIRECTION_EAST, test5);

        MapDirection directionSouthWest = DIRECTION_SOUTH_WEST;
        MapDirection test6 = directionSouthWest.rotate(7);
        assertEquals(DIRECTION_SOUTH, test6);

        MapDirection directionWest = DIRECTION_WEST;
        MapDirection test7 = directionWest.rotate(0);
        assertEquals(DIRECTION_WEST, test7);

        MapDirection directionNorthWest = DIRECTION_NORTH_WEST;
        MapDirection test8 = directionNorthWest.rotate(1);
        assertEquals(DIRECTION_NORTH, test8);

    }

    @Test
    public void oppositeTest() {
        // given
        List<MapDirection> directions = List.of(
                MapDirection.NORTH,
                MapDirection.NORTH_EAST,
                MapDirection.EAST,
                MapDirection.SOUTH_EAST,
                MapDirection.SOUTH,
                MapDirection.SOUTH_WEST,
                MapDirection.WEST,
                MapDirection.NORTH_WEST
        );

        List<MapDirection> expectedDirections = List.of(
                MapDirection.SOUTH,
                MapDirection.SOUTH_WEST,
                MapDirection.WEST,
                MapDirection.NORTH_WEST,
                MapDirection.NORTH,
                MapDirection.NORTH_EAST,
                MapDirection.EAST,
                MapDirection.SOUTH_EAST
        );

        // when
        List<MapDirection> resultDirections = new ArrayList<>();

        for (var direction : directions) {
            resultDirections.add(direction.opposite());
        }

        // then
        Assertions.assertEquals(expectedDirections, resultDirections);
    }
}
