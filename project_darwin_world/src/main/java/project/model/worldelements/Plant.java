package project.model.worldelements;

public interface Plant extends WorldElement {

    /**
     * Return plant nutritiousness
     *
     * @return number of stored energy
     */
    int getNutritiousness();
}