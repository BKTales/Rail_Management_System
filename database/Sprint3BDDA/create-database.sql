CREATE TABLE Owner (
    vatNumber nvarchar2(255),
    name nvarchar2(255) NOT NULL UNIQUE,
    shortName nvarchar2(255) NOT NULL,
    PRIMARY KEY (vatNumber)
);

CREATE TABLE Operator (
    vatNumber nvarchar2(255),
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
    gaugeName nvarchar2(255),
    width number(10) NOT NULL UNIQUE,
    PRIMARY KEY (gaugeName),
    CONSTRAINT CHK_GAUGE_WIDTH CHECK (width > 0)
);

CREATE TABLE Bogie (
    name nvarchar2(255),
    PRIMARY KEY (name)
);

CREATE TABLE Wagon_Type (
    wagonTypeId number(10) GENERATED AS IDENTITY,
    description nvarchar2(255) NOT NULL,
    PRIMARY KEY (wagonTypeId),
    CONSTRAINT CHK_WAGONTYPE_DESC CHECK (
        description IN (
            'Box Car',
            'Flat Car',
            'Chemical Tank Car',
            'Mineral Oil Car',
            'Pressure Gas Car',
            'Hopper Car',
            'Refrigerated Car',
            'Grain Car'
        )
    )
);

CREATE TABLE Building_Type (
    buildingTypeName nvarchar2(255),
    PRIMARY KEY (buildingTypeName)
);

CREATE TABLE Dimension (
    length float(20),
    width float(20),
    height float(20),
    PRIMARY KEY (length, width, height),
    CONSTRAINT CHK_DIM_LENGTH CHECK (length > 0),
    CONSTRAINT CHK_DIM_WIDTH CHECK (width > 0),
    CONSTRAINT CHK_DIM_HEIGHT CHECK (height > 0)
);

CREATE TABLE Facility (
    facilityId number(10),
    name nvarchar2(255) NOT NULL UNIQUE,
    PRIMARY KEY (facilityId)
);

CREATE TABLE Building (
    facilityId number(10),
    buildingTypeName nvarchar2(255),
    PRIMARY KEY (facilityId, buildingTypeName),
    CONSTRAINT FK_BUILDING_FACILITY FOREIGN KEY (facilityId) REFERENCES Facility (facilityId),
    CONSTRAINT FK_BUILDING_TYPE FOREIGN KEY (buildingTypeName) REFERENCES Building_Type (buildingTypeName)
);

CREATE TABLE Route (
    routeId number(10),
    startPoint number(10) NOT NULL,
    endPoint number(10) NOT NULL,
    PRIMARY KEY (routeId),
    CONSTRAINT FK_ROUTE_ENDPOINTA FOREIGN KEY (startPoint) REFERENCES Facility (facilityId),
    CONSTRAINT FK_ROUTE_ENDPOINTB FOREIGN KEY (endPoint) REFERENCES Facility (facilityId)
);

CREATE TABLE Route_Point (
    routeId number(10),
    "order" number(10),
    facilityId number(10) NOT NULL,
    PRIMARY KEY (routeId, "order"),
    CONSTRAINT UQ_UNIQUE_FACILITY_IN_ROUTE UNIQUE (routeId, facilityId),
    CONSTRAINT CHK_ROUTEPOINT_ORDER CHECK ("order" > 0),
    CONSTRAINT FK_ROUTEPOINT_ROUTE FOREIGN KEY (routeId) REFERENCES Route (routeId),
    CONSTRAINT FK_ROUTEPOINT_FACILITY FOREIGN KEY (facilityId) REFERENCES Facility (facilityId)
);

CREATE TABLE Train (
    trainId number(10),
    routeId number(10) NOT NULL,
    maxSize float(10) NOT NULL,
    startDate date NOT NULL,
    endDate date NOT NULL,
    PRIMARY KEY (trainId),
    CONSTRAINT CHK_TRAIN_MAXSIZE CHECK (maxSize > 0),
    CONSTRAINT FK_TRAIN_ROUTE FOREIGN KEY (routeId) REFERENCES Route (routeId)
);

CREATE TABLE Freight (
    freightId number(10),
    startPoint number(10) NOT NULL,
    endPoint number(10) NOT NULL,
    routeId number(10),
    PRIMARY KEY (freightId),
    CONSTRAINT FK_FREIGHT_ENDPOINTA FOREIGN KEY (startPoint) REFERENCES Facility (facilityId),
    CONSTRAINT FK_FREIGHT_ENDPOINTB FOREIGN KEY (endPoint) REFERENCES Facility (facilityId),
    CONSTRAINT FK_FREIGHT_ROUTE FOREIGN KEY (routeId) REFERENCES Route (routeId)
);

CREATE TABLE Rail_Line (
    railLineId number(10),
    ownerId nvarchar2(255) NOT NULL,
    startFacilityId number(10) NOT NULL,
    endFacilityId number(10) NOT NULL,
    name nvarchar2(255) NOT NULL UNIQUE,
    PRIMARY KEY (railLineId),
    CONSTRAINT FK_RAILLINE_OWNER FOREIGN KEY (ownerId) REFERENCES Owner (vatNumber),
    CONSTRAINT FK_RAILLINE_STARTFAC FOREIGN KEY (startFacilityId) REFERENCES Facility (facilityId),
    CONSTRAINT FK_RAILLINE_ENDFAC FOREIGN KEY (endFacilityId) REFERENCES Facility (facilityId)
);

CREATE TABLE Siding (
    sidingId number(10),
    startPosition float(20) NOT NULL,
    length float(20) NOT NULL,
    PRIMARY KEY (sidingId),
    CONSTRAINT CHK_SIDING_LENGTH CHECK (length > 0),
    CONSTRAINT CHK_SIDING_STARTPOS CHECK (startPosition >= 0)
);

CREATE TABLE Rail_Segment (
    segmentId number(10),
    railLineId number(10) NOT NULL,
    gaugeName nvarchar2(255) NOT NULL,
    sidingId number(10),
    "order" number(10) NOT NULL,
    trackElectrification number(1) NOT NULL,
    numberOfTracks number(10) NOT NULL,
    length number(10) NOT NULL,
    maxWeight number(10) NOT NULL,
    speedLimit number(10) NOT NULL,
    PRIMARY KEY (segmentId),
    CONSTRAINT CHK_LINESEG_ORDER CHECK ("order" > 0),
    CONSTRAINT CHK_UNIQUE_ORDER_RAIL_LINE UNIQUE ("order", railLineId),
    CONSTRAINT CHK_SEG_SPEEDLIMIT CHECK (speedLimit > 0),
    CONSTRAINT CHK_SEG_NUMTRACKS CHECK (numberOfTracks > 0),
    CONSTRAINT CHK_SEG_ELEC CHECK (trackElectrification IN (0, 1)),
    CONSTRAINT CHK_SEG_MAXWEIGHT CHECK (maxWeight > 0),
    CONSTRAINT FK_SEG_RAIL_LINE_ID FOREIGN KEY (railLineId) REFERENCES Rail_Line (railLineId),
    CONSTRAINT FK_SEG_GAUGE FOREIGN KEY (gaugeName) REFERENCES Gauge (gaugeName),
    CONSTRAINT FK_SEG_SIDING FOREIGN KEY (sidingId) REFERENCES Siding (sidingId)
);

CREATE TABLE Locomotive_Model (
    locomotiveModelId number(10) GENERATED AS IDENTITY,
    bogieName nvarchar2(255) NOT NULL,
    makeId number(10) NOT NULL,
    length float(20) NOT NULL,
    width float(20) NOT NULL,
    height float(20) NOT NULL,
    name nvarchar2(255) NOT NULL UNIQUE,
    weight number(10) NOT NULL,
    power number(10) NOT NULL,
    acceleration number(10) NOT NULL,
    numOfBogies number(10) NOT NULL,
    maxSpeed number(10) NOT NULL,
    PRIMARY KEY (locomotiveModelId),
    CONSTRAINT CHK_LOCOMODEL_BOGIES CHECK (numOfBogies > 0),
    CONSTRAINT CHK_LOCOMODEL_POWER CHECK (power > 0),
    CONSTRAINT CHK_LOCOMODEL_WEIGHT CHECK (weight > 0),
    CONSTRAINT CHK_LOCOMODEL_ACCEL CHECK (acceleration > 0),
    CONSTRAINT FK_LOCOMODEL_BOGIE FOREIGN KEY (bogieName) REFERENCES Bogie (name),
    CONSTRAINT FK_LOCOMODEL_MAKE FOREIGN KEY (makeId) REFERENCES Make (makeId),
    CONSTRAINT FK_LOCOMODEL_DIM FOREIGN KEY (length, width, height) REFERENCES Dimension (length, width, height)
);

CREATE TABLE Electric_Locomotive_Model (
    locomotiveModelId number(10),
    voltage number(10) NOT NULL,
    frequency number(10) NOT NULL,
    PRIMARY KEY (locomotiveModelId),
    CONSTRAINT CHK_ELEC_VOLT CHECK (voltage > 0),
    CONSTRAINT CHK_ELEC_FREQ CHECK (frequency > 0),
    CONSTRAINT FK_ELEC_LOC_MODEL FOREIGN KEY (locomotiveModelId) REFERENCES Locomotive_Model (locomotiveModelId)
);

CREATE TABLE Diesel_Locomotive_Model (
    locomotiveModelId number(10),
    fuelCapacity number(10) NOT NULL,
    PRIMARY KEY (locomotiveModelId),
    CONSTRAINT CHK_DIESEL_FUEL CHECK (fuelCapacity > 0),
    CONSTRAINT FK_DIESEL_LOC_MODEL FOREIGN KEY (locomotiveModelId) REFERENCES Locomotive_Model (locomotiveModelId)
);

CREATE TABLE Locomotive_Model_Gauge (
    locomotiveModelId number(10),
    gaugeName nvarchar2(255),
    PRIMARY KEY (locomotiveModelId, gaugeName),
    CONSTRAINT FK_LMG_MODEL FOREIGN KEY (locomotiveModelId) REFERENCES Locomotive_Model (locomotiveModelId),
    CONSTRAINT FK_LMG_GAUGE FOREIGN KEY (gaugeName) REFERENCES Gauge (gaugeName)
);

CREATE TABLE Locomotive (
    locomotiveId number(10),
    operatorId nvarchar2(255) NOT NULL,
    locomotiveModelId number(10) NOT NULL,
    startFacilityId number(10) NOT NULL,
    yearOfService number(4) NOT NULL,
    PRIMARY KEY (locomotiveId),
    CONSTRAINT CHK_LOCO_YEAR CHECK (yearOfService > 0),
    CONSTRAINT FK_LOCO_OPERATOR FOREIGN KEY (operatorId) REFERENCES Operator (vatNumber),
    CONSTRAINT FK_LOCO_MODEL FOREIGN KEY (locomotiveModelId) REFERENCES Locomotive_Model (locomotiveModelId),
    CONSTRAINT FK_LOCO_STARTFAC FOREIGN KEY (startFacilityId) REFERENCES Facility (facilityId)
);

CREATE TABLE Wagon_Model (
    wagonModelId number(10),
    bogieName nvarchar2(255) NOT NULL,
    makeId number(10) NOT NULL,
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
    numOfBogies number(10) NOT NULL,
    PRIMARY KEY (wagonModelId),
    CONSTRAINT CHK_WAGONMODEL_BOGIES CHECK (numOfBogies > 0),
    CONSTRAINT CHK_WAGONMODEL_MAXSPEED CHECK (maxSpeed > 0),
    CONSTRAINT CHK_WAGONMODEL_TARE CHECK (tare > 0),
    CONSTRAINT CHK_WAGONMODEL_WEIGHT CHECK (weight > 0),
    CONSTRAINT CHK_WAGONMODEL_PAYLOAD CHECK (payload > 0),
    CONSTRAINT CHK_WAGONMODEL_VOL CHECK (volumeCapacity > 0),
    CONSTRAINT FK_WMODEL_BOGIE FOREIGN KEY (bogieName) REFERENCES Bogie (name),
    CONSTRAINT FK_WMODEL_MAKE FOREIGN KEY (makeId) REFERENCES Make (makeId),
    CONSTRAINT FK_WMODEL_TYPE FOREIGN KEY (wagonTypeId) REFERENCES Wagon_Type (wagonTypeId),
    CONSTRAINT FK_WMODEL_DIM FOREIGN KEY (length, width, height) REFERENCES Dimension (length, width, height)
);

CREATE TABLE Wagon_Model_Gauge (
    wagonModelId number(10),
    gaugeName nvarchar2(255),
    PRIMARY KEY (wagonModelId, gaugeName),
    CONSTRAINT FK_WMG_MODEL FOREIGN KEY (wagonModelId) REFERENCES Wagon_Model (wagonModelId),
    CONSTRAINT FK_WMG_GAUGE FOREIGN KEY (gaugeName) REFERENCES Gauge (gaugeName)
);

CREATE TABLE Wagon (
    wagonId number(10),
    facilityId number(10) NOT NULL,
    operatorId nvarchar2(255) NOT NULL,
    wagonModelId number(10) NOT NULL,
    PRIMARY KEY (wagonId),
    CONSTRAINT FK_WAGON_FACILITY FOREIGN KEY (facilityId) REFERENCES Facility (facilityId),
    CONSTRAINT FK_WAGON_OPERATOR FOREIGN KEY (operatorId) REFERENCES Operator (vatNumber),
    CONSTRAINT FK_WAGON_MODEL FOREIGN KEY (wagonModelId) REFERENCES Wagon_Model (wagonModelId)
);

CREATE TABLE Wagon_Freight (
    wagonId number(10),
    freightId number(10),
    PRIMARY KEY (wagonId, freightId),
    CONSTRAINT FK_WF_WAGON FOREIGN KEY (wagonId) REFERENCES Wagon (wagonId),
    CONSTRAINT FK_WF_FREIGHT FOREIGN KEY (freightId) REFERENCES Freight (freightId)
);

CREATE TABLE Locomotive_Train (
    locomotiveId number(10),
    trainId number(10),
    PRIMARY KEY (locomotiveId, trainId),
    CONSTRAINT FK_LT_LOCO FOREIGN KEY (locomotiveId) REFERENCES Locomotive (locomotiveId),
    CONSTRAINT FK_LT_TRAIN FOREIGN KEY (trainId) REFERENCES Train (trainId)
);