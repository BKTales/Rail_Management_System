package org.dei.Sprint1.Parser;


import org.dei._Facilities.Facility;
import org.dei.Repository.FacilityRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FacilitiesParser {

    private static final String FacilityFile = "src/main/resources/train_dataset_sprint3/Facilities.csv";


    public static void parseFacility(FacilityRepository facilityRepo){

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FacilityFile))) {
            String line = bufferedReader.readLine();

            while((line = bufferedReader.readLine()) != null) {
                String[] atributes = line.split(",");

//                if ( atributes.length < 6){
//                    System.out.println("Line with missing atributes: " + line);
//                    continue;
//                } ??????

                int facilityId = Integer.parseInt(atributes[0]);
                String facilityName = atributes[1];

                Facility facility = new Facility(facilityId, facilityName);
                facilityRepo.addFacility(facility);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
