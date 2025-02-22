package project.presenter;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import project.model.map.WorldMap;
import project.model.movement.Vector2d;
import project.model.util.AnimalMediator;
import project.model.util.AnimalMediatorStandardVariant;
import project.model.worldelements.Animal;
import project.model.worldelements.Plant;

import java.util.*;

public class MapDrawer {
    private final static String AXIS_DESCRIPTION_STRING = "y/x";
    private final static int MAX_ENERGY_FOR_HEALTH_BAR = 100;
    private final static Color DEFAULT_HIGHLIGHT_COLOR = new Color(0.8, 0.8, 0.8, 0.7);
    private final static Image DIRT_TILE_IMAGE = new Image(MapDrawer.class.getResource("/images/tiles/dirt_tile.png").toExternalForm());
    private final static Image MULTIPLE_ANIMALS_IMAGE = new Image(MapDrawer.class.getResource("/images/animals/multiple_animals.png").toExternalForm());

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

        Optional<Plant> optionalPlant = this.worldMap.plantAt(mapPosition);

        Node fieldTile = optionalPlant.map(plant -> this.getGrassTile(field, plant))
                .orElseGet(() -> this.getDirtTile(field));

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
                    this.getMultipleAnimalsNode(parentPane, animalsSet) :
                    this.getSingleAnimalNode(parentPane, animalsSet.iterator().next());
    }

    private Node getMultipleAnimalsNode(Pane parentPane, Set<Animal> animalSet) {
        List<Animal> animals = this.animalMediator.resolveAnimalsConflict(animalSet, 1);
        return this.getAnimalsNodeFromImage(parentPane, MULTIPLE_ANIMALS_IMAGE, animals.getFirst());
    }

    private Node getSingleAnimalNode(Pane parentPane, Animal animal) {
        return this.getAnimalsNodeFromImage(parentPane, WorldElementBoxCreator.getWorldElementImage(animal), animal);
    }

    private Node getAnimalsNodeFromImage(Pane parentPane, Image animalsImage, Animal animal) {
        ImageView animalsNode = new ImageView(animalsImage);

        animalsNode.setPreserveRatio(true);

        ProgressBar healthBar = new ProgressBar();
        double healthPercentage = Math.min(1.0, (double) animal.getStatistics().getEnergy() / MAX_ENERGY_FOR_HEALTH_BAR);
        healthBar.setProgress(healthPercentage);

        healthBar.prefWidthProperty().bind(parentPane.prefWidthProperty().multiply(0.9));
        healthBar.prefHeightProperty().bind(parentPane.prefHeightProperty().multiply(0.18));
        healthBar.setStyle("-fx-accent: red; -fx-border-width: 0px;");

        BorderPane animalContainer = new BorderPane();

        animalContainer.prefWidthProperty().bind(parentPane.prefWidthProperty());
        animalContainer.prefHeightProperty().bind(parentPane.prefHeightProperty());

        animalsNode.fitWidthProperty().bind(animalContainer.prefWidthProperty());
        animalsNode.fitHeightProperty().bind(animalContainer.prefHeightProperty().multiply(0.8));

        animalContainer.setCenter(animalsNode);
        animalContainer.setBottom(healthBar);

        BorderPane.setAlignment(healthBar, Pos.CENTER);
        BorderPane.setAlignment(animalsNode, Pos.CENTER);

        return animalContainer;
    }

    private Node getGrassTile(Pane parentPane, Plant plant) {
        return this.getMapTileFromImage(parentPane, WorldElementBoxCreator.getWorldElementImage(plant));
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
