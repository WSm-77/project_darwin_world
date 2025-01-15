package project.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import project.model.util.AnimalMediatorStandardVariant;
import project.model.util.PlantGrowerCreepingJungleVariant;
import project.model.util.SimulationBuilder;
import project.model.worldelements.AnimalStandardVariant;


public class MenuController {
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

    public void onStartButtonClick(ActionEvent actionEvent) {
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
                .setAnimalConstructor(AnimalStandardVariant::new)
                .setAnimalMediatorConstructor(AnimalMediatorStandardVariant::new)
                .setPlantGrowerConstructor(PlantGrowerCreepingJungleVariant::new)
                .build();

        var thread = new Thread(sphereSimulation);
        System.out.println("Simulation started");

        thread.start();

        try {
            thread.join(3000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        thread.interrupt();

        // stop
        System.out.println("Simulation stopped");
    }
}
