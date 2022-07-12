package hu.plantae.IT;

import hu.plantae.domain.SensorType;
import hu.plantae.dto.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static hu.plantae.domain.SunRequirement.FULLSUN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlantControllerRestTestTemplateIT {

    @Autowired
    TestRestTemplate restTemplate;

    private List<String> firstPlantName = List.of("Tracheophytes",
            "Angiosperms",
            "Monocots",
            "Commelinids",
            "Arecales",
            "Arecaceae",
            "Phoenix",
            "canariensis");

    private List<String> secondPlantName = List.of("Tracheophytes",
            "Angiosperms",
            "Monocots",
            "Asparagales",
            "Asparagaceae",
            "Nolinoideae",
            "Dracaena",
            "draco");

    private PlantCreateCommand plantCreateCommand;
    private PlantCreateCommand plantSecondCreateCommand;
    private SensorCreateCommand sensorCreateCommand;
    private SensorCreateCommand sensor2CreateCommand;
    private SensorCreateCommand sensor3CreateCommand;
    private SensorCreateCommand sensor4CreateCommand;
    private SensorCreateCommand sensor5CreateCommand;
    private SensorCreateCommand sensor6CreateCommand;

    @BeforeEach
    void init() {
        plantCreateCommand = new PlantCreateCommand(firstPlantName, List.of("https://example.jpg"),
                10d,
                20d,
                30d,
                40d,
                50d,
                60d,
                FULLSUN);
        plantSecondCreateCommand = new PlantCreateCommand(secondPlantName, List.of("https://exampleSecond.jpg"),
                20d,
                30d,
                20d,
                30d,
                20d,
                30d,
                FULLSUN);

        sensorCreateCommand = new SensorCreateCommand(
                1,
                SensorType.TEMPERATURE,
                100
        );

        sensor2CreateCommand = new SensorCreateCommand(
                1,
                SensorType.TEMPERATURE,
                100
        );

        sensor3CreateCommand = new SensorCreateCommand(
                1,
                SensorType.SOILMOISTURE,
                100
        );

        sensor4CreateCommand = new SensorCreateCommand(
                1,
                SensorType.SOILMOISTURE,
                100
        );

        sensor5CreateCommand = new SensorCreateCommand(
                1,
                SensorType.HUMIDITY,
                100
        );

        sensor6CreateCommand = new SensorCreateCommand(
                1,
                SensorType.HUMIDITY,
                100
        );

    }

    @Test
    @Order(1)
    void testSaveAndFindOne_Valid() {
        init();
        restTemplate.postForObject("/api/plants/addPlant",
                plantCreateCommand,
                PlantInfo.class);

        PlantInfo getPlant = restTemplate.getForObject("/api/plants/{id}", PlantInfo.class, "1");

        assertEquals(1, getPlant.getPlantId());
        assertEquals(firstPlantName, getPlant.getPlantName());
        assertEquals(List.of("https://example.jpg"), getPlant.getPictures());
        assertEquals(getPlant.getMinimumTemperature(), 10);
        assertEquals(getPlant.getMaximumTemperature(), 20);
        assertEquals(getPlant.getMinimumAirHumidity(), 30);
        assertEquals(getPlant.getMaximumAirHumidity(), 40);
        assertEquals(getPlant.getMinimumSoilHumidity(), 50);
        assertEquals(getPlant.getMaximumSoilHumidity(), 60);
    }

    @Test
    @Order(2)
    void testSave2AndFindOne_Valid() {
        init();
        restTemplate.postForObject("/api/plants/addPlant",
                plantSecondCreateCommand,
                PlantInfo.class);

        PlantInfo getSecondPlant = restTemplate.getForObject("/api/plants/{id}", PlantInfo.class, "2");

        assertEquals(2, getSecondPlant.getPlantId());
        assertEquals(secondPlantName, getSecondPlant.getPlantName());
        assertEquals(List.of("https://exampleSecond.jpg"), getSecondPlant.getPictures());
        assertEquals(getSecondPlant.getMinimumTemperature(), 20);
        assertEquals(getSecondPlant.getMaximumTemperature(), 30);
        assertEquals(getSecondPlant.getMinimumAirHumidity(), 20);
        assertEquals(getSecondPlant.getMaximumAirHumidity(), 30);
        assertEquals(getSecondPlant.getMinimumSoilHumidity(), 20);
        assertEquals(getSecondPlant.getMaximumSoilHumidity(), 30);
    }

    @Test
    @Order(3)
    void testFindAll_Valid() {
        PlantInfo[] foundPlants = restTemplate.getForObject("/api/plants/all", PlantInfo[].class);
        assertTrue(foundPlants.length == 2);
    }

    @Test
    @Order(4)
    void testAssignSensor_Valid() {
        restTemplate.postForObject("/api/sensors/addSensor",
                sensorCreateCommand,
                SensorInfo.class);
        restTemplate.put("/api/plants/{plantId}/{sensorId}", PlantInfo.class, "1", "1");
        PlantInfo getPlant = restTemplate.getForObject("/api/plants/{id}", PlantInfo.class, "1");

        assertTrue(getPlant.getSensors().get(0).getSensorId() == 1);
    }

    @Test
    @Order(5)
    void testModifyPlant_Valid() {
        init();
        restTemplate.put("/api/plants/2", plantCreateCommand);
        PlantInfo getPlant = restTemplate.getForObject("/api/plants/{id}", PlantInfo.class, "2");

        assertEquals(2, getPlant.getPlantId());
        assertEquals(firstPlantName, getPlant.getPlantName());
        assertEquals(List.of("https://example.jpg"), getPlant.getPictures());
        assertEquals(getPlant.getMinimumTemperature(), 10);
        assertEquals(getPlant.getMaximumTemperature(), 20);
        assertEquals(getPlant.getMinimumAirHumidity(), 30);
        assertEquals(getPlant.getMaximumAirHumidity(), 40);
        assertEquals(getPlant.getMinimumSoilHumidity(), 50);
        assertEquals(getPlant.getMaximumSoilHumidity(), 60);
    }

    @Test
    @Order(6)
    void testDeletePlant_List_ListAll_Valid() {
        restTemplate.delete("/api/plants/2");
        PlantInfo[] getPlants = restTemplate.getForObject("/api/plants", PlantInfo[].class);
        PlantInfo[] getAllPlants = restTemplate.getForObject("/api/plants/all", PlantInfo[].class);

        assertTrue(getPlants.length == 1);
        assertTrue(getAllPlants.length == 2);
    }


    @Test
    @Order(7)
    void testWarnings_Valid() {

        restTemplate.postForObject("/api/sensors/addSensor",
                sensor2CreateCommand,
                SensorInfo.class);
        restTemplate.postForObject("/api/sensors/addSensor",
                sensor3CreateCommand,
                SensorInfo.class);
        restTemplate.postForObject("/api/sensors/addSensor",
                sensor4CreateCommand,
                SensorInfo.class);
        restTemplate.postForObject("/api/sensors/addSensor",
                sensor5CreateCommand,
                SensorInfo.class);
        restTemplate.postForObject("/api/sensors/addSensor",
                sensor6CreateCommand,
                SensorInfo.class);

        restTemplate.postForObject("/api/sensors/reading/1/-15", null, SensorReadingInfo.class);
        restTemplate.postForObject("/api/sensors/reading/2/65", null, SensorReadingInfo.class);
        restTemplate.postForObject("/api/sensors/reading/3/5", null, SensorReadingInfo.class);
        restTemplate.postForObject("/api/sensors/reading/4/90", null, SensorReadingInfo.class);
        restTemplate.postForObject("/api/sensors/reading/5/5", null, SensorReadingInfo.class);
        restTemplate.postForObject("/api/sensors/reading/6/95", null, SensorReadingInfo.class);

        restTemplate.put("/api/plants/{plantId}/{sensorId}", PlantInfo.class, "1", "2");
        restTemplate.put("/api/plants/{plantId}/{sensorId}", PlantInfo.class, "1", "3");
        restTemplate.put("/api/plants/{plantId}/{sensorId}", PlantInfo.class, "1", "4");
        restTemplate.put("/api/plants/{plantId}/{sensorId}", PlantInfo.class, "1", "5");
        restTemplate.put("/api/plants/{plantId}/{sensorId}", PlantInfo.class, "1", "6");
        PlantInfo getPlant = restTemplate.getForObject("/api/plants/{id}", PlantInfo.class, "1");
        System.out.println(getPlant);
        PlantWarningMessage[] messages = restTemplate.getForObject("/api/plants/warnings", PlantWarningMessage[].class);
        System.out.println(messages.length);
        assertTrue(messages.length == 6);
    }

    @Test
    @Order(8)
    void testPlantData_Valid() {
        PlantDataInfo getPlant = restTemplate.getForObject("/api/plants/plantData/{id}/{day}",
                PlantDataInfo.class,"1","1");
        assertTrue(getPlant.getSensors().size() == 6);
        List<SensorReadingInfo> readings = new ArrayList<>();
        getPlant.getSensors().forEach(sensorInfoPlantless -> readings.addAll(sensorInfoPlantless.getSensorReadings()));
        assertTrue(readings.size() == 6);
    }


}



