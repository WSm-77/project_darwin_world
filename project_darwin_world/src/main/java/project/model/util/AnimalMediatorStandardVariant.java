package project.model.util;

import project.model.worldelements.Animal;

import java.util.*;
import java.util.stream.Collectors;

public class AnimalMediatorStandardVariant implements AnimalMediator {
    private final Random random = new Random();
    private Map<Animal, Integer> animalsRandomFactorMap = new HashMap<>();

    public void setAnimalsRandomFactorMap(Map<Animal, Integer> animalsRandomFactorMap) {
        this.animalsRandomFactorMap = animalsRandomFactorMap;
    }

    @Override
    public List<Animal> resolveAnimalsConflict(Collection<Animal> animals, int numberOfWinners) {
        Comparator<Animal> compareEnergyDesc = Comparator.comparing((Animal animal) ->
                animal.getStatistics().getEnergy())
                .reversed();
        Comparator<Animal> compareDaysAliveDesc = Comparator.comparing((Animal animal) ->
                animal.getStatistics().getDaysAlive())
                .reversed();
        Comparator<Animal> compareChildrenCountDesc = Comparator.comparing((Animal animal) ->
                animal.getStatistics().getChildrenCount())
                .reversed();
        Comparator<Animal> compareRandomFactorDesc = Comparator.comparing((Animal animal) -> {
                    if (!this.animalsRandomFactorMap.containsKey(animal)) {
                        this.animalsRandomFactorMap.put(animal, this.random.nextInt());
                    }

                    return this.animalsRandomFactorMap.get(animal);
                })
                .reversed();

        return animals.stream()
                .sorted(compareEnergyDesc
                        .thenComparing(compareDaysAliveDesc)
                        .thenComparing(compareChildrenCountDesc)
                        .thenComparing(compareRandomFactorDesc))
                .limit(numberOfWinners)
                .collect(Collectors.toList());
    }
}
