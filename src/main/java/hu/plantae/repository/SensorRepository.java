package hu.plantae.repository;

import hu.plantae.domain.Sensor;
import hu.plantae.dto.SensorInfoReadingless;
import hu.plantae.dto.SensorInitInfo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class SensorRepository {

    @PersistenceContext
    private EntityManager entityManager;


    public Sensor addSensor(Sensor sensorToSave) {
        entityManager.persist(sensorToSave);
        return sensorToSave;
    }


    public Optional<Sensor> findSensorById(Integer sensorId) {
        return Optional.ofNullable(entityManager.find(Sensor.class, sensorId));
    }


    public List<SensorInfoReadingless> findSensors() {
        return entityManager.createQuery(
                        "SELECT NEW hu.plantae.dto.SensorInfoReadingless(" +
                                "s.sensorId, " +
                                "s.plant.plantId, " +
                                "s.sensorGroup, " +
                                "s.sensorType, " +
                                "s.samplingPeriod" +
                                ") " +
                                "FROM Sensor s " +
                                "WHERE s.isDeleted = false "
                        , SensorInfoReadingless.class)
                .getResultList();
    }


    public Sensor modifySensor(Sensor foundSensor) {
        return entityManager.merge(foundSensor);
    }

    public List<SensorInitInfo> initSensors(Integer sensorGroupId) {
        return entityManager.createQuery(
                        "SELECT NEW hu.plantae.dto.SensorInitInfo(" +
                                "s.sensorId, " +
                                "s.sensorType, " +
                                "s.samplingPeriod" +
                                ") " +
                                "FROM Sensor s " +
                                "WHERE s.isDeleted = false " +
                                "AND s.sensorGroup = (:value) " +
                                "ORDER BY s.sensorType, s.sensorId ASC", SensorInitInfo.class)
                .setParameter("value", sensorGroupId)
                .getResultList();
    }

    public List<Sensor> findAllSensor() {
        return entityManager.createQuery("SELECT s FROM Sensor s " +
                "WHERE s.isDeleted = false ", Sensor.class).getResultList();
    }
}
