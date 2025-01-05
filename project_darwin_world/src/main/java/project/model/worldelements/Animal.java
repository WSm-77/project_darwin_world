package project.model.worldelements;

import project.model.movement.MapDirection;
import project.model.movement.NextPositionCalculator;

/**
 * Represents an animal in the world that has energy, a genome, and orientation,
 * can move and interact with its environment.
 */
public interface Animal extends WorldElement {

    /**
     * Retrieves the current energy level of the animal.
     *
     * @return The current energy of the animal as an integer.
     */
    int getEnergy();

    /**
     * Retrieves the genome associated with this animal.
     *
     * @return A Genome object representing the genetic code of the animal.
     */
    Genome getGenome();

    /**
     * Retrieves the current orientation of the animal.
     *
     * @return A MapDirection enum representing the direction the animal is facing.
     */
    MapDirection getOrientation();

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
     * Updates the animal's energy level by adding or subtracting the given amount.
     *
     * @param energyAmount the amount to modify the energy by. Positive to increase,
     *                     negative to decrease.
     */
    void updateEnergy(int energyAmount);
}
