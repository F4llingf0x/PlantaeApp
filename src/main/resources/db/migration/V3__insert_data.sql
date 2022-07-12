INSERT INTO plant (is_deleted, maximum_air_humidity, maximum_soil_humidity, maximum_temperature, minimum_air_humidity,
                   minimum_soil_humidity, minimum_temperature, sun_requirement)
VALUES (false, 80, 80, 50, 10, 40, -10, 'FULLSUN');

INSERT
INTO plant_pictures (plant_plant_id, pictures)
VALUES (1,
        "https://cdn.shopify.com/s/files/1/0059/8835/2052/products/Canary-Island-Date-Palm-450w_650x.jpg?v=1612444420");

INSERT INTO plant_name (plant_plant_id, plant_name)
VALUES (1, "Phoenix");

INSERT INTO plant_name (plant_plant_id, plant_name)
VALUES (1, "canariensis");

INSERT INTO sensor (is_calibrated, is_deleted, last_calibrated, plant_plant_id, sampling_period, sensor_group,
                    sensor_type)
VALUES (false, false , null , 1, 200, 1, 'TEMPERATURE');