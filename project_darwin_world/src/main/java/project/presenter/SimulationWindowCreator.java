package project.presenter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.model.simulation.Simulation;

import java.io.IOException;

//public class SimulationWindowCreator {
//    public static final String PATH_TO_SIMULATION_FXML_CONFIGURATION_FILE = "simulation.fxml";
//    public static final String PATH_TO_SETTINGS_PANE_FXML_CONFIGURATION_FILE = "settingsPane.fxml";
//    public static final String WINDOW_TITLE = "Simulation Window";
//
//    public static void createNewSimulationWindow(Simulation simulation) throws IOException {
//        FXMLLoader simulationFxmlLoader = new FXMLLoader(SimulationController.class.getClassLoader()
//                .getResource(PATH_TO_SIMULATION_FXML_CONFIGURATION_FILE));
//        SplitPane simulationRoot = simulationFxmlLoader.load();
//
//        SimulationController simulationController = simulationFxmlLoader.getController();
//        simulationController.initializeSimulation(simulation);
//
//        Stage simulationStage = new Stage();
//        simulationStage.setTitle(WINDOW_TITLE);
//        simulationStage.setScene(new Scene(simulationRoot));
//        simulationStage.show();
//    }
//}

public class SimulationWindowCreator {
    public static final String PATH_TO_SIMULATION_WINDOW_FXML_CONFIGURATION_FILE = "simulationWindow.fxml";
    public static final String PATH_TO_SETTINGS_PANE_FXML_CONFIGURATION_FILE = "settingsPane.fxml";
    public static final String PATH_TO_MAP_PANE_FXML_CONFIGURATION_FILE = "mapPane.fxml";
    public static final String PATH_TO_CHART_PANE_FXML_CONFIGURATION_FILE = "chartPane.fxml";
    public static final String WINDOW_TITLE = "Simulation Window";

    public static void createNewSimulationWindow(Simulation simulation) throws IOException {
        // WINDOW CONTROLLER
        FXMLLoader simulationWindowFxmlLoader = new FXMLLoader(SimulationWindowCreator.class.getClassLoader()
                .getResource(PATH_TO_SIMULATION_WINDOW_FXML_CONFIGURATION_FILE));
        SplitPane simulationRoot = simulationWindowFxmlLoader.load();

        SimulationWindowController simulationWindowController = simulationWindowFxmlLoader.getController();

        // SETTINGS PANE CONTROLLER
        FXMLLoader settingsPaneFxmlLoader = new FXMLLoader(SettingsPaneController.class.getClassLoader()
                .getResource(PATH_TO_SETTINGS_PANE_FXML_CONFIGURATION_FILE));
        settingsPaneFxmlLoader.load();

        SettingsPaneController settingsPaneController = settingsPaneFxmlLoader.getController();

        // MAP PANE CONTROLLER
        FXMLLoader mapPaneFxmlLoader = new FXMLLoader(MapPaneController.class.getClassLoader()
                .getResource(PATH_TO_MAP_PANE_FXML_CONFIGURATION_FILE));
        mapPaneFxmlLoader.load();

        MapPaneController mapPaneController = mapPaneFxmlLoader.getController();

        // CHART PANE CONTROLLER
        FXMLLoader chartPaneFxmlLoader = new FXMLLoader(ChartPaneController.class.getClassLoader()
                .getResource(PATH_TO_CHART_PANE_FXML_CONFIGURATION_FILE));
        chartPaneFxmlLoader.load();

        ChartPaneController chartPaneController = chartPaneFxmlLoader.getController();

        simulation.subscribe(settingsPaneController);
        simulation.subscribe(mapPaneController);
        simulation.subscribe(chartPaneController);

        Thread simulationThread = new Thread(simulation);

        settingsPaneController.setSimulation(simulation);
        mapPaneController.setSimulation(simulation);
        chartPaneController.setSimulation(simulation);

        settingsPaneController.setSimulationThread(simulationThread);
        mapPaneController.setSimulationThread(simulationThread);
        chartPaneController.setSimulationThread(simulationThread);

        simulationThread.start();

        Stage simulationStage = new Stage();
        simulationStage.setTitle(WINDOW_TITLE);
        simulationStage.setScene(new Scene(simulationRoot));
        simulationStage.show();

        System.out.println("Window created");
    }
}
