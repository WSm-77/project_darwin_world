package project.model.simulation;

import project.model.util.AnimalFactory;
import project.model.util.SimulationBuilder;
import project.model.map.WorldMap;

public class Simulation implements Runnable {
    private final WorldMap worldMap;
    private final int energyPerPlant;
    private final int dailyPlantGrowth;
    private final AnimalFactory animalFactory;
    private final int energyToReproduce;
    private boolean running = true;
    private int day = 1;

    public static final String INTERRUPTION_MESSAGE_TEMPLATE = "Simulation of map %s interrupted!!!";
    public static final String INTERRUPTION_DURING_SLEEP_MESSAGE_TEMPLATE = "Simulation of map %s interrupted during sleep!!!";
    public static final int SIMULATION_REFRESH_TIME_MS = 500;

    public Simulation(SimulationBuilder simulationBuilder) {
        this.worldMap = simulationBuilder.getWorldMap();
        this.energyPerPlant = simulationBuilder.getEnergyPerPlant();
        this.dailyPlantGrowth = simulationBuilder.getDailyPlantGrowth();
        this.animalFactory = simulationBuilder.getAnimalFactory();
        this.energyToReproduce = simulationBuilder.getEnergyToReproduce();
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
                .forEach(this.worldMap::removeAnimal);
    }

    private void moveAnimals() {
        this.worldMap.getAnimals()
                .forEach(this.worldMap::move);
    }

    private void consumePlants() {

    }

    private void reproduceAnimals() {

    }

    private void growPlants() {

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

            if (Thread.currentThread().isInterrupted()) {
                System.out.println(this.createInterruptionMessage());
                this.running = false;
            }

            try {
                Thread.sleep(Simulation.SIMULATION_REFRESH_TIME_MS);
            } catch (InterruptedException e) {
                System.out.println(this.createInterruptionDuringSleepMessage());
                this.running = false;
            }

            this.day++;
        }
    }
}
