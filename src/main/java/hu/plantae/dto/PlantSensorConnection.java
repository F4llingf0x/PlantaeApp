package hu.plantae.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class PlantSensorConnection {

    @Schema(description = "Id of plant", defaultValue = "1")
    private Integer plantId;

    @Schema(description = "Name of plant", defaultValue = "[Clades, Order, Family, Genus, Species]")
    private Map<String, String> plantName;

    @Schema(description = "Sensors assigned to plant")
    private List<SensorInfoReadingless> sensors;

}
