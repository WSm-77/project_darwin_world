package project.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import project.model.util.*;

public class MenuController {
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
                    .build();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader()
                    .getResource(SimulationController.PATH_TO_FXML_CONFIGURATION_FILE));
            Parent simulationRoot = fxmlLoader.load();

            SimulationController simulationController = fxmlLoader.getController();
            simulationController.setSimulation(sphereSimulation);

            Stage simulationStage = new Stage();
            simulationStage.setTitle("Simulation Window");
            simulationStage.setScene(new Scene(simulationRoot));
            simulationStage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
