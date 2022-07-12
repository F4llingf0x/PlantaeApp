package hu.plantae.dto;

import hu.plantae.domain.SunRequirement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantCreateCommand {

    @NotNull
    @Schema(description = "Name of plant", defaultValue = "[Clades, Order, Family, Genus, Species]")
    private List<String> plantName;

    @NotEmpty
    @Schema(description = "Pictures of plant", defaultValue = "https://www.iconexperience.com/g_collection/icons/?icon=plant")
    private List<String> pictures;

    @Min(-40)
    @Max(60)
    @Schema(description = "Minimum temperature value for warning", defaultValue = "0")
    private Double minimumTemperature;

    @Min(-40)
    @Max(60)
    @Schema(description = "Maximum temperature value for warning", defaultValue = "50")
    private Double maximumTemperature;

    @PositiveOrZero
    @Max(100)
    @Schema(description = "Minimum air humidity percentage for warning", defaultValue = "20")
    private Double minimumAirHumidity;

    @PositiveOrZero
    @Max(100)
    @Schema(description = "Maximum air humidity percentage for warning", defaultValue = "80")
    private Double maximumAirHumidity;

    @PositiveOrZero
    @Max(100)
    @Schema(description = "Minimum soil moisture percentage for warning", defaultValue = "30")
    private Double minimumSoilHumidity;

    @PositiveOrZero
    @Max(100)
    @Schema(description = "Maximum soil moisture percentage for warning", defaultValue = "90")
    private Double maximumSoilHumidity;

    @NotNull
    @Schema(description = "Ideal sun requirement of plant", defaultValue = "PARTIALSUN")
    private SunRequirement sunRequirement;

}
