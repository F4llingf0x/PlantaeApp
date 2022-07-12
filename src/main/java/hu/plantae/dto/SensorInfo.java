package hu.plantae.dto;

import hu.plantae.domain.SensorType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedList;

@Data
@NoArgsConstructor

public class SensorInfo {

    @Schema(description = "Id of sensor", defaultValue = "1")
    private Integer sensorId;

    @Schema(description = "Id of the assigned plant", defaultValue = "1")
    private Integer plantId;

    @Schema(description = "Id of the sensor group", defaultValue = "1")
    private Integer sensorGroup;

    @Schema(description = "Time of the last calibration")
    private LocalDateTime lastCalibrated;

    @Schema(description = "Information about the sensor's validity", defaultValue = "false")
    private boolean isCalibrated;

    @Schema(description = "Type of sensor", defaultValue = "TEMPERATURE")
    private SensorType sensorType;

    @Schema(description = "Sensor sampling period value in seconds", defaultValue = "3600")
    private Integer samplingPeriod;

    @Schema(description = "Sensor's reading values")
    private LinkedList<SensorReadingInfo> sensorReadings;

}
