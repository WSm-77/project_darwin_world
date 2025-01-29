package project.model.worldelements;

import project.model.movement.NextPositionCalculator;

import java.util.Random;

public class AnimalAgingVariant extends AnimalStandardVariant {
    public static final double MOVE_SKIP_MAX_PERCENTAGE = 0.8;
    private final Random random = new Random();

    public AnimalAgingVariant(AnimalStatistics animalStatistics) {
        super(animalStatistics);
    }

    private double calculateSkipPercentage() {
        return Math.min((double)this.getStatistics().getDaysAlive() / 100, AnimalAgingVariant.MOVE_SKIP_MAX_PERCENTAGE);
    }

    @Override
    public boolean move(NextPositionCalculator nextPositionCalculator) {
        if (random.nextDouble() < this.calculateSkipPercentage()) {
            return false;
        }

        return super.move(nextPositionCalculator);
    }
}
