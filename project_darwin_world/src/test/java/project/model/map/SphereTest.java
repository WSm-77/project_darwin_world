package project.model.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import project.model.exceptions.IncorrectPositionException;
import project.model.movement.MapDirection;
import project.model.movement.PositionDirectionPair;
import project.model.movement.Vector2d;
import project.model.worldelements.Animal;
import project.model.worldelements.AnimalStandardVariant;
import project.model.worldelements.AnimalStatistics;
import project.model.worldelements.Genome;

import java.util.*;

class SphereIT {
    private static final int WIDTH = 5;
    private static final int HEIGHT = 6;
    private Sphere map;

    @BeforeEach
    public void setUp() {
        this.map = new Sphere(SphereIT.WIDTH, SphereIT.HEIGHT);
    }


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
                false,  // new Vector2d(5, 6)
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
    void calculateNextPositionDirectionPairUpperBoundary() {
        // given
        Vector2d position = new Vector2d(2, 5);
        MapDirection direction = MapDirection.NORTH_WEST;
        Vector2d moveVector = new Vector2d(1, 1);

        var currentPositionDirectionPair = new PositionDirectionPair(position, direction);
        var expectedPositionDirectionPair = new PositionDirectionPair(position, MapDirection.SOUTH_EAST);

        // when
        var newPositionDirectionPair = map.calculateNextPositionDirectionPair(currentPositionDirectionPair, moveVector);

        // then
        Assertions.assertEquals(expectedPositionDirectionPair, newPositionDirectionPair);
    }

    @Test
    void calculateNextPositionDirectionLowerBoundary() {
        // given
        Vector2d position = new Vector2d(2, 0);
        MapDirection direction = MapDirection.SOUTH_WEST;
        Vector2d moveVector = new Vector2d(-1, -1);

        var currentPositionDirectionPair = new PositionDirectionPair(position, direction);
        var expectedPositionDirectionPair = new PositionDirectionPair(position, MapDirection.NORTH_EAST);

        // when
        var newPositionDirectionPair = map.calculateNextPositionDirectionPair(currentPositionDirectionPair, moveVector);

        // then
        Assertions.assertEquals(expectedPositionDirectionPair, newPositionDirectionPair);
    }

    @Test
    void placeAnimalsOnMap() {
        // given
        Vector2d position = new Vector2d(2, 2);
        var animalStatistics = new AnimalStatistics(position);
        var animal = new AnimalStandardVariant(animalStatistics);

        // when
        // then
        Assertions.assertDoesNotThrow(() -> map.place(animal));
        Assertions.assertTrue(map.animalsAt(position).isPresent());
    }

    @Test
    void placeAnimalsOutsideMap() {
        // given
        Vector2d position = new Vector2d(100, 100);
        var animalStatistics = new AnimalStatistics(position);
        var animal = new AnimalStandardVariant(animalStatistics);

        // when
        // then
        Assertions.assertThrowsExactly(IncorrectPositionException.class, () -> map.place(animal));
        Assertions.assertFalse(map.animalsAt(position).isPresent());
    }

    @Test
    void placeMultipleAnimalsAtTheSamePosition() {
        // given
        Vector2d position = new Vector2d(2, 2);
        var firstAnimalStatistics = new AnimalStatistics(position);
        var secondAnimalStatistics = new AnimalStatistics(position);
        var thirdAnimalStatistics = new AnimalStatistics(position);
        var firstAnimal = new AnimalStandardVariant(firstAnimalStatistics);
        var secondAnimal = new AnimalStandardVariant(secondAnimalStatistics);
        var thirdAnimal = new AnimalStandardVariant(thirdAnimalStatistics);

        Set<Animal> expectedAnimalsSet = Set.of(firstAnimal, secondAnimal, thirdAnimal);

        // when
        Executable placeFirstAnimal = () -> map.place(firstAnimal);
        Executable placeSecondAnimal = () -> map.place(secondAnimal);
        Executable placeThirdAnimal = () -> map.place(thirdAnimal);

        // then
        Assertions.assertDoesNotThrow(placeFirstAnimal);
        Assertions.assertDoesNotThrow(placeSecondAnimal);
        Assertions.assertDoesNotThrow(placeThirdAnimal);
        Assertions.assertEquals(expectedAnimalsSet, map.animalsAt(position).get());
    }

    @Test
    void getAnimals() {
        //  given
        var repeatedPosition = new Vector2d(2, 2);
        var animalStatistics1 = new AnimalStatistics(repeatedPosition);
        var animalStatistics2 = new AnimalStatistics(repeatedPosition);
        var animalStatistics3 = new AnimalStatistics(repeatedPosition);
        var animalStatistics4 = new AnimalStatistics(new Vector2d(1, 0));
        var animal1 = new AnimalStandardVariant(animalStatistics1);
        var animal2 = new AnimalStandardVariant(animalStatistics2);
        var animal3 = new AnimalStandardVariant(animalStatistics3);
        var animal4 = new AnimalStandardVariant(animalStatistics4);
        List<Animal> expectedAnimals = List.of(animal1, animal2, animal3, animal4);

        // when
        this.map.place(animal1);
        this.map.place(animal2);
        this.map.place(animal3);
        this.map.place(animal4);
        List<Animal> animals = this.map.getAnimals();

        // then
        Assertions.assertEquals(new HashSet<>(expectedAnimals), new HashSet<>(animals));
    }

    @Test
    void generalAnimalMovement() {
        // given
        Vector2d animalStartPosition = new Vector2d(4, 5);   // start in top right corner
        List<Integer> rotations = List.of(7, 5, 5, 6, 1, 0, 0, 7, 0, 2);
        Genome genome = new Genome(rotations, 0);
        var animalStatistics = new AnimalStatistics(animalStartPosition, genome, 100, MapDirection.NORTH);
        var animal = new AnimalStandardVariant(animalStatistics);

        List<Vector2d> expectedNextPositions = List.of(
                new Vector2d(4, 5),
                new Vector2d(4, 5),
                new Vector2d(4, 5),
                new Vector2d(0, 4),
                new Vector2d(0, 3),
                new Vector2d(0, 2),
                new Vector2d(0, 1),
                new Vector2d(1, 0),
                new Vector2d(1, 0),
                new Vector2d(2, 1)
        );

        List<MapDirection> expectedNextOrientations = List.of(
                MapDirection.SOUTH_EAST,        // North Pole crossed
                MapDirection.SOUTH,             // North Pole crossed
                MapDirection.SOUTH_WEST,        // North Pole crossed
                MapDirection.SOUTH_EAST,
                MapDirection.SOUTH,
                MapDirection.SOUTH,
                MapDirection.SOUTH,
                MapDirection.SOUTH_EAST,
                MapDirection.NORTH_WEST,        // South Pole crossed
                MapDirection.NORTH_EAST
        );

        // when
        List<Vector2d> nextPositions = new ArrayList<>();
        List<MapDirection> nextOrientations = new ArrayList<>();

        map.place(animal);

        for (int i = 0; i < rotations.size(); i++) {
            map.move(animal);
            nextPositions.add(animal.getPosition());
            nextOrientations.add(animal.getStatistics().getOrientation());
        }

        // then
        Assertions.assertEquals(expectedNextPositions, nextPositions);
        Assertions.assertEquals(expectedNextOrientations, nextOrientations);
    }

    @Test
    void multipleAnimalsAtTheSamePosition() {
        // given
        Vector2d firstAnimalStartPosition = new Vector2d(0, 0);
        Vector2d secondAnimalStartPosition = new Vector2d(0, 2);
        List<Integer> firstAnimalRotations = List.of(0);
        List<Integer> secondAnimalRotations = List.of(4, 0);
        Genome firstAnimalGenome = new Genome(firstAnimalRotations, 0);
        Genome secondAnimalGenome = new Genome(secondAnimalRotations, 0);
        var firstAnimalStatistics = new AnimalStatistics(firstAnimalStartPosition, firstAnimalGenome, 100);
        var secondAnimalStatistics = new AnimalStatistics(secondAnimalStartPosition, secondAnimalGenome, 100);
        var firstAnimal = new AnimalStandardVariant(firstAnimalStatistics);
        var secondAnimal = new AnimalStandardVariant(secondAnimalStatistics);

        List<Vector2d> firstAnimalExpectedNextPositions = List.of(
                new Vector2d(0, 1),
                new Vector2d(0, 2)
        );

        List<MapDirection> firstAnimalExpectedNextOrientations = List.of(
                MapDirection.NORTH,
                MapDirection.NORTH
        );

        List<Vector2d> secondAnimalExpectedNextPositions = List.of(
                new Vector2d(0, 1),
                new Vector2d(0, 0)
        );

        List<MapDirection> secondAnimalExpectedNextOrientations = List.of(
                MapDirection.SOUTH,
                MapDirection.SOUTH
        );

        // when
        List<Vector2d> firstAnimalNextPositions = new ArrayList<>();
        List<MapDirection> firstAnimalNextOrientations = new ArrayList<>();
        List<Vector2d> secondAnimalNextPositions = new ArrayList<>();
        List<MapDirection> secondAnimalNextOrientations = new ArrayList<>();

        map.place(firstAnimal);
        map.place(secondAnimal);

        for (int i = 0; i < firstAnimalExpectedNextPositions.size(); i++) {
            map.move(firstAnimal);
            firstAnimalNextPositions.add(firstAnimal.getPosition());
            firstAnimalNextOrientations.add(firstAnimal.getStatistics().getOrientation());
            map.move(secondAnimal);
            secondAnimalNextPositions.add(secondAnimal.getPosition());
            secondAnimalNextOrientations.add(secondAnimal.getStatistics().getOrientation());
        }

        // then
        Assertions.assertEquals(firstAnimalExpectedNextPositions, firstAnimalNextPositions);
        Assertions.assertEquals(firstAnimalExpectedNextOrientations, firstAnimalNextOrientations);
        Assertions.assertEquals(secondAnimalExpectedNextPositions, secondAnimalNextPositions);
        Assertions.assertEquals(secondAnimalExpectedNextOrientations, secondAnimalNextOrientations);
    }
}
