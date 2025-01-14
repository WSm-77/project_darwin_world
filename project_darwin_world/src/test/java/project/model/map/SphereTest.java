package project.model.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import project.model.exceptions.IncorrectPositionException;
import project.model.movement.MapDirection;
import project.model.movement.PositionDirectionPair;
import project.model.movement.Vector2d;
import project.model.worldelements.*;

import java.util.*;

class SphereIT {
    private static final int WIDTH = 5;
    private static final int HEIGHT = 6;
    private static final MapDirection DEFAULT_ORIENTATION = MapDirection.NORTH;
    private static final List<Integer> DEFAULT_GENOME_LIST = List.of(1, 2, 3);
    private static final int DEFAULT_ENERGY = 0;
    private static final int DEFAULT_ACTIVE_GENE_IDX = 0;

    private Sphere map;

    public static AnimalStatistics getAnimalStatistics(Vector2d position) {
        return SphereIT.getAnimalStatistics(position, new Genome(DEFAULT_GENOME_LIST, DEFAULT_ACTIVE_GENE_IDX), DEFAULT_ENERGY);
    }

    public static AnimalStatistics getAnimalStatistics(Vector2d position, Genome genome, int startEnergy) {
        return  SphereIT.getAnimalStatistics(position, genome, startEnergy, DEFAULT_ORIENTATION);
    }

    private static AnimalStatistics getAnimalStatistics(Vector2d position, Genome genome, int startEnergy, MapDirection startOrientation) {
        return new AnimalStatistics(position, genome, startEnergy, startOrientation);
    }

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
        var animalStatistics = SphereIT.getAnimalStatistics(position);
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
        var animalStatistics = SphereIT.getAnimalStatistics(position);
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
        var firstAnimalStatistics = SphereIT.getAnimalStatistics(position);
        var secondAnimalStatistics = SphereIT.getAnimalStatistics(position);
        var thirdAnimalStatistics = SphereIT.getAnimalStatistics(position);
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
        var animalStatistics1 = SphereIT.getAnimalStatistics(repeatedPosition);
        var animalStatistics2 = SphereIT.getAnimalStatistics(repeatedPosition);
        var animalStatistics3 = SphereIT.getAnimalStatistics(repeatedPosition);
        var animalStatistics4 = SphereIT.getAnimalStatistics(new Vector2d(1, 0));
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
        var animalStatistics = SphereIT.getAnimalStatistics(animalStartPosition, genome, 100, MapDirection.NORTH);
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
        var firstAnimalStatistics = SphereIT.getAnimalStatistics(firstAnimalStartPosition, firstAnimalGenome, 100);
        var secondAnimalStatistics = SphereIT.getAnimalStatistics(secondAnimalStartPosition, secondAnimalGenome, 100);
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

    @Test
    void growSinglePlantCorrectPosition() throws IncorrectPositionException {
        // given
        Plant grass = new Grass(new Vector2d(2, 2), 10);

        // when
        map.growPlants(grass);

        // then
        Assertions.assertTrue(map.getPlants().contains(grass));
    }

    @Test
    void growPlantOnOccupiedPosition() {
        // given
        Plant grass1 = new Grass(new Vector2d(2, 2), 10);
        Plant grass2 = new Grass(new Vector2d(2, 2), 5);

        // when
        map.growPlants(grass1);

        // then
        Assertions.assertThrows(IncorrectPositionException.class, () -> map.growPlants(grass2));
    }

    @Test
    void growPlantOutsideMap() {
        // given
        Plant grass = new Grass(new Vector2d(10, 10), 10);

        // when & then
        Assertions.assertThrows(IncorrectPositionException.class, () -> map.growPlants(grass));
    }

    @Test
    void growMultiplePlantsCorrectPositions() throws IncorrectPositionException {
        // given
        Plant grass1 = new Grass(new Vector2d(2, 2), 10);
        Plant grass2 = new Grass(new Vector2d(3, 3), 5);
        Plant grass3 = new Grass(new Vector2d(4, 4), 7);

        // when
        map.growPlants(grass1, grass2, grass3);

        // then
        Assertions.assertTrue(map.getPlants().contains(grass1));
        Assertions.assertTrue(map.getPlants().contains(grass2));
        Assertions.assertTrue(map.getPlants().contains(grass3));
    }

    @Test
    void growMultiplePlantsWithErrors() {
        // given
        Plant grass1 = new Grass(new Vector2d(2, 2), 10);
        Plant grass2 = new Grass(new Vector2d(2, 2), 5);
        Plant grass3 = new Grass(new Vector2d(3, 3), 7);

        // when
        map.growPlants(grass1);

        // then
        Assertions.assertThrows(IncorrectPositionException.class, () -> map.growPlants(grass2, grass3));
        Assertions.assertFalse(map.getPlants().contains(grass3));
    }

    @Test
    void growPlantsStopsOnFirstError() {
        // given
        Plant grass1 = new Grass(new Vector2d(10, 10), 10);
        Plant grass2 = new Grass(new Vector2d(3, 3), 5);

        // when & then
        Assertions.assertThrows(IncorrectPositionException.class, () -> map.growPlants(grass1, grass2));
        Assertions.assertFalse(map.getPlants().contains(grass2));
    }

    @Test
    void feedAnimal_increasesEnergyWhenAnimalEats() {
        // given
        Vector2d animalPosition = new Vector2d(2, 2);
        var animalStatistics = SphereIT.getAnimalStatistics(animalPosition);
        var animal = new AnimalStandardVariant(animalStatistics);
        map.place(animal);

        int initialEnergy = animal.getStatistics().getEnergy();
        int plantEnergy = 5;  // Energy provided by the plant
        Plant grass = new Grass(new Vector2d(2, 2), plantEnergy);
        map.growPlants(grass);

        // when
        map.feedAnimal(animal, grass);  // This assumes that feedAnimal increases energy by plant's energy

        // then
        Assertions.assertEquals(initialEnergy + plantEnergy, animal.getStatistics().getEnergy());
    }

    @Test
    void feedAnimal_throwsExceptionWhenNoPlantsNearby() {
        // given
        Vector2d animalPosition = new Vector2d(2, 2);
        var animalStatistics = SphereIT.getAnimalStatistics(animalPosition);
        var animal = new AnimalStandardVariant(animalStatistics);
        map.place(animal);

        // when & then
        Assertions.assertThrows(IllegalArgumentException.class, () -> map.feedAnimal(animal, null));
    }

    @Test
    void feedAnimal_throwsExceptionWhenNoAnimalsNearby() {
        // given
        int plantEnergy = 5;  // Energy provided by the plant
        Plant grass = new Grass(new Vector2d(2, 2), plantEnergy);
        map.growPlants(grass);

        // when & then
        Assertions.assertThrows(IllegalArgumentException.class, () -> map.feedAnimal(null, grass));
    }

    @Test
    void removePlantFromMap_removesPlantSuccessfully() {
        // given
        Vector2d plantPosition = new Vector2d(3, 3);
        Plant grass = new Grass(plantPosition, 10);
        map.growPlants(grass);
        Assertions.assertTrue(map.getPlants().contains(grass));

        // when
        map.removePlantFromMap(plantPosition);

        // then
        Assertions.assertFalse(map.getPlants().contains(grass));
    }

    @Test
    void removePlantFromMap_throwsExceptionWhenPlantNotOnMap() {
        // given
        Vector2d plantPosition = new Vector2d(3, 3);

        // when & then
        Assertions.assertThrows(IncorrectPositionException.class, () -> map.removePlantFromMap(plantPosition));
    }


}
