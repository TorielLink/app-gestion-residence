package iut.dam.gestionresidence.entities;

public class User {
    private int id;
    private String name;

    public void setHabitat(Habitat habitat) {
        this.habitat = habitat;
    }

    private Habitat habitat;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
