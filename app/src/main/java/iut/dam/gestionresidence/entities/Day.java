package iut.dam.gestionresidence.entities;

import java.util.List;

public class Day {
    private final List<Slot> slots;

    public Day(List<Slot> slots) {
        this.slots = slots;
    }

    public List<Slot> getSlots() {
        return slots;
    }
}
