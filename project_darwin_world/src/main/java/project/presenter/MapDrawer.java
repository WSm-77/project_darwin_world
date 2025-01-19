package project.presenter;

import javafx.beans.binding.Bindings;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import project.model.map.WorldMap;
import project.model.movement.Vector2d;
import project.model.util.AnimalMediator;
import project.model.util.AnimalMediatorStandardVariant;
import project.model.worldelements.Animal;
import project.model.worldelements.Plant;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class MapDrawer {
    private final static String AXIS_DESCRIPTION_STRING = "y/x";
    private final static int MAX_ENERGY_COLOR = 100;
    private final static double MIN_ANIMAL_OPACITY = 0.1;
    private final GridPane mapGridPane;
    private final WorldMap worldMap;
    private final AnimalMediator animalMediator = new AnimalMediatorStandardVariant();

    public MapDrawer(GridPane mapGridPane, WorldMap worldMap) {
        this.mapGridPane = mapGridPane;
        this.worldMap = worldMap;
    }

    public void drawMap() {
        this.clearGrid();
        this.buildMapGrid();
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
            rowConstraints.setPrefHeight(100.0 / rows);
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

                Node field = this.getFieldNode(mapPosition, rowConstraints, columnConstraints);
                this.mapGridPane.add(field, gridColumn + 1, gridRow + 1);

                GridPane.setHalignment(field, HPos.CENTER);
                GridPane.setValignment(field, VPos.CENTER);
            }
        }
    }

    private Node getFieldNode(Vector2d mapPosition, RowConstraints rowConstraints, ColumnConstraints columnConstraints) {
        StackPane field = new StackPane();

        Optional<Plant> plant = this.worldMap.plantAt(mapPosition);
        if (plant.isPresent()) {
            field.getChildren().add(this.getGrassNode(field));
        }

        Optional<Set<Animal>> animals = this.worldMap.animalsAt(mapPosition);
        if (animals.isPresent()) {
            List<Animal> strongestAnimalList = this.animalMediator.resolveAnimalsConflict(animals.get(), 1);
            Node animalNode = this.getAnimalNode(field, strongestAnimalList.getFirst());
            field.getChildren().add(animalNode);
        }

        field.prefHeightProperty().bind(rowConstraints.prefHeightProperty());
        field.prefWidthProperty().bind(columnConstraints.prefWidthProperty());

        return field;
    }

    private Node getAnimalNode(Pane parentPane, Animal animal) {
        double animalEnergy = Math.min(animal.getStatistics().getEnergy(), MAX_ENERGY_COLOR);
        double colorRate = animalEnergy / MAX_ENERGY_COLOR;
        colorRate = Math.max(colorRate, MIN_ANIMAL_OPACITY);
        var color = new Color(0.8, 0.0, 0.0, colorRate);

        Circle animalCircle = new Circle();
        animalCircle.setFill(color);

        animalCircle.radiusProperty()
                .bind(Bindings.min(
                        parentPane.widthProperty(),
                        parentPane.heightProperty())
                .divide(2));

        return animalCircle;
    }

    private Node getGrassNode(Pane parentPane) {
        Rectangle grassRectangle = new Rectangle();
        grassRectangle.setFill(new Color(0, 0.8, 0.05, 0.5));

        grassRectangle.widthProperty().bind(parentPane.widthProperty());
        grassRectangle.heightProperty().bind(parentPane.heightProperty());

        return grassRectangle;
    }
}
