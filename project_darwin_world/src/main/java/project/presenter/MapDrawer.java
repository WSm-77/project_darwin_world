package project.presenter;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import project.app.DarwinWorldApp;
import project.model.map.WorldMap;
import project.model.movement.Vector2d;
import project.model.util.AnimalMediator;
import project.model.util.AnimalMediatorStandardVariant;
import project.model.worldelements.Animal;
import project.model.worldelements.Plant;

import java.util.*;

public class MapDrawer {
    private final static String AXIS_DESCRIPTION_STRING = "y/x";
    private final static int MAX_ENERGY_COLOR = 100;
    private final static double MIN_ANIMAL_OPACITY = 0.1;
    private final static Color DEFAULT_HIGHLIGHT_COLOR = new Color(0.8, 0.8, 0.8, 0.7);
    private final static Image GRASS_TILE_IMAGE = new Image(MapDrawer.class.getResource("/images/tiles/grass_tile.jpg").toExternalForm());
    private final static Image DIRT_TILE_IMAGE = new Image(MapDrawer.class.getResource("/images/tiles/dirt_tile.png").toExternalForm());
    private final static Image SINGLE_ANIMAL_IMAGE_1 = new Image(MapDrawer.class.getResource("/images/animals/single_animal1.png").toExternalForm());
    private final static Image SINGLE_ANIMAL_IMAGE_2 = new Image(MapDrawer.class.getResource("/images/animals/single_animal2.png").toExternalForm());
    private final static Image MULTIPLE_ANIMALS_IMAGE = new Image(MapDrawer.class.getResource("/images/animals/multiple_animals.png").toExternalForm());
    private final static List<Image> SINGLE_ANIMALS_IMAGES_LIST = List.of(SINGLE_ANIMAL_IMAGE_1, SINGLE_ANIMAL_IMAGE_2);

    private final Random random = new Random();
    private final GridPane mapGridPane;
    private final WorldMap worldMap;
    private final AnimalMediator animalMediator = new AnimalMediatorStandardVariant();
    private Set<MapDrawerListener> listeners = new HashSet<>();
    private Map<Vector2d, Pane> mapFieldsMap = new HashMap<>();

    public MapDrawer(GridPane mapGridPane, WorldMap worldMap) {
        this.mapGridPane = mapGridPane;
        this.worldMap = worldMap;
    }

    public void drawMap() {
        this.clearGrid();
        this.buildMapGrid();

        this.notifyMapDrawn();
    }

    private void clearGrid() {
        this.mapGridPane.getChildren().clear();
        this.mapGridPane.getColumnConstraints().clear();
        this.mapGridPane.getRowConstraints().clear();
    }

    private void buildMapGrid() {
        var boundaries = this.worldMap.getCurrentBounds();

        var upperLeft = new Vector2d(boundaries.lowerLeft().getX(), boundaries.upperRight().getY());

        int mapRowsCnt = this.worldMap.getHeight();
        int mapColumnsCnt = this.worldMap.getWidth();

        this.setGridCellsSize(mapRowsCnt + 1, mapColumnsCnt + 1);
        this.addRowsAndColumnsHeaders(mapRowsCnt, mapColumnsCnt, upperLeft);
        this.fillGridCells(upperLeft);
    }


    private void setGridCellsSize(int rows, int columns) {
        this.setRowsSize(rows);
        this.setColumnsSize(columns);
    }

    private void setRowsSize(int rows) {
        for (int i = 0; i < rows; i++){
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / rows);
            rowConstraints.setVgrow(Priority.ALWAYS);
            this.mapGridPane.getRowConstraints().add(rowConstraints);
        }
    }

    private void setColumnsSize(int columns) {
        for (int i = 0; i < columns; i++){
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.ALWAYS);
            columnConstraints.setPercentWidth(100.0 / columns);
            this.mapGridPane.getColumnConstraints().add(columnConstraints);
        }
    }

    private void addRowsAndColumnsHeaders(int mapRows, int mapColumns, Vector2d upperLeft) {
        var axisDescription = new Label(AXIS_DESCRIPTION_STRING);
        this.mapGridPane.add(axisDescription, 0, 0);
        GridPane.setHalignment(axisDescription, HPos.CENTER);
        GridPane.setValignment(axisDescription, VPos.CENTER);

        // fill x coordinate header
        for (int row = 0; row < mapRows; row++){
            var field = new Label(String.valueOf(upperLeft.getY() - row));
            this.mapGridPane.add(field, 0, row + 1);
            GridPane.setHalignment(field, HPos.CENTER);
            GridPane.setValignment(field, VPos.CENTER);
        }

        // fill y coordinate header
        for (int column = 0; column < mapColumns; column++){
            var field = new Label(String.valueOf(upperLeft.getX() + column));
            this.mapGridPane.add(field,column + 1, 0);
            GridPane.setHalignment(field, HPos.CENTER);
            GridPane.setValignment(field, VPos.CENTER);
        }
    }

    private void fillGridCells(Vector2d upperLeft) {
        for (int gridRow = 0; gridRow < this.mapGridPane.getRowCount() - 1; gridRow++) {
            for (int gridColumn = 0; gridColumn < this.mapGridPane.getColumnCount() - 1; gridColumn++) {
                var mapPosition = upperLeft.add(new Vector2d(gridColumn, -gridRow));
                RowConstraints rowConstraints = this.mapGridPane.getRowConstraints().get(gridRow + 1);
                ColumnConstraints columnConstraints = this.mapGridPane.getColumnConstraints().get(gridColumn + 1);

                Pane field = this.getFieldPane(mapPosition, rowConstraints, columnConstraints);
                this.mapGridPane.add(field, gridColumn + 1, gridRow + 1);
                this.mapFieldsMap.put(mapPosition, field);

                GridPane.setHalignment(field, HPos.CENTER);
                GridPane.setValignment(field, VPos.CENTER);
            }
        }
    }

    private Pane getFieldPane(Vector2d mapPosition, RowConstraints rowConstraints, ColumnConstraints columnConstraints) {
        StackPane field = new StackPane();

        field.prefHeightProperty().bind(this.mapGridPane.heightProperty().multiply(rowConstraints.percentHeightProperty()).divide(100.0));
        field.prefWidthProperty().bind(this.mapGridPane.widthProperty().multiply(columnConstraints.percentWidthProperty()).divide(100.0));

        Node fieldTile;
        Optional<Plant> plant = this.worldMap.plantAt(mapPosition);

        if (plant.isPresent()) {
            fieldTile = this.getGrassTile(field);
        } else {
            fieldTile = this.getDirtTile(field);
        }

        field.getChildren().add(fieldTile);

        Optional<Set<Animal>> animals = this.worldMap.animalsAt(mapPosition);

        if (animals.isPresent()) {
            Node animalNode = this.getAnimalNode(field, animals.get());
            field.getChildren().add(animalNode);
        }

        field.setOnMouseClicked(event -> this.onMapFieldClicked(mapPosition));

        return field;
    }

    private Node getAnimalNode(Pane parentPane, Set<Animal> animalsSet) {
            return animalsSet.size() > 1 ?
                    this.getMultipleAnimalsNode(parentPane) :
                    this.getSingleAnimalNode(parentPane);
    }

    private Node getMultipleAnimalsNode(Pane parentPane) {
        return this.getAnimalsNodeFromImage(parentPane, MULTIPLE_ANIMALS_IMAGE);
    }

    private Node getSingleAnimalNode(Pane parentPane) {
        return this.getAnimalsNodeFromImage(parentPane, this.getRandomSingleAnimalIMage());
    }

    private Image getRandomSingleAnimalIMage() {
        int idx = this.random.nextInt(SINGLE_ANIMALS_IMAGES_LIST.size());

        return SINGLE_ANIMALS_IMAGES_LIST.get(idx);
    }

    private Node getAnimalsNodeFromImage(Pane parentPane, Image animalsImage) {
        ImageView animalsNode = new ImageView(animalsImage);

        animalsNode.setPreserveRatio(true);

        animalsNode.fitWidthProperty().bind(parentPane.prefWidthProperty());
        animalsNode.fitHeightProperty().bind(parentPane.prefHeightProperty());

        return animalsNode;
    }

    private Node getGrassTile(Pane parentPane) {
        return this.getMapTileFromImage(parentPane, GRASS_TILE_IMAGE);
    }

    private Node getDirtTile(Pane parentPane) {
        return this.getMapTileFromImage(parentPane, DIRT_TILE_IMAGE);
    }

    private Node getMapTileFromImage(Pane parentPane, Image mapTileImage) {
        ImageView mapTile = new ImageView(mapTileImage);

        mapTile.setPreserveRatio(false);

        mapTile.fitWidthProperty().bind(parentPane.prefWidthProperty());
        mapTile.fitHeightProperty().bind(parentPane.prefHeightProperty());

        return mapTile;
    }

    public void highlightPositions(List<Vector2d> mapPositions, Color color) {
        for (var mapPosition : mapPositions) {
            Pane parentPane = this.mapFieldsMap.get(mapPosition);

            Rectangle highlightRectangle = new Rectangle();
            highlightRectangle.setFill(color);

            highlightRectangle.widthProperty().bind(parentPane.widthProperty());
            highlightRectangle.heightProperty().bind(parentPane.heightProperty());

            parentPane.getChildren().add(highlightRectangle);
        }
    }

    public void highlightPositions(List<Vector2d> mapPositions) {
        this.highlightPositions(mapPositions, DEFAULT_HIGHLIGHT_COLOR);
    }

    private void onMapFieldClicked(Vector2d mapPosition) {
        for (var listener : this.listeners) {
            listener.mapFieldClicked(mapPosition);
        }
    }

    private void notifyMapDrawn() {
        for (var listener : this.listeners) {
            listener.mapDrawn();
        }
    }

    public void subscribe(MapDrawerListener mapDrawerListener) {
        this.listeners.add(mapDrawerListener);
    }

    public void unsubscribe(MapDrawerListener mapDrawerListener) {
        this.listeners.remove(mapDrawerListener);
    }
}
