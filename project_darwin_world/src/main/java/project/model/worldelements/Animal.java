package project.model.worldelements;

import project.model.movement.NextPositionCalculator;

/**
 * Represents an animal in the world that has energy, a genome, and orientation,
 * can move and interact with its environment.
 */
public interface Animal extends WorldElement {

    /**
     * Checks if the animal is still alive.
     *
     * @return True if the animal is alive, false otherwise.
     */
    boolean isAlive();

    /**
     * Moves the animal to a new position determined by the provided position calculator.
     *
     * @param nextPositionCalculator a functional interface or class that determines
     *                               the next position based on the current position.
     * @return True if animal changed position, false otherwise.
     */
    boolean move(NextPositionCalculator nextPositionCalculator);

    /**
     * Retrive animal statistics
     *
     * @return AnimalStatistics object representing animal state
     */
    AnimalStatistics getStatistics();
}
