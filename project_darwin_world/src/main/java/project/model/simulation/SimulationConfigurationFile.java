package project.model.simulation;

import project.model.util.AnimalVariant;
import project.model.util.PlantGrowerVariant;

public class SimulationConfigurationFile {
    public double initialAnimalCount;
    public double initialAnimalEnergy;
    public double energyToReproduce;
    public double initialChildEnergy;
    public double genomeLength;
    public double minMutations;
    public double maxMutations;
    public double initialPlantCount;
    public double plantNutritiousness;
    public double dailyPlantGrowth;
    public double mapWidth;
    public double mapHeight;
    public boolean simulationRefreshType;
    public PlantGrowerVariant mapVariant;
    public AnimalVariant animalVariant;
}
