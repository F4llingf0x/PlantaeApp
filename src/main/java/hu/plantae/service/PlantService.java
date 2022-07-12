package hu.plantae.service;

import hu.plantae.domain.Plant;
import hu.plantae.domain.Sensor;
import hu.plantae.domain.SensorReading;
import hu.plantae.dto.*;
import hu.plantae.exceptionHandling.PlantNotFoundException;
import hu.plantae.exceptionHandling.SwappedLimitParametersException;
import hu.plantae.repository.PlantRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlantService {

    private PlantRepository plantRepository;
    private SensorService sensorService;
    private ModelMapper modelMapper;

    private static final String maximum = "maximum";
    private static final String minimum = "minimum";


    public PlantService(PlantRepository plantRepository,
                        SensorService service,
                        ModelMapper modelMapper) {
        this.plantRepository = plantRepository;
        this.sensorService = service;
        this.modelMapper = modelMapper;
    }

    public PlantInfo savePlant(PlantCreateCommand command) {
        String badParam = checkParams(command);
        if (badParam != null) {
            throw new SwappedLimitParametersException().setParameter(badParam);
        }
        Plant plantToSave = modelMapper.map(command, Plant.class);
        Plant savedPlant = plantRepository.savePlant(plantToSave);
        return modelMapper.map(savedPlant, PlantInfo.class);
    }

    private Plant getPlantWithId(int id) {
        Optional<Plant> foundPlant = plantRepository.findPlantById(id);
        if (!foundPlant.isPresent() || (foundPlant.get().isDeleted())) {
            throw new PlantNotFoundException().setIdNotFound(id);
        }
        return foundPlant.get();
    }


    public PlantInfo modifyPlant(int id, PlantCreateCommand command) {
        Plant foundPlant = getPlantWithId(id);
        Plant modifiedPlant = modelMapper.map(command, Plant.class);
        modifiedPlant.setPlantId(foundPlant.getPlantId())
                .setSensors(foundPlant.getSensors());
        foundPlant = modifiedPlant;
        Plant savedPlant = plantRepository.modifyPlant(foundPlant);
        return modelMapper.map(savedPlant, PlantInfo.class);
    }

    public PlantInfo findPlantById(int id) {
        Plant foundPlant = getPlantWithId(id);
        return modelMapper.map(foundPlant, PlantInfo.class);
    }

    public List<PlantInfoSensorless> listPlants() {
        List<Plant> plants = plantRepository.listPlants();
        return plants.stream()
                .map(plant -> modelMapper.map(plant, PlantInfoSensorless.class))
                .collect(Collectors.toList());
    }

    public List<PlantInfoSensorless> listAllPlants() {
        List<Plant> plants = plantRepository.listAllPlants();
        return plants.stream()
                .map(plant -> modelMapper.map(plant, PlantInfoSensorless.class))
                .collect(Collectors.toList());
    }


    public PlantInfo deletePlant(int id) {
        Plant plantToDelete = getPlantWithId(id);
        plantToDelete.setDeleted(true);
        return modelMapper.map(plantToDelete, PlantInfo.class);
    }

    public PlantInfo assignSensor(int plantId, int sensorId) {
        Plant foundPlant = getPlantWithId(plantId);
        Sensor foundSensor = sensorService.getSensor(sensorId);
        foundSensor.setPlant(foundPlant);
        PlantInfo plantToReturn = modelMapper.map(foundPlant, PlantInfo.class);
        plantToReturn.getSensors().add(modelMapper.map(foundSensor, SensorInfoReadingAndPlantless.class));
        return plantToReturn;
    }


    public PlantDataInfo plantData(Integer id, Integer day) {
        Plant foundPlant = getPlantWithId(id);
        List<Sensor> foundSensors = foundPlant.getSensors();
        List<Sensor> modifiedSensors = new ArrayList<>();
        Plant plantToReturn = foundPlant;
        for (Sensor foundSensor : foundSensors) {
            modifiedSensors.add(sensorService.getReadingInterval(foundSensor, day));
        }
        plantToReturn.setSensors(modifiedSensors);
        return modelMapper.map(plantToReturn, PlantDataInfo.class);
    }


    public List<PlantWarningMessage> warnings() {
        List<PlantWarningMessage> warningsMessages = new ArrayList<>();

        for (Plant plants : plantRepository.listPlants()) {
            for (Sensor sensor : plants.getSensors()) {

                String type = null;
                String limit = null;

                List<SensorReading> sensorReadings = sensor.getSensorReadings();

                if (sensorReadings.size() > 0) {
                    double lastReading = sensorReadings
                            .get(sensorReadings.size() - 1)
                            .getValue();

                    switch (sensor.getSensorType()) {
                        case TEMPERATURE:
                            type = "TEMPERATURE";
                            limit = checkTemperature(plants, lastReading);
                            break;
                        case HUMIDITY:
                            type = "Humidity";
                            limit = checkHumidity(plants, lastReading);
                            break;

                        case SOILMOISTURE:
                            type = "Soilmoisture";
                            limit = checkSoilMoisture(plants, lastReading);
                            break;
                    }

                    if (limit != null) {
                        PlantWarningMessage warning = new PlantWarningMessage();
                        warning.setPlantid(plants.getPlantId());
                        warning.setErrormessage(type + " " + limit + " warning, according to sensor: " + sensor.getSensorId());
                        warningsMessages.add(warning);
                    }
                }

            }

        }
        return warningsMessages;
    }

    private String checkTemperature(Plant plant, double lastReading) {
        String stringToReturn = null;
        if (plant.getMaximumTemperature() < lastReading) {
            stringToReturn = maximum;
        }
        if (plant.getMinimumTemperature() > lastReading) {
            stringToReturn = minimum;
        }
        return stringToReturn;
    }

    private String checkHumidity(Plant plant, double lastReading) {
        String stringToReturn = null;
        if (plant.getMaximumAirHumidity() < lastReading) {
            stringToReturn = maximum;
        }
        if (plant.getMinimumAirHumidity() > lastReading) {
            stringToReturn = minimum;
        }
        return stringToReturn;
    }

    private String checkSoilMoisture(Plant plant, double lastReading) {
        String stringToReturn = null;
        if (plant.getMaximumSoilHumidity() < lastReading) {
            stringToReturn = maximum;
        }
        if (plant.getMinimumSoilHumidity() > lastReading) {
            stringToReturn = minimum;
        }
        return stringToReturn;
    }

    private String checkParams(PlantCreateCommand command){
        String stringToReturn = null;
        if (command.getMinimumTemperature() > command.getMaximumTemperature()){
            stringToReturn = "temperature";
        }
        if (command.getMinimumAirHumidity() > command.getMaximumAirHumidity()){
            stringToReturn = "air Humidity";
        }
        if (command.getMinimumSoilHumidity() > command.getMaximumSoilHumidity()){
            stringToReturn = "soil moisture";
        }
        return stringToReturn;
    }

}
