package hu.plantae.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlantWarningMessage {

    @Schema(description = "Id of plant", defaultValue = "1")
    private int plantid;

    @Schema(description = "Warning about an exceeded limit, with sensor id")
    private String errormessage;

}
