package org.dei.Sprint1.Parser;

import org.dei._Facilities.Facility;
import org.dei._RailLineNetwork.RailLine;
import org.dei._RailLineNetwork.RailSegment;
import org.dei.Repository.FacilityRepository;
import org.dei.Repository.LinesRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class LinesParser {

    private static final String SegmentsFile = "src/main/resources/train_dataset_sprint3/Segments.csv";
    private static final String LinesFile = "src/main/resources/train_dataset_sprint3/Lines.csv";
    private static final int SIDING_COLUMN = 8;

    LinesRepository lineRepo = LinesRepository.getInstance();
    static FacilityRepository flRepo = FacilityRepository.getInstance();


    public static void parseLine(LinesRepository linesRepo) {

        parseSegments(linesRepo);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(LinesFile))) {
            String line = bufferedReader.readLine();
            ArrayList<RailSegment> railSegments = linesRepo.getAllRailSegments();
            ArrayList<RailSegment> railSegmentsToAdd = new ArrayList<>();

            while((line = bufferedReader.readLine()) != null) {
                String[] atributes = line.split(",");

//                if ( atributes.length < 6){
//                    System.out.println("Line with missing atributes: " + line);
//                    continue;
//                } ??????
                String lineId = atributes[0];
                String startFacilityName = atributes[4];
                String endFacilityName = atributes[6];

                Facility startFacility = flRepo.getFacilityByName(startFacilityName);
                Facility endFacility = flRepo.getFacilityByName(endFacilityName);

                for (RailSegment railSegment : railSegments) {
                    if(Objects.equals(railSegment.getSegmentId(), lineId)){
                        railSegmentsToAdd.add(railSegment);
                    }
                }

                RailLine railLine = new RailLine(lineId, startFacility, endFacility, railSegmentsToAdd);
                linesRepo.addRailLines(railLine);

                startFacility.getConnections().put(endFacility, railLine);
                endFacility.getConnections().put(startFacility, railLine);

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void parseSegments(LinesRepository linesRepo){

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(SegmentsFile))) {
            String line = bufferedReader.readLine();
            int sidingSize;
            int sidingPosition;

            while((line = bufferedReader.readLine()) != null) {
                String[] atributes = line.split(",");


//                if ( atributes.length < 6){
//                    System.out.println("Line with missing atributes: " + line);
//                    continue;
//                } ??????

                String segmentId = atributes[1]; // TODO: This might be an issue in the future - BKTales
                double length = Double.parseDouble(atributes[5]);
                int numberOfTracks = Integer.parseInt(atributes[6]);
                boolean isEletrified = atributes[3].equals("Yes");
                double maxWeight = Double.parseDouble(atributes[4]);
                if (atributes.length < SIDING_COLUMN) {
                    sidingSize = -1;
                    sidingPosition = -1;
                }else{
                    sidingSize = Integer.parseInt(atributes[8]);
                    sidingPosition = Integer.parseInt(atributes[7]);
                }

                RailSegment railSegment = new RailSegment(segmentId, length,0, numberOfTracks, isEletrified, maxWeight, sidingSize, sidingPosition);
                linesRepo.addRailSegments(railSegment);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    }
