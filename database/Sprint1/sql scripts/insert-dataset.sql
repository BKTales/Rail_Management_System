INSERT INTO "Owner" (ownerId, name, shortName) VALUES (503933813, 'Infraestruturas de Portugal, SA', 'IP');
INSERT INTO "Operator" (operatorId, name, shortName) VALUES (509017800, 'Medway - Operador Ferroviário de Mercadorias, S.A', 'Medway');

INSERT INTO "Track Type" (description) VALUES ('single-track');
INSERT INTO "Track Type" (description) VALUES ('double-track');

INSERT INTO "Track Electrification" (description) VALUES ('electrified');
INSERT INTO "Track Electrification" (description) VALUES ('non-electrified');

INSERT INTO "Fuel Type" (description) VALUES ('diesel');
INSERT INTO "Fuel Type" (description) VALUES ('electric');

INSERT INTO "Make" (name) VALUES ('Metalsines');
INSERT INTO "Make" (name) VALUES ('Equimetal');
INSERT INTO "Make" (name) VALUES ('Sepsa Cometna');
INSERT INTO "Make" (name) VALUES ('Metalsines');
INSERT INTO "Make" (name) VALUES ('Emef');
INSERT INTO "Make" (name) VALUES ('Siemens');
INSERT INTO "Make" (name) VALUES ('Sorefame - Alsthom');

INSERT INTO "Gauge" (width) VALUES (1435);
INSERT INTO "Gauge" (width) VALUES (1668);

INSERT INTO "Wagon Type" (description) VALUES ('Box Car');
INSERT INTO "Wagon Type" (description) VALUES ('Flat Car');
INSERT INTO "Wagon Type" (description) VALUES ('Chemical Tank Car');
INSERT INTO "Wagon Type" (description) VALUES ('Mineral Oil Car');
INSERT INTO "Wagon Type" (description) VALUES ('Pressure Gas Car');
INSERT INTO "Wagon Type" (description) VALUES ('Hopper Car');
INSERT INTO "Wagon Type" (description) VALUES ('Refrigerated Car');

INSERT INTO "Facility" (name) VALUES ('São Romão');
INSERT INTO "Facility" (name) VALUES ('Tamel');
INSERT INTO "Facility" (name) VALUES ('Senhora das Dores');
INSERT INTO "Facility" (name) VALUES ('Lousado');
INSERT INTO "Facility" (name) VALUES ('Porto Campanhã');
INSERT INTO "Facility" (name) VALUES ('Leandro');
INSERT INTO "Facility" (name) VALUES ('Porto São Bento');
INSERT INTO "Facility" (name) VALUES ('Barcelos');
INSERT INTO "Facility" (name) VALUES ('Vila Nova de Cerveira');
INSERT INTO "Facility" (name) VALUES ('Midões');
INSERT INTO "Facility" (name) VALUES ('Valença');
INSERT INTO "Facility" (name) VALUES ('Darque');
INSERT INTO "Facility" (name) VALUES ('Contumil');
INSERT INTO "Facility" (name) VALUES ('Ermesinde');
INSERT INTO "Facility" (name) VALUES ('São Frutuoso');
INSERT INTO "Facility" (name) VALUES ('São Pedro da Torre');
INSERT INTO "Facility" (name) VALUES ('Viana do Castelo');
INSERT INTO "Facility" (name) VALUES ('Famalicão');
INSERT INTO "Facility" (name) VALUES ('Barroselas');
INSERT INTO "Facility" (name) VALUES ('Nine');
INSERT INTO "Facility" (name) VALUES ('Caminha');
INSERT INTO "Facility" (name) VALUES ('Carvalha');
INSERT INTO "Facility" (name) VALUES ('Carreço');

INSERT INTO "Rail Line" (ownerId, startFacilityId, endFacilityId) VALUES (503933813, 7, 5);
INSERT INTO "Rail Line" (ownerId, startFacilityId, endFacilityId) VALUES (503933813, 5, 20);
INSERT INTO "Rail Line" (ownerId, startFacilityId, endFacilityId) VALUES (503933813, 20, 8);
INSERT INTO "Rail Line" (ownerId, startFacilityId, endFacilityId) VALUES (503933813, 8, 17);
INSERT INTO "Rail Line" (ownerId, startFacilityId, endFacilityId) VALUES (503933813, 17, 21);
INSERT INTO "Rail Line" (ownerId, startFacilityId, endFacilityId) VALUES (503933813, 21, 16);
INSERT INTO "Rail Line" (ownerId, startFacilityId, endFacilityId) VALUES (503933813, 16, 11);

INSERT INTO "Rail Line Segment" (railLineId, trackTypeId, trackElectrificationId, gaugeId, "order", length, maxWeight, speedLimit) VALUES (1, 2, 1, 2, 1, 2618, 8000, 150);
INSERT INTO "Rail Line Segment" (railLineId, trackTypeId, trackElectrificationId, gaugeId, "order", length, maxWeight, speedLimit) VALUES (2, 1, 1, 2, 1, 29003, 8000, 150);
INSERT INTO "Rail Line Segment" (railLineId, trackTypeId, trackElectrificationId, gaugeId, "order", length, maxWeight, speedLimit) VALUES (2, 1, 1, 2, 2, 10000, 8000, 150);
INSERT INTO "Rail Line Segment" (railLineId, trackTypeId, trackElectrificationId, gaugeId, "order", length, maxWeight, speedLimit) VALUES (3, 1, 1, 2, 1, 5286, 8000, 150);
INSERT INTO "Rail Line Segment" (railLineId, trackTypeId, trackElectrificationId, gaugeId, "order", length, maxWeight, speedLimit) VALUES (3, 1, 1, 2, 2, 6000, 8000, 150);
INSERT INTO "Rail Line Segment" (railLineId, trackTypeId, trackElectrificationId, gaugeId, "order", length, maxWeight, speedLimit) VALUES (4, 1, 1, 2, 1, 10387, 8000, 150);
INSERT INTO "Rail Line Segment" (railLineId, trackTypeId, trackElectrificationId, gaugeId, "order", length, maxWeight, speedLimit) VALUES (4, 1, 1, 2, 2, 12000, 8000, 150);
INSERT INTO "Rail Line Segment" (railLineId, trackTypeId, trackElectrificationId, gaugeId, "order", length, maxWeight, speedLimit) VALUES (4, 1, 1, 2, 3, 8000, 6400, 150);
INSERT INTO "Rail Line Segment" (railLineId, trackTypeId, trackElectrificationId, gaugeId, "order", length, maxWeight, speedLimit) VALUES (5, 1, 1, 2, 1, 6000, 8000, 150);
INSERT INTO "Rail Line Segment" (railLineId, trackTypeId, trackElectrificationId, gaugeId, "order", length, maxWeight, speedLimit) VALUES (5, 1, 1, 2, 2, 3000, 8000, 150);
INSERT INTO "Rail Line Segment" (railLineId, trackTypeId, trackElectrificationId, gaugeId, "order", length, maxWeight, speedLimit) VALUES (5, 1, 1, 2, 3, 15000, 8000, 150);
INSERT INTO "Rail Line Segment" (railLineId, trackTypeId, trackElectrificationId, gaugeId, "order", length, maxWeight, speedLimit) VALUES (6, 1, 1, 2, 1, 20829, 8000, 150);
INSERT INTO "Rail Line Segment" (railLineId, trackTypeId, trackElectrificationId, gaugeId, "order", length, maxWeight, speedLimit) VALUES (7, 1, 1, 2, 1, 4264, 8000, 150);

INSERT INTO "Wagon Model" (wagonModelId, wagonTypeId, gaugeId, name, payload, volumeCapacity, tare, length, width, height, weight, maxSpeed) VALUES (1245, 6, 2, 'Tadgs 32 94 082 3', 56, 75, 500, 17.240, 3.072, 4.270, 24, 120);
INSERT INTO "Wagon Model" (wagonModelId, wagonTypeId, gaugeId, name, payload, volumeCapacity, tare, length, width, height, weight, maxSpeed) VALUES (1278, 6, 2, 'Tdgs 41 94 074 1', 26.2, 38, 300, 9.640, 3.120, 4.1655, 13.8, 100);
INSERT INTO "Wagon Model" (wagonModelId, wagonTypeId, gaugeId, name, payload, volumeCapacity, tare, length, width, height, weight, maxSpeed) VALUES (1325, 7, 2, 'Gabs 81 94 181 1', 50.2, 110, 250, 21.700, 3.180, 4.170, 29.8, 100);
INSERT INTO "Wagon Model" (wagonModelId, wagonTypeId, gaugeId, name, payload, volumeCapacity, tare, length, width, height, weight, maxSpeed) VALUES (1104, 1, 2, 'Regmms 32 94 356 3', 60.6, 76.3, 250, 14.040, 3.104, 2.535, 21.2, 120);
INSERT INTO "Wagon Model" (wagonModelId, wagonTypeId, gaugeId, name, payload, volumeCapacity, tare, length, width, height, weight, maxSpeed) VALUES (985, 1, 2, 'Lgs 22 94 441 6', 28.1, 76.3, 250, 13.860, 2.850, 1.060, 11.9, 120);
INSERT INTO "Wagon Model" (wagonModelId, wagonTypeId, gaugeId, name, payload, volumeCapacity, tare, length, width, height, weight, maxSpeed) VALUES (987, 1, 2, 'Sgnss 12 94 455 2', 68.4, 76.3, 250, 18.116, 2.950, 1.030, 21.6, 120);
INSERT INTO "Wagon Model" (wagonModelId, wagonTypeId, gaugeId, name, payload, volumeCapacity, tare, length, width, height, weight, maxSpeed) VALUES (988, 1, 1, 'Sgnss 12 94 455 2', 68.4, 76.3, 250, 18.116, 2.950, 1.030, 21.6, 120);

INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (3563077, 509017800, 1104);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (3563078, 509017800, 1104);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (3563079, 509017800, 1104);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (3563080, 509017800, 1104);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (3563081, 509017800, 1104);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (3563082, 509017800, 1104);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (3563083, 509017800, 1104);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (3563084, 509017800, 1104);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (3563085, 509017800, 1104);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (3563086, 509017800, 1104);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (3563087, 509017800, 1104);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (3563088, 509017800, 1104);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (3563089, 509017800, 1104);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (3563090, 509017800, 1104);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (3563091, 509017800, 1104);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (3563092, 509017800, 1104);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (823045, 509017800, 1245);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (823046, 509017800, 1245);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (823047, 509017800, 1245);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (823048, 509017800, 1245);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (741001, 509017800, 1278);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (741002, 509017800, 1278);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (741003, 509017800, 1278);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (741004, 509017800, 1278);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (741005, 509017800, 1278);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (741006, 509017800, 1278);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (1811010, 509017800, 1325);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (1811011, 509017800, 1325);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (1811012, 509017800, 1325);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (1811013, 509017800, 1325);
INSERT INTO "Wagon" (wagonId, operatorId, wagonModelId) VALUES (1811014, 509017800, 1325);

INSERT INTO "Locomotive Model" (makeId, fuelTypeId, name, power, acceleration, length, width, height, weight, fuelCapacity) VALUES (6, 2, 'Eurosprinter', 5600, 50, 19.2, 3, 4.375, 87, null);
INSERT INTO "Locomotive Model" (makeId, fuelTypeId, name, power, acceleration, length, width, height, weight, fuelCapacity) VALUES (7, 1, 'CP 1900', 1623, 11, 19.084, 3.062, 4.31, 117, 4882);

INSERT INTO "Locomotive" (locomotiveId, operatorId, locomotiveModelId, yearOfService) VALUES (5621, 509017800, 1, 1995);
INSERT INTO "Locomotive" (locomotiveId, operatorId, locomotiveModelId, yearOfService) VALUES (5623, 509017800, 1, 1995);
INSERT INTO "Locomotive" (locomotiveId, operatorId, locomotiveModelId, yearOfService) VALUES (5630, 509017800, 1, 1996);
INSERT INTO "Locomotive" (locomotiveId, operatorId, locomotiveModelId, yearOfService) VALUES (1903, 509017800, 2, 1981);

INSERT INTO "Locomotive Model_Gauge"(gaugeId, locomotiveModelId) VALUES (2, 1);
INSERT INTO "Locomotive Model_Gauge"(gaugeId, locomotiveModelId) VALUES (2, 2);