package project;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    public static final int NO_THREADS_IN_THREAD_POOL = 4;
    public static final int THREAD_POOL_TERMINATION_TIMEOUT_S = 10;
    public static final String TIMEOUT_REACHED_MESSAGE = "Timeout reached - stopping all active threads...";
    public static final String SHUTDOWN_FAILURE_MESSAGE = "Shutdown failed!!!";
    private final List<Simulation> simulationList;
    private final List<Thread> simulationThreadList = new ArrayList<>();
    private final ExecutorService simulationsThreadPool = Executors.newFixedThreadPool(SimulationEngine.NO_THREADS_IN_THREAD_POOL);

    public SimulationEngine(List<Simulation> simulationList) {
        this.simulationList = simulationList;
    }

    public void runSync() {
        for (var simulation : this.simulationList) {
            simulation.run();
        }
    }

    public void runAsync() {
        for (var simulation : this.simulationList) {
            var simulationThread = new Thread(simulation);
            this.simulationThreadList.add(simulationThread);
            simulationThread.start();
        }
    }

    public void awaitSimulationsEnd() throws InterruptedException {
        for (var simulationThread : this.simulationThreadList) {
            simulationThread.join();
        }

        // check if thread pool was used to run tasks
        if (!this.simulationsThreadPool.isShutdown())
            return;

        if (!this.simulationsThreadPool.awaitTermination(
                SimulationEngine.THREAD_POOL_TERMINATION_TIMEOUT_S, TimeUnit.SECONDS)) {
            System.out.println(SimulationEngine.TIMEOUT_REACHED_MESSAGE);
            this.simulationsThreadPool.shutdownNow();

            // check if shutdown was successful
            if (!this.simulationsThreadPool.awaitTermination(SimulationEngine.THREAD_POOL_TERMINATION_TIMEOUT_S, TimeUnit.SECONDS)) {
                System.out.println(SimulationEngine.SHUTDOWN_FAILURE_MESSAGE);
            }
        }
    }

    public void runAsyncInThreadPool() {
        for (var simulation : this.simulationList) {
            this.simulationsThreadPool.submit(simulation);
        }

        this.simulationsThreadPool.shutdown();
    }
}
