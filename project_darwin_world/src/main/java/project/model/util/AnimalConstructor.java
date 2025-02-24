package project.model.util;

import project.model.movement.MapDirection;
import project.model.movement.Vector2d;
import project.model.worldelements.Animal;
import project.model.worldelements.AnimalStatistics;
import project.model.worldelements.Genome;

@FunctionalInterface
public interface AnimalConstructor<AnimalType extends Animal> {
    public AnimalType construct(AnimalStatistics statistics);
}
