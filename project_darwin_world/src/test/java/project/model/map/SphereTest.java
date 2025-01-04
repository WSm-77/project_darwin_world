package project.model.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import project.model.exceptions.IncorrectPositionException;
import project.model.movement.MapDirection;
import project.model.movement.Vector2d;
import project.model.worldelements.Animal;
import project.model.worldelements.Genome;

import java.util.ArrayList;
import java.util.List;

class SphereIT {
    private static final int WIDTH = 5;
    private static final int HEIGHT = 6;
    private static final Sphere map = new Sphere(SphereIT.WIDTH, SphereIT.HEIGHT);

    @Test
    void getCurrentBounds() {
        // given
        Boundary expectedBounds = new Boundary(new Vector2d(0, 0), new Vector2d(4, 5));

        // when
        Boundary currentBounds = map.getCurrentBounds();

        Assertions.assertEquals(expectedBounds, currentBounds);
    }

    @Test
    void isOnMap() {
        // given
        List<Vector2d> positions = List.of(
                new Vector2d(0, 0),
                new Vector2d(4, 5),
                new Vector2d(2, 2),
                new Vector2d(5, 6),
                new Vector2d(100, 100),
                new Vector2d(-100, -100),
                new Vector2d(-100, 100),
                new Vector2d(100, -100),
                new Vector2d(2, 100),
                new Vector2d(2, -100),
                new Vector2d(100, 2),
                new Vector2d(-100, 2)
        );

        List<Boolean> expectedList = List.of(
                true, // new Vector2d(0, 0)
                true,   // new Vector2d(4, 5)
                true,   // new Vector2d(2, 2)
                false,   // new Vector2d(5, 6)
                false,  // new Vector2d(100, 100)
                false,  // new Vector2d(-100, -100)
                false,  // new Vector2d(-100, 100)
                false,  // new Vector2d(100, -100)
                false,  // new Vector2d(2, 100)
                false,  // new Vector2d(2, -100)
                false,  // new Vector2d(100, 2)
                false   // new Vector2d(-100, 2)
        );

        // when
        List<Boolean> isOnMapResultsList = new ArrayList<>();
        for (var position : positions) {
            isOnMapResultsList.add(map.isOnMap(position));
        }

        // then
        Assertions.assertEquals(expectedList, isOnMapResultsList);
    }

    @Test
    void calculateNextPositionOnMiddleOfTheMap() {
        // given
        Vector2d position = new Vector2d(2, 2);
        Vector2d moveVector = MapDirection.NORTH_EAST.toUnitVector();
        Vector2d expectedPosition = new Vector2d(3, 3);

        // when
        Vector2d newPosition = map.calculateNextPosition(position, moveVector);

        // then
        Assertions.assertEquals(expectedPosition, newPosition);
    }

    @Test
    void calculateNextPositionRightBoundary() {
        // given
        Vector2d position = new Vector2d(4, 2);
        Vector2d moveVector = new Vector2d(1, 1);
        Vector2d expectedPosition = new Vector2d(0, 3);

        // when
        Vector2d newPosition = map.calculateNextPosition(position, moveVector);

        // then
        Assertions.assertEquals(expectedPosition, newPosition);
    }

    @Test
    void calculateNextPositionLeftBoundary() {
        // given
        Vector2d position = new Vector2d(0, 2);
        Vector2d moveVector = new Vector2d(-1, -1);
        Vector2d expectedPosition = new Vector2d(4, 1);

        // when
        Vector2d newPosition = map.calculateNextPosition(position, moveVector);

        // then
        Assertions.assertEquals(expectedPosition, newPosition);
    }

    @Test
    void calculateNextPositionUpperBoundary() {
        // given
        Vector2d position = new Vector2d(2, 5);
        Vector2d moveVector = new Vector2d(1, 1);

        // when
        Vector2d newPosition = map.calculateNextPosition(position, moveVector);

        // then
        Assertions.assertEquals(position, newPosition);
    }

    @Test
    void calculateNextPositionLowerBoundary() {
        // given
        Vector2d position = new Vector2d(2, 0);
        Vector2d moveVector = new Vector2d(-1, -1);

        // when
        Vector2d newPosition = map.calculateNextPosition(position, moveVector);

        // then
        Assertions.assertEquals(position, newPosition);
    }

    @Test
    void placeAnimalsOnMap() {
        // given
        Vector2d position = new Vector2d(2, 2);
        Animal animal = new Animal(position);

        // when
        // then
        Assertions.assertDoesNotThrow(() -> map.place(animal));
    }

    @Test
    void placeAnimalsOutsideMap() {
        // given
        Vector2d position = new Vector2d(100, 100);
        Animal animal = new Animal(position);

        // when
        // then
        Assertions.assertThrowsExactly(IncorrectPositionException.class, () -> map.place(animal));
    }

    @Test
    void generalAnimalMovement() {
        // given
        Vector2d animalStartPosition = new Vector2d(4, 5);   // start in top right corner
        List<Integer> rotations = List.of(7, 1, 1, 2);
        Genome genome = new Genome(rotations, 0);
        Animal animal = new Animal(animalStartPosition, genome, 100);

        List<Vector2d> expectedNextPositions = List.of(
                new Vector2d(4, 5),
                new Vector2d(4, 5),
                new Vector2d(4, 5),
                new Vector2d(0, 4)
        );

        List<MapDirection> expectedNextOrientations = List.of(
                MapDirection.NORTH_WEST,
                MapDirection.NORTH,
                MapDirection.NORTH_EAST,
                MapDirection.SOUTH_EAST
        );

        // when
        List<Vector2d> nextPositions = new ArrayList<>();
        List<MapDirection> nextOrientations = new ArrayList<>();

        map.place(animal);

        for (int i = 0; i < rotations.size(); i++) {
            map.move(animal);
            nextPositions.add(animal.getPosition());
            nextOrientations.add(animal.getOrientation());
        }

        // then
        Assertions.assertEquals(expectedNextPositions, nextPositions);
        Assertions.assertEquals(expectedNextOrientations, nextOrientations);
    }
}
