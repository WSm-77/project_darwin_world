package project.model.worldelements;

import project.model.movement.NextPositionCalculator;
import project.model.movement.Vector2d;

import java.util.Random;

public class AnimalAgingVariant extends AnimalStandardVariant {
    public static final double MOVE_SKIP_MAX_PERCENTAGE = 0.8;
    private final Random random = new Random();

    public AnimalAgingVariant(Vector2d position) {
        super(position);
    }

    public AnimalAgingVariant(Vector2d position, Genome genome, int startEnergy) {
        super(position, genome, startEnergy);
    }

    private double calculateSkipPercentage() {
        return Math.min((double)this.daysAlive / 100, AnimalAgingVariant.MOVE_SKIP_MAX_PERCENTAGE);
    }

    @Override
    public boolean move(NextPositionCalculator nextPositionCalculator) {
        if (random.nextDouble() < this.calculateSkipPercentage()) {
            return false;
        }

        return super.move(nextPositionCalculator);
    }
}
