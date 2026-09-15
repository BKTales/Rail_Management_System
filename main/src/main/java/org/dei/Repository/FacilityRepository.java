package org.dei.Repository;

import org.dei._Facilities.Facility;

import java.util.ArrayList;

public class FacilityRepository {

    private static FacilityRepository instance = null;
    private ArrayList<Facility> facilities;


    public static FacilityRepository getInstance() {
        if (instance == null) {
            instance = new FacilityRepository();
        }
        return instance;
    }

    private FacilityRepository() {
        facilities = new ArrayList<>();
    }

    /*
    Facility ----
    */

    public void addFacility(Facility facility){
        facilities.add(facility);
    }

    public void removeFacility(Facility facility){
        facilities.remove(facility);
    }

    public Facility getFacilityByIndex(int index){
        return facilities.get(index);
    }

    public Facility getFacility(int facilityId) {
        for (Facility f : facilities) {
            if (f.getId() == facilityId) {
                return f;
            }
        }
        // Se não encontrar, devolve null em vez de dar erro
        return null;
    }

    public Facility getFacilityByName(String name){
        for (Facility facility : facilities) {
            if(facility.getName().equals(name)){
                return facility;
            }
        }
        return null;
    }

    public String[] listFacility(){
        StringBuilder s = new StringBuilder();
        int i = 0;

        if (facilities.isEmpty())
            return (null);
        for (Facility facility : facilities) {
            s.append("[" + i + "] - Facility" + i + "\n");
            i++;
        }
        return (s.toString().split("\n"));
    }

    public ArrayList<Facility> getFacilities(){
        return facilities;
    }


}
