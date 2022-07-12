alter table plant_pictures
    add constraint fk_pictures_plant foreign key (plant_plant_id) references plant (plant_id);

alter table plant_name
    add constraint fk_name_plant foreign key (plant_plant_id) references plant (plant_id);

alter table sensor
    add constraint fk_sensor_plant foreign key (plant_plant_id) references plant (plant_id);

alter table sensor_reading
    add constraint fk_sensor_reading foreign key (sensor_sensor_id) references sensor (sensor_id);