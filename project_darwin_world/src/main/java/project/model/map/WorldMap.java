package project.model.map;

import project.model.exceptions.IncorrectPositionException;
import project.model.movement.NextPositionCalculator;
import project.model.movement.Vector2d;
import project.model.worldelements.Animal;
import project.model.worldelements.Plant;
import project.model.worldelements.WorldElement;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface responsible for interacting with the map of the world.
 *
 * @author apohllo, idzik
 */
public interface WorldMap extends NextPositionCalculator {
    /**
     * Return boolean value informing whether position is on the map or not.
     *
     * @param position The position to check.
     * @return True, if position is on the map otherwise false.
     */
    boolean isOnMap(Vector2d position);

    /**
     * Grow plants on the map.
     *
     * @param plants Plants to place on the map.
     * @throws IncorrectPositionException If position is occupied on the map.
     */
    void growPlants(Plant... plants) throws IncorrectPositionException;

    /**
     * Remove animal from the map.
     *
     * @param animal The animal to remove from the map.
     * @throws IllegalArgumentException If animal is not present on the map.
     */
    void removeAnimal(Animal animal) throws IllegalArgumentException;

    /**
     * Place a animal on the map.
     *
     * @param animal The animal to place on the map.
     */

    void place(Animal animal) throws IncorrectPositionException;

    /**
     * Return plants located on the map
     *
     * @return List of all plants located on the map.
     */
    List<Plant> getPlants();

    /**
     * Moves an animal (if it is present on the map) according to it's orientation.
     * If the move is not possible, this method has no effect.
     *
     * @param animal The animal to move
     */
    void move(Animal animal);

    /**
     * Return a Optional Object of WorldElement at a given position.
     *
     * @param position The position of the animal.
     * @return Optional object of WorldElement or empty Optional if the position is not occupied.
     */
    Optional<WorldElement> objectAt(Vector2d position);

    /**
     * Return map boundaries
     *
     * @return Boundary object representing map boundaries.
     */
    Boundary getCurrentBounds();

    /**
     * Return animals located on the map
     *
     * @return List of all animals located on the map.
     */
    List<Animal> getAnimals();

    /**
     * Return object's UUID
     *
     * @return object's UUID
     */
    UUID getId();
}
