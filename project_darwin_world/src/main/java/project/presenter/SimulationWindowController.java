package project.presenter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import project.model.simulation.Simulation;
import project.model.simulation.SimulationEvent;

import java.io.IOException;

public class SimulationWindowController extends AbstractController {
    @FXML
    private SplitPane verticalSplit;
    @FXML
    private SplitPane horizontalSplit;

    @Override
    public void simulationChanged(SimulationEvent simulationEvent) {

    }
}
