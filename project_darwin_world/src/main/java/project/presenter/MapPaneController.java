package project.presenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.*;
import project.model.simulation.Simulation;
import project.model.simulation.SimulationEvent;

public class MapPaneController extends AbstractController {
    @FXML
    private GridPane mapGridPane;
    @FXML
    private BorderPane parentBorderPane;

    private MapDrawer mapDrawer;

    @FXML
    public void initialize() {
        this.mapGridPane.prefHeightProperty().bind(this.parentBorderPane.heightProperty());
        this.mapGridPane.prefWidthProperty().bind(this.parentBorderPane.widthProperty());
    }

    public void drawMap() {
        this.mapDrawer.drawMap();
    }

    public void mapChange() {
        this.drawMap();
    }

    @Override
    public void simulationChanged(SimulationEvent simulationEvent) {
        switch (simulationEvent) {
            case MAP_CHANGED -> Platform.runLater(this::mapChange);
        }
    }

    @Override
    public void setSimulation(Simulation simulation) {
        super.setSimulation(simulation);
    }

    public void setMapDrawer(MapDrawer mapDrawer) {
        this.mapDrawer = mapDrawer;
    }

    public GridPane getMapGridPane() {
        return this.mapGridPane;
    }
}
