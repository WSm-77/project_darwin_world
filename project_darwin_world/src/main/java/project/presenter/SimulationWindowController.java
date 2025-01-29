package project.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import project.model.simulation.SimulationEvent;

public class SimulationWindowController extends AbstractController {
    @FXML
    private SplitPane verticalSplit;
    @FXML
    private SplitPane horizontalSplit;
    @FXML
    private SettingsPaneController settingsPaneController;
    @FXML
    private MapPaneController mapPaneController;
    @FXML
    private ChartPaneController chartPaneController;

    @Override
    public void simulationChanged(SimulationEvent simulationEvent) {

    }

    public SettingsPaneController getSettingsPaneController() {
        return settingsPaneController;
    }

    public MapPaneController getMapPaneController() {
        return mapPaneController;
    }

    public ChartPaneController getChartPaneController() {
        return chartPaneController;
    }
}
