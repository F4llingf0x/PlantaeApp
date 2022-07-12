package hu.plantae.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sensorId;

    private Integer sensorGroup;
    private LocalDateTime lastCalibrated;
    private boolean isCalibrated;

    @Enumerated(value = EnumType.STRING)
    private SensorType sensorType;
    private Integer samplingPeriod;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.MERGE)
    private List<SensorReading> sensorReadings;

    @ManyToOne()
    private Plant plant;

    private boolean isDeleted;

}
