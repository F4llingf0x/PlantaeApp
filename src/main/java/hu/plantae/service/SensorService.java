package hu.plantae.service;

import hu.plantae.domain.Sensor;
import hu.plantae.domain.SensorReading;
import hu.plantae.dto.*;
import hu.plantae.exceptionHandling.ReadingNotFoundException;
import hu.plantae.exceptionHandling.SensorNotFoundException;
import hu.plantae.repository.SensorReadingRepository;
import hu.plantae.repository.SensorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class SensorService {

    @Value("${sensorValidMonths}")
    private Integer sensorValidMonths;

    private SensorRepository sensorRepository;
    private SensorReadingRepository sensorReadingRepository;
    private ModelMapper modelMapper;


    public SensorService(SensorRepository sensorRepository,
                         SensorReadingRepository sensorReadingRepository,
                         ModelMapper modelMapper) {
        this.sensorRepository = sensorRepository;
        this.sensorReadingRepository = sensorReadingRepository;
        this.modelMapper = modelMapper;
    }

    public SensorInfoAfterCreation addSensor(SensorCreateCommand command) {
        Sensor sensorToSave = modelMapper.map(command, Sensor.class);
        Sensor savedSensor = sensorRepository.addSensor(sensorToSave);
        return modelMapper.map(savedSensor, SensorInfoAfterCreation.class);
    }

    protected Sensor getSensor(int sensorId) {
        Optional<Sensor> foundSensor = sensorRepository.findSensorById(sensorId);
        if (foundSensor.isEmpty() || foundSensor.get().isDeleted()) {
            throw new SensorNotFoundException().setIdNotFound(sensorId);
        }
        return foundSensor.get();
    }

    public SensorReadingInfo addReading(Integer sensorId, Double measuredData) {
        SensorReading sensorReadingToSave = new SensorReading();
        sensorReadingToSave.setTime(LocalDateTime.now());
        sensorReadingToSave.setValue(measuredData);

        Sensor foundSensor = getSensor(sensorId);
        sensorReadingToSave.setSensor(foundSensor);

        SensorReading savedSensorReading = sensorReadingRepository.addReading(sensorReadingToSave);
        return modelMapper.map(savedSensorReading, SensorReadingInfo.class);
    }

    public SensorInfo findSensor(Integer sensorId) {
        Sensor foundSensor = getSensor(sensorId);
        return modelMapper.map(foundSensor, SensorInfo.class);
    }

    public List<SensorInfoReadingless> listAllSensors() {
        return sensorRepository.findSensors();
    }

    public SensorInfoReadingless modifySamplingPeriod(Integer sensorId, Integer samplingPeriod) {
        Sensor foundSensor = getSensor(sensorId);
        foundSensor.setSamplingPeriod(samplingPeriod);
        return modelMapper.map(foundSensor, SensorInfoReadingless.class);
    }

    public SensorReadingInfo lastReading(int sensorId) {
        getSensor(sensorId);
        Optional<SensorReading> foundReading = sensorReadingRepository.getLastReading(sensorId);
        if (foundReading.isEmpty()) {
            throw new ReadingNotFoundException();
        }
        return modelMapper.map(foundReading.get(), SensorReadingInfo.class);
    }

    public List<SensorInitInfo> initSensors(Integer sensorGroupId) {
        return sensorRepository.initSensors(sensorGroupId);
    }

    public List<SensorInfoReadingless> invalidSensors() {
        List<Sensor> foundSensors = sensorRepository.findAllSensor();
        List<SensorInfoReadingless> invalidSensors = new ArrayList<>();
        LocalDate deadline = LocalDate.now().minusMonths(sensorValidMonths);

        for (Sensor sensor : foundSensors) {
            LocalDateTime time = sensor.getLastCalibrated();
            boolean checkDeadline = time == null || time.toLocalDate().isBefore(deadline);
            if (checkDeadline) {
                invalidSensors.add(modelMapper.map(sensor, SensorInfoReadingless.class));
                sensor.setCalibrated(false);
            }
        }
        return invalidSensors;
    }

    public SensorInfoReadingless deleteSensor(Integer id) {
        Sensor foundSensor = getSensor(id);
        foundSensor.setDeleted(true);
        return modelMapper.map(foundSensor, SensorInfoReadingless.class);
    }

    protected Sensor getReadingInterval(Sensor sensor, int day) {
        LocalDateTime deadline = LocalDateTime.now().minusDays(day);
        List<SensorReading> readings = sensor.getSensorReadings();
        int deadlineIndex = 0;
        for (int i = 0; i < readings.size(); i++) {
            if (readings.get(i).getTime().isBefore(deadline)) {
                deadlineIndex = i;
                break;
            }
        }
        List<SensorReading> extractedReadings = readings.subList(deadlineIndex, readings.size());
        Sensor sensorModified = sensor;
        sensorModified.setSensorReadings(extractedReadings);
        return sensorModified;
    }

    public SensorInfo sensorData(Integer id, Integer day) {
        Sensor foundModifiedSensor = getReadingInterval(getSensor(id), day);
        return modelMapper.map(foundModifiedSensor, SensorInfo.class);
    }

    public SensorInfoReadingless calibrateSensor(Integer sensorId) {
        Sensor foundSensor = getSensor(sensorId);
        foundSensor.setCalibrated(true);
        foundSensor.setLastCalibrated(LocalDateTime.now());
        return modelMapper.map(foundSensor, SensorInfoReadingless.class);
    }
}
