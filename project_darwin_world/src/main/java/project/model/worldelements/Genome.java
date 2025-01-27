package project.model.worldelements;

import java.util.List;
import java.util.stream.Collectors;

public class Genome {
    public static final int GENE_MIN_VALUE = 0;
    public static final int GENE_MAX_VALUE = 7;
    public static final String INVALID_GENE_VALUE_MESSAGE_TEMPLATE = "Invalid gene value: %d; gene value must have " +
            "value in range: %d - %d";
    public static final String HIGHLIGHT_ACTIVE_IDX_TEMPLATE = "<%s>";
    final private List<Integer> genome;
    private int activeGeneIdx;

    public Genome(List<Integer> genome, int activeGeneIdx) {
        for (int gene : genome) {
            if (gene < Genome.GENE_MIN_VALUE || gene > Genome.GENE_MAX_VALUE){
                throw new IllegalArgumentException(String.format(Genome.INVALID_GENE_VALUE_MESSAGE_TEMPLATE, gene,
                        Genome.GENE_MIN_VALUE, Genome.GENE_MAX_VALUE));
            }
        }

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
        if (this.activeGeneIdx < 0 || this.activeGeneIdx >= this.genome.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + this.activeGeneIdx);
        }

        List<String> highlightedActiveGenomeList = this.genome.stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        highlightedActiveGenomeList.set(this.activeGeneIdx,
                String.format(HIGHLIGHT_ACTIVE_IDX_TEMPLATE, this.genome.get(this.activeGeneIdx)));

        return highlightedActiveGenomeList.toString();
    }
}
