package project.presenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import project.model.map.WorldMap;
import project.model.simulation.Simulation;
import project.model.simulation.SimulationEvent;
import project.model.simulation.SimulationListener;

public class MapPaneController extends AbstractController {
    @FXML
    private Button testButton;
    @FXML
    private Label label2;

    @FXML
    private Label mapLabel;

    @FXML
    public void initialize() {
        this.label2.setText("Ala ma kota");
    }

    public void mapChange(WorldMap map) {
        this.label2.setText(map.toString());
    }

    public void onClick() {
        Platform.runLater(() -> {
            System.out.println("Map button clicked");
            this.mapLabel.setText(this.simulation.getWorldMap().toString());
        });
//        this.mapLabel.setText("Hello")
    }

    @Override
    public void simulationChanged(SimulationEvent simulationEvent) {
//        Platform.runLater(() -> this.mapLabel.setText(this.simulation.getWorldMap().toString()));
        Platform.runLater(() -> {
            System.out.println("Platform task executed!!!");
//            this.mapChange(this.simulation.getWorldMap());
            this.mapChange(this.simulation.getWorldMap());
        });
//        this.mapLabel.setText(this.simulation.getWorldMap().toString());
//        this.label2.setText("Test");
//        System.out.println("Simulation changed mapController");
        System.out.println(this.simulation.getWorldMap().toString());
    }
}
