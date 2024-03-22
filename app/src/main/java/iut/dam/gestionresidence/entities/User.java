package iut.dam.gestionresidence.entities;

public class User {
    private int id;
    private final String name;

    private Habitat habitat;

    public User(String name) {
        this.name = name;
    }

    public void setHabitat(Habitat habitat) {
        this.habitat = habitat;
    }

    public String getName() {
        return name;
    }
}
