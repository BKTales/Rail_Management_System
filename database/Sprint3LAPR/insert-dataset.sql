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

-- Position (OK)
INSERT INTO Position (latitude, longitude) VALUES (41.5383, -8.4258);
INSERT INTO Position (latitude, longitude) VALUES (41.5797, -8.5625);
INSERT INTO Position (latitude, longitude) VALUES (41.5289, -8.4419);
INSERT INTO Position (latitude, longitude) VALUES (41.4889, -8.5419);
INSERT INTO Position (latitude, longitude) VALUES (41.1496, -8.5839);
INSERT INTO Position (latitude, longitude) VALUES (41.4756, -8.5283);
INSERT INTO Position (latitude, longitude) VALUES (41.1455, -8.6108);
INSERT INTO Position (latitude, longitude) VALUES (41.5388, -8.6175);
INSERT INTO Position (latitude, longitude) VALUES (41.9406, -8.7397);
INSERT INTO Position (latitude, longitude) VALUES (41.8167, -8.7333);
INSERT INTO Position (latitude, longitude) VALUES (42.0289, -8.6428);
INSERT INTO Position (latitude, longitude) VALUES (41.6928, -8.8308);
INSERT INTO Position (latitude, longitude) VALUES (41.1678, -8.5539);
INSERT INTO Position (latitude, longitude) VALUES (41.2097, -8.5508);
INSERT INTO Position (latitude, longitude) VALUES (41.5042, -8.5906);
INSERT INTO Position (latitude, longitude) VALUES (41.8556, -8.7122);
INSERT INTO Position (latitude, longitude) VALUES (41.6936, -8.8342);
INSERT INTO Position (latitude, longitude) VALUES (41.4078, -8.5272);
INSERT INTO Position (latitude, longitude) VALUES (41.6486, -8.7733);
INSERT INTO Position (latitude, longitude) VALUES (41.5156, -8.5394);
INSERT INTO Position (latitude, longitude) VALUES (41.8747, -8.8378);
INSERT INTO Position (latitude, longitude) VALUES (41.5519, -8.5622);
INSERT INTO Position (latitude, longitude) VALUES (41.7267, -8.8281);
INSERT INTO Position (latitude, longitude) VALUES (41.1839, -8.7011);
INSERT INTO Position (latitude, longitude) VALUES (41.1881, -8.6081);
INSERT INTO Position (latitude, longitude) VALUES (41.2086, -8.6256);
INSERT INTO Position (latitude, longitude) VALUES (41.5664, -8.5847);

-- Facility (OK)
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (1, 41.5383, -8.4258, 'São Romão');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (2, 41.5797, -8.5625, 'Tamel');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (3, 41.5289, -8.4419, 'Senhora das Dores');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (4, 41.4889, -8.5419, 'Lousado');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (5, 41.1496, -8.5839, 'Porto Campanhã');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (6, 41.4756, -8.5283, 'Leandro');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (7, 41.1455, -8.6108, 'Porto São Bento');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (8, 41.5388, -8.6175, 'Barcelos');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (9, 41.9406, -8.7397, 'Vila Nova de Cerveira');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (10, 41.8167, -8.7333, 'Midões');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (11, 42.0289, -8.6428, 'Valença');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (12, 41.6928, -8.8308, 'Darque');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (13, 41.1678, -8.5539, 'Contumil');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (14, 41.2097, -8.5508, 'Ermesinde');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (15, 41.5042, -8.5906, 'São Frutuoso');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (16, 41.8556, -8.7122, 'São Pedro da Torre');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (17, 41.6936, -8.8342, 'Viana do Castelo');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (18, 41.4078, -8.5272, 'Famalicão');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (19, 41.6486, -8.7733, 'Barroselas');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (20, 41.5156, -8.5394, 'Nine');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (21, 41.8747, -8.8378, 'Caminha');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (22, 41.5519, -8.5622, 'Carvalha');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (23, 41.7267, -8.8281, 'Carreço');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (43, 41.5664, -8.5847, 'São Gemil');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (45, 41.1881, -8.6081, 'São Mamede de Infesta');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (48, 41.2086, -8.6256, 'Leça do Balio');
INSERT INTO Facility (facilityId, latitude, longitude, name) VALUES (50, 41.1839, -8.7011, 'Leixões');

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
INSERT INTO Siding (startPosition, length) VALUES (0, 2618);
INSERT INTO Siding (startPosition, length) VALUES (0, 2443);
INSERT INTO Siding (startPosition, length) VALUES (0, 26560);
INSERT INTO Siding (startPosition, length) VALUES (26560, 10000);
INSERT INTO Siding (startPosition, length) VALUES (0, 5286);
INSERT INTO Siding (startPosition, length) VALUES (5286, 6000);
INSERT INTO Siding (startPosition, length) VALUES (0, 10387);
INSERT INTO Siding (startPosition, length) VALUES (10387, 12000);
INSERT INTO Siding (startPosition, length) VALUES (22387, 3100);
INSERT INTO Siding (startPosition, length) VALUES (0, 4890);
INSERT INTO Siding (startPosition, length) VALUES (0, 6000);
INSERT INTO Siding (startPosition, length) VALUES (6000, 5000);
INSERT INTO Siding (startPosition, length) VALUES (11000, 12000);
INSERT INTO Siding (startPosition, length) VALUES (0, 20829);
INSERT INTO Siding (startPosition, length) VALUES (0, 4264);
INSERT INTO Siding (startPosition, length) VALUES (0, 3883);
INSERT INTO Siding (startPosition, length) VALUES (0, 1174);
INSERT INTO Siding (startPosition, length) VALUES (1174, 2534);
INSERT INTO Siding (startPosition, length) VALUES (0, 1566);
INSERT INTO Siding (startPosition, length) VALUES (1566, 1453);
INSERT INTO Siding (startPosition, length) VALUES (0, 3597);
INSERT INTO Siding (startPosition, length) VALUES (3597, 4334);

-- Rail_Segment (OK)
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (1, 1, 1, 1, 'Iberian gauge', 1, 2, 26, 8000, 150);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (2, 2, 1, 2, 'Iberian gauge', 1, 2, 24, 8000, 120);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (3, 3, 1, 3, 'Iberian gauge', 1, 2, 26, 8000, 160);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (4, 3, 2, 4, 'Iberian gauge', 1, 2, 10, 8000, 150);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (5, 4, 1, 5, 'Iberian gauge', 1, 2, 5.2, 8000, 150);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (6, 4, 2, 6, 'Iberian gauge', 1, 2, 32, 8000, 150);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (7, 5, 1, 7, 'Iberian gauge', 1, 2, 10, 8000, 110);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (8, 5, 2, 8, 'Iberian gauge', 1, 2, 12, 8000, 110);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (9, 5, 3, 9, 'Iberian gauge', 1, 2, 31, 8000, 110);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (10, 6, 1, 10, 'Iberian gauge', 1, 2, 25, 6400, 140);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (11, 7, 1, 11, 'Iberian gauge', 1, 1,    36, 8000, 150);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (12, 7, 2, 12, 'Iberian gauge', 1, 1, 9, 8000, 120);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (13, 7, 3, 13, 'Iberian gauge', 1, 1, 12, 8000, 130);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (14, 8, 1 ,14, 'Iberian gauge', 1, 1, 20, 8000, 140);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (15, 9, 1, 15, 'Iberian gauge', 1, 1, 12, 8000, 110);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (16, 21, 1, 16, 'Iberian gauge', 1, 2, 20, 8000, 100);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (17, 22, 1, 17, 'Iberian gauge', 1, 2, 14, 8400, 100);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (18, 22, 2, 18, 'Iberian gauge', 1, 2, 4, 8000, 110);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (19, 23, 1, 19, 'Iberian gauge', 1, 2, 15, 8000, 120);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (20, 23, 2, 20, 'Iberian gauge', 1, 2, 8, 8000, 140);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (21, 24, 1, 21, 'Iberian gauge', 1, 2, 10, 8100, 170);
INSERT INTO Rail_Segment (segmentId, railLineId, "order", sidingId, gaugeName, trackElectrification, numberOfTracks, length, maxWeight, speedLimit)
VALUES (22, 24, 2, 22, 'Iberian gauge', 1, 2, 5, 8000, 150);

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
INSERT INTO Locomotive_Model (locomotiveModelId,bogieName, makeId, length, width, height, name, weight,maxSpeed, power, acceleration, numOfBogies)
VALUES (1,'Y25', 5, 19.2, 3, 4.375, 'Siemens 5600', 84, 220,5600, 800, 4);
INSERT INTO Locomotive_Model (locomotiveModelId,bogieName, makeId, length, width, height, name, weight,maxSpeed, power, acceleration, numOfBogies)
VALUES (2,'Type B', 6, 19.084, 3.062, 4.31, 'Alsthom 2600', 80,220, 2832, 750, 4);
INSERT INTO Locomotive_Model (locomotiveModelId,bogieName, makeId, length, width, height, name, weight,maxSpeed, power, acceleration, numOfBogies)
VALUES (3,'Y25', 7, 23.02, 3, 4.264, 'Stadler EuroDual', 90, 220,7000, 850, 4);

INSERT INTO Electric_Locomotive_Model (locomotiveModelId, voltage, frequency) VALUES (1, 25000, 50);
INSERT INTO Electric_Locomotive_Model (locomotiveModelId, voltage, frequency) VALUES (2, 25000, 50);
INSERT INTO Electric_Locomotive_Model (locomotiveModelId, voltage, frequency) VALUES (3, 25000, 50);

INSERT INTO Locomotive_Model_Gauge (locomotiveModelId, gaugeName) VALUES (1, 'Iberian gauge');
INSERT INTO Locomotive_Model_Gauge (locomotiveModelId, gaugeName) VALUES (2, 'Iberian gauge');
INSERT INTO Locomotive_Model_Gauge (locomotiveModelId, gaugeName) VALUES (3, 'Iberian gauge');
INSERT INTO Locomotive_Model_Gauge (locomotiveModelId, gaugeName) VALUES (4, 'Iberian gauge');
INSERT INTO Locomotive_Model_Gauge (locomotiveModelId, gaugeName) VALUES (5, 'Iberian gauge');
INSERT INTO Locomotive_Model_Gauge (locomotiveModelId, gaugeName) VALUES (6, 'Iberian gauge');


-- Locomotive Models (Diesel)
INSERT INTO Locomotive_Model (locomotiveModelId,bogieName, makeId, length, width, height, name, weight,maxSpeed, power, acceleration, numOfBogies)
VALUES (4,'Type B', 8, 19.2, 3, 4.375, 'Simmering 1800', 78000, 200,1800, 600, 4);
INSERT INTO Locomotive_Model (locomotiveModelId,bogieName, makeId, length, width, height, name, weight,maxSpeed, power, acceleration, numOfBogies)
VALUES (5,'Diamond Frame', 5, 19.084, 3.062, 4.31, 'Siemens 2400', 82000, 200,2400, 680, 4);
INSERT INTO Locomotive_Model (locomotiveModelId,bogieName, makeId, length, width, height, name, weight,maxSpeed, power, acceleration, numOfBogies)
VALUES (6,'Y25', 7, 23.02, 3, 4.264, 'Stadler Diesel 3000', 88000, 120,3000, 720, 4);
INSERT INTO Diesel_Locomotive_Model (locomotiveModelId, fuelCapacity) VALUES (4, 3500);
INSERT INTO Diesel_Locomotive_Model (locomotiveModelId, fuelCapacity) VALUES (5, 4000);
INSERT INTO Diesel_Locomotive_Model (locomotiveModelId, fuelCapacity) VALUES (6, 4000);


-- Locomotive
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService)
VALUES (3002, 'PT507832388', 6, 19, 2020);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService)
VALUES (5601, 'PT507832388', 4, 19, 2020);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService)
VALUES (2401, 'PT507832388', 1, 19, 2020);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService)
VALUES (1234, 'PT507832388', 1, 19, 2020);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService)
VALUES (2124, 'PT507832388', 6, 19, 2020);

-- Batch 1: Model 1 (Older/Mixed)
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6001, 'PT507832388', 1, 19, 2001);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6002, 'PT507832388', 1, 19, 2002);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6003, 'PT507832388', 1, 19, 2003);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6004, 'PT507832388', 1, 19, 2004);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6005, 'PT507832388', 1, 19, 2005);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6006, 'PT507832388', 1, 19, 2006);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6007, 'PT507832388', 1, 19, 2007);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6008, 'PT507832388', 1, 19, 2008);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6009, 'PT507832388', 1, 19, 2009);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6010, 'PT507832388', 1, 19, 2010);

-- Batch 2: Model 4 (Mid-range)
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6011, 'PT507832388', 4, 19, 2011);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6012, 'PT507832388', 4, 19, 2012);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6013, 'PT507832388', 4, 19, 2013);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6014, 'PT507832388', 4, 19, 2014);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6015, 'PT507832388', 4, 19, 2015);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6016, 'PT507832388', 4, 19, 2016);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6017, 'PT507832388', 4, 19, 2017);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6018, 'PT507832388', 4, 19, 2018);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6019, 'PT507832388', 4, 19, 2019);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6020, 'PT507832388', 4, 19, 2020);

-- Batch 3: Model 6 (Modern)
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6021, 'PT507832388', 6, 19, 2021);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6022, 'PT507832388', 6, 19, 2022);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6023, 'PT507832388', 6, 19, 2023);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6024, 'PT507832388', 6, 19, 2024);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6025, 'PT507832388', 6, 19, 2024);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6026, 'PT507832388', 6, 19, 2023);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6027, 'PT507832388', 6, 19, 2022);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6028, 'PT507832388', 6, 19, 2021);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6029, 'PT507832388', 6, 19, 2020);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6030, 'PT507832388', 6, 19, 2019);

-- Batch 4: Mixed Models
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6031, 'PT507832388', 1, 19, 2005);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6032, 'PT507832388', 4, 19, 2010);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6033, 'PT507832388', 6, 19, 2015);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6034, 'PT507832388', 1, 19, 2008);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6035, 'PT507832388', 4, 19, 2012);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6036, 'PT507832388', 6, 19, 2018);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6037, 'PT507832388', 1, 19, 2003);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6038, 'PT507832388', 4, 19, 2014);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6039, 'PT507832388', 6, 19, 2022);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6040, 'PT507832388', 1, 19, 2001);

-- Batch 5: High IDs
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6041, 'PT507832388', 4, 19, 2019);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6042, 'PT507832388', 6, 19, 2024);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6043, 'PT507832388', 1, 19, 2002);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6044, 'PT507832388', 4, 19, 2011);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6045, 'PT507832388', 6, 19, 2020);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6046, 'PT507832388', 1, 19, 2007);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6047, 'PT507832388', 4, 19, 2016);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6048, 'PT507832388', 6, 19, 2023);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6049, 'PT507832388', 1, 19, 2004);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (6050, 'PT507832388', 4, 19, 2013);

-- Porto Campanhã (ID 5) - Grande Hub
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7001, 'PT507832388', 6, 5, 2022);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7002, 'PT507832388', 4, 5, 2019);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7003, 'PT507832388', 6, 5, 2024);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7004, 'PT507832388', 1, 5, 2005);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7005, 'PT507832388', 4, 5, 2015);

-- Porto São Bento (ID 7)
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7006, 'PT507832388', 6, 7, 2023);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7007, 'PT507832388', 1, 7, 2008);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7008, 'PT507832388', 1, 7, 2010);

-- Viana do Castelo (ID 17)
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7009, 'PT507832388', 4, 17, 2018);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7010, 'PT507832388', 6, 17, 2021);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7011, 'PT507832388', 4, 17, 2016);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7012, 'PT507832388', 1, 17, 2003);

-- Valença (ID 11) - Fronteira
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7013, 'PT507832388', 6, 11, 2022);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7014, 'PT507832388', 6, 11, 2023);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7015, 'PT507832388', 4, 11, 2014);

-- Leixões / Leça (ID 50) - Carga
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7016, 'PT507832388', 1, 50, 2001);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7017, 'PT507832388', 1, 50, 2002);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7018, 'PT507832388', 4, 50, 2011);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7019, 'PT507832388', 4, 50, 2013);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7020, 'PT507832388', 6, 50, 2020);

-- Nine (ID 20) - Nó ferroviário
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7021, 'PT507832388', 6, 20, 2021);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7022, 'PT507832388', 4, 20, 2017);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7023, 'PT507832388', 1, 20, 2006);

-- Ermesinde (ID 14)
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7024, 'PT507832388', 1, 14, 2009);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7025, 'PT507832388', 4, 14, 2012);

-- Lousado (ID 4)
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7026, 'PT507832388', 6, 4, 2024);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7027, 'PT507832388', 1, 4, 2004);

-- Contumil (ID 13) - Parque de material
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7028, 'PT507832388', 1, 13, 2000);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7029, 'PT507832388', 1, 13, 2001);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7030, 'PT507832388', 4, 13, 2010);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7031, 'PT507832388', 6, 13, 2020);

-- Famalicão (ID 18)
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7032, 'PT507832388', 4, 18, 2015);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7033, 'PT507832388', 6, 18, 2022);

-- Barcelos (ID 8)
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7034, 'PT507832388', 1, 8, 2007);
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7035, 'PT507832388', 4, 8, 2014);

INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7036, 'PT507832388', 1, 1, 2005); -- São Romão
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7037, 'PT507832388', 4, 2, 2016); -- Tamel
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7038, 'PT507832388', 6, 3, 2023); -- Senhora das Dores
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7039, 'PT507832388', 1, 6, 2008); -- Leandro
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7040, 'PT507832388', 4, 9, 2019); -- VN Cerveira
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7041, 'PT507832388', 6, 10, 2021); -- Midões
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7042, 'PT507832388', 1, 12, 2003); -- Darque
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7043, 'PT507832388', 4, 15, 2011); -- São Frutuoso
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7044, 'PT507832388', 6, 16, 2024); -- São Pedro da Torre
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7045, 'PT507832388', 1, 21, 2002); -- Caminha
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7046, 'PT507832388', 4, 22, 2013); -- Carvalha
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7047, 'PT507832388', 6, 23, 2020); -- Carreço
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7048, 'PT507832388', 1, 43, 2006); -- São Gemil
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7049, 'PT507832388', 4, 45, 2018); -- S. Mamede
INSERT INTO Locomotive (locomotiveId, operatorId, locomotiveModelId, startFacilityId, yearOfService) VALUES (7050, 'PT507832388', 6, 48, 2022); -- Leça do Balio

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
INSERT INTO Route (routeId, startPoint, endPoint,departureDate) VALUES (1, 7, 17,TIMESTAMP '2026-01-02 08:00:00');
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (1, 1, 7);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (1, 2, 5);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (1, 3, 13);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (1, 4, 20);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (1, 5, 8);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (1, 6, 12);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (1, 7, 17);

-- Route 2: Viana (17) -> Caminha (21) -> Torre (16) -> Valença (11)
INSERT INTO Route (routeId, startPoint, endPoint,departureDate) VALUES (2, 17, 11,TIMESTAMP '2026-01-02 09:00:00');
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (2, 1, 17);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (2, 2, 21);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (2, 3, 16);
INSERT INTO Route_Point (routeId, "order", facilityId) VALUES (2, 4, 11);

-- Trains
INSERT INTO Train (trainId, routeId, maxSize, startDate, endDate)
VALUES (701, 1, 1000, TO_DATE('2024-01-15', 'YYYY-MM-DD'), TO_DATE('2024-12-31', 'YYYY-MM-DD'));

INSERT INTO Train (trainId, routeId, maxSize, startDate, endDate)
VALUES (702, 2, 800, TO_DATE('2024-02-01', 'YYYY-MM-DD'), TO_DATE('2024-12-31', 'YYYY-MM-DD'));

-- Freight
INSERT INTO Freight (freightId, startPoint, endPoint, routeId) VALUES (801, 7, 17, 1);
INSERT INTO Freight (freightId, startPoint, endPoint, routeId) VALUES (802, 17, 11, 2);
INSERT INTO Freight (freightId, startPoint, endPoint, routeId) VALUES (803, 17, 7, 1);
INSERT INTO Freight (freightId, startPoint, endPoint, routeId) VALUES (804, 11, 17, 2);

-- Locomotive_Train assignments
INSERT INTO Locomotive_Train (locomotiveId, trainId) VALUES (5601, 701);
INSERT INTO Locomotive_Train (locomotiveId, trainId) VALUES (2401, 702);

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