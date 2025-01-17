package project.presenter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.model.simulation.Simulation;

import java.io.IOException;

public class SimulationController {
    public static final String APP_TITLE = "Darwin World";
    public static final String PATH_TO_FXML_CONFIGURATION_FILE = "simulation.fxml";

    public static void openNewSimulationWindow(Simulation simulation) throws IOException {
        Stage simulationWindow = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SimulationController.class.getClassLoader().getResource(PATH_TO_FXML_CONFIGURATION_FILE));
        AnchorPane viewRoot = loader.load();

        configureMenuStage(simulationWindow, viewRoot);
        simulationWindow.show();
    }

    private static void configureMenuStage(Stage menuStage, AnchorPane viewRoot) {
        Scene scene = new Scene(viewRoot);
        menuStage.setScene(scene);
        menuStage.setTitle(APP_TITLE);
    }
}
