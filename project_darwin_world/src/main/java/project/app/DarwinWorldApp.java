package project.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DarwinWorldApp extends Application {
    public static final String APP_TITLE = "Darwin World";
    public static final String PATH_TO_FXML_CONFIGURATION_FILE = "menu.fxml";

    @Override
    public void start(Stage menuStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(PATH_TO_FXML_CONFIGURATION_FILE));
        AnchorPane viewRoot = loader.load();

        this.configureMenuStage(menuStage, viewRoot);
        menuStage.show();
    }

    private void configureMenuStage(Stage menuStage, AnchorPane viewRoot) {
        Scene scene = new Scene(viewRoot);
        menuStage.setScene(scene);
        menuStage.setTitle(APP_TITLE);
    }
}
