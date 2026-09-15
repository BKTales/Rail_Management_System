package org.dei.Sprint1.Parser;

import org.dei.Repository.TrainRepository;
import org.dei._Train.Locomotive;
import org.dei._Train.LocomotiveModel;
import org.dei._Train.LocomotiveType;
import org.dei._Train.Wagon;
import org.dei._Train.WagonModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TrainParser {

    public static void parseLocomotives(TrainRepository repository) {
        String fileName = "train_dataset_sprint3/Locomotive.csv";

        try (InputStream inputStream = TrainParser.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                System.err.println("File not found in classpath: " + fileName);
                return;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                boolean firstLine = true;

                while ((line = br.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false;
                        continue;
                    }

                    String[] data = parseCSVLine(line);
                    if (data.length >= 12) {
                        String number = data[0].trim();
                        String name = data[1].trim();
                        String make = data[2].trim();
                        String modelName = data[3].trim();
                        int serviceYear = Integer.parseInt(data[4].trim());
                        int numberBogies = Integer.parseInt(data[5].trim());
                        double power = Double.parseDouble(data[7].trim());
                        double length = Double.parseDouble(data[8].trim());
                        double width = Double.parseDouble(data[9].trim());
                        double height = Double.parseDouble(data[10].trim());
                        double weight = Double.parseDouble(data[11].trim());
                        double maxSpeed = Double.parseDouble(data[12].trim());
                        String typeStr = data[15].trim();
                        String operator = data.length > 18 ? data[18].trim() : "";
                        String gauge = data.length > 19 ? data[19].trim() : "";

                        LocomotiveType type = typeStr.equalsIgnoreCase("Electric") ?
                                LocomotiveType.ELECTRIC : LocomotiveType.DIESEL;

                        LocomotiveModel locomotiveModel = new LocomotiveModel(
                                power, length, width, height, weight, maxSpeed, make, name, type, gauge
                        );


                        Locomotive locomotive = new Locomotive(
                                number, serviceYear, numberBogies, operator, locomotiveModel, maxSpeed
                        );

                        repository.addLocomotive(locomotive);
                    }
                }
                //System.out.println("Parsed " + repository.getAllLocomotives().size() + " locomotives");
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error parsing locomotives from " + fileName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void parseWagons(TrainRepository repository) {
        String wagonModelsFileName = "train_dataset_sprint3/WagonModels.csv";
        String wagonFileName = "train_dataset_sprint3/Wagon.csv";

        parseWagonModels(wagonModelsFileName, repository);

        try (InputStream inputStream = TrainParser.class.getClassLoader().getResourceAsStream(wagonFileName)) {
            if (inputStream == null) {
                System.err.println("File not found in classpath: " + wagonFileName);
                return;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                boolean firstLine = true;

                while ((line = br.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false;
                        continue;
                    }

                    String[] data = parseCSVLine(line);
                    if (data.length >= 4) {
                        String wagonId = data[0].trim();
                        String modelName = data[1].trim();
                        String operator = data[2].trim();
                        int serviceYear = Integer.parseInt(data[3].trim());

                        WagonModel wagonModel = repository.getWagonModel(modelName);
                        if (wagonModel != null) {
                            Wagon wagon = new Wagon(wagonId);
                            wagon.setWagonModel(wagonModel);
                            repository.addWagon(wagon);
                        } else {
                            System.err.println("Wagon model not found for: " + modelName);
                        }
                    }
                }
                System.out.println("Parsed " + repository.getAllWagons().size() + " wagons");
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error parsing wagons from " + wagonFileName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void parseWagonModels(String fileName, TrainRepository repository) {
        try (InputStream inputStream = TrainParser.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                System.err.println("File not found in classpath: " + fileName);
                return;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                boolean firstLine = true;

                while ((line = br.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false;
                        continue;
                    }

                    String[] data = parseCSVLine(line);
                    if (data.length >= 13) {
                        String id = data[0].trim();
                        String modelName = data[1].trim();
                        String maker = data[2].trim();
                        double length = Double.parseDouble(data[5].trim());
                        double width = Double.parseDouble(data[6].trim());
                        double height = Double.parseDouble(data[7].trim());
                        double weight = Double.parseDouble(data[8].trim());
                        double maxSpeed = Double.parseDouble(data[9].trim());
                        String gauge = data.length > 13 ? data[13].trim() : "";

                        int boxCapacity = calculateBoxCapacity(length, width, height);

                        WagonModel wagonModel = new WagonModel(
                                modelName, length, height, width, weight, maxSpeed, gauge, boxCapacity
                        );

                        repository.addWagonModel(wagonModel);
                    }
                }
                System.out.println("Parsed " + repository.getAllWagonModels().size() + " wagon models");
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error parsing wagon models from " + fileName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int calculateBoxCapacity(double length, double width, double height) {
        double wagonVolume = length * width * height;
        return (int) (wagonVolume / 2.0);
    }

    private static String[] parseCSVLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    }
}