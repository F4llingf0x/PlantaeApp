package hu.plantae.dto;

import hu.plantae.domain.SensorType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class SensorInfoAfterCreation {

    @Schema(description = "Id of sensor", defaultValue = "1")
    private Integer sensorId;

    @Schema(description = "Id of the sensor group", defaultValue = "1")
    private Integer sensorGroup;

    @Schema(description = "Type of sensor", defaultValue = "TEMPERATURE")
    private SensorType sensorType;

    @Schema(description = "Sensor sampling period value in seconds", defaultValue = "3600")
    private Integer samplingPeriod;

}
