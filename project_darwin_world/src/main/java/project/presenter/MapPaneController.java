package project.presenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import project.model.movement.Vector2d;
import project.model.simulation.SimulationEvent;
import project.model.worldelements.WorldElement;

import java.util.Optional;

public class MapPaneController extends AbstractController {
    @FXML
    public void initialize() {
        this.mapGridPane.prefHeightProperty().bind(this.parentBorderPane.heightProperty());
        this.mapGridPane.prefWidthProperty().bind(this.parentBorderPane.widthProperty());
    }

    public void mapChange() {
        this.drawMap();
        System.out.println("smth");
    }

    @Override
    public void simulationChanged(SimulationEvent simulationEvent) {
        Platform.runLater(() -> {
            System.out.println("Platform task executed!!!");
            this.mapChange();
        });
        System.out.println(this.simulation.getWorldMap().toString());
    }

    private final static String AXIS_DESCRIPTION_STRING = "y/x";

    @FXML
    private GridPane mapGridPane;
    @FXML
    private BorderPane parentBorderPane;

    public void drawMap() {
        System.out.println(this.mapGridPane.toString());
        System.out.println(this.mapGridPane.getChildren().toString());
        this.clearGrid();

        System.out.println(String.format("grid width: %.2f, grid height: %.2f", this.mapGridPane.getWidth(), this.mapGridPane.getHeight()));

        this.buildMapGrid();
        System.out.println(String.format("grid width: %.2f, grid height: %.2f", this.mapGridPane.getWidth(), this.mapGridPane.getHeight()));
        System.out.println(this.mapGridPane.getColumnCount());
    }

    private void clearGrid() {
        this.mapGridPane.getColumnConstraints().clear();
        this.mapGridPane.getRowConstraints().clear();

        System.out.println(this.mapGridPane.getChildren());
        System.out.println(this.mapGridPane.getColumnConstraints());
        System.out.println(this.mapGridPane.getRowConstraints());
    }

    private void buildMapGrid() {
        var boundaries = this.simulation.getWorldMap().getCurrentBounds();

        var upperLeft = new Vector2d(boundaries.lowerLeft().getX(), boundaries.upperRight().getY());

        int mapRowsCnt = this.simulation.getWorldMap().getHeight();
        int mapColumnsCnt = this.simulation.getWorldMap().getWidth();

        this.setGridCellsSize(mapRowsCnt + 1, mapColumnsCnt + 1);
        this.addRowsAndColumnsHeaders(mapRowsCnt, mapColumnsCnt, upperLeft);
//        this.fillGridCells(upperLeft);
    }


    private void setGridCellsSize(int rows, int columns) {
        this.setRowsSize(rows);
        this.setColumnsSize(columns);
    }

    private void setRowsSize(int rows) {
        double maxHeight = this.parentBorderPane.getHeight();
        double cellHeight = maxHeight / rows;

        for (int i = 0; i < rows; i++){
            RowConstraints rowConstraints = new RowConstraints();
//            rowConstraints.setPercentHeight((double) (1 / rows) * 100);
            rowConstraints.setPrefHeight(cellHeight);
            rowConstraints.setVgrow(Priority.ALWAYS);
            this.mapGridPane.getRowConstraints().add(rowConstraints);
        }
    }

    private void setColumnsSize(int columns) {
        double maxWidth = this.parentBorderPane.getWidth();
        double cellWidth = maxWidth / columns;

        for (int i = 0; i < columns; i++){
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.ALWAYS);
//            columnConstraints.setPercentWidth((double) (1 / columns) * 100);
            columnConstraints.setPrefWidth(cellWidth);
            this.mapGridPane.getColumnConstraints().add(columnConstraints);
        }

//        ColumnConstraints lastColumnConstraint = new ColumnConstraints(-1);
//        lastColumnConstraint.setHgrow(Priority.ALWAYS);
//        this.mapGridPane.getColumnConstraints().add(lastColumnConstraint);
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
        for (int gridRow = 0; gridRow < this.mapGridPane.getRowCount(); gridRow++) {
            for (int gridColumn = 0; gridColumn < this.mapGridPane.getColumnCount(); gridColumn++) {
                var mapPosition = upperLeft.add(new Vector2d(gridColumn, -gridRow));
                Label field = new Label("Empty");

                Optional<WorldElement> optionalWorldElement = this.simulation.getWorldMap().objectAt(mapPosition);
                if (optionalWorldElement.isPresent()) {

                    field.setText(optionalWorldElement.get().toString());

                    GridPane.setHalignment(field, HPos.CENTER);
                    GridPane.setValignment(field, VPos.CENTER);
                }

                this.mapGridPane.add(field, gridColumn + 1, gridRow + 1, 1, 1);
            }
        }
    }
}
