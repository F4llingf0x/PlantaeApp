package hu.plantae.service;

import hu.plantae.domain.Sensor;
import hu.plantae.domain.SensorReading;
import hu.plantae.domain.SensorType;
import hu.plantae.dto.SensorCreateCommand;
import hu.plantae.dto.SensorInfo;
import hu.plantae.dto.SensorInfoAfterCreation;
import hu.plantae.dto.SensorReadingInfo;
import hu.plantae.repository.SensorReadingRepository;
import hu.plantae.repository.SensorRepository;
import hu.plantae.service.SensorService;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;


@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
public class SensorServiceTest {


    @Mock
    SensorRepository sensorRepository;
    @Mock
    SensorReadingRepository sensorReadingRepository;

    ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    SensorService sensorService;

    private SensorCreateCommand toSave;
    private Sensor saved;
    private SensorReading reading;
    private SensorReadingInfo readingSaved;


    @BeforeEach
    void init() {
        sensorService = new SensorService(sensorRepository, sensorReadingRepository, modelMapper);

        toSave = new SensorCreateCommand();
        toSave.setSensorGroup(1);
        toSave.setSensorType(SensorType.TEMPERATURE);
        toSave.setSamplingPeriod(100);

        reading = new SensorReading();
        reading.setSensor(saved);
        reading.setValue(10);

        saved = modelMapper.map(toSave, Sensor.class);
        readingSaved = modelMapper.map(reading, SensorReadingInfo.class);
    }

    @Test
    void testSaveSensor_Valid() {
        init();
        when(sensorRepository.addSensor(saved)).thenReturn(saved);
        SensorInfoAfterCreation result = sensorService.addSensor(toSave);
        SensorInfoAfterCreation savedInfo = modelMapper.map(saved, SensorInfoAfterCreation.class);
        assertEquals(savedInfo, result);
    }

    @Test
    void testGetSensorWithId_Valid() {
        init();
        Optional<Sensor> savedOptional = Optional.of(saved);
        when(sensorRepository.findSensorById(anyInt())).thenReturn(savedOptional);
        SensorInfo result = sensorService.findSensor(1);
        SensorInfo savedInfo = modelMapper.map(saved, SensorInfo.class);
        assertEquals(savedInfo, result);
    }

}
