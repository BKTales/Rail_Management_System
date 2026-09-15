package org.dei.Sprint2;

import java.io.*;

import org.dei._Location.GeographicalLocation;
import org.dei._Facilities.Station.Station;
import org.dei.Sprint2.Trees.AVL;
import org.dei.Sprint2.Trees.NodeData;

public class Parser64K {
    //add/remove the first main/ if needed
    private static final String euStationFile = "src/main/resources/train_station_database_sprint2/train_stations_europe.csv";

    public static AVL parseEUStations(){
        AVL avl = new AVL();
        try (InputStream is = Parser64K.class.getClassLoader()
                .getResourceAsStream("train_station_database_sprint2/train_stations_europe.csv")) {

            if (is == null) {
                throw new RuntimeException("EU station file not found in resources!");
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                String[] atributes = line.split(",");
                if (atributes.length < 10) {
                    continue;//Missing atributes
                }


                TimeZone timeZone = new TimeZone(atributes[1] + atributes[2]);
                TimeZoneGroup timeZoneGroup = TimeZoneGroup.fromString(atributes[3]);
                if (timeZoneGroup == null) {
                    continue;
                }
                String name = atributes[4];

                if(name == null || name.isEmpty() || atributes[0] == null || atributes[0].isEmpty()){
                    continue;
                }

                Country country = new Country(atributes[0]);

                GeographicalLocation location = new GeographicalLocation();
                if(!parseCheckLatLong(atributes[5], atributes[6], location)) continue;


                boolean isCity;
                if (atributes[7].trim().equals("True")) {
                    isCity = true;
                } else {
                    isCity = false;
                }

                boolean is_main_station;
                if (atributes[8].trim().equals("True")) {
                    is_main_station = true;
                } else {
                    is_main_station = false;
                }

                boolean is_airport;
                if (atributes[9].trim().equals("True")) {
                    is_airport = true;
                } else {
                    is_airport = false;
                }

                Station station = new Station(location, timeZoneGroup, timeZone, country, name, 1, isCity, is_airport, is_main_station );

                NodeData node = avl.findNodeByCoordinates(location);
                if (node != null) {
                    node.addStation(station);
                } else {
                    avl.insert(new NodeData(location, station));
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + euStationFile);
        } catch (Exception e) {
            System.err.println("Error parsing file: " + euStationFile);
        }
        return avl;
    }


    public static AVL parseTestStations(String stationFilename){
        AVL avl = new AVL();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(stationFilename))){
            String line = bufferedReader.readLine();
            int i = 1;
            while ((line = bufferedReader.readLine()) != null) {
                String[] atributes = line.split(",");
                if (atributes.length < 10) {
                    continue;//Missing atributes
                }


                TimeZone timeZone = new TimeZone(atributes[1] + atributes[2]);
                TimeZoneGroup timeZoneGroup = TimeZoneGroup.fromString(atributes[3]);
                if (timeZoneGroup == null) {
                    continue;
                }
                String name = atributes[4];

                if(name == null || name.isEmpty() || atributes[0] == null || atributes[0].isEmpty()){
                    continue;
                }

                Country country = new Country(atributes[0]);

                GeographicalLocation location = new GeographicalLocation();
                if(!parseCheckLatLong(atributes[5], atributes[6], location)) continue;

                boolean isCity;
                if (atributes[7].trim().equals("True")) {
                    isCity = true;
                } else {
                    isCity = false;
                }

                boolean is_main_station;
                if (atributes[8].trim().equals("True")) {
                    is_main_station = true;
                } else {
                    is_main_station = false;
                }

                boolean is_airport;
                if (atributes[9].trim().equals("True")) {
                    is_airport = true;
                } else {
                    is_airport = false;
                }

                Station station = new Station(location, timeZoneGroup, timeZone, country, name, 1, isCity, is_airport, is_main_station );

                NodeData node = avl.findNodeByCoordinates(location);
                if (node != null) {
                    node.addStation(station);
                } else {
                    avl.insert(new NodeData(location, station));
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + euStationFile);
        } catch (Exception e) {
            System.err.println("Error parsing file: " + euStationFile);
        }
        return avl;
    }


    private static Boolean parseCheckLatLong(String lineLat, String lineLong, GeographicalLocation location) {
        Boolean result = false;
        if(!(lineLat.trim().isEmpty())){
            double latitude = Double.parseDouble(lineLat);

            if (latitude < -90.0 || latitude > 90.0) {
                return result;
            }
            location.setLatitude(latitude);
        }
        else {
            return result;
        }

        if(!(lineLong.trim().isEmpty())){
            double longitude = Double.parseDouble(lineLong);
            if (longitude < -180.0 || longitude > 180.0) {
                return result;
            }
            location.setLongitude(longitude);
            result = true;
        }
        else {
            return result;
        }
        return result;
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
