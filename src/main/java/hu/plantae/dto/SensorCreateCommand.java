package hu.plantae.dto;

import hu.plantae.domain.SensorType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorCreateCommand {

    @NotNull
    @Schema(description = "Id of sensor group", defaultValue = "1")
    private Integer sensorGroup;

    @NotNull
    @Schema(description = "Type of sensor")
    private SensorType sensorType;

    @Min(5)
    @Max(86400) //one day
    @Schema(description = "Sensor sampling period value in seconds", defaultValue = "3600")
    private Integer samplingPeriod;

}
