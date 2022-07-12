package hu.plantae.domain;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Entity
public class Plant {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer plantId;

    @ElementCollection()
    @CollectionTable(
            name = "plant_name"
    )
    private List<String> plantName;

    @ElementCollection
    private List<String> pictures;

    private Double minimumTemperature;
    private Double maximumTemperature;

    private Double minimumAirHumidity;
    private Double maximumAirHumidity;

    private Double minimumSoilHumidity;
    private Double maximumSoilHumidity;

    @Enumerated(EnumType.STRING)
    private SunRequirement sunRequirement;

    private boolean isDeleted;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.MERGE)
    private List<Sensor> sensors;

}
