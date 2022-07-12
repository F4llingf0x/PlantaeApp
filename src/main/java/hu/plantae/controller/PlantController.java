package hu.plantae.controller;


import hu.plantae.dto.*;
import hu.plantae.service.PlantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/plants")
@Tag(name = "Plant operations")
@Slf4j
@Validated
public class PlantController {

    private PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }


    @PostMapping("/addPlant")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates a plant")
    @ApiResponse(responseCode = "201", description = "Plant has been created")
    public PlantInfo savePlant(@Valid @RequestBody PlantCreateCommand command) {
        log.info(String.format("POST request /api/plants/addPlant invoke savePlant(%s)", command));
        return plantService.savePlant(command);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Finds a plant by id")
    @ApiResponse(responseCode = "202", description = "Plant has been found")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PlantInfo findPlantById(
            @PathVariable("id") @Min(value = 1, message = "must be more than or equal to 1") int id) {
        log.info(String.format("GET request /api/plants/%s invoke findPlantById(%s)", id, id));
        return plantService.findPlantById(id);
    }

    @GetMapping
    @Operation(summary = "Finds all not deleted plants")
    @ApiResponse(responseCode = "202", description = "Plants has been listed")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<PlantInfoSensorless> listPlants() {
        log.info(String.format("GET request /api/plants/ invoke listPlants()"));
        return plantService.listPlants();
    }

    @GetMapping("/all")
    @Operation(summary = "Finds all plants, even the deleted ones")
    @ApiResponse(responseCode = "202", description = "All plants has been listed")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<PlantInfoSensorless> listAllPlants() {
        log.info(String.format("GET request /api/plants/all invoke listAllPlants()"));
        return plantService.listAllPlants();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifies the plant, regarding to the plant's id")
    @ApiResponse(responseCode = "202", description = "Plant has been modified")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PlantInfo modifyPlant(@PathVariable("id") @Min(value = 1, message = "must be more than or equal to 1") int id,
                                 @Valid @RequestBody PlantCreateCommand command) {
        log.info(String.format("PUT request /api/plants/%s invoke modifyPlant(%s, %s)", id, id, command));
        return plantService.modifyPlant(id, command);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes the plant, regarding to the plant's id")
    @ApiResponse(responseCode = "202", description = "Plant has been deleted")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PlantInfo deletePlant(@PathVariable("id") @Min(value = 1, message = "must be more than or equal to 1") int id) {
        log.info(String.format("DELETE request /api/plants/%s invoke deletePlant(%s)", id, id));
        return plantService.deletePlant(id);
    }

    @PutMapping("/{plantId}/{sensorId}")
    @Operation(summary = "Assigns a sensor to a plant, regarding to the plant's and the sensor's ids")
    @ApiResponse(responseCode = "202", description = "Sensor has been assigned to plant")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PlantInfo assignSensor(@PathVariable("plantId")
                                  @Min(value = 1, message = "must be more than or equal to 1") Integer plantId,
                                  @PathVariable("sensorId")
                                  @Min(value = 1, message = "must be more than or equal to 1") Integer sensorId) {
        log.info(String.format("PUT request /api/plants/%s/%s invoke assignSensor(%s, %s)",
                plantId, sensorId, plantId, sensorId));
        return plantService.assignSensor(plantId, sensorId);
    }

    @GetMapping("/plantData/{id}/{day}")
    @Operation(summary = "Returns a plant's information, regarding to the plant's id, and sensor readings for the given " +
            "interval ")
    @ApiResponse(responseCode = "202", description = "Plant information has been returned")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PlantDataInfo plantData(@PathVariable("id")
                                   @Min(value = 1, message = "must be more than or equal to 1") Integer id,
                                   @PathVariable("day")
                                   @Min(value = 1, message = "must be more than or equal to 1") Integer day) {
        log.info(String.format("GET request /api/plants/plantData/%s/%s invoke plantData(%s, %s)",
                id, day, id, day));
        return plantService.plantData(id, day);
    }

    @GetMapping("/warnings")
    @Operation(summary = "Returns warning, if any last sensor's reading is exceeding the limit")
    @ApiResponse(responseCode = "202", description = "Warnings are provided")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<PlantWarningMessage> warnings() {
        log.info(String.format("GET request /api/plants/warnings invoke warnings()"));
        return plantService.warnings();
    }

}
