package iut.dam.gestionresidence.entities;

public class Slot {
    private final int consumptionLevel; // Level of consummation (0-100)

    public Slot(int consumptionLevel) {
        this.consumptionLevel = consumptionLevel;
    }

    public int getConsumptionLevel() {
        return consumptionLevel;
    }
}
