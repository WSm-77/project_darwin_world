package project.model.simulation;

import project.model.movement.Vector2d;
import project.model.util.*;
import project.model.map.WorldMap;
import project.model.worldelements.Animal;
import project.model.worldelements.WorldElement;

import java.util.*;
import java.util.stream.Collectors;

public class Simulation implements Runnable, MapChangeListener {
    private final WorldMap worldMap;
    private final int dailyPlantGrowth;
    private final AnimalFactory animalFactory;
    private final AnimalMediator animalMediator;
    private final PlantGrower plantGrower;
    private final int energyToReproduce;
    private boolean running = true;
    private boolean paused = false;
    private int day = 1;
    private final List<SimulationListener> simulationListeners = new ArrayList<>();
    private Optional<Thread> simulationThread = Optional.empty();
    private int simulationRefreshTime = 500;
    private final List<Animal> deadAnimals = new ArrayList<>();

    public static final String INTERRUPTION_MESSAGE_TEMPLATE = "Simulation of map %s interrupted!!!";
    public static final String INTERRUPTION_DURING_SLEEP_MESSAGE_TEMPLATE = "Simulation of map %s interrupted during sleep!!!";
    public static final int ENERGY_DAILY_LOSS = 1;
    public static final int PARENTS_NEEDED_TO_BREED_COUNT = 2;

    public Simulation(SimulationBuilder simulationBuilder) {
        this.worldMap = simulationBuilder.getWorldMap();
        this.dailyPlantGrowth = simulationBuilder.getDailyPlantGrowth();
        this.animalFactory = simulationBuilder.getAnimalFactory();
        this.energyToReproduce = simulationBuilder.getEnergyToReproduce();
        this.animalMediator = simulationBuilder.getAnimalMediator();
        this.plantGrower = simulationBuilder.getPlantGrower();
    }

    private String createInterruptionMessage() {
        return String.format(Simulation.INTERRUPTION_MESSAGE_TEMPLATE, this.worldMap.getId());
    }

    private String createInterruptionDuringSleepMessage() {
        return String.format(Simulation.INTERRUPTION_DURING_SLEEP_MESSAGE_TEMPLATE, this.worldMap.getId());
    }

    private void removeDeadAnimals() {
        this.worldMap.getAnimals().stream()
                .filter(animal -> !animal.isAlive())
                .forEach(animal -> {
                    var animalStatistics = animal.getStatistics();
                    animalStatistics.setDeathDay(this.day);
                    this.worldMap.removeAnimal(animal);
                    this.deadAnimals.add(animal);
                }
                );
    }

    private Set<Vector2d> getWorldElementsPositions(Collection<? extends WorldElement> worldElements) {
        return worldElements.stream()
                .map(WorldElement::getPosition)
                .collect(Collectors.toSet());
    }

    private void moveAnimals() {
        this.worldMap.getAnimals()
                .forEach(this.worldMap::move);
    }

    private void consumePlants() {

    }

    private boolean canBreed(Animal parent1, Animal parent2) {
        return parent1.getStatistics().getEnergy() >= this.energyToReproduce &&
                parent2.getStatistics().getEnergy() >= this.energyToReproduce;
    }

    private void breedGroup(Set<Animal> groupOfAnimals) {
        List<Animal> parentsList = this.animalMediator.resolveAnimalsConflict(groupOfAnimals, PARENTS_NEEDED_TO_BREED_COUNT);

        if (parentsList.size() != PARENTS_NEEDED_TO_BREED_COUNT)
            return;

        Animal parent1 = parentsList.getFirst();
        Animal parent2 = parentsList.getLast();

        if (!this.canBreed(parent1, parent2)) {
            return;
        }

        Animal child = this.animalFactory.createFromParents(parent1, parent2);
        this.worldMap.place(child);
    }

    private void reproduceAnimals() {
        Set<Vector2d> animalsPositions = this.getWorldElementsPositions(this.worldMap.getAnimals());

        for (var groupOfAnimalsPosition : animalsPositions) {
            Optional<Set<Animal>> groupOfAnimals = this.worldMap.animalsAt(groupOfAnimalsPosition);

            groupOfAnimals.ifPresent(this::breedGroup);
        }
    }

    private void growPlants() {
        this.plantGrower.growPlants(this.dailyPlantGrowth);
    }

    private void consumeDailyEnergyAmount() {
        this.worldMap.getAnimals()
                .forEach(animal -> animal.getStatistics().updateEnergy(-Simulation.ENERGY_DAILY_LOSS));
    }

    private void updateDaysAlive() {
        this.worldMap.getAnimals()
                .forEach(animal -> animal.getStatistics().incrementDaysAlive());
    }

    private void finishDay() {
        this.consumeDailyEnergyAmount();
        this.updateDaysAlive();
        this.day++;

        this.notifyListeners(SimulationEvent.NEXT_DAY);
    }

    @Override
    public void run() {
        while (this.running) {
            System.out.println();
            System.out.printf("Simulation day: %d\n", this.day);
            System.out.printf("Animals on the map: %s\n", this.worldMap.getAnimals());
            System.out.println();

            this.removeDeadAnimals();
            this.moveAnimals();
            this.consumePlants();
            this.reproduceAnimals();
            this.growPlants();

            this.finishDay();
        }
    }

    public WorldMap getWorldMap() {
        return this.worldMap;
    }

    public int getDay() {
        return this.day;
    }

    public List<Animal> getDeadAnimals() {
        return this.deadAnimals;
    }

    public void subscribe(SimulationListener simulationListener) {
        this.simulationListeners.add(simulationListener);
    }

    private void notifyListeners(SimulationEvent simulationEvent) {
        for (var listener : this.simulationListeners) {
            listener.simulationChanged(simulationEvent);
        }

        try {
            this.simulationStep();
        } catch (InterruptedException e) {
            this.simulationThread.ifPresent(Thread::interrupt);
            this.running = false;
            this.paused = false;
        }
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        this.notifyListeners(SimulationEvent.MAP_CHANGED);
    }

    public void simulationStep() throws InterruptedException {
        synchronized (this) {
                while (this.isPaused()) {
                    this.wait();
                }
            }

        Thread.sleep(this.simulationRefreshTime);
    }

    public void setSimulationRefreshTime(int ms) {
        this.simulationRefreshTime = ms;
    }

    public void start() {
        this.simulationThread = Optional.of(new Thread(this));
        this.simulationThread.ifPresent(Thread::start);
    }

    public void terminate() {
        if (this.simulationThread.isEmpty()) {
            throw new IllegalThreadStateException("Thread not Started!!!");
        }

        this.simulationThread.get().interrupt();
        this.running = false;
    }

    public synchronized void pause() {
        this.paused = true;
        System.out.println("Simulation paused!!!");
    }

    public synchronized void resume() {
        this.paused = false;
        this.notifyAll();
    }

    public synchronized boolean isPaused() {
        return this.paused;
    }
}
