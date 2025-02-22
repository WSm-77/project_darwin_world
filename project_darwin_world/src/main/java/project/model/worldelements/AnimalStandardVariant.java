package project.model.worldelements;

import project.model.movement.NextPositionCalculator;
import project.model.movement.PositionDirectionPair;
import project.model.movement.Vector2d;

import java.util.List;
import java.util.Random;

public class AnimalStandardVariant implements Animal {
    private final static String SINGLE_ANIMAL_RESOURCE_PATH_1 = "/images/animals/single_animal1.png";
    private final static String SINGLE_ANIMAL_RESOURCE_PATH_2 = "/images/animals/single_animal2.png";
    private final static List<String > SINGLE_ANIMALS_IMAGES_LIST = List.of(SINGLE_ANIMAL_RESOURCE_PATH_1, SINGLE_ANIMAL_RESOURCE_PATH_2);

    private final Random random = new Random();
    private final String resourcePath;
    final private AnimalStatistics statistics;

    public AnimalStandardVariant(AnimalStatistics statistics) {
        this.statistics = statistics;
        this.resourcePath = getRandomSingleAnimalResourcePath();
    }

    private String getRandomSingleAnimalResourcePath() {
        int idx = this.random.nextInt(SINGLE_ANIMALS_IMAGES_LIST.size());

        return SINGLE_ANIMALS_IMAGES_LIST.get(idx);
    }

    public boolean move(NextPositionCalculator nextPositionCalculator) {
        int angleVal = this.getStatistics().getGenome().next();
        this.getStatistics().setOrientation(this.getStatistics().getOrientation().rotate(angleVal));
        Vector2d prevPosition = this.getPosition();

        PositionDirectionPair currentPositionDirectionPair = new PositionDirectionPair(prevPosition, this.getStatistics().getOrientation());
        var newPositionDirectionPair = nextPositionCalculator.calculateNextPositionDirectionPair(
                currentPositionDirectionPair, this.getStatistics().getOrientation().toUnitVector());

        this.getStatistics().setPosition(newPositionDirectionPair.position());
        this.getStatistics().setOrientation(newPositionDirectionPair.mapDirection());

        return this.getStatistics().getPosition() != prevPosition;
    }

    @Override
    public String toString() {
        return this.getStatistics().getOrientation().toString();
    }

    @Override
    public Vector2d getPosition() {
        return this.getStatistics().getPosition();
    }

    @Override
    public String getResourcePath() {
        return this.resourcePath;
    }

    @Override
    public AnimalStatistics getStatistics() {
        return this.statistics;
    }

    @Override
    public boolean isAlive() {
        return this.getStatistics().getEnergy() > 0;
    }
}
