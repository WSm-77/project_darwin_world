package project.presenter;

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
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MenuController {
    public static final String CONFIGURATION_LOADED_SUCCESSFULLY_FROM = "Configuration loaded successfully from: ";
    public static final String LOAD_CONFIGURATION = "Load Configuration";
    public static final String JSON_FILES = "JSON Files";
    public static final String JSON = "*.json";
    public static final String CONFIGURATION_SAVED_SUCCESSFULLY_TO = "Configuration saved successfully to: ";
    public static final String SAVE_CONFIGURATION = "Save Configuration";
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
                SimulationConfigurationFile config = new SimulationConfigurationFile();
                config.initialAnimalCount = initialAnimalCountSlider.getValue();
                config.initialAnimalEnergy = initialAnimalEnergySlider.getValue();
                config.energyToReproduce = energyToReproduceSlider.getValue();
                config.initialChildEnergy = initialChildEnergySlider.getValue();
                config.genomeLength = genomeLengthSlider.getValue();
                config.minMutations = minMutationsSlider.getValue();
                config.maxMutations = maxMutationsSlider.getValue();
                config.initialPlantCount = initialPlantCountSlider.getValue();
                config.plantNutritiousness = plantNutritiousnessSlider.getValue();
                config.dailyPlantGrowth = dailyPlantGrowthSlider.getValue();
                config.mapWidth = mapWidthSlider.getValue();
                config.mapHeight = mapHeightSlider.getValue();
                config.simulationRefreshType = simulationRefreshTypeCheckbox.isSelected();

                gson.toJson(config, writer);
                System.out.println(CONFIGURATION_SAVED_SUCCESSFULLY_TO + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
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
                Gson gson = new Gson();
                SimulationConfigurationFile config = gson.fromJson(reader, SimulationConfigurationFile.class);

                initialAnimalCountSlider.setValue(config.initialAnimalCount);
                initialAnimalEnergySlider.setValue(config.initialAnimalEnergy);
                energyToReproduceSlider.setValue(config.energyToReproduce);
                initialChildEnergySlider.setValue(config.initialChildEnergy);
                genomeLengthSlider.setValue(config.genomeLength);
                minMutationsSlider.setValue(config.minMutations);
                maxMutationsSlider.setValue(config.maxMutations);
                initialPlantCountSlider.setValue(config.initialPlantCount);
                plantNutritiousnessSlider.setValue(config.plantNutritiousness);
                dailyPlantGrowthSlider.setValue(config.dailyPlantGrowth);
                mapWidthSlider.setValue(config.mapWidth);
                mapHeightSlider.setValue(config.mapHeight);
                simulationRefreshTypeCheckbox.setSelected(config.simulationRefreshType);

                System.out.println(CONFIGURATION_LOADED_SUCCESSFULLY_FROM + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createAlertWindow(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.show();
    }
}
