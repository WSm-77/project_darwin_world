package project.model.worldelements;

import project.model.movement.NextPositionCalculator;
import project.model.movement.PositionDirectionPair;
import project.model.movement.Vector2d;

public class AnimalStandardVariant implements Animal {
    final private AnimalStatistics statistics;

    public AnimalStandardVariant(AnimalStatistics statistics) {
        this.statistics = statistics;
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
    public AnimalStatistics getStatistics() {
        return this.statistics;
    }

    @Override
    public boolean isAlive() {
        return this.getStatistics().getEnergy() > 0;
    }
}
