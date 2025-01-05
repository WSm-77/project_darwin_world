package project.model.worldelements;

import project.model.movement.MapDirection;
import project.model.movement.NextPositionCalculator;
import project.model.movement.Vector2d;

public interface Animal extends WorldElement {
    int getEnergy();
    Genome getGenome();
    MapDirection getOrientation();
    boolean isAlive();
    boolean move(NextPositionCalculator nextPositionCalculator);
    void updateEnergy(int energyAmount);
}
