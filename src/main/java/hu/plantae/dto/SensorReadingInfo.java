package hu.plantae.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SensorReadingInfo {

    @Schema(description = "Time of the sensor reading")
    private LocalDateTime time;

    @Schema(description = "Value of the sensor reading")
    private double value;

}




