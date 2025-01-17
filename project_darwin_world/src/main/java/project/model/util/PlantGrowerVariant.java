package project.model.util;

public enum PlantGrowerVariant {
    DEFAULT,
    CREEPING_JUNGLE;

    public PlantGrowerConstructor<? extends PlantGrower> toPlantGrowerConstructor() {
        return switch (this) {
            case DEFAULT -> PlantGrowerStandardVariant::new;
            case CREEPING_JUNGLE -> PlantGrowerCreepingJungleVariant::new;
        };
    }
}
