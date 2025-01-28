package project.presenter;

import com.google.gson.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.stage.FileChooser;
import project.model.simulation.*;
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
    public static final String CONFIGURATION_SAVED_SUCCESSFULLY_TO = "Configuration saved successfully to:\n";
    public static final String CSV_FILES = "CSV Files";
    public static final String CSV = "*.csv";
    public static final String SAVE_SIMULATION_STATISTICS = "Save Simulation Statistics";
    public static final String SIMULATION_STATS_CSV = "simulation_stats.csv";
    public static final String FILE_NOT_SELECTED_STATISTICS_WILL_NOT_BE_SAVED = "File not selected. Statistics will not be saved.";

    @FXML
    private CheckBox simulationRefreshTypeCheckbox;
    @FXML
    private CheckBox saveStatisticsCheckbox;
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

        saveStatisticsCheckbox.setSelected(false);
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

            if (saveStatisticsCheckbox.isSelected()) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle(SAVE_SIMULATION_STATISTICS);
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(CSV_FILES, CSV));

                fileChooser.setInitialFileName(SIMULATION_STATS_CSV);

                File file = fileChooser.showSaveDialog(null);

                if (file != null) {
                    SimulationStatistics statistics = new SimulationStatistics(sphereSimulation);
                    CSVStatisticsSaver csvSaver = new CSVStatisticsSaver(statistics, file.getAbsolutePath());
                    sphereSimulation.subscribe(csvSaver);
                } else {
                    createAlertWindow(FILE_NOT_SELECTED_STATISTICS_WILL_NOT_BE_SAVED);
                }
            }

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
                    saveStatisticsCheckbox.isSelected(),
                    mapVariantChoiceBox.getValue(),
                    animalVariantChoiceBox.getValue()
                );

                gson.toJson(config, writer);
                createAlertWindowInfo(CONFIGURATION_SAVED_SUCCESSFULLY_TO + file.getAbsolutePath());
            } catch (IOException e) {
                createAlertWindow(FAILED_TO_SAVE_CONFIGURATION + e.getMessage());
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

                SimulationConfigurationFile config = ConfigLoader.loadConfigFromJson(reader);

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
                saveStatisticsCheckbox.setSelected(config.csvSaver());
                mapVariantChoiceBox.setValue(config.mapVariant());
                animalVariantChoiceBox.setValue(config.animalVariant());

                createAlertWindowInfo(CONFIGURATION_LOADED_SUCCESSFULLY_FROM + file.getAbsolutePath());
            } catch (IOException | JsonParseException e) {
                createAlertWindow(INVALID_CONFIGURATION_FILE_THE_FILE_DOES_NOT_MATCH_THE_REQUIRED_FORMAT);
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
