package project.presenter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import project.model.simulation.Simulation;

import java.io.IOException;

public class SimulationWindowCreator {
    private static final String PATH_TO_SIMULATION_WINDOW_FXML_CONFIGURATION_FILE = "simulationWindow.fxml";
    private static final String WINDOW_TITLE = "Simulation Window";

    public static void createNewSimulationWindow(Simulation simulation) throws IOException {
        FXMLLoader simulationWindowFxmlLoader = new FXMLLoader(
                SimulationWindowCreator.class.getClassLoader().getResource(PATH_TO_SIMULATION_WINDOW_FXML_CONFIGURATION_FILE)
        );

        SplitPane simulationRoot = simulationWindowFxmlLoader.load();
        SimulationWindowController simulationWindowController = simulationWindowFxmlLoader.getController();

        initializeChildControllers(simulationWindowController, simulation);

        Stage simulationStage = new Stage();

        simulationStage.setOnCloseRequest((WindowEvent event) -> simulation.terminate());
        simulationStage.setTitle(WINDOW_TITLE);
        simulationStage.setScene(new Scene(simulationRoot));
        simulationStage.show();
    }

    private static void initializeChildControllers(SimulationWindowController simulationWindowController, Simulation simulation) {
        SettingsPaneController settingsPaneController = simulationWindowController.getSettingsPaneController();
        MapPaneController mapPaneController = simulationWindowController.getMapPaneController();
        ChartPaneController chartPaneController = simulationWindowController.getChartPaneController();

        MapDrawer mapDrawer = new MapDrawer(mapPaneController.getMapGridPane(), simulation.getWorldMap());

        simulation.subscribe(settingsPaneController);
        simulation.subscribe(mapPaneController);
        simulation.subscribe(chartPaneController);

        settingsPaneController.setSimulation(simulation);
        mapPaneController.setSimulation(simulation);
        chartPaneController.setSimulation(simulation);

        mapPaneController.setMapDrawer(mapDrawer);
        settingsPaneController.setMapDrawer(mapDrawer);

        simulation.start();
    }
}
