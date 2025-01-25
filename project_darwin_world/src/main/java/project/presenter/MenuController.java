package project.presenter;

import com.google.gson.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.stage.FileChooser;
import project.model.simulation.SimulationConfigurationFile;
import project.model.simulation.Simulation;
import project.model.simulation.SimulationDayStep;
import project.model.simulation.SimulationEachChangeStep;
import project.model.util.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import project.model.util.AnimalVariant;
import project.model.util.PlantGrowerVariant;

import java.lang.reflect.Type;

public class MenuController {
    public static final String LOAD_CONFIGURATION = "Load Configuration";
    public static final String JSON_FILES = "JSON Files";
    public static final String JSON = "*.json";
    public static final String SAVE_CONFIGURATION = "Save Configuration";
    public static final String FAILED_TO_SAVE_CONFIGURATION = "Failed to save configuration: ";
    public static final String INVALID_CONFIGURATION_FILE_THE_FILE_DOES_NOT_MATCH_THE_REQUIRED_FORMAT = "Invalid configuration file: The file does not match the required format.";
    public static final String CONFIGURATION_LOADED_SUCCESSFULLY_FROM = "Configuration loaded successfully from:\n";
    public static final String INVALID_CONFIGURATION_FILE_FORMAT = "Invalid configuration file format.";
    public static final String CONFIGURATION_SAVED_SUCCESSFULLY_TO = "Configuration saved successfully to:\n";
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
    @FXML
    private CheckBox simulationRefreshTypeCheckbox;
    @FXML
    private ChoiceBox<PlantGrowerVariant> mapVariantChoiceBox;
    @FXML
    private ChoiceBox<AnimalVariant> animalVariantChoiceBox;
    @FXML
    private Slider initialAnimalCountSlider;
    @FXML
    private Slider initialAnimalEnergySlider;
    @FXML
    private Slider energyToReproduceSlider;
    @FXML
    private Slider initialChildEnergySlider;
    @FXML
    private Slider genomeLengthSlider;
    @FXML
    private Slider minMutationsSlider;
    @FXML
    private Slider maxMutationsSlider;
    @FXML
    private Slider initialPlantCountSlider;
    @FXML
    private Slider plantNutritiousnessSlider;
    @FXML
    private Slider dailyPlantGrowthSlider;
    @FXML
    private Slider mapWidthSlider;
    @FXML
    private Slider mapHeightSlider;

    @FXML
    public void initialize() {
        this.mapVariantChoiceBox.getItems().addAll(PlantGrowerVariant.values());
        this.animalVariantChoiceBox.getItems().addAll(AnimalVariant.values());

        this.mapVariantChoiceBox.setValue(PlantGrowerVariant.DEFAULT);
        this.animalVariantChoiceBox.setValue(AnimalVariant.DEFAULT);
    }

    public void onStartButtonClick(ActionEvent actionEvent) {
        try {
            SimulationConstructor<? extends Simulation> simulationType = simulationRefreshTypeCheckbox.isSelected() ?
                    SimulationEachChangeStep::new : SimulationDayStep::new;

            var sphereSimulation = new SimulationBuilder()
                    .setMapWidth((int)this.mapWidthSlider.getValue())
                    .setMapHeight((int)this.mapHeightSlider.getValue())
                    .setInitialPlantCount((int)this.initialPlantCountSlider.getValue())
                    .setEnergyPerPlant((int)this.plantNutritiousnessSlider.getValue())
                    .setDailyPlantGrowth((int)this.dailyPlantGrowthSlider.getValue())
                    .setInitialAnimalCount((int)this.initialAnimalCountSlider.getValue())
                    .setInitialAnimalEnergy((int)this.initialAnimalEnergySlider.getValue())
                    .setEnergyToReproduce((int)this.energyToReproduceSlider.getValue())
                    .setChildInitialEnergy((int)this.initialChildEnergySlider.getValue())
                    .setMinMutations((int)this.minMutationsSlider.getValue())
                    .setMaxMutations((int)this.maxMutationsSlider.getValue())
                    .setGenomeLength((int)this.genomeLengthSlider.getValue())
                    .setAnimalConstructor(this.animalVariantChoiceBox.getValue().toAnimalConstructor())
                    .setAnimalMediatorConstructor(AnimalMediatorStandardVariant::new)
                    .setPlantGrowerConstructor(this.mapVariantChoiceBox.getValue().toPlantGrowerConstructor())
                    .setSimulationConstructor(simulationType)
                    .build();

            SimulationWindowCreator.createNewSimulationWindow(sphereSimulation);
        } catch (Exception e) {
            this.createAlertWindow(e.getMessage());
        }
    }

    @FXML
    public void onSaveConfigurationClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(SAVE_CONFIGURATION);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(JSON_FILES, JSON));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                Gson gson = new Gson();
                SimulationConfigurationFile config = new SimulationConfigurationFile(
                initialAnimalCountSlider.getValue(),
                initialAnimalEnergySlider.getValue(),
                energyToReproduceSlider.getValue(),
                initialChildEnergySlider.getValue(),
                genomeLengthSlider.getValue(),
                minMutationsSlider.getValue(),
                maxMutationsSlider.getValue(),
                initialPlantCountSlider.getValue(),
                plantNutritiousnessSlider.getValue(),
                dailyPlantGrowthSlider.getValue(),
                mapWidthSlider.getValue(),
                mapHeightSlider.getValue(),
                simulationRefreshTypeCheckbox.isSelected(),
                mapVariantChoiceBox.getValue(),
                animalVariantChoiceBox.getValue()
                );

                gson.toJson(config, writer);
                createAlertWindowInfo(CONFIGURATION_SAVED_SUCCESSFULLY_TO + file.getAbsolutePath());
            } catch (IOException e) {
                createAlertWindowInfo(FAILED_TO_SAVE_CONFIGURATION + e.getMessage());
            }
        }
    }

    @FXML
    public void onLoadConfigurationClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(LOAD_CONFIGURATION);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(JSON_FILES, JSON));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try (FileReader reader = new FileReader(file)) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(SimulationConfigurationFile.class, (JsonDeserializer<SimulationConfigurationFile>)
                                (JsonElement json, Type typeOfT, com.google.gson.JsonDeserializationContext context) -> {
                                    JsonObject obj = json.getAsJsonObject();
                                    try {
                                        return new SimulationConfigurationFile(
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
                                    } catch (Exception e) {
                                        throw new JsonParseException(INVALID_CONFIGURATION_FILE_FORMAT);
                                    }
                                }).create();

                SimulationConfigurationFile config = gson.fromJson(reader, SimulationConfigurationFile.class);

                initialAnimalCountSlider.setValue(config.initialAnimalCount());
                initialAnimalEnergySlider.setValue(config.initialAnimalEnergy());
                energyToReproduceSlider.setValue(config.energyToReproduce());
                initialChildEnergySlider.setValue(config.initialChildEnergy());
                genomeLengthSlider.setValue(config.genomeLength());
                minMutationsSlider.setValue(config.minMutations());
                maxMutationsSlider.setValue(config.maxMutations());
                initialPlantCountSlider.setValue(config.initialPlantCount());
                plantNutritiousnessSlider.setValue(config.plantNutritiousness());
                dailyPlantGrowthSlider.setValue(config.dailyPlantGrowth());
                mapWidthSlider.setValue(config.mapWidth());
                mapHeightSlider.setValue(config.mapHeight());
                simulationRefreshTypeCheckbox.setSelected(config.simulationRefreshType());
                mapVariantChoiceBox.setValue(config.mapVariant());
                animalVariantChoiceBox.setValue(config.animalVariant());

                createAlertWindowInfo(CONFIGURATION_LOADED_SUCCESSFULLY_FROM + file.getAbsolutePath());
            } catch (IOException | JsonParseException e) {
                createAlertWindowInfo(INVALID_CONFIGURATION_FILE_THE_FILE_DOES_NOT_MATCH_THE_REQUIRED_FORMAT);
            }
        }
    }

    private void createAlertWindow(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.show();
    }

    private void createAlertWindowInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
}
