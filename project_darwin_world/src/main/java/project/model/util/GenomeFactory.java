package project.model.util;

import project.model.worldelements.Animal;
import project.model.worldelements.Genome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenomeFactory {
    private final static String PARENTS_DIFFERENT_GENOME_SIZE_MESSAGE = "The parents' genomes must be the same length!";
    private final Random random = new Random();
    private final int genomeLength;

    private int getRandomGene() {
        return random.nextInt(Genome.GENE_MAX_VALUE - Genome.GENE_MIN_VALUE + 1) + Genome.GENE_MIN_VALUE;
    }

    public GenomeFactory(int genomeLength) {
        this.genomeLength = genomeLength;
    }

    public Genome createRandomGenome() {
        List<Integer> genes = new ArrayList<>();
        int activeGeneIdx = random.nextInt(this.genomeLength);

        for (int i = 0; i < this.genomeLength; i++) {
            genes.add(this.getRandomGene());
        }

        return new Genome(genes, activeGeneIdx);
    }

    public Genome createFromParents(Animal parent1, Animal parent2) {
        Animal strongerParent = parent1;
        Animal weakerParent = parent2;

        // Ensure strongerParent is indeed the animal with higher energy
        if (strongerParent.getEnergy() < weakerParent.getEnergy()) {
            Animal temp = strongerParent;
            strongerParent = weakerParent;
            weakerParent = temp;
        }

        Genome strongerParentGenome = strongerParent.getGenome();
        Genome weakerParentGenome = weakerParent.getGenome();

        List<Integer> strongerParentGenes = strongerParentGenome.getGenome();
        List<Integer> weakerParentGenes = weakerParentGenome.getGenome();

        if (strongerParentGenes.size() != weakerParentGenes.size()) {
            throw new IllegalArgumentException(GenomeFactory.PARENTS_DIFFERENT_GENOME_SIZE_MESSAGE);
        }

        int strongerParentEnergy = strongerParent.getEnergy();
        int weakerParentEnergy = weakerParent.getEnergy();

        int length = strongerParentGenes.size();
        List<Integer> childGenes = new ArrayList<>();

        double totalEnergy = strongerParentEnergy + weakerParentEnergy;
        int strongerParentSplitPoint = (int) Math.round((strongerParentEnergy / totalEnergy) * length);
        int weakerParentSplitPoint = length - strongerParentSplitPoint;

        boolean strongerParentRightSide = random.nextBoolean();

        if (strongerParentRightSide) {
            childGenes.addAll(strongerParentGenes.subList(0, strongerParentSplitPoint));
            childGenes.addAll(weakerParentGenes.subList(strongerParentSplitPoint, length));
        } else {
            childGenes.addAll(weakerParentGenes.subList(0, weakerParentSplitPoint));
            childGenes.addAll(strongerParentGenes.subList(weakerParentSplitPoint, length));
        }

        int mutations = random.nextInt(length);

        for (int i = 0; i < mutations; i++) {
            int indexToMutate = random.nextInt(length);
            childGenes.set(indexToMutate, this.getRandomGene());
        }

        int activeGeneIdx = random.nextInt(length);

        return new Genome(childGenes, activeGeneIdx);
    }
}
