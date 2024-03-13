package iut.dam.gestionresidence.entities;

public class Notification {
    String title;
    String subtitle;

    public Notification(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
