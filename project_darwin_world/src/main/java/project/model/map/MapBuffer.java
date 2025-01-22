package project.model.map;

import project.model.worldelements.Animal;
import project.model.worldelements.Plant;

import java.util.Optional;

public class MapBuffer {
    private Optional<Animal> lastAnimalPlaced = Optional.empty();
    private Optional<Animal> lastAnimalRemoved = Optional.empty();
    private Optional<Plant> lastPlantPlaced = Optional.empty();
    private Optional<Plant> lastPlantRemoved = Optional.empty();

    public void setLastAnimalPlaced(Animal animal) {
        this.lastAnimalPlaced = Optional.of(animal);
    }

    public void setLastPlantPlaced(Plant plant) {
        this.lastPlantPlaced = Optional.of(plant);
    }

    public void setLastAnimalRemoved(Animal animal) {
        this.lastAnimalRemoved = Optional.of(animal);
    }

    public void setLastPlantRemoved(Plant plant) {
        this.lastPlantRemoved = Optional.of(plant);
    }

    public Optional<Animal> getLastAnimalPlaced() {
        return this.lastAnimalPlaced;
    }

    public Optional<Plant> getLastPlantPlaced() {
        return this.lastPlantPlaced;
    }

    public Optional<Animal> getLastAnimalRemoved() {
        return this.lastAnimalRemoved;
    }

    public Optional<Plant> getLastPlantRemoved() {
        return this.lastPlantRemoved;
    }
}
