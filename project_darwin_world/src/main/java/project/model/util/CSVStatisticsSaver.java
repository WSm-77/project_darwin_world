package project.model.util;

import project.model.simulation.SimulationEvent;
import project.model.simulation.SimulationListener;
import project.model.simulation.SimulationStatistics;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVStatisticsSaver implements SimulationListener {
    public static final String DAY_ANIMALS_COUNT_PLANTS_COUNT_AVERAGE_ENERGY_AVERAGE_LIFETIME_AVERAGE_CHILDREN_MOST_POPULAR_GENOME_EMPTY_SPACES = "Day;Animals Count;Plants Count;Average Energy;Average lifetime;Average Children;Most Popular Genome;Empty Spaces\n";
    public static final String ERROR_INITIALIZING_CSV_FILE = "Error initializing CSV file: ";
    public static final String DELIMITER = ";";
    public static final String LINE_BREAK = "\n";
    public static final String ERROR_WRITING_TO_CSV_FILE = "Error writing to CSV file: ";
    private final SimulationStatistics statistics;
    private final String filePath;

    public CSVStatisticsSaver(SimulationStatistics statistics, String filePath) {
        this.statistics = statistics;
        this.filePath = filePath;
        initializeCSV();
    }

    private void initializeCSV() {
        try (FileWriter writer = new FileWriter(filePath, false)) {
            writer.append(DAY_ANIMALS_COUNT_PLANTS_COUNT_AVERAGE_ENERGY_AVERAGE_LIFETIME_AVERAGE_CHILDREN_MOST_POPULAR_GENOME_EMPTY_SPACES);
        } catch (IOException e) {
            System.err.println(ERROR_INITIALIZING_CSV_FILE + e.getMessage());
        }
    }

    @Override
    public void simulationChanged(SimulationEvent event) {
        if (event == SimulationEvent.NEXT_DAY) {
            saveStatistics();
        }
    }

    private void saveStatistics() {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            List<String> stats = statistics.getCurrentStatistics();
            writer.append(String.join(DELIMITER, stats)).append(LINE_BREAK);
        } catch (IOException e) {
            System.err.println(ERROR_WRITING_TO_CSV_FILE + e.getMessage());
        }
    }
}
