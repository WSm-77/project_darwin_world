package project.model.worldelements;

import project.model.movement.MapDirection;
import project.model.movement.Vector2d;

import java.util.*;

public class AnimalStatistics {
    private static final String TO_STRING_TEMPLATE = """
            AnimalStatistics{
            \tposition=%s
            \torientation=%s
            \tgenome=%s
            \tactiveGeneIndex=%s
            \tenergy=%s
            \teatenPlants=%s
            \tchildrenCount=%s
            \tdaysAlive=%s
            \tdeathDay=%s
            '}'
            """;
    private static final String ALIVE_ANIMAL_STATUS_STRING = "Alive";

    private Vector2d position;
    private MapDirection orientation;
    private Genome genome;
    private int energy;
    private int eatenPlants = 0;
    private int daysAlive = 0;
    private Integer deathDay = null;
    final private Set<Animal> children = Collections.synchronizedSet(new HashSet<>());
    private Set<AnimalStatisticsListener> listeners = new HashSet<>();

    public AnimalStatistics(Vector2d position, Genome genome, int startEnergy, MapDirection startOrientation) {
        this.position = position;
        this.genome = genome;
        this.energy = startEnergy;
        this.orientation = startOrientation;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
        this.notifyListeners();
    }

    public MapDirection getOrientation() {
        return this.orientation;
    }

    public void setOrientation(MapDirection orientation) {
        this.orientation = orientation;
        this.notifyListeners();
    }

    public Genome getGenome() {
        return this.genome;
    }

    public void setGenome(Genome genome) {
        this.genome = genome;
        this.notifyListeners();
    }

    public List<Integer> getGenesList() {
        return this.genome.getGenome();
    }

    public int getActiveGeneIndex() {
        return this.genome.getActiveGeneIdx();
    }

    public int getEnergy() {
        return this.energy;
    }

    /**
     * Updates the animal's energy level by adding or subtracting the given amount.
     *
     * @param energyAmount the amount to modify the energy by. Positive to increase,
     *                     negative to decrease.
     */
    public void updateEnergy(int energyAmount) {
        this.energy = Integer.max(0, this.energy + energyAmount);
    }

    public int getEatenPlants() {
        return eatenPlants;
    }

    /**
     * Updates the animal's eaten plants count by adding the given amount.
     *
     * @param plantsAmount the amount to modify the eaten plant count by.
     */
    public void updateEatenPlants(int plantsAmount) {
        this.eatenPlants += plantsAmount;
        this.notifyListeners();
    }

    public int getChildrenCount() {
        return this.children.size();
    }

    public void addChild(Animal animal) {
        this.children.add(animal);
    }

    public int getDaysAlive() {
        return this.daysAlive;
    }

    public void incrementDaysAlive() {
        this.daysAlive++;
    }

    public Optional<Integer> getDeathDay() {
        return Optional.ofNullable(this.deathDay);
    }

    public int getDescendantsCount() {
        return this.getDescendantsCount(new HashSet<>());
    }

    public int getDescendantsCount(Set<Animal> checked) {
        synchronized (this.children) {
            int descendantsCount = 0;

            for (Animal child : this.children) {
                if (checked.contains(child)) {
                    continue;
                }

                checked.add(child);
                descendantsCount++;
                descendantsCount += child.getStatistics().getDescendantsCount(checked);
            }

            return descendantsCount;
        }
    }

    public void setDeathDay(Integer deathDay) {
        this.deathDay = deathDay;
        this.notifyListeners();
    }

    @Override
    public String toString() {
        return String.format(AnimalStatistics.TO_STRING_TEMPLATE,
            this.getPosition(),
            this.getOrientation(),
            this.getGenesList(),
            this.getActiveGeneIndex(),
            this.getEnergy(),
            this.getEatenPlants(),
            this.getChildrenCount(),
            this.getDaysAlive(),
            this.getDeathDay()
                    .map(Object::toString)
                    .orElse(AnimalStatistics.ALIVE_ANIMAL_STATUS_STRING)
        );
    }

    public void subscribe(AnimalStatisticsListener animalStatisticsListener) {
        this.listeners.add(animalStatisticsListener);
    }

    public void unsubscribe(AnimalStatisticsListener animalStatisticsListener) {
        this.listeners.remove(animalStatisticsListener);
    }

    public void notifyListeners() {
        for (var listener : this.listeners) {
            listener.statisticsChanged();
        }
    }
}
