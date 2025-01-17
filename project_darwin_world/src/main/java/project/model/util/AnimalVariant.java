package project.model.util;

import project.model.worldelements.Animal;
import project.model.worldelements.AnimalAgingVariant;
import project.model.worldelements.AnimalStandardVariant;

public enum AnimalVariant {
    DEFAULT,
    AGING_ANIMALS;

    public AnimalConstructor<? extends Animal> toAnimalConstructor() {
        return switch (this) {
            case DEFAULT -> AnimalStandardVariant::new;
            case AGING_ANIMALS -> AnimalAgingVariant::new;
        };
    }
}
