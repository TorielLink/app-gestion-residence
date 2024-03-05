package iut.dam.gestionresidence.entities;

public class Country {
    int flagResourceId;
    String name;

    public Country(int flagResourceId, String name) {
        this.flagResourceId = flagResourceId;
        this.name = name;
    }

    public int getFlagImage() {
        return flagResourceId;
    }

    public String getName(){
        return name;
    }
}
