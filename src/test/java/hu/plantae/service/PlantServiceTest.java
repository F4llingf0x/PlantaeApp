package hu.plantae.service;

import hu.plantae.domain.*;
import hu.plantae.dto.PlantCreateCommand;
import hu.plantae.dto.PlantDataInfo;
import hu.plantae.dto.PlantInfo;
import hu.plantae.dto.PlantWarningMessage;
import hu.plantae.repository.PlantRepository;
import hu.plantae.service.PlantService;
import hu.plantae.service.SensorService;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;


@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
public class PlantServiceTest {


    @Mock
    PlantRepository plantRepository;
    @Mock
    SensorService sensorService;

    ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    PlantService plantService;

    private PlantCreateCommand toSave;
    private PlantCreateCommand toSaveSecond;
    private Plant saved;
    private Sensor sensor;
    private SensorReading reading;


    @BeforeEach
    void init() {
        plantService = new PlantService(plantRepository, sensorService, modelMapper);

        toSave = new PlantCreateCommand();
        List<String> plantName = List.of("Tracheophytes",
                "Angiosperms",
                "Monocots",
                "Commelinids",
                "Arecales",
                "Arecaceae",
                "Phoenix",
                "canariensis");

        toSave.setPlantName(plantName);
        toSave.setPictures(List.of("https://upload.wikimedia.org/wikipedia/commons/f/fc/Phoenix_canariensis_CBMen_6.jpg"));
        toSave.setMinimumTemperature(10d);
        toSave.setMinimumAirHumidity(20d);
        toSave.setMinimumSoilHumidity(30d);
        toSave.setMaximumTemperature(40d);
        toSave.setMaximumAirHumidity(50d);
        toSave.setMaximumSoilHumidity(60d);
        toSave.setSunRequirement(SunRequirement.FULLSUN);

        toSaveSecond = new PlantCreateCommand();
        List<String> plantNameSecond = List.of("Tracheophyte",
                "Angiosperm",
                "Monocot",
                "Commelinid",
                "Arecale",
                "Arecacea",
                "Phoeni",
                "canariensi");

        toSaveSecond.setPlantName(plantNameSecond);
        toSaveSecond.setPictures(List.of("https://upload.wikimedia.org/wikipedia/commons/f/fc/Phoenix_canariensis_CBMen_6.png"));
        toSaveSecond.setMinimumTemperature(20d);
        toSaveSecond.setMinimumAirHumidity(10d);
        toSaveSecond.setMinimumSoilHumidity(20d);
        toSaveSecond.setMaximumTemperature(10d);
        toSaveSecond.setMaximumAirHumidity(20d);
        toSaveSecond.setMaximumSoilHumidity(10d);
        toSaveSecond.setSunRequirement(SunRequirement.SHADE);

        saved = new Plant();
        List<String> plantName2 = List.of("Tracheophytes",
                "Angiosperms",
                "Monocots",
                "Commelinids",
                "Arecales",
                "Arecaceae",
                "Phoenix",
                "canariensis");

        saved.setPlantName(plantName2);
        saved.setPictures(List.of("https://upload.wikimedia.org/wikipedia/commons/f/fc/Phoenix_canariensis_CBMen_6.jpg"));
        saved.setMinimumTemperature(10d);
        saved.setMinimumAirHumidity(20d);
        saved.setMinimumSoilHumidity(30d);
        saved.setMaximumTemperature(40d);
        saved.setMaximumAirHumidity(50d);
        saved.setMaximumSoilHumidity(60d);
        saved.setSunRequirement(SunRequirement.FULLSUN);

        sensor = new Sensor();
        reading = new SensorReading();

    }

    @Test
    void testSavePlant_Valid() {
        init();
        when(plantRepository.savePlant(saved)).thenReturn(saved);
        PlantInfo result = plantService.savePlant(toSave);
        PlantInfo savedInfo = modelMapper.map(saved, PlantInfo.class);
        assertEquals(savedInfo, result);
    }

    @Test
    void testFindPlantWithId_Valid() {
        init();
        Optional<Plant> savedOptional = Optional.of(saved);
        when(plantRepository.findPlantById(anyInt())).thenReturn(savedOptional);
        PlantInfo result = plantService.findPlantById(1);
        PlantInfo savedInfo = modelMapper.map(saved, PlantInfo.class);
        assertEquals(savedInfo, result);
    }

    @Test
    void testModifyPlant_Valid() {
        init();
        Optional<Plant> savedOptional = Optional.of(saved);
        when(plantRepository.findPlantById(anyInt())).thenReturn(savedOptional);
        when(plantRepository.modifyPlant(saved)).thenReturn(saved);
        PlantInfo result = plantService.modifyPlant(anyInt(), toSave);
        PlantInfo savedInfo = modelMapper.map(saved, PlantInfo.class);
        assertEquals(savedInfo, result);
    }

    @Test
    void testListPlants_Valid() {
        init();
        when(plantRepository.listPlants()).thenReturn(List.of(saved, saved));
        int result = plantService.listPlants().size();
        assertEquals(2, result);
    }

    @Test
    void testListAllPlants_Valid() {
        init();
        when(plantRepository.listAllPlants()).thenReturn(List.of(saved, saved));
        int result = plantService.listAllPlants().size();
        assertEquals(2, result);
    }

    @Test
    void testDeletePlant_Valid() {
        init();
        Optional<Plant> savedOptional = Optional.of(saved);
        when(plantRepository.findPlantById(anyInt())).thenReturn(savedOptional);
        PlantInfo result = plantService.deletePlant(anyInt());
        assertEquals(modelMapper.map(saved, PlantInfo.class), result);
    }

    @Test
    void testPlantData_Valid() {
        init();
        reading.setValue(10);
        sensor.setSensorReadings(List.of(reading));
        saved.setSensors(List.of(sensor));

        Optional<Plant> savedOptional = Optional.of(saved);
        when(plantRepository.findPlantById(anyInt())).thenReturn(savedOptional);
        PlantDataInfo result = plantService.plantData(1, 1);
        assertEquals(modelMapper.map(saved, PlantDataInfo.class), result);
    }

    @Test
    void testWarnings_Valid() {
        init();

        List<PlantWarningMessage> warningMessages = new ArrayList<>();
        List<Integer> readingvalues = List.of(-11, 60, 10, 90, 10, 90);
        List<SensorType> sensortypes = List.of(SensorType.TEMPERATURE,
                SensorType.TEMPERATURE,
                SensorType.HUMIDITY,
                SensorType.HUMIDITY,
                SensorType.SOILMOISTURE,
                SensorType.SOILMOISTURE
        );

        List<Sensor> setSensors = new ArrayList<>();
        for (int i = 0; i < readingvalues.size(); i++) {
            Sensor sensor = new Sensor();
            sensor.setSensorId(i);
            SensorReading reading = new SensorReading();
            reading.setValue(readingvalues.get(i));
            sensor.setSensorReadings(List.of(reading));
            sensor.setSensorType(sensortypes.get(i));

            setSensors.add(sensor);

        }

        saved.setSensors(setSensors);
        saved.setPlantId(1);

        when(plantRepository.listPlants()).thenReturn(List.of(saved));
        warningMessages = (plantService.warnings());

        assertEquals(6, warningMessages.size());
    }

}
