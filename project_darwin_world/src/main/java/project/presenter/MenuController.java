package project.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import project.model.simulation.Simulation;
import project.model.simulation.SimulationDayStep;
import project.model.simulation.SimulationEachChangeStep;
import project.model.util.*;

public class MenuController {
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

    private void createAlertWindow(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.show();
    }
}
