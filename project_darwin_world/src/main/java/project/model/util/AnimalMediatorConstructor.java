package project.model.util;

@FunctionalInterface
public interface AnimalMediatorConstructor<AnimalMediatorType extends AnimalMediator> {
    public AnimalMediatorType construct();
}
