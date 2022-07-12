CREATE TABLE plant
(
    plant_id              integer NOT NULL auto_increment,
    is_deleted            bit     NOT NULL,
    maximum_air_humidity  double precision,
    maximum_soil_humidity double precision,
    maximum_temperature   double precision,
    minimum_air_humidity  double precision,
    minimum_soil_humidity double precision,
    minimum_temperature   double precision,
    sun_requirement       varchar(255),
    primary key (plant_id)
);

CREATE TABLE plant_name
(
    plant_plant_id integer NOT NULL,
    plant_name     varchar(255)
);

CREATE TABLE plant_pictures
(
    plant_plant_id integer NOT NULL,
    pictures       varchar(255)
);

CREATE TABLE sensor
(
    sensor_id       integer NOT NULL auto_increment,
    is_calibrated   bit     NOT NULL,
    is_deleted      bit     NOT NULL,
    last_calibrated datetime(6),
    sampling_period integer,
    sensor_group    integer,
    sensor_type     varchar(255),
    plant_plant_id  integer,
    primary key (sensor_id)
);

CREATE TABLE sensor_reading
(
    sensor_reading_id integer          NOT NULL auto_increment,
    time              datetime(6),
    value             double precision NOT NULL,
    sensor_sensor_id  integer,
    PRIMARY KEY (sensor_reading_id)
);