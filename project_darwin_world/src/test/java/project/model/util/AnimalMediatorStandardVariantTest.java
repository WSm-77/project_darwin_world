package project.model.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.model.map.Boundary;
import project.model.movement.MapDirection;
import project.model.movement.Vector2d;
import project.model.worldelements.Animal;
import project.model.worldelements.AnimalStandardVariant;
import project.model.worldelements.AnimalStatistics;
import project.model.worldelements.Genome;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AnimalMediatorStandardVariantTest {
    private Vector2d position;
    private Genome genome;
    private AnimalStatistics animalStatistics1;
    private AnimalStatistics animalStatistics2;
    private AnimalStatistics animalStatistics3;
    private AnimalStatistics animalStatistics4;
    private Animal animal1;
    private Animal animal2;
    private Animal animal3;
    private Animal animal4;

    private void setChildrenCount(Animal animal, int count) {
        Boundary boundary = new Boundary(animal.getPosition(), animal.getPosition());
        GenomeFactory genomeFactory = new GenomeFactory(10, 0, 10);
        AnimalFactory animalFactory = new AnimalFactory(genomeFactory, 0, 0, boundary, AnimalStandardVariant::new);

        for (int i = 0; i < count; i++) {
            animal.getStatistics().addChild(animalFactory.createRandomAnimal());
        }
    }

    private void setDaysAlive(Animal animal, int count) {
        for (int i = 0; i < count; i++){
            animal.getStatistics().incrementDaysAlive();
        }
    }

    @BeforeEach
    public void setUp() {
        this.position = new Vector2d(0, 0);
        this.genome = new Genome(List.of(0), 0);
        this.animalStatistics1 = new AnimalStatistics(position, genome, 0, MapDirection.NORTH);
        this.animalStatistics2 = new AnimalStatistics(position, genome, 0, MapDirection.NORTH);
        this.animalStatistics3 = new AnimalStatistics(position, genome, 0, MapDirection.NORTH);
        this.animalStatistics4 = new AnimalStatistics(position, genome, 0, MapDirection.NORTH);
        this.animal1 = new AnimalStandardVariant(animalStatistics1);
        this.animal2 = new AnimalStandardVariant(animalStatistics2);
        this.animal3 = new AnimalStandardVariant(animalStatistics3);
        this.animal4 = new AnimalStandardVariant(animalStatistics4);
    }

    @Test
    public void differentEnergy() {
        // given
        AnimalMediatorStandardVariant mediator = new AnimalMediatorStandardVariant();
        List<Animal> testAnimals = List.of(this.animal1, this.animal2, this.animal3, this.animal4);
        this.animal1.getStatistics().updateEnergy(1);
        this.animal2.getStatistics().updateEnergy(3);
        this.animal3.getStatistics().updateEnergy(4);
        this.animal4.getStatistics().updateEnergy(2);

        List<Animal> expectedAnimals1Winner = List.of(this.animal3);
        List<Animal> expectedAnimals2Winners = List.of(this.animal3, this.animal2);
        List<Animal> expectedAnimals3Winners = List.of(this.animal3, this.animal2, this.animal4);
        List<Animal> expectedAnimals4Winners = List.of(this.animal3, this.animal2, this.animal4, this.animal1);


        // when
        List<Animal> result1Winner = mediator.resolveAnimalsConflict(testAnimals, 1);
        List<Animal> result2Winners = mediator.resolveAnimalsConflict(testAnimals, 2);
        List<Animal> result3Winners = mediator.resolveAnimalsConflict(testAnimals, 3);
        List<Animal> result4Winners = mediator.resolveAnimalsConflict(testAnimals, 4);

        // then
        assertEquals(expectedAnimals1Winner, result1Winner);
        assertEquals(expectedAnimals2Winners, result2Winners);
        assertEquals(expectedAnimals3Winners, result3Winners);
        assertEquals(expectedAnimals4Winners, result4Winners);
    }

    @Test
    public void differentDaysAlive() {
        // given
        AnimalMediatorStandardVariant mediator = new AnimalMediatorStandardVariant();

        List<Animal> testAnimals = List.of(this.animal1, this.animal2, this.animal3, this.animal4);
        this.setDaysAlive(this.animal1, 1);
        this.setDaysAlive(this.animal2, 3);
        this.setDaysAlive(this.animal3, 4);
        this.setDaysAlive(this.animal4, 2);

        List<Animal> expectedAnimals1Winner = List.of(this.animal3);
        List<Animal> expectedAnimals2Winners = List.of(this.animal3, this.animal2);
        List<Animal> expectedAnimals3Winners = List.of(this.animal3, this.animal2, this.animal4);
        List<Animal> expectedAnimals4Winners = List.of(this.animal3, this.animal2, this.animal4, this.animal1);


        // when
        List<Animal> result1Winner = mediator.resolveAnimalsConflict(testAnimals, 1);
        List<Animal> result2Winners = mediator.resolveAnimalsConflict(testAnimals, 2);
        List<Animal> result3Winners = mediator.resolveAnimalsConflict(testAnimals, 3);
        List<Animal> result4Winners = mediator.resolveAnimalsConflict(testAnimals, 4);

        // then
        assertEquals(expectedAnimals1Winner, result1Winner);
        assertEquals(expectedAnimals2Winners, result2Winners);
        assertEquals(expectedAnimals3Winners, result3Winners);
        assertEquals(expectedAnimals4Winners, result4Winners);
    }

    @Test
    public void differentChildrenCount() {
        // given
        AnimalMediatorStandardVariant mediator = new AnimalMediatorStandardVariant();
        List<Animal> testAnimals = List.of(this.animal1, this.animal2, this.animal3, this.animal4);
        this.setChildrenCount(this.animal1, 1);
        this.setChildrenCount(this.animal2, 3);
        this.setChildrenCount(this.animal3, 4);
        this.setChildrenCount(this.animal4, 2);

        List<Animal> expectedAnimals1Winner = List.of(this.animal3);
        List<Animal> expectedAnimals2Winners = List.of(this.animal3, this.animal2);
        List<Animal> expectedAnimals3Winners = List.of(this.animal3, this.animal2, this.animal4);
        List<Animal> expectedAnimals4Winners = List.of(this.animal3, this.animal2, this.animal4, this.animal1);


        // when
        List<Animal> result1Winner = mediator.resolveAnimalsConflict(testAnimals, 1);
        List<Animal> result2Winners = mediator.resolveAnimalsConflict(testAnimals, 2);
        List<Animal> result3Winners = mediator.resolveAnimalsConflict(testAnimals, 3);
        List<Animal> result4Winners = mediator.resolveAnimalsConflict(testAnimals, 4);

        // then
        assertEquals(expectedAnimals1Winner, result1Winner);
        assertEquals(expectedAnimals2Winners, result2Winners);
        assertEquals(expectedAnimals3Winners, result3Winners);
        assertEquals(expectedAnimals4Winners, result4Winners);
    }

    @Test
    public void differentRandomFactor() {
        // given
        AnimalMediatorStandardVariant mediator = new AnimalMediatorStandardVariant();
        Map<Animal, Integer> randomFactorMap = Map.of(
                this.animal1, 1,
                this.animal2, 3,
                this.animal3, 4,
                this.animal4, 2
        );

        mediator.setAnimalsRandomFactorMap(randomFactorMap);
        List<Animal> testAnimals = List.of(this.animal1, this.animal2, this.animal3, this.animal4);

        List<Animal> expectedAnimals1Winner = List.of(this.animal3);
        List<Animal> expectedAnimals2Winners = List.of(this.animal3, this.animal2);
        List<Animal> expectedAnimals3Winners = List.of(this.animal3, this.animal2, this.animal4);
        List<Animal> expectedAnimals4Winners = List.of(this.animal3, this.animal2, this.animal4, this.animal1);


        // when
        List<Animal> result1Winner = mediator.resolveAnimalsConflict(testAnimals, 1);
        List<Animal> result2Winners = mediator.resolveAnimalsConflict(testAnimals, 2);
        List<Animal> result3Winners = mediator.resolveAnimalsConflict(testAnimals, 3);
        List<Animal> result4Winners = mediator.resolveAnimalsConflict(testAnimals, 4);

        // then
        assertEquals(expectedAnimals1Winner, result1Winner);
        assertEquals(expectedAnimals2Winners, result2Winners);
        assertEquals(expectedAnimals3Winners, result3Winners);
        assertEquals(expectedAnimals4Winners, result4Winners);
    }

    @Test
    public void generalMediation() {
        // given
        AnimalMediatorStandardVariant mediator = new AnimalMediatorStandardVariant();
        this.animal3.getStatistics().updateEnergy(5);         // animal3 has the highest energy
        this.setDaysAlive(this.animal2, 5);                         // animal2 has the highest days alive count
        this.setChildrenCount(this.animal4, 5);                     // animal4 has the highest children count

        Map<Animal, Integer> randomFactorMap = Map.of(
                this.animal1, 0,
                this.animal2, 0,
                this.animal3, 0,
                this.animal4, 0
        );

        mediator.setAnimalsRandomFactorMap(randomFactorMap);
        List<Animal> testAnimals = List.of(this.animal1, this.animal2, this.animal3, this.animal4);

        List<Animal> expectedAnimals1Winner = List.of(this.animal3);
        List<Animal> expectedAnimals2Winners = List.of(this.animal3, this.animal2);
        List<Animal> expectedAnimals3Winners = List.of(this.animal3, this.animal2, this.animal4);
        List<Animal> expectedAnimals4Winners = List.of(this.animal3, this.animal2, this.animal4, this.animal1);

        // when
        List<Animal> result1Winner = mediator.resolveAnimalsConflict(testAnimals, 1);
        List<Animal> result2Winners = mediator.resolveAnimalsConflict(testAnimals, 2);
        List<Animal> result3Winners = mediator.resolveAnimalsConflict(testAnimals, 3);
        List<Animal> result4Winners = mediator.resolveAnimalsConflict(testAnimals, 4);

        // then
        assertEquals(expectedAnimals1Winner, result1Winner);
        assertEquals(expectedAnimals2Winners, result2Winners);
        assertEquals(expectedAnimals3Winners, result3Winners);
        assertEquals(expectedAnimals4Winners, result4Winners);
    }
}
