package project.model.util;

import project.model.worldelements.Animal;
import project.model.worldelements.Genome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenomeFactory {
    private final Random random = new Random();

    public Genome createRandomGenome(int length) {
        List<Integer> genes = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            genes.add(random.nextInt(8));
        }
        return new Genome(genes, 0);
    }

    public Genome createFromParents(Animal parent1, Animal parent2) {
        Genome genome1 = parent1.getGenome();
        Genome genome2 = parent2.getGenome();

        List<Integer> genes1 = genome1.getGenome();
        List<Integer> genes2 = genome2.getGenome();

        if (genes1.size() != genes2.size()) {
            throw new IllegalArgumentException("The parents' genomes must be the same length!");
        }

        int energy1 = parent1.getEnergy();
        int energy2 = parent2.getEnergy();

        int length = genes1.size();
        List<Integer> childGenes = new ArrayList<>();

        double totalEnergy = energy1 + energy2;
        int splitPoint1 = (int) Math.round((energy1 / totalEnergy) * length);
        int splitPoint2 = length - splitPoint1;

        boolean strongerParentRightSide = random.nextBoolean();

        if (strongerParentRightSide) {
            childGenes.addAll(genes1.subList(0, splitPoint1));
            childGenes.addAll(genes2.subList(splitPoint1, length));
        } else {
            childGenes.addAll(genes2.subList(0, splitPoint2));
            childGenes.addAll(genes1.subList(splitPoint2, length));
        }
        
        int mutations = random.nextInt(length / 2 + 1);
        for (int i = 0; i < mutations; i++) {
            int indexToMutate = random.nextInt(length);
            childGenes.set(indexToMutate, random.nextInt(8));
        }

        return new Genome(childGenes, 0);
    }
}
