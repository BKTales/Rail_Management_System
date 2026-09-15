-- Owner and Operator (OK)
INSERT INTO Owner (vatNumber, name, shortName) VALUES ('PT503933813', 'Infraestruturas de Portugal, SA', 'IP');

INSERT INTO Operator (vatNumber, name, shortName) VALUES ('PT509017800', 'Medway - Operador Ferroviário de Mercadorias, S.A', 'Medway');
INSERT INTO Operator (vatNumber, name, shortName) VALUES ('PT507832388', 'Captrain Portugal S.A.', 'Captrain');

-- Make (OK)
INSERT INTO Make (name) VALUES ('Metalsines');
INSERT INTO Make (name) VALUES ('Equimetal');
INSERT INTO Make (name) VALUES ('Sepsa Cometna');
INSERT INTO Make (name) VALUES ('Emef');
INSERT INTO Make (name) VALUES ('Siemens');
INSERT INTO Make (name) VALUES ('Sorefame - Alsthom');
INSERT INTO Make (name) VALUES ('Stadler');
INSERT INTO Make (name) VALUES ('Simmering');

-- Gauge (OK)
INSERT INTO Gauge (gaugeName, width) VALUES ('Standart gauge', 1435);
INSERT INTO Gauge (gaugeName, width) VALUES ('Iberian gauge', 1668);

-- Bogie (OK)
INSERT INTO Bogie (name) VALUES ('Y25');
INSERT INTO Bogie (name) VALUES ('Type B');
INSERT INTO Bogie (name) VALUES ('Diamond Frame');

-- Wagon_Type (OK)
INSERT INTO Wagon_Type (description) VALUES ('Box Car');
INSERT INTO Wagon_Type (description) VALUES ('Flat Car');
INSERT INTO Wagon_Type (description) VALUES ('Chemical Tank Car');
INSERT INTO Wagon_Type (description) VALUES ('Mineral Oil Car');
INSERT INTO Wagon_Type (description) VALUES ('Pressure Gas Car');
INSERT INTO Wagon_Type (description) VALUES ('Hopper Car');
INSERT INTO Wagon_Type (description) VALUES ('Refrigerated Car');
INSERT INTO Wagon_Type (description) VALUES ('Grain Car');

-- Building_Type
INSERT INTO Building_Type (buildingTypeName) VALUES ('Station');
INSERT INTO Building_Type (buildingTypeName) VALUES ('Depot');
INSERT INTO Building_Type (buildingTypeName) VALUES ('Warehouse');
INSERT INTO Building_Type (buildingTypeName) VALUES ('Workshop');

-- Dimension (OK)
INSERT INTO Dimension (length, width, height) VALUES (17.240, 3.072, 4.270);
INSERT INTO Dimension (length, width, height) VALUES (9.640, 3.120, 4.1655);
INSERT INTO Dimension (length, width, height) VALUES (21.700, 3.180, 4.170);
INSERT INTO Dimension (length, width, height) VALUES (14.040, 3.104, 2.535);
INSERT INTO Dimension (length, width, height) VALUES (13.860, 2.850, 1.060);
INSERT INTO Dimension (length, width, height) VALUES (18.116, 2.950, 1.030);
INSERT INTO Dimension (length, width, height) VALUES (13.800, 2.950, 4.226);
INSERT INTO Dimension (length, width, height) VALUES (14.020, 2.842, 3.300);
INSERT INTO Dimension (length, width, height) VALUES (19.2, 3, 4.375);
INSERT INTO Dimension (length, width, height) VALUES (19.084, 3.062, 4.31);
INSERT INTO Dimension (length, width, height) VALUES (23.02, 3, 4.264);

-- Facility (OK)
INSERT INTO Facility (facilityId, name) VALUES (1, 'São Romão');
INSERT INTO Facility (facilityId, name) VALUES (2, 'Tamel');
INSERT INTO Facility (facilityId, name) VALUES (3, 'Senhora das Dores');
INSERT INTO Facility (facilityId, name) VALUES (4, 'Lousado');
INSERT INTO Facility (facilityId, name) VALUES (5, 'Porto Campanhã');
INSERT INTO Facility (facilityId, name) VALUES (6, 'Leandro');
INSERT INTO Facility (facilityId, name) VALUES (7, 'Porto São Bento');
INSERT INTO Facility (facilityId, name) VALUES (8, 'Barcelos');
INSERT INTO Facility (facilityId, name) VALUES (9, 'Vila Nova de Cerveira');
INSERT INTO Facility (facilityId, name) VALUES (10, 'Midões');
INSERT INTO Facility (facilityId, name) VALUES (11, 'Valença');
INSERT INTO Facility (facilityId, name) VALUES (12, 'Darque');
INSERT INTO Facility (facilityId, name) VALUES (13, 'Contumil');
INSERT INTO Facility (facilityId, name) VALUES (14, 'Ermesinde');
INSERT INTO Facility (facilityId, name) VALUES (15, 'São Frutuoso');
INSERT INTO Facility (facilityId, name) VALUES (16, 'São Pedro da Torre');
INSERT INTO Facility (facilityId, name) VALUES (17, 'Viana do Castelo');
INSERT INTO Facility (facilityId, name) VALUES (18, 'Famalicão');
INSERT INTO Facility (facilityId, name) VALUES (19, 'Barroselas');
INSERT INTO Facility (facilityId, name) VALUES (20, 'Nine');
INSERT INTO Facility (facilityId, name) VALUES (21, 'Caminha');
INSERT INTO Facility (facilityId, name) VALUES (22, 'Carvalha');
INSERT INTO Facility (facilityId, name) VALUES (23, 'Carreço');
INSERT INTO Facility (facilityId, name) VALUES (43, 'São Gemil');
INSERT INTO Facility (facilityId, name) VALUES (45, 'São Mamede de Infesta');
INSERT INTO Facility (facilityId, name) VALUES (48, 'Leça do Balio');
INSERT INTO Facility (facilityId, name) VALUES (50, 'Leixões');

-- Rail_Line (OK)
INSERT INTO Rail_Line (railLineId, ownerId, startFacilityId, endFacilityId, name) VALUES (1, 'PT503933813', 7, 5, 'Ramal São Bento - Campanhã');
INSERT INTO Rail_Line (railLineId, ownerId, startFacilityId, endFacilityId, name) VALUES (2, 'PT503933813', 5, 13, 'Ramal Camapanhã - Contumil');
INSERT INTO Rail_Line (railLineId, ownerId, startFacilityId, endFacilityId, name) VALUES (3, 'PT503933813', 13, 20, 'Ramal Contumil - Nine');
INSERT INTO Rail_Line (railLineId, ownerId, startFacilityId, endFacilityId, name) VALUES (4, 'PT503933813', 20, 8, 'Ramal Nine - Barcelos');
INSERT INTO Rail_Line (railLineId, ownerId, startFacilityId, endFacilityId, name) VALUES (5, 'PT503933813', 8, 12, 'Ramal Barcelos - Darque');
INSERT INTO Rail_Line (railLineId, ownerId, startFacilityId, endFacilityId, name) VALUES (6, 'PT503933813', 12, 17, 'Ramal Darque - Viana');
INSERT INTO Rail_Line (railLineId, ownerId, startFacilityId, endFacilityId, name) VALUES (7, 'PT503933813', 17, 21, 'Ramal Viana - Caminha');
INSERT INTO Rail_Line (railLineId, ownerId, startFacilityId, endFacilityId, name) VALUES (8, 'PT503933813', 21, 16, 'Ramal Caminha - Torre');
INSERT INTO Rail_Line (railLineId, ownerId, startFacilityId, endFacilityId, name) VALUES (9, 'PT503933813', 16, 11, 'Ramal Torre - Valença');
INSERT INTO Rail_Line (railLineId, ownerId, startFacilityId, endFacilityId, name) VALUES (21, 'PT503933813', 13, 43, 'Ramal Contumil - São Gemil');
INSERT INTO Rail_Line (railLineId, ownerId, startFacilityId, endFacilityId, name) VALUES (22, 'PT503933813', 43, 45, 'Ramal São Gemil - São Mamede de Infesta');
INSERT INTO Rail_Line (railLineId, ownerId, startFacilityId, endFacilityId, name) VALUES (23, 'PT503933813', 45, 48, 'Ramal São Mamede de Infesta - Leça do Balio');
INSERT INTO Rail_Line (railLineId, ownerId, startFacilityId, endFacilityId, name) VALUES (24, 'PT503933813', 48, 50, 'Ramal Leça do Balio - Leixões');

-- Siding (OK)
INSERT INTO Siding (sidingId, startPosition, length) VALUES (1, 0, 2618);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (2, 0, 2443);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (3, 0, 26560);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (4, 26560, 10000);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (5, 0, 5286);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (6, 5286, 6000);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (7, 0, 10387);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (8, 10387, 12000);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (9, 22387, 3100);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (10, 0, 4890);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (11, 0, 6000);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (12, 6000, 5000);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (13, 11000, 12000);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (14, 0, 20829);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (15, 0, 4264);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (16, 0, 3883);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (17, 0, 1174);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (18, 1174, 2534);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (19, 0, 1566);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (20, 1566, 1453);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (21, 0, 3597);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (22, 3597, 4334);
-- siding for test
INSERT INTO Siding (sidingId, startPosition, length) VALUES (23, 1, 1);
INSERT INTO Siding (sidingId, startPosition, length) VALUES (24, 2, 2);

-- Rail_Segment (OK)
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (1, 1, 1, 1, 'Iberian gauge', 1, 2, 2618, 8000, 150);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (2, 2, 1, 2, 'Iberian gauge', 1, 2, 2443, 8000, 120);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (3, 3, 1, 3, 'Iberian gauge', 1, 2, 26560, 8000, 160);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (4, 3, 2, 4, 'Iberian gauge', 1, 2, 10000, 8000, 150);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (5, 4, 1, 5, 'Iberian gauge', 1, 2, 5286, 8000, 150);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (6, 4, 2, 6, 'Iberian gauge', 1, 2, 6000, 8000, 150);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (7, 5, 1, 7, 'Iberian gauge', 1, 2, 10387, 8000, 110);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (8, 5, 2, 8, 'Iberian gauge', 1, 2, 12000, 8000, 110);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (9, 5, 3, 9, 'Iberian gauge', 1, 2, 3100, 8000, 110);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (10, 6, 1, 10, 'Iberian gauge', 1, 2, 4890, 6400, 140);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (11, 7, 1, 11, 'Iberian gauge', 1, 1, 6000, 8000, 150);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (12, 7, 2, 12, 'Iberian gauge', 1, 1, 5000, 8000, 120);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (13, 7, 3, 13, 'Iberian gauge', 1, 1, 12000, 8000, 130);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (14, 8, 1, 14, 'Iberian gauge', 1, 1, 20829, 8000, 140);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (15, 9, 1, 15, 'Iberian gauge', 1, 1, 4264, 8000, 110);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (16, 21, 1, 16, 'Iberian gauge', 1, 2, 3883, 8000, 100);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (17, 22, 1, 17, 'Iberian gauge', 1, 2, 1174, 8400, 100);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (18, 22, 2, 18, 'Iberian gauge', 1, 2, 2534, 8000, 110);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (19, 23, 1, 19, 'Iberian gauge', 1, 2, 1566, 8000, 120);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (20, 23, 2, 20, 'Iberian gauge', 1, 2, 1453, 8000, 140);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (21, 24, 1, 21, 'Iberian gauge', 1, 2, 3597, 8100, 170);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (22, 24, 2, 22, 'Iberian gauge', 1, 2, 4334, 8000, 150);

-- Wagon_Model (OK)
INSERT INTO Wagon_Model (wagonModelId, bogieName, makeId, wagonTypeId, length, width, height, name, weight, payload, volumeCapacity, tare, maxSpeed, numOfBogies)
VALUES (1245, 'Y25', 1, 6, 17.240, 3.072, 4.270, 'Tadgs 32 94 082 3', 24, 56, 75, 500, 120, 2);
INSERT INTO Wagon_Model (wagonModelId, bogieName, makeId, wagonTypeId, length, width, height, name, weight, payload, volumeCapacity, tare, maxSpeed, numOfBogies)
VALUES (1278, 'Y25', 2, 6, 9.640, 3.120, 4.1655, 'Tdgs 41 94 074 1', 13.8, 26.2, 38, 300, 100, 2);
INSERT INTO Wagon_Model (wagonModelId, bogieName, makeId, wagonTypeId, length, width, height, name, weight, payload, volumeCapacity, tare, maxSpeed, numOfBogies)
VALUES (1325, 'Y25', 3, 7, 21.700, 3.180, 4.170, 'Gabs 81 94 181 1', 29.8, 50.2, 110, 250, 100, 2);
INSERT INTO Wagon_Model (wagonModelId, bogieName, makeId, wagonTypeId, length, width, height, name, weight, payload, volumeCapacity, tare, maxSpeed, numOfBogies)
VALUES (1104, 'Type B', 4, 2, 14.040, 3.104, 2.535, 'Regmms 32 94 356 3', 21.2, 60.6, 76.3, 250, 120, 2);
INSERT INTO Wagon_Model (wagonModelId, bogieName, makeId, wagonTypeId, length, width, height, name, weight, payload, volumeCapacity, tare, maxSpeed, numOfBogies)
VALUES (985, 'Type B', 1, 3, 13.860, 2.850, 1.060, 'Lgs 22 94 441 6', 11.9, 28.1, 76.3, 250, 120, 2);
INSERT INTO Wagon_Model (wagonModelId, bogieName, makeId, wagonTypeId, length, width, height, name, weight, payload, volumeCapacity, tare, maxSpeed, numOfBogies)
VALUES (987, 'Diamond Frame', 2, 4, 18.116, 2.950, 1.030, 'Sgnss 12 94 455 2', 21.6, 68.4, 76.3, 250, 120, 4);
INSERT INTO Wagon_Model (wagonModelId, bogieName, makeId, wagonTypeId, length, width, height, name, weight, payload, volumeCapacity, tare, maxSpeed, numOfBogies)
VALUES (1523, 'Y25', 3, 5, 13.800, 2.950, 4.226, 'Zaes 81 94 788', 22.9, 57.1, 64.6, 200, 120, 2);
INSERT INTO Wagon_Model (wagonModelId, bogieName, makeId, wagonTypeId, length, width, height, name, weight, payload, volumeCapacity, tare, maxSpeed, numOfBogies)
VALUES (1212, 'Y25', 4, 7, 14.020, 2.842, 3.300, 'Kbs', 22.9, 57.1, 64.6, 200, 120, 2);

INSERT INTO Wagon_Model_Gauge (wagonModelId, gaugeName) VALUES (1245, 'Iberian gauge');
INSERT INTO Wagon_Model_Gauge (wagonModelId, gaugeName) VALUES (1278, 'Iberian gauge');
INSERT INTO Wagon_Model_Gauge (wagonModelId, gaugeName) VALUES (1325, 'Iberian gauge');
INSERT INTO Wagon_Model_Gauge (wagonModelId, gaugeName) VALUES (1104, 'Iberian gauge');
INSERT INTO Wagon_Model_Gauge (wagonModelId, gaugeName) VALUES (985, 'Iberian gauge');
INSERT INTO Wagon_Model_Gauge (wagonModelId, gaugeName) VALUES (987, 'Iberian gauge');
INSERT INTO Wagon_Model_Gauge (wagonModelId, gaugeName) VALUES (1523, 'Iberian gauge');
INSERT INTO Wagon_Model_Gauge (wagonModelId, gaugeName) VALUES (1212, 'Iberian gauge');

-- Locomotive Models (Electric)
INSERT INTO Locomotive_Model (bogieName, makeId, length, width, height, name, weight, power, acceleration, numOfBogies, maxSpeed) VALUES ('Y25', 5, 19.2, 3, 4.375, 'Siemens 5600', 84000, 5600, 800, 4, 140);
INSERT INTO Locomotive_Model (bogieName, makeId, length, width, height, name, weight, power, acceleration, numOfBogies, maxSpeed) VALUES ('Type B', 6, 19.084, 3.062, 4.31, 'Alsthom 2600', 80000, 2832, 750, 4, 140);
INSERT INTO Locomotive_Model (bogieName, makeId, length, width, height, name, weight, power, acceleration, numOfBogies, maxSpeed) VALUES ('Y25', 7, 23.02, 3, 4.264, 'Stadler EuroDual', 90000, 7000, 850, 4, 140);

INSERT INTO Electric_Locomotive_Model (locomotiveModelId, voltage, frequency) VALUES (1, 25000, 50);
INSERT INTO Electric_Locomotive_Model (locomotiveModelId, voltage, frequency) VALUES (2, 25000, 50);
INSERT INTO Electric_Locomotive_Model (locomotiveModelId, voltage, frequency) VALUES (3, 25000, 50);

INSERT INTO Locomotive_Model_Gauge (locomotiveModelId, gaugeName) VALUES (1, 'Iberian gauge');
INSERT INTO Locomotive_Model_Gauge (locomotiveModelId, gaugeName) VALUES (2, 'Iberian gauge');
INSERT INTO Locomotive_Model_Gauge (locomotiveModelId, gaugeName) VALUES (3, 'Iberian gauge');

-- Locomotive Models (Diesel)
INSERT INTO Locomotive_Model (bogieName, makeId, length, width, height, name, weight, power, acceleration, numOfBogies, maxSpeed) VALUES ('Type B', 8, 19.2, 3, 4.375, 'Simmering 1800', 78000, 1800, 600, 4, 140);
INSERT INTO Locomotive_Model (bogieName, makeId, length, width, height, name, weight, power, acceleration, numOfBogies, maxSpeed) VALUES ('Diamond Frame', 5, 19.084, 3.062, 4.31, 'Siemens 2400', 82000, 2400, 680, 4, 140);
INSERT INTO Locomotive_Model (bogieName, makeId, length, width, height, name, weight, power, acceleration, numOfBogies, maxSpeed) VALUES ('Y25', 7, 23.02, 3, 4.264, 'Stadler Diesel 3000', 88000, 3000, 720, 4, 140);

INSERT INTO Diesel_Locomotive_Model (locomotiveModelId, fuelCapacity) VALUES (4, 3500);
INSERT INTO Diesel_Locomotive_Model (locomotiveModelId, fuelCapacity) VALUES (5, 4000);
INSERT INTO Diesel_Locomotive_Model (locomotiveModelId, fuelCapacity) VALUES (6, 4000);

INSERT INTO Locomotive_Model_Gauge (locomotiveModelId, gaugeName) VALUES (4, 'Iberian gauge');
INSERT INTO Locomotive_Model_Gauge (locomotiveModelId, gaugeName) VALUES (5, 'Iberian gauge');

-- Locomotive
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (3002, 'PT507832388', 6, 19, 2020);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (5601, 'PT507832388', 4, 19, 2020);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (2401, 'PT507832388', 1, 19, 2020);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (1234, 'PT507832388', 1, 19, 2020);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (2124, 'PT507832388', 6, 19, 2020);

-- Wagons distributed across facilities (50 wagons)
-- Porto São Bento (7) - 7 wagons
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5001, 7, 'PT509017800', 1245);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5002, 7, 'PT509017800', 1278);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5003, 7, 'PT507832388', 1325);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5004, 7, 'PT507832388', 1104);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5005, 7, 'PT509017800', 985);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5006, 7, 'PT509017800', 987);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5007, 7, 'PT507832388', 1523);

-- Porto Campanhã (5) - 6 wagons
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5008, 5, 'PT509017800', 1212);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5009, 5, 'PT507832388', 1245);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5010, 5, 'PT509017800', 1278);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5011, 5, 'PT509017800', 1325);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5012, 5, 'PT507832388', 1104);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5013, 5, 'PT509017800', 985);

-- Viana do Castelo (17) - 6 wagons
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5014, 17, 'PT509017800', 987);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5015, 17, 'PT507832388', 1523);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5016, 17, 'PT509017800', 1212);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5017, 17, 'PT507832388', 1245);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5018, 17, 'PT509017800', 1278);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5019, 17, 'PT507832388', 1325);

-- Barcelos (8) - 5 wagons
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5020, 8, 'PT509017800', 1104);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5021, 8, 'PT507832388', 985);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5022, 8, 'PT509017800', 987);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5023, 8, 'PT507832388', 1523);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5024, 8, 'PT509017800', 1212);

-- Famalicão (18) - 5 wagons
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5025, 18, 'PT509017800', 1245);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5026, 18, 'PT507832388', 1278);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5027, 18, 'PT509017800', 1325);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5028, 18, 'PT507832388', 1104);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5029, 18, 'PT509017800', 985);

-- Valença (11) - 4 wagons
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5030, 11, 'PT507832388', 987);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5031, 11, 'PT509017800', 1523);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5032, 11, 'PT507832388', 1212);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5033, 11, 'PT509017800', 1245);

-- Caminha (21) - 3 wagons
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5034, 21, 'PT507832388', 1278);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5035, 21, 'PT509017800', 1325);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5036, 21, 'PT507832388', 1104);

-- Contumil (13) - 3 wagons
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5037, 13, 'PT509017800', 985);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5038, 13, 'PT507832388', 987);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5039, 13, 'PT509017800', 1523);

-- Other facilities (remaining wagons distributed)
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5040, 1, 'PT509017800', 1212);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5041, 2, 'PT507832388', 1245);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5042, 3, 'PT509017800', 1278);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5043, 4, 'PT507832388', 1325);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5044, 6, 'PT509017800', 1104);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5045, 9, 'PT507832388', 985);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5046, 12, 'PT509017800', 987);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5047, 14, 'PT507832388', 1523);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5048, 15, 'PT509017800', 1212);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5049, 19, 'PT507832388', 1245);
INSERT INTO Wagon (wagonId, facilityId, operatorId, wagonModelId) VALUES (5050, 20, 'PT509017800', 1278);

-- Routes with multiple points
-- Route 1: Porto São Bento (7) -> Campanhã (5) -> Contumil (13) -> Nine (20) -> Barcelos (8) -> Darque (12) -> Viana (17)
INSERT INTO Route (routeId, startPoint, endPoint) VALUES (1, 7, 17);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (1, 1, 7);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (1, 2, 5);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (1, 3, 13);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (1, 4, 20);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (1, 5, 8);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (1, 6, 12);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (1, 7, 17);

-- Route 2: Viana (17) -> Caminha (21) -> Torre (16) -> Valença (11)
INSERT INTO Route (routeId, startPoint, endPoint) VALUES (2, 17, 11);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (2, 1, 17);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (2, 2, 21);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (2, 3, 16);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (2, 4, 11);

-- Route 3: Porto São Bento (7) -> Porto Campanhã (5)
INSERT INTO Route (routeId, startPoint, endPoint) VALUES (3, 7, 5);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (3, 1, 7);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (3, 2, 5);

-- Route 4: Caminha (21) -> Barcelos (8)
INSERT INTO Route (routeId, startPoint, endPoint) VALUES (4, 21, 8);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (4, 1, 21);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (4, 2, 8);

-- Trains
INSERT INTO Train (trainId, routeId, maxSize, startDate, endDate)
VALUES (701, 1, 1000, TO_DATE('2024-01-15', 'YYYY-MM-DD'), TO_DATE('2024-12-31', 'YYYY-MM-DD'));

INSERT INTO Train (trainId, routeId, maxSize, startDate, endDate)
VALUES (702, 2, 800, TO_DATE('2024-02-01', 'YYYY-MM-DD'), TO_DATE('2024-12-31', 'YYYY-MM-DD'));

-- trains to test usbd44
INSERT INTO Train (trainId, routeId, maxSize, startDate, endDate)
VALUES (703, 3, 20, TO_DATE('2024-02-01', 'YYYY-MM-DD'), TO_DATE('2024-12-31', 'YYYY-MM-DD'));

INSERT INTO Train (trainId, routeId, maxSize, startDate, endDate)
VALUES (704, 4, 900, TO_DATE('2024-03-06', 'YYYY-MM-DD'), TO_DATE('2024-10-10', 'YYYY-MM-DD'));

-- Freight
INSERT INTO Freight (freightId, startPoint, endPoint, routeId) VALUES (801, 7, 17, 1);
INSERT INTO Freight (freightId, startPoint, endPoint, routeId) VALUES (802, 17, 11, 2);
INSERT INTO Freight (freightId, startPoint, endPoint, routeId) VALUES (803, 17, 7, 1);
INSERT INTO Freight (freightId, startPoint, endPoint, routeId) VALUES (804, 11, 17, 2);
-- Freight without route
INSERT INTO Freight (freightId, startPoint, endPoint, routeId) VALUES (805, 7, 5, NULL);

-- Locomotive_Train assignments
INSERT INTO Locomotive_Train (locomotiveId, trainId) VALUES (5601, 701);
INSERT INTO Locomotive_Train (locomotiveId, trainId) VALUES (2401, 702);
INSERT INTO Locomotive_Train (locomotiveId, trainId) VALUES (2401, 703);
INSERT INTO Locomotive_Train (locomotiveId, trainId) VALUES (2401, 704);

-- Wagon_Freight assignments
INSERT INTO Wagon_Freight (wagonId, freightId) VALUES (5001, 801);
INSERT INTO Wagon_Freight (wagonId, freightId) VALUES (5002, 801);
INSERT INTO Wagon_Freight (wagonId, freightId) VALUES (5003, 801);
INSERT INTO Wagon_Freight (wagonId, freightId) VALUES (5004, 801);
INSERT INTO Wagon_Freight (wagonId, freightId) VALUES (5014, 802);
INSERT INTO Wagon_Freight (wagonId, freightId) VALUES (5015, 802);
INSERT INTO Wagon_Freight (wagonId, freightId) VALUES (5016, 802);
INSERT INTO Wagon_Freight (wagonId, freightId) VALUES (5017, 803);
INSERT INTO Wagon_Freight (wagonId, freightId) VALUES (5018, 803);
INSERT INTO Wagon_Freight (wagonId, freightId) VALUES (5030, 804);
INSERT INTO Wagon_Freight (wagonId, freightId) VALUES (5031, 804);
-- Freight without route but same wagon as other freight
INSERT INTO Wagon_Freight (wagonId, freightId) VALUES (5031, 805);