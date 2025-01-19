package project.presenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.*;
import project.model.simulation.SimulationEvent;

public class MapPaneController extends AbstractController {
    @FXML
    private GridPane mapGridPane;
    @FXML
    private BorderPane parentBorderPane;

    @FXML
    public void initialize() {
        this.mapGridPane.prefHeightProperty().bind(this.parentBorderPane.heightProperty());
        this.mapGridPane.prefWidthProperty().bind(this.parentBorderPane.widthProperty());
    }

    public void drawMap() {
        MapDrawer mapDrawer = new MapDrawer(this.mapGridPane, this.simulation.getWorldMap());
        mapDrawer.drawMap();
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
}
