package iut.dam.gestionresidence.entities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Habitat {
    int id;
    User resident;
    int floor;
    double area;

    public Habitat(int id, User resident, int floor, double area, List<Appliance> appliances) {
        this(id, resident, floor, area);
        this.appliances = appliances;
    }

    public List<Appliance> getAppliances() {
        return appliances;
    }

    List<Appliance> appliances;

    public Habitat(int id, User resident, int floor, double area) {
        this.id = id;
        this.resident = resident;
        this.floor = floor;
        this.area = area;
        this.appliances = new ArrayList<>();
    }

    public void addAppliances(String appliances) {
        for (char appliance : appliances.toCharArray()) {
            switch (appliance) {
                case 'w' :
                    this.appliances.add(new Appliance(1, "Machine à laver", "Samsung Y456", 800));
                    break;
                case 'v' :
                    this.appliances.add(new Appliance(2, "Aspirateur", "Philips X123", 600));
                    break;
                case 'a' :
                    this.appliances.add(new Appliance(3, "Climatiseur", "Marque XYZ", 1500));
                    break;
                case 'i' :
                    this.appliances.add(new Appliance(4, "Fer à repasser", "Marque ABC", 1000));
                    break;
            }
        }
    }

    public String getName() {
        return this.resident.getName();
    }

    public String getFloor() {
        return String.valueOf(this.floor);
    }

    public static Habitat getFromJson(String json){
        Gson gson = new Gson();
        Habitat obj = gson.fromJson(json, Habitat.class);
        return obj;
    }
    public static List<Habitat> getListFromJson(String json){
        Gson gson = new Gson();
        Type type = new TypeToken<List<Habitat>>(){}.getType();
        List<Habitat> list = gson.fromJson(json, type);
        return list;
    }
}
