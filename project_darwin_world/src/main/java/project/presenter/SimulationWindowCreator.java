package project.presenter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import project.model.simulation.Simulation;

import java.io.IOException;

public class SimulationWindowCreator {
    public static final String PATH_TO_FXML_CONFIGURATION_FILE = "simulation.fxml";
    public static final String WINDOW_TITLE = "Simulation Window";

    public static void createNewSimulationWindow(Simulation simulation) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(SimulationController.class.getClassLoader()
                .getResource(PATH_TO_FXML_CONFIGURATION_FILE));
        Parent simulationRoot = fxmlLoader.load();

        SimulationController simulationController = fxmlLoader.getController();
        simulationController.initializeSimulation(simulation);

        Stage simulationStage = new Stage();
        simulationStage.setTitle(WINDOW_TITLE);
        simulationStage.setScene(new Scene(simulationRoot));
        simulationStage.show();
    }
}
