CREATE TABLE "Rail Line" (
  railLineId number(10) GENERATED AS IDENTITY,
  ownerId number(10) NOT NULL,
  startFacilityId number(10) NOT NULL,
  endFacilityId number(10) NOT NULL,
  PRIMARY KEY (railLineId)
);

CREATE TABLE "Rail Line Segment" (
  railSegmentId number(10) GENERATED AS IDENTITY,
  railLineId number(10) NOT NULL,
  trackTypeId number(10) NOT NULL,
  trackElectrificationId number(10) NOT NULL,
  gaugeId number(10) NOT NULL,
  "order" number(10) NOT NULL,
  length number(10) NOT NULL,
  maxWeight number(10) NOT NULL,
  speedLimit number(10) NOT NULL,
  PRIMARY KEY (railSegmentId),
  CONSTRAINT chk_segSpeedLimit CHECK (speedLimit > 0),
  CONSTRAINT chk_segLength CHECK (length > 0),
  CONSTRAINT chk_order CHECK ("order" > 0),
  CONSTRAINT chk_segMaxWeigth CHECK (maxWeight > 0)
);

CREATE TABLE "Track Type" (
  trackTypeId number(10) GENERATED AS IDENTITY,
  description nvarchar2(255) NOT NULL,
  PRIMARY KEY (trackTypeId),
  CONSTRAINT chk_trackType_desc CHECK (description IN ('single-track', 'double-track'))
);

CREATE TABLE "Track Electrification" (
  trackElectrificationId number(10) GENERATED AS IDENTITY,
  description nvarchar2(255) NOT NULL,
  PRIMARY KEY (trackElectrificationId),
  CONSTRAINT chk_trackElectrification_desc CHECK (
    description IN ('electrified', 'non-electrified')
  )
);

CREATE TABLE "Owner" (
  ownerId number(10) NOT NULL,
  name nvarchar2(255) NOT NULL,
  shortName nvarchar2(255) NOT NULL,
  PRIMARY KEY (ownerId)
);

CREATE TABLE "Locomotive Model_Gauge" (
  gaugeId number(10) NOT NULL,
  locomotiveModelId number(10) NOT NULL,
  PRIMARY KEY (gaugeId, locomotiveModelId)
);

CREATE TABLE "Fuel Type" (
  fuelTypeId number(10) GENERATED AS IDENTITY,
  description nvarchar2(255) NOT NULL,
  PRIMARY KEY (fuelTypeId),
  CONSTRAINT chk_fuelType_desc CHECK (description IN ('diesel', 'electric'))
);

CREATE TABLE "Locomotive" (
  locomotiveId number(10) NOT NULL,
  operatorId number(10) NOT NULL,
  locomotiveModelId number(10) NOT NULL,
  yearOfService number(4) NOT NULL,
  PRIMARY KEY (locomotiveId),
  CONSTRAINT chk_year CHECK (yearOfService > 0)
);

CREATE TABLE "Gauge" (
  gaugeId number(10) GENERATED AS IDENTITY,
  width number(10) NOT NULL,
  PRIMARY KEY (gaugeId),
  CONSTRAINT chk_gaugeWidth CHECK (width > 0)
);

CREATE TABLE "Wagon" (
  wagonId number(10) NOT NULL,
  operatorId number(10) NOT NULL,
  wagonModelId number(10) NOT NULL,
  PRIMARY KEY (wagonId)
);

CREATE TABLE "Locomotive Model" (
  locomotiveModelId number(10) GENERATED AS IDENTITY,
  makeId number(10) NOT NULL,
  fuelTypeId number(10) NOT NULL,
  name nvarchar2(255) NOT NULL,
  power number(10) NOT NULL,
  acceleration number(10) NOT NULL,
  length number(10) NOT NULL,
  width number(10) NOT NULL,
  height number(10) NOT NULL,
  weight number(10) NOT NULL,
  fuelCapacity number(10),
  PRIMARY KEY (locomotiveModelId),
  CONSTRAINT chk_fuelCapacity CHECK (fuelCapacity > 0),
  CONSTRAINT chk_locoModelWidth CHECK (width > 0),
  CONSTRAINT chk_locoModelLength CHECK (length > 0),
  CONSTRAINT chk_power CHECK (power > 0),
  CONSTRAINT chk_locoModelWeigth CHECK (weight > 0),
  CONSTRAINT chk_locoModelHeight CHECK (height > 0),
  CONSTRAINT chk_acceleration CHECK (acceleration > 0)
);

CREATE TABLE "Make" (
  makeId number(10) GENERATED AS IDENTITY,
  name nvarchar2(255) NOT NULL,
  PRIMARY KEY (makeId)
);

CREATE TABLE "Facility" (
  facilityId number(10) GENERATED AS IDENTITY,
  name nvarchar2(255) NOT NULL,
  PRIMARY KEY (facilityId)
);

CREATE TABLE "Wagon Model" (
  wagonModelId number(10),
  wagonTypeId number(10) NOT NULL,
  gaugeId number(10) NOT NULL,
  name nvarchar2(255) NOT NULL,
  payload number(10) NOT NULL,
  volumeCapacity number(10) NOT NULL,
  tare number(10) NOT NULL,
  length number(10) NOT NULL,
  width number(10) NOT NULL,
  height number(10) NOT NULL,
  weight number(10) NOT NULL,
  maxSpeed number(10) NOT NULL,
  PRIMARY KEY (wagonModelId),
  CONSTRAINT chk_wagonModelLength CHECK (length > 0),
  CONSTRAINT chk_wagonModelMaxSpeed CHECK (maxSpeed > 0),
  CONSTRAINT chk_tare CHECK (tare > 0),
  CONSTRAINT chk_wagonModelWeigth CHECK (weight > 0),
  CONSTRAINT chk_wagonModelHeight CHECK (height > 0),
  CONSTRAINT chk_payload CHECK (payload > 0),
  CONSTRAINT chk_volumeCapacity CHECK (volumeCapacity > 0),
  CONSTRAINT chk_wagonModelWidth CHECK (width > 0)
);

CREATE TABLE "Wagon Type" (
  wagonTypeId number(10) GENERATED AS IDENTITY,
  description nvarchar2(255) NOT NULL,
  PRIMARY KEY (wagonTypeId),
  CONSTRAINT chk_wagonType_desc CHECK (
    description IN (
      'Box Car',
      'Flat Car',
      'Chemical Tank Car',
      'Mineral Oil Car',
      'Pressure Gas Car',
      'Hopper Car',
      'Refrigerated Car'
    )
  )
);

CREATE TABLE "Operator" (
  operatorId number(10),
  name nvarchar2(255) NOT NULL,
  shortName nvarchar2(255) NOT NULL,
  PRIMARY KEY (operatorId)
);

ALTER TABLE "Rail Line Segment"
  ADD CONSTRAINT FK_RailLineSegment_railLineId_RailLine
  FOREIGN KEY (railLineId) REFERENCES "Rail Line" (railLineId);

ALTER TABLE "Rail Line Segment"
  ADD CONSTRAINT FK_RailLineSegment_trackTypeId_TrackType
  FOREIGN KEY (trackTypeId) REFERENCES "Track Type" (trackTypeId);

ALTER TABLE "Rail Line Segment"
  ADD CONSTRAINT FK_RailLineSegment_trackElectrificationId_TrackElectrification
  FOREIGN KEY (trackElectrificationId) REFERENCES "Track Electrification" (trackElectrificationId);

ALTER TABLE "Rail Line Segment"
  ADD CONSTRAINT FK_RailLineSegment_gaugeId_Gauge
  FOREIGN KEY (gaugeId) REFERENCES "Gauge" (gaugeId);

ALTER TABLE "Locomotive Model_Gauge"
  ADD CONSTRAINT FK_LocomotiveModelGauge_gaugeId_Gauge
  FOREIGN KEY (gaugeId) REFERENCES "Gauge" (gaugeId);

ALTER TABLE "Locomotive Model"
  ADD CONSTRAINT FK_LocomotiveModel_fuelTypeId_FuelType
  FOREIGN KEY (fuelTypeId) REFERENCES "Fuel Type" (fuelTypeId);

ALTER TABLE "Locomotive"
  ADD CONSTRAINT FK_Locomotive_locomotiveModelId_LocomotiveModel
  FOREIGN KEY (locomotiveModelId) REFERENCES "Locomotive Model" (locomotiveModelId);

ALTER TABLE "Locomotive Model"
  ADD CONSTRAINT FK_LocomotiveModel_makeId_Make
  FOREIGN KEY (makeId) REFERENCES "Make" (makeId);

ALTER TABLE "Locomotive Model_Gauge"
  ADD CONSTRAINT FK_LocomotiveModelGauge_locomotiveModelId_LocomotiveModel
  FOREIGN KEY (locomotiveModelId) REFERENCES "Locomotive Model" (locomotiveModelId);

ALTER TABLE "Rail Line"
  ADD CONSTRAINT FK_RailLine_startFacilityId_Facility
  FOREIGN KEY (startFacilityId) REFERENCES "Facility" (facilityId);

ALTER TABLE "Rail Line"
  ADD CONSTRAINT FK_RailLine_endFacilityId_Facility
  FOREIGN KEY (endFacilityId) REFERENCES "Facility" (facilityId);

ALTER TABLE "Wagon Model"
  ADD CONSTRAINT FK_WagonModel_gaugeId_Gauge
  FOREIGN KEY (gaugeId) REFERENCES "Gauge" (gaugeId);

ALTER TABLE "Wagon"
  ADD CONSTRAINT FK_Wagon_wagonModelId_WagonModel
  FOREIGN KEY (wagonModelId) REFERENCES "Wagon Model" (wagonModelId);

ALTER TABLE "Wagon Model"
  ADD CONSTRAINT FK_WagonModel_wagonTypeId_WagonType
  FOREIGN KEY (wagonTypeId) REFERENCES "Wagon Type" (wagonTypeId);

ALTER TABLE "Rail Line"
  ADD CONSTRAINT FK_RailLine_ownerId_Owner 
  FOREIGN KEY (ownerId) REFERENCES "Owner" (ownerId);

ALTER TABLE "Wagon"
  ADD CONSTRAINT FK_Wagon_operatorId_Operator
  FOREIGN KEY (operatorId) REFERENCES "Operator" (operatorId);

ALTER TABLE "Locomotive"
  ADD CONSTRAINT FK_Locomotive_operatorId_Operator
  FOREIGN KEY (operatorId) REFERENCES "Operator" (operatorId);