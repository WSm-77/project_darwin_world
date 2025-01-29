package project.model.simulation;

import project.model.map.WorldMap;
import project.model.movement.Vector2d;
import project.model.util.AnimalFactory;
import project.model.util.AnimalMediator;
import project.model.util.PlantGrower;
import project.model.util.SimulationBuilder;
import project.model.worldelements.Animal;
import project.model.worldelements.Plant;
import project.model.worldelements.WorldElement;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractSimulation implements Simulation {
    protected final WorldMap worldMap;
    protected final int dailyPlantGrowth;
    protected final AnimalFactory animalFactory;
    protected final AnimalMediator animalMediator;
    protected final PlantGrower plantGrower;
    protected final int energyToReproduce;
    protected boolean running = true;
    protected boolean paused = false;
    protected int day = 1;
    protected final List<SimulationListener> simulationListeners = new ArrayList<>();
    protected Optional<Thread> simulationThread = Optional.empty();
    protected int simulationRefreshTime = 500;
    protected final List<Animal> deadAnimals = new ArrayList<>();
    protected final SimulationStatistics statistics;

    public static final int ENERGY_DAILY_LOSS = 1;
    public static final int PARENTS_NEEDED_TO_BREED_COUNT = 2;

    public AbstractSimulation(SimulationBuilder simulationBuilder) {
        this.worldMap = simulationBuilder.getWorldMap();
        this.dailyPlantGrowth = simulationBuilder.getDailyPlantGrowth();
        this.animalFactory = simulationBuilder.getAnimalFactory();
        this.energyToReproduce = simulationBuilder.getEnergyToReproduce();
        this.animalMediator = simulationBuilder.getAnimalMediator();
        this.plantGrower = simulationBuilder.getPlantGrower();

        this.statistics = new SimulationStatistics(this);
    }

    protected void removeDeadAnimals() {
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

    protected Set<Vector2d> getWorldElementsPositions(Collection<? extends WorldElement> worldElements) {
        return worldElements.stream()
                .map(WorldElement::getPosition)
                .collect(Collectors.toSet());
    }

    protected void moveAnimals() {
        this.worldMap.getAnimals()
                .forEach(this.worldMap::move);
    }

    protected void consumePlants() {
        for (Plant plant : worldMap.getPlants()) {
            worldMap.animalsAt(plant.getPosition())
                    .filter(animals -> !animals.isEmpty())
                    .ifPresent(animals -> feedStrongestAnimal(animals, plant));
        }
    }

    protected void feedStrongestAnimal(Set<Animal> animals, Plant plant) {
        List<Animal> winners = animalMediator.resolveAnimalsConflict(animals, 1);

        if (!winners.isEmpty()) {
            Animal winner = winners.getFirst();
            worldMap.feedAnimal(winner, plant);
        }
    }

    protected boolean canBreed(Animal parent1, Animal parent2) {
        return parent1.getStatistics().getEnergy() >= this.energyToReproduce &&
                parent2.getStatistics().getEnergy() >= this.energyToReproduce;
    }

    protected void breedGroup(Set<Animal> groupOfAnimals) {
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

    protected void reproduceAnimals() {
        Set<Vector2d> animalsPositions = this.getWorldElementsPositions(this.worldMap.getAnimals());

        for (var groupOfAnimalsPosition : animalsPositions) {
            Optional<Set<Animal>> groupOfAnimals = this.worldMap.animalsAt(groupOfAnimalsPosition);

            groupOfAnimals.ifPresent(this::breedGroup);
        }
    }

    protected void growPlants() {
        this.plantGrower.growPlants(this.dailyPlantGrowth);
    }

    protected void consumeDailyEnergyAmount() {
        this.worldMap.getAnimals()
                .forEach(animal -> animal.getStatistics().updateEnergy(-ENERGY_DAILY_LOSS));
    }

    protected void updateDaysAlive() {
        this.worldMap.getAnimals()
                .forEach(animal -> animal.getStatistics().incrementDaysAlive());
    }

    protected void finishDay() {
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

    @Override
    public WorldMap getWorldMap() {
        return this.worldMap;
    }

    @Override
    public int getDay() {
        return this.day;
    }

    @Override
    public List<Animal> getDeadAnimals() {
        return this.deadAnimals;
    }

    @Override
    public SimulationStatistics getStatistics() {
        return this.statistics;
    }

    @Override
    public void subscribe(SimulationListener simulationListener) {
        this.simulationListeners.add(simulationListener);
    }

    protected void notifyListeners(SimulationEvent simulationEvent) {
        for (var listener : this.simulationListeners) {
            listener.simulationChanged(simulationEvent);
        }
    }

    // for subclasses use
    @Override
    public void simulationStep() {
        try {
            synchronized (this) {
                while (this.isPaused()) {
                    this.wait();
                }
            }

            Thread.sleep(this.simulationRefreshTime);
        } catch (InterruptedException e) {
            this.simulationThread.ifPresent(Thread::interrupt);
            this.running = false;
            this.paused = false;
        }
    }

    @Override
    public void setSimulationRefreshTime(int ms) {
        this.simulationRefreshTime = ms;
    }

    @Override
    public void start() {
        this.simulationThread = Optional.of(new Thread(this));
        this.simulationThread.ifPresent(Thread::start);
    }

    @Override
    public void terminate() {
        if (this.simulationThread.isEmpty()) {
            throw new IllegalThreadStateException("Thread not Started!!!");
        }

        this.simulationThread.get().interrupt();
        this.running = false;
    }

    @Override
    public synchronized void pause() {
        this.paused = true;
    }

    @Override
    public synchronized void resume() {
        this.paused = false;
        this.notifyAll();
    }

    @Override
    public synchronized boolean isPaused() {
        return this.paused;
    }
}
