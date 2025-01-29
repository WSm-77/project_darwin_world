package project.model.util;

import project.model.worldelements.Animal;

import java.util.Collection;
import java.util.List;

public interface AnimalMediator {
    /**
     * @param animals collections of animals which compete
     * @param numberOfWinners number of winning animals
     * @return list of conflict winners of size <b>numberOfAnimals</b> or <b>less</b>
     */
    List<Animal> resolveAnimalsConflict(Collection<Animal> animals, int numberOfWinners);
}
