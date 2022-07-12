package hu.plantae.IT;

import hu.plantae.domain.SensorType;
import hu.plantae.dto.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SensorControllerRestTestTemplateIT {

    @Autowired
    TestRestTemplate restTemplate;

    private SensorCreateCommand sensorCreateCommand;
    private SensorCreateCommand sensor2CreateCommand;

    @BeforeEach
    void init() {

        sensorCreateCommand = new SensorCreateCommand(
                1,
                SensorType.TEMPERATURE,
                100
        );

        sensor2CreateCommand = new SensorCreateCommand(
                1,
                SensorType.HUMIDITY,
                200
        );

    }

    @Test
    @Order(1)
    void testSaveAndFindOne_Valid() {
        init();
        restTemplate.postForObject("/api/sensors/addSensor",
                sensorCreateCommand,
                SensorInfo.class);

        SensorInfo getSensor = restTemplate
                .getForObject("/api/sensors/{sensorId}", SensorInfo.class, "1");

        assertEquals(1, getSensor.getSensorId());
        assertEquals(SensorType.TEMPERATURE, getSensor.getSensorType());
        assertEquals(100, getSensor.getSamplingPeriod());

    }

    @Test
    @Order(2)
    void testSave2AndFindOne_Valid() {
        init();
        restTemplate.postForObject("/api/sensors/addSensor",
                sensor2CreateCommand,
                SensorInfo.class);

        SensorInfo getSensor = restTemplate
                .getForObject("/api/sensors/{sensorId}", SensorInfo.class, "2");

        assertEquals(2, getSensor.getSensorId());
        assertEquals(SensorType.HUMIDITY, getSensor.getSensorType());
        assertEquals(200, getSensor.getSamplingPeriod());
    }

    @Test
    @Order(3)
    void testFindAll_Valid() {
        SensorInfoReadingless[] sensorInfo = restTemplate.getForObject("/api/sensors/all",
                SensorInfoReadingless[].class);
        assertTrue(sensorInfo.length == 2);
    }
//
    @Test
    @Order(4)
    void testAddSensorReadings_Valid() {
        restTemplate.postForObject("/api/sensors/reading/1/5", null, SensorReadingInfo.class);
        restTemplate.postForObject("/api/sensors/reading/1/10", null, SensorReadingInfo.class);
        restTemplate.postForObject("/api/sensors/reading/1/15", null, SensorReadingInfo.class);
        restTemplate.postForObject("/api/sensors/reading/1/20", null, SensorReadingInfo.class);

        SensorInfo getSensor = restTemplate
                .getForObject("/api/sensors/{sensorId}", SensorInfo.class, "1");

        assertEquals(4, getSensor.getSensorReadings().size());
    }

    @Test
    @Order(5)
    void testModifySamplingPeriod_Valid() {
        init();
        restTemplate.put("/api/sensors/samplingPeriod/{sensorId}/{samplingPeriod}",
                null, "1","333");
        SensorInfo getSensor = restTemplate
                .getForObject("/api/sensors/{sensorId}", SensorInfo.class, "1");

        assertEquals(333, getSensor.getSamplingPeriod());
    }

    @Test
    @Order(6)
    void testCalibrate_Valid() {
        restTemplate.put("/api/sensors/calibrate/{sensorId}",
                null, "1");

        SensorInfo getSensor = restTemplate
                .getForObject("/api/sensors/{sensorId}", SensorInfo.class, "1");

        assertTrue(getSensor.isCalibrated());
    }


    @Test
    @Order(7)
    void testCheckSensors_Valid() {
        SensorInfo[] getSensor = restTemplate
                .getForObject("/api/sensors/checkSensors", SensorInfo[].class);

        assertEquals(getSensor.length, 1);
        assertEquals(getSensor[0].getSensorId(), 2);

    }

    @Test
    @Order(8)
    void testLastReading_Valid() {
        SensorReadingInfo getReading = restTemplate
                .getForObject("/api/sensors/lastReading/{sensorId}", SensorReadingInfo.class, "1");

        assertEquals(getReading.getValue(), 20);

    }

    @Test
    @Order(8)
    void testInitSensors_Valid() {
        SensorInitInfo[] getSensors = restTemplate
                .getForObject("/api/sensors/init/{sensorGroupId}", SensorInitInfo[].class, "1");
        assertEquals(getSensors.length, 2);
    }

    @Test
    @Order(9)
    void testDeleteSensor_Valid() {
        restTemplate.delete("/api/sensors/{id}","2");
        SensorInfoReadingless[] sensorInfo = restTemplate.getForObject("/api/sensors/all",
                SensorInfoReadingless[].class);

        assertTrue(sensorInfo.length == 1);

    }

    @Test
    @Order(10)
    void testSensorData_Valid() {
        SensorInfo getSensor = restTemplate.getForObject("/api/sensors/sensorData/{id}/{day}",
                SensorInfo.class, "1", "1");
        assertTrue(getSensor.getSensorReadings().size() == 4);
    }

}



