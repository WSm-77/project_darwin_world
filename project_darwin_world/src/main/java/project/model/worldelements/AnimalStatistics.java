package project.model.worldelements;

import project.model.movement.MapDirection;
import project.model.movement.Vector2d;

import java.util.*;

public class AnimalStatistics {
    private static final String TO_STRING_TEMPLATE = """
            AnimalStatistics{
            \tposition=%s,
            \torientation=%s,
            \tgenome=%s,
            \tactiveGeneIndex=%s,
            \tenergy=%s,
            \teatenPlants=%s
            \tchildrenCount=%s
            \tdaysAlive=%s
            \tdeathDay=%s
            '}'
            """;
    private static final String ALIVE_ANIMAL_STATUS_STRING = "Alive";
    private static final MapDirection DEFAULT_ORIENTATION = MapDirection.NORTH;
    private static final List<Integer> DEFAULT_GENOME_LIST = List.of(1, 2, 3);
    private static final int DEFAULT_ENERGY = 0;
    private static final int DEFAULT_ACTIVE_GENE_IDX = 0;

    private Vector2d position;
    private MapDirection orientation;
    private Genome genome;
    private int energy;
    private int eatenPlants = 0;
    private int daysAlive = 0;
    private Integer deathDay = null;
    final private Set<Animal> children = new HashSet<>();

    public AnimalStatistics(Vector2d position) {
        this(position, new Genome(DEFAULT_GENOME_LIST, DEFAULT_ACTIVE_GENE_IDX), DEFAULT_ENERGY);
    }

    public AnimalStatistics(Vector2d position, Genome genome, int startEnergy) {
        this(position, genome, startEnergy, DEFAULT_ORIENTATION);
    }

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
    }

    public MapDirection getOrientation() {
        return this.orientation;
    }

    public void setOrientation(MapDirection orientation) {
        this.orientation = orientation;
    }

    public Genome getGenome() {
        return this.genome;
    }

    public void setGenome(Genome genome) {
        this.genome = genome;
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

    public void updateEnergy(int energyAmount) {
        this.energy += energyAmount;
    }

    public int getEatenPlants() {
        return eatenPlants;
    }

    public void updateEatenPlants(int plantsAmount) {
        this.eatenPlants += plantsAmount;
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

    public void setDeathDay(Integer deathDay) {
        this.deathDay = deathDay;
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
}
