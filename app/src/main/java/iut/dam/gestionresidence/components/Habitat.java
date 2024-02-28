package iut.dam.gestionresidence.components;

import java.util.ArrayList;
import java.util.List;

public class Habitat {

    int id;
    String residentName;
    int floor;
    double area;

    public String getResidentName() {
        return residentName;
    }

    public int getFloor() {
        return floor;
    }

    public List<Appliance> getAppliances() {
        return appliances;
    }

    List<Appliance> appliances;

    public Habitat(int id, String residentName, int floor, double area, ArrayList<Appliance> appliances) {
        this.id = id;
        this.residentName = residentName;
        this.floor = floor;
        this.area = area;

        this.appliances = appliances;
    }
}
