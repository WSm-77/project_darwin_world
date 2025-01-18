package project.presenter;

import javafx.event.ActionEvent;
import project.model.simulation.SimulationEvent;

public class SettingsPaneController extends AbstractController {
    public void onClick(ActionEvent event) {
        System.out.println("SettingsControllerButton clicked");
    }

    @Override
    public void simulationChanged(SimulationEvent simulationEvent) {

    }
}
