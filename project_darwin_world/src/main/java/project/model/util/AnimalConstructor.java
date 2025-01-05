package project.model.util;

import project.model.movement.Vector2d;
import project.model.worldelements.Animal;
import project.model.worldelements.Genome;

@FunctionalInterface
public interface AnimalConstructor<AnimalType extends Animal> {
    public AnimalType construct(Vector2d position, Genome genome, int startEnergy);
}
