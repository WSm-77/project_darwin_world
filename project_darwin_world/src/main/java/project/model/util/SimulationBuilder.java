package project.model.util;

import project.model.map.Sphere;
import project.model.map.WorldMap;
import project.model.simulation.Simulation;
import project.model.simulation.SimulationDayStep;
import project.model.simulation.SimulationEachChangeStep;
import project.model.worldelements.Animal;

public class SimulationBuilder {
    private static final String BUILDER_NOT_READY_MESSAGE = "All parameters must be set before building the Simulation.";
    private static final String INVALID_CHILD_INITIAL_ENERGY_TO_ENERGY_TO_REPRODUCE_RATIO_MESSAGE =
            "Child initial energy must be less or equal to 2 * energy needed to reproduce!!!";
    private Integer mapWidth;
    private Integer mapHeight;
    private Integer initialPlantCount;
    private Integer energyPerPlant;
    private Integer dailyPlantGrowth;
    private Integer initialAnimalCount;
    private Integer initialAnimalEnergy;
    private Integer energyToReproduce;
    private Integer childInitialEnergy;
    private Integer minMutations;
    private Integer maxMutations;
    private Integer genomeLength;
    private AnimalConstructor<? extends Animal> animalConstructor;
    private AnimalMediatorConstructor<? extends AnimalMediator> animalMediatorConstructor;
    private PlantGrowerConstructor<? extends PlantGrower> plantGrowerConstructor;
    private SimulationConstructor<? extends Simulation> simulationConstructor;

    private WorldMap worldMap;
    private AnimalFactory animalFactory;
    private PlantGrower plantGrower;
    private AnimalMediator animalMediator;

    public SimulationBuilder setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
        return this;
    }

    public SimulationBuilder setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
        return this;
    }

    public SimulationBuilder setInitialPlantCount(int initialPlantCount) {
        this.initialPlantCount = initialPlantCount;
        return this;
    }

    public SimulationBuilder setEnergyPerPlant(int energyPerPlant) {
        this.energyPerPlant = energyPerPlant;
        return this;
    }

    public SimulationBuilder setDailyPlantGrowth(int dailyPlantGrowth) {
        this.dailyPlantGrowth = dailyPlantGrowth;
        return this;
    }

    public SimulationBuilder setInitialAnimalCount(int initialAnimalCount) {
        this.initialAnimalCount = initialAnimalCount;
        return this;
    }

    public SimulationBuilder setInitialAnimalEnergy(int initialAnimalEnergy) {
        this.initialAnimalEnergy = initialAnimalEnergy;
        return this;
    }

    public SimulationBuilder setEnergyToReproduce(int energyToReproduce) {
        this.energyToReproduce = energyToReproduce;
        return this;
    }

    public SimulationBuilder setChildInitialEnergy(int childInitialEnergy) {
        this.childInitialEnergy = childInitialEnergy;
        return this;
    }

    public SimulationBuilder setMinMutations(int minMutations) {
        this.minMutations = minMutations;
        return this;
    }

    public SimulationBuilder setMaxMutations(int maxMutations) {
        this.maxMutations = maxMutations;
        return this;
    }

    public SimulationBuilder setGenomeLength(int genomeLength) {
        this.genomeLength = genomeLength;
        return this;
    }

    public SimulationBuilder setAnimalConstructor(AnimalConstructor<? extends Animal> animalConstructor) {
        this.animalConstructor = animalConstructor;
        return this;
    }

    public SimulationBuilder setAnimalMediatorConstructor(AnimalMediatorConstructor<? extends AnimalMediator> animalMediatorConstructor) {
        this.animalMediatorConstructor = animalMediatorConstructor;
        return this;
    }

    public SimulationBuilder setPlantGrowerConstructor(PlantGrowerConstructor<? extends PlantGrower> plantGrowerConstructor) {
        this.plantGrowerConstructor = plantGrowerConstructor;
        return this;
    }

    public SimulationBuilder setSimulationConstructor(SimulationConstructor<? extends Simulation> simulationConstructor) {
        this.simulationConstructor = simulationConstructor;
        return this;
    }

    private void validateParameters() {
        if (this.mapWidth == null || this.mapHeight == null  ||
                this.initialPlantCount == null || this.energyPerPlant == null || this.dailyPlantGrowth == null ||
                this.initialAnimalCount == null || this.initialAnimalEnergy == null ||
                this.energyToReproduce == null || this.childInitialEnergy == null || this.minMutations == null ||
                this.maxMutations == null || this.genomeLength == null || this.animalConstructor == null ||
                this.animalMediatorConstructor == null || this.plantGrowerConstructor == null ||
                this.simulationConstructor == null) {
            throw new IllegalStateException(SimulationBuilder.BUILDER_NOT_READY_MESSAGE);
        }

        if (this.childInitialEnergy > 2 * this.energyToReproduce) {
            throw new IllegalArgumentException(INVALID_CHILD_INITIAL_ENERGY_TO_ENERGY_TO_REPRODUCE_RATIO_MESSAGE);
        }
    }

    private void createAndPlaceAnimalsOnMap() {
        for (int i = 0; i < this.initialAnimalCount; i++) {
            Animal randomAnimal = this.animalFactory.createRandomAnimal();
            this.worldMap.place(randomAnimal);
        }
    }

    private void growPlants() {
        this.plantGrower.growPlants(this.initialPlantCount);
    }

    public Simulation build() {
        validateParameters();

        GenomeFactory genomeFactory = new GenomeFactory(this.genomeLength, this.minMutations, this.maxMutations);

        Sphere sphere = new Sphere(this.mapWidth, this.mapHeight);
        PlantGrower plantGrower = this.plantGrowerConstructor.construct(sphere, this.energyPerPlant);
        AnimalMediator animalMediatorVariant = this.animalMediatorConstructor.construct();

        MapChangeListener consoleLog = new ConsoleMapDisplay();
        sphere.subscribe(consoleLog);

        this.worldMap = sphere;
        this.animalFactory = new AnimalFactory(
                genomeFactory,
                this.initialAnimalEnergy,
                this.childInitialEnergy,
                this.worldMap.getCurrentBounds(),
                this.animalConstructor
        );
        this.animalMediator = animalMediatorVariant;
        this.plantGrower = plantGrower;

        this.createAndPlaceAnimalsOnMap();
        this.growPlants();

        return this.simulationConstructor.construct(this);
    }

    public int getDailyPlantGrowth() { return this.dailyPlantGrowth; }
    public int getEnergyToReproduce() { return this.energyToReproduce; }
    public WorldMap getWorldMap() { return this.worldMap; }
    public AnimalFactory getAnimalFactory() { return this.animalFactory; }
    public AnimalMediator getAnimalMediator() { return this.animalMediator; }
    public PlantGrower getPlantGrower() { return this.plantGrower; }
}
