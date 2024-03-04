package iut.dam.gestionresidence.entities;

public class Appliance {
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
