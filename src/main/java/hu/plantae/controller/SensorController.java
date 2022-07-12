package hu.plantae.controller;

import hu.plantae.dto.*;
import hu.plantae.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Tag(name = "Sensor operations")
@RequestMapping("/api/sensors")
@Slf4j
@Validated
public class SensorController {

    private SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @PostMapping("/addSensor")
    @Operation(summary = "Creates a sensor")
    @ApiResponse(responseCode = "201", description = "Sensor has been created")
    @ResponseStatus(HttpStatus.CREATED)
    public SensorInfoAfterCreation addSensor(@Valid @RequestBody SensorCreateCommand command) {
        log.info(String.format("POST request /api/sensors/addSensor invoke addSensor(%s)", command));
        return sensorService.addSensor(command);
    }

    @PostMapping("/reading/{sensorId}/{measuredData}")
    @Operation(summary = "Creates a reading for the given sensor. Sensor is identified by id.")
    @ApiResponse(responseCode = "202", description = "Reading has been created")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SensorReadingInfo addReading(@PathVariable("sensorId")
                                        @Min(value = 1, message = "must be more than or equal to 1") Integer sensorId,
                                        @PathVariable("measuredData")
                                        @NotNull Double measuredData) {
        log.info(String.format("POST request /api/sensors/reading/%s/%s invoke addReading(%s, %s)",
                sensorId, measuredData, sensorId, measuredData));
        return sensorService.addReading(sensorId, measuredData);
    }

    @GetMapping("/{sensorId}")
    @Operation(summary = "Returns all information for the sensor with the given id. All sensor readings are shown.")
    @ApiResponse(responseCode = "202", description = "Sensor has been found")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SensorInfo findSensor(@PathVariable("sensorId")
                                 @Min(value = 1, message = "must be more than or equal to 1") Integer sensorId) {
        log.info(String.format("GET request /api/sensors/%s invoke findSensor(%s)", sensorId,sensorId));
        return sensorService.findSensor(sensorId);
    }

    @GetMapping("/all")
    @Operation(summary = "Returns all not deleted sensors, without readings")
    @ApiResponse(responseCode = "202", description = "Sensor has been found")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<SensorInfoReadingless> listAllSensors() {
        log.info(String.format("GET request /api/sensors/all invoke listAllSensors()"));
        return sensorService.listAllSensors();
    }

    @PutMapping("/samplingPeriod/{sensorId}/{samplingPeriod}")
    @Operation(summary = "Modifies the sampling period of a sensor with the given id")
    @ApiResponse(responseCode = "202", description = "Sensor sampling period has been modified")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SensorInfoReadingless modifySamplingPeriod(@PathVariable("sensorId")
                                                      @Min(value = 1, message = "must be more than or equal to 1")
                                                              Integer sensorId,
                                                      @PathVariable("samplingPeriod")
                                                      @Min(value = 1, message = "must be more than or equal to 1")
                                                              Integer samplingPeriod) {
        log.info(String.format("PUT request /api/sensors/samplingPeriod/%s/%s invoke modifySamplingPeriod(%s,%s)",
                sensorId, samplingPeriod, sensorId, samplingPeriod));
        return sensorService.modifySamplingPeriod(sensorId, samplingPeriod);
    }

    @PutMapping("/calibrate/{sensorId}")
    @Operation(summary = "In case of sensor calibration, the method sets the current time and to calibrated state")
    @ApiResponse(responseCode = "202", description = "Sensor has been calibrated")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SensorInfoReadingless calibrateSensor(@PathVariable("sensorId")
                                                 @Min(value = 1, message = "must be more than or equal to 1")
                                                         Integer sensorId) {
        log.info(String.format("PUT request /api/sensors/calibrate/%s invoke calibrateSensor", sensorId));
        return sensorService.calibrateSensor(sensorId);
    }

    @GetMapping("/lastReading/{sensorId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Returns the last reading of the given sensor. Sensor identified by id.")
    @ApiResponse(responseCode = "202", description = "Reading has been returned")
    public SensorReadingInfo lastReading(@PathVariable("sensorId")
                                         @Min(value = 1, message = "must be more than or equal to 1")
                                                 Integer sensorId) {
        return sensorService.lastReading(sensorId);
    }

    @GetMapping("/init/{sensorGroupId}")
    @Operation(summary = "Returns all sensor information regarding to sensor group id, for initialization" +
            " purposes")
    @ApiResponse(responseCode = "202", description = "Sensor group initialization information has sent")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<SensorInitInfo> initSensors(@PathVariable("sensorGroupId")
                                            @Min(value = 1, message = "must be more than or equal to 1")
                                                    Integer sensorGroupId) {
        log.info(String.format("GET request /api/sensors/init/%s invoke initSensors(%s)",
                sensorGroupId, sensorGroupId));
        return sensorService.initSensors(sensorGroupId);
    }

    @GetMapping("/checkSensors")
    @Operation(summary = "Check sensors, if become invalid, and returns all invalid sensors without readings")
    @ApiResponse(responseCode = "202", description = "Invalid sensors has been returned")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<SensorInfoReadingless> invalidSensors() {
        log.info(String.format("GET request /api/sensors/checkSensors invoke invalidSensors()"));
        return sensorService.invalidSensors();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes the sensor with the given id")
    @ApiResponse(responseCode = "202", description = "Sensor has been deleted")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SensorInfoReadingless deleteSensor(@PathVariable("id")
                                              @Min(value = 1, message = "must be more than or equal to 1") Integer id) {
        log.info(String.format("DELETE request /api/sensors/%s invoke deleteSensor(%s)", id, id));
        return sensorService.deleteSensor(id);
    }

    @GetMapping("/sensorData/{id}/{day}")
    @Operation(summary = "Returns all information from the sensor, the sensor readings are modified for" +
            " the given interval")
    @ApiResponse(responseCode = "202", description = "Sensor data has been returned")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SensorInfo sensorData(@PathVariable("id")
                                 @Min(value = 1, message = "must be more than or equal to 1") Integer id,
                                 @PathVariable("day")
                                 @Min(value = 1, message = "must be more than or equal to 1") Integer day) {
        log.info(String.format("GET request /api/sensorData/%s/%s invoke sensorData(%s/%s)",
                id, day, id, day));
        return sensorService.sensorData(id, day);
    }
}
