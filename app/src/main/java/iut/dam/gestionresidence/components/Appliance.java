package iut.dam.gestionresidence.components;

public class Appliance {
    public int getId() {
        return id;
    }

    int id;
    String name;
    String reference;
    int wattage;

    public Appliance(int id, String name, String reference, int wattage) {
        this.id = id;
        this.name = name;
        this.reference = reference;
        this.wattage = wattage;
    }
}
