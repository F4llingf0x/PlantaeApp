package hu.plantae.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PlantDataInfo {

    @Schema(description = "Id of plant", defaultValue = "1")
    private Integer plantId;

    @Schema(description = "Minimum temperature value for warning", defaultValue = "0")
    private Double minimumTemperature;

    @Schema(description = "Maximum temperature value for warning", defaultValue = "50")
    private Double maximumTemperature;

    @Schema(description = "Minimum air humidity percentage for warning", defaultValue = "20")
    private Double minimumAirHumidity;

    @Schema(description = "Maximum air humidity percentage for warning", defaultValue = "80")
    private Double maximumAirHumidity;

    @Schema(description = "Minimum soil moisture percentage for warning", defaultValue = "30")
    private Double minimumSoilHumidity;

    @Schema(description = "Maximum soil moisture percentage for warning", defaultValue = "90")
    private Double maximumSoilHumidity;

    @Schema(description = "Sensors assigned to plant")
    private List<SensorInfoPlantless> sensors;

}
