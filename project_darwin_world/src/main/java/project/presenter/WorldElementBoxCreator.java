package project.presenter;

import javafx.scene.image.Image;
import project.model.worldelements.WorldElement;

import java.util.HashMap;
import java.util.Map;

public class WorldElementBoxCreator {
    private static final Map<String, Image> pathToImageMap = new HashMap<>();

    public static Image getWorldElementImage(WorldElement worldElement) {
        String pathToResource = worldElement.getResourcePath();

        if (!pathToImageMap.containsKey(pathToResource)) {
            String resourceStr = WorldElementBoxCreator.class.getResource(pathToResource).toExternalForm();
            Image worldElementImage = new Image(resourceStr);

            pathToImageMap.put(pathToResource, worldElementImage);
        }

        return pathToImageMap.get(pathToResource);
    }
}
