CREATE TABLE Owner (
  vatNumber nvarchar2(255) NOT NULL,
  name nvarchar2(255) NOT NULL UNIQUE,
  shortName nvarchar2(255) NOT NULL,
  PRIMARY KEY (vatNumber)
);

CREATE TABLE Operator (
  vatNumber nvarchar2(255) NOT NULL,
  name nvarchar2(255) NOT NULL UNIQUE,
  shortName nvarchar2(255) NOT NULL,
  PRIMARY KEY (vatNumber)
);

CREATE TABLE Make (
  makeId number(10) GENERATED AS IDENTITY,
  name nvarchar2(255) NOT NULL UNIQUE,
  PRIMARY KEY (makeId)
);

CREATE TABLE Gauge (
  gaugeName nvarchar2(255) NOT NULL,
  width number(10) NOT NULL UNIQUE,
  PRIMARY KEY (gaugeName),
  CONSTRAINT CHK_GAUGE_WIDTH CHECK (width > 0)
);

CREATE TABLE Wagon_Type (
  wagonTypeId number(10) GENERATED AS IDENTITY,
  description nvarchar2(255) NOT NULL,
  PRIMARY KEY (wagonTypeId),
  CONSTRAINT CHK_WAGONTYPE_DESC CHECK (
    description IN (
      'Box Car','Flat Car','Chemical Tank Car','Mineral Oil Car','Pressure Gas Car','Hopper Car','Refrigerated Car','Grain Car'
    )
  )
);

CREATE TABLE Dimension (
  length float(20) NOT NULL,
  width float(20) NOT NULL,
  height float(20) NOT NULL,
  PRIMARY KEY (length, width, height),
  CONSTRAINT CHK_DIM_HEIGHT CHECK (height > 0),
  CONSTRAINT CHK_DIM_WIDTH CHECK (width > 0),
  CONSTRAINT CHK_DIM_LENGTH CHECK (length > 0)
);

CREATE TABLE Facility (
  facilityId number(10) NOT NULL,
  name nvarchar2(255) NOT NULL UNIQUE,
  hasWarehouse number(1) NOT NULL,
  hasGrainSilo number(1) NOT NULL,
  hasRefrigeratedArea number(1) NOT NULL,
  PRIMARY KEY (facilityId),
  CONSTRAINT CHK_FAC_WAREHOUSE CHECK (hasWarehouse IN (0,1)),
  CONSTRAINT CHK_FAC_REFRIG CHECK (hasRefrigeratedArea IN (0,1)),
  CONSTRAINT CHK_FAC_SILO CHECK (hasGrainSilo IN (0,1))
);

CREATE TABLE Path (
  pathId number(10) NOT NULL,
  endpointA number(10) NOT NULL,
  endpointB number(10) NOT NULL,
  PRIMARY KEY (pathId),
  CONSTRAINT FK_PATH_ENDPOINTA FOREIGN KEY (endpointA) REFERENCES Facility (facilityId),
  CONSTRAINT FK_PATH_ENDPOINTB FOREIGN KEY (endpointB) REFERENCES Facility (facilityId)
);

CREATE TABLE Route (
  routeId number(10) NOT NULL,
  pathId number(10) NOT NULL,
  PRIMARY KEY (routeId),
  CONSTRAINT FK_ROUTE_PATH FOREIGN KEY (pathId) REFERENCES Path (pathId)
);

CREATE TABLE Train (
  trainId number(10) NOT NULL,
  routeId number(10) NOT NULL,
  startDate date NOT NULL,
  PRIMARY KEY (trainId),
  CONSTRAINT FK_TRAIN_ROUTE FOREIGN KEY (routeId) REFERENCES Route (routeId)
);

CREATE TABLE Freight (
  freightId number(10) NOT NULL,
  endpointA number(10) NOT NULL,
  endpointB number(10) NOT NULL,
  PRIMARY KEY (freightId),
  CONSTRAINT FK_FREIGHT_ENDPOINTA FOREIGN KEY (endpointA) REFERENCES Facility (facilityId),
  CONSTRAINT FK_FREIGHT_ENDPOINTB FOREIGN KEY (endpointB) REFERENCES Facility (facilityId)
);

CREATE TABLE Path_Point (
  pathId number(10) NOT NULL,
  "order" number(10) NOT NULL,
  facilityId number(10) NOT NULL,
  arriveInstant date NOT NULL,
  leaveInstant date,
  PRIMARY KEY (pathId, "order"),
  CONSTRAINT CHK_PATHPOINT_ORDER CHECK ("order" > 0),
  CONSTRAINT FK_PATHPOINT_PATH FOREIGN KEY (pathId) REFERENCES Path (pathId),
  CONSTRAINT FK_PATHPOINT_FACILITY FOREIGN KEY (facilityId) REFERENCES Facility (facilityId)
);

CREATE TABLE Track_Type (
  trackTypeId number(10) GENERATED AS IDENTITY,
  description nvarchar2(255) NOT NULL,
  PRIMARY KEY (trackTypeId),
  CONSTRAINT CHK_TRACKTYPE_DESC CHECK (description IN ('single-track', 'double-track'))
);

CREATE TABLE Rail_Line (
  railLineId number(10) NOT NULL,
  ownerId nvarchar2(255) NOT NULL,
  startFacilityId number(10) NOT NULL,
  endFacilityId number(10) NOT NULL,
  name nvarchar2(255) NOT NULL UNIQUE,
  PRIMARY KEY (railLineId),
  CONSTRAINT FK_RAILLINE_OWNER         FOREIGN KEY (ownerId) REFERENCES Owner (vatNumber),
  CONSTRAINT FK_RAILLINE_STARTFAC      FOREIGN KEY (startFacilityId) REFERENCES Facility (facilityId),
  CONSTRAINT FK_RAILLINE_ENDFAC        FOREIGN KEY (endFacilityId) REFERENCES Facility (facilityId)
);

CREATE TABLE Rail_Line_Segment (
  railLineId number(10) NOT NULL,
  "order" number(10) NOT NULL,
  trackTypeId number(10) NOT NULL,
  gaugeName nvarchar2(255) NOT NULL,
  trackElectrification number(1) NOT NULL,
  length number(10) NOT NULL,
  maxWeight number(10) NOT NULL,
  speedLimit number(10) NOT NULL,
  PRIMARY KEY (railLineId, "order"),
  CONSTRAINT CHK_SEG_SPEEDLIMIT CHECK (speedLimit > 0),
  CONSTRAINT CHK_SEG_LENGTH CHECK (length > 0),
  CONSTRAINT CHK_SEG_ORDER CHECK ("order" > 0),
  CONSTRAINT CHK_SEG_ELEC CHECK (trackElectrification IN (0,1)),
  CONSTRAINT CHK_SEG_MAXWEIGHT CHECK (maxWeight > 0),
  CONSTRAINT FK_SEG_TRACKTYPE FOREIGN KEY (trackTypeId) REFERENCES Track_Type (trackTypeId),
  CONSTRAINT FK_SEG_GAUGE FOREIGN KEY (gaugeName) REFERENCES Gauge (gaugeName),
  CONSTRAINT FK_SEG_RAILLINE FOREIGN KEY (railLineId) REFERENCES Rail_Line (railLineId)
);

CREATE TABLE Locomotive_Model (
  locomotiveModelId number(10) GENERATED AS IDENTITY,
  makeId number(10) NOT NULL,
  length float(20) NOT NULL,
  width float(20) NOT NULL,
  height float(20) NOT NULL,
  name nvarchar2(255) NOT NULL UNIQUE,
  weight number(10) NOT NULL,
  power number(10) NOT NULL,
  acceleration number(10) NOT NULL,
  PRIMARY KEY (locomotiveModelId),
  CONSTRAINT CHK_LOCOMODEL_POWER CHECK (power > 0),
  CONSTRAINT CHK_LOCOMODEL_WEIGHT CHECK (weight > 0),
  CONSTRAINT CHK_LOCOMODEL_ACCEL CHECK (acceleration > 0),
  CONSTRAINT FK_LOCOMODEL_MAKE FOREIGN KEY (makeId) REFERENCES Make (makeId),
  CONSTRAINT FK_LOCOMODEL_DIM FOREIGN KEY (length, width, height) REFERENCES Dimension (length, width, height)
);

CREATE TABLE Electric_Locomotive_Model (
  locomotiveModelId number(10) NOT NULL,
  voltage number(10) NOT NULL,
  frequency number(10) NOT NULL,
  PRIMARY KEY (locomotiveModelId),
  CONSTRAINT CHK_ELEC_VOLT CHECK (voltage > 0),
  CONSTRAINT CHK_ELEC_FREQ CHECK (frequency > 0),
  CONSTRAINT FK_ELEC_LOC_MODEL FOREIGN KEY (locomotiveModelId) REFERENCES Locomotive_Model (locomotiveModelId)
);

CREATE TABLE Diesel_Locomotive_Model (
  locomotiveModelId number(10) NOT NULL,
  fuelCapacity number(10) NOT NULL,
  PRIMARY KEY (locomotiveModelId),
  CONSTRAINT CHK_DIESEL_FUEL CHECK (fuelCapacity > 0),
  CONSTRAINT FK_DIESEL_LOC_MODEL FOREIGN KEY (locomotiveModelId) REFERENCES Locomotive_Model (locomotiveModelId)
);

CREATE TABLE Locomotive_Model_Gauge (
  locomotiveModelId number(10) NOT NULL,
  gaugeName nvarchar2(255) NOT NULL,
  PRIMARY KEY (locomotiveModelId, gaugeName),
  CONSTRAINT FK_LMG_MODEL FOREIGN KEY (locomotiveModelId) REFERENCES Locomotive_Model (locomotiveModelId),
  CONSTRAINT FK_LMG_GAUGE FOREIGN KEY (gaugeName) REFERENCES Gauge (gaugeName)
);

CREATE TABLE Locomotive (
  locomotiveId number(10) NOT NULL,
  operatorId nvarchar2(255) NOT NULL,
  locomotiveModelId number(10) NOT NULL,
  yearOfService number(4) NOT NULL,
  PRIMARY KEY (locomotiveId),
  CONSTRAINT CHK_LOCO_YEAR CHECK (yearOfService > 0),
  CONSTRAINT FK_LOCO_OPERATOR FOREIGN KEY (operatorId) REFERENCES Operator (vatNumber),
  CONSTRAINT FK_LOCO_MODEL FOREIGN KEY (locomotiveModelId) REFERENCES Locomotive_Model (locomotiveModelId)
);

CREATE TABLE Wagon_Model (
  wagonModelId number(10) NOT NULL,
  wagonTypeId number(10) NOT NULL,
  length float(20) NOT NULL,
  width float(20) NOT NULL,
  height float(20) NOT NULL,
  name nvarchar2(255) NOT NULL UNIQUE,
  weight float(20) NOT NULL,
  payload float(20) NOT NULL,
  volumeCapacity float(20) NOT NULL,
  tare float(20) NOT NULL,
  maxSpeed number(10) NOT NULL,
  PRIMARY KEY (wagonModelId),
  CONSTRAINT CHK_WAGONMODEL_MAXSPEED CHECK (maxSpeed > 0),
  CONSTRAINT CHK_WAGONMODEL_TARE CHECK (tare > 0),
  CONSTRAINT CHK_WAGONMODEL_WEIGHT CHECK (weight > 0),
  CONSTRAINT CHK_WAGONMODEL_PAYLOAD CHECK (payload > 0),
  CONSTRAINT CHK_WAGONMODEL_VOL CHECK (volumeCapacity > 0),
  CONSTRAINT FK_WMODEL_TYPE FOREIGN KEY (wagonTypeId) REFERENCES Wagon_Type (wagonTypeId),
  CONSTRAINT FK_WMODEL_DIM FOREIGN KEY (length, width, height) REFERENCES Dimension (length, width, height)
);

CREATE TABLE Wagon (
  wagonId number(10) NOT NULL,
  operatorId nvarchar2(255) NOT NULL,
  wagonModelId number(10) NOT NULL,
  PRIMARY KEY (wagonId),
  CONSTRAINT FK_WAGON_OPERATOR FOREIGN KEY (operatorId) REFERENCES Operator (vatNumber),
  CONSTRAINT FK_WAGON_MODEL FOREIGN KEY (wagonModelId) REFERENCES Wagon_Model (wagonModelId)
);

CREATE TABLE Wagon_Model_Gauge (
  wagonModelId number(10) NOT NULL,
  gaugeName nvarchar2(255) NOT NULL,
  PRIMARY KEY (wagonModelId, gaugeName),
  CONSTRAINT FK_WMG_MODEL FOREIGN KEY (wagonModelId) REFERENCES Wagon_Model (wagonModelId),
  CONSTRAINT FK_WMG_GAUGE FOREIGN KEY (gaugeName) REFERENCES Gauge (gaugeName)
);

CREATE TABLE Freight_Wagon (
  freightId number(10) NOT NULL,
  wagonId number(10) NOT NULL,
  PRIMARY KEY (freightId, wagonId),
  CONSTRAINT FK_FW_FREIGHT FOREIGN KEY (freightId) REFERENCES Freight (freightId),
  CONSTRAINT FK_FW_WAGON FOREIGN KEY (wagonId) REFERENCES Wagon (wagonId)
);

CREATE TABLE Locomotive_Train (
  locomotiveId number(10) NOT NULL,
  trainId number(10) NOT NULL,
  PRIMARY KEY (locomotiveId, trainId),
  CONSTRAINT FK_LT_LOCO FOREIGN KEY (locomotiveId) REFERENCES Locomotive (locomotiveId),
  CONSTRAINT FK_LT_TRAIN FOREIGN KEY (trainId) REFERENCES Train (trainId)
);

CREATE TABLE Route_Freight (
  routeId number(10) NOT NULL,
  freightId number(10) NOT NULL,
  PRIMARY KEY (routeId, freightId),
  CONSTRAINT FK_RF_ROUTE FOREIGN KEY (routeId) REFERENCES Route (routeId),
  CONSTRAINT FK_RF_FREIGHT FOREIGN KEY (freightId) REFERENCES Freight (freightId)
);
