package project.model.util;

import com.google.gson.*;
import project.model.simulation.SimulationConfigurationFile;

import java.io.FileReader;
import java.lang.reflect.Type;

public class ConfigLoader {
    public static final String INVALID_CONFIGURATION_FILE_FORMAT = "Invalid configuration file format.";
    public static final String INITIAL_ANIMAL_COUNT = "initialAnimalCount";
    public static final String INITIAL_ANIMAL_ENERGY = "initialAnimalEnergy";
    public static final String ENERGY_TO_REPRODUCE = "energyToReproduce";
    public static final String INITIAL_CHILD_ENERGY = "initialChildEnergy";
    public static final String GENOME_LENGTH = "genomeLength";
    public static final String MIN_MUTATIONS = "minMutations";
    public static final String MAX_MUTATIONS = "maxMutations";
    public static final String INITIAL_PLANT_COUNT = "initialPlantCount";
    public static final String PLANT_NUTRITIOUSNESS = "plantNutritiousness";
    public static final String DAILY_PLANT_GROWTH = "dailyPlantGrowth";
    public static final String MAP_WIDTH = "mapWidth";
    public static final String MAP_HEIGHT = "mapHeight";
    public static final String SIMULATION_REFRESH_TYPE = "simulationRefreshType";
    public static final String MAP_VARIANT = "mapVariant";
    public static final String ANIMAL_VARIANT = "animalVariant";

    public static SimulationConfigurationFile loadConfigFromJson(FileReader reader) throws JsonParseException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SimulationConfigurationFile.class, (JsonDeserializer<SimulationConfigurationFile>)
                ConfigLoader::deserializeJsonFile);

        Gson gson = gsonBuilder.create();

        SimulationConfigurationFile config = gson.fromJson(reader, SimulationConfigurationFile.class);

        return config;
    }

    private static SimulationConfigurationFile deserializeJsonFile(
            JsonElement json, Type typeOfT, com.google.gson.JsonDeserializationContext context) {

        JsonObject obj = json.getAsJsonObject();

        try {
            SimulationConfigurationFile config = new SimulationConfigurationFile(
                    obj.get(INITIAL_ANIMAL_COUNT).getAsDouble(),
                    obj.get(INITIAL_ANIMAL_ENERGY).getAsDouble(),
                    obj.get(ENERGY_TO_REPRODUCE).getAsDouble(),
                    obj.get(INITIAL_CHILD_ENERGY).getAsDouble(),
                    obj.get(GENOME_LENGTH).getAsDouble(),
                    obj.get(MIN_MUTATIONS).getAsDouble(),
                    obj.get(MAX_MUTATIONS).getAsDouble(),
                    obj.get(INITIAL_PLANT_COUNT).getAsDouble(),
                    obj.get(PLANT_NUTRITIOUSNESS).getAsDouble(),
                    obj.get(DAILY_PLANT_GROWTH).getAsDouble(),
                    obj.get(MAP_WIDTH).getAsDouble(),
                    obj.get(MAP_HEIGHT).getAsDouble(),
                    obj.get(SIMULATION_REFRESH_TYPE).getAsBoolean(),
                    PlantGrowerVariant.valueOf(obj.get(MAP_VARIANT).getAsString()),
                    AnimalVariant.valueOf(obj.get(ANIMAL_VARIANT).getAsString())
            );

            if (!isValidRange(config.mapWidth(), 5, 35) ||
                    !isValidRange(config.mapHeight(), 5, 35) ||
                    !isValidRange(config.initialAnimalCount(), 0, 50) ||
                    !isValidRange(config.initialAnimalEnergy(), 0, 50) ||
                    !isValidRange(config.energyToReproduce(), 0, 50) ||
                    !isValidRange(config.initialChildEnergy(), 0, 50) ||
                    !isValidRange(config.genomeLength(), 0, 50) ||
                    !isValidRange(config.minMutations(), 0, 50) ||
                    !isValidRange(config.maxMutations(), 0, 50) ||
                    !isValidRange(config.initialPlantCount(), 0, 50) ||
                    !isValidRange(config.plantNutritiousness(), 0, 50) ||
                    !isValidRange(config.dailyPlantGrowth(), 0, 50)) {
                throw new JsonParseException(INVALID_CONFIGURATION_FILE_FORMAT);
            }

            return config;
        } catch (Exception e) {
            throw new JsonParseException(INVALID_CONFIGURATION_FILE_FORMAT);
        }
    }

    private static boolean isValidRange(double value, double min, double max) {
        return value >= min && value <= max;
    }
}
