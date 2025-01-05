package project.model.worldelements;

import java.util.List;

public class Genome {
    final private List<Integer> genome;
    private int activeGeneIdx;

    public Genome(List<Integer> genome, int activeGeneIdx) {
        this.genome = genome;
        this.activeGeneIdx = activeGeneIdx;
    }

    public int next() {
        int gene = this.genome.get(this.activeGeneIdx);
        this.activeGeneIdx = (this.activeGeneIdx + 1) % this.genome.size();

        return gene;
    }

    public int getActiveGeneIdx() {
        return activeGeneIdx;
    }

    public List<Integer> getGenome() {
        return genome;
    }

    @Override
    public String toString() {
        return "Genome{" +
                "genome=" + genome +
                ", activeGeneIdx=" + activeGeneIdx +
                '}';
    }
}
