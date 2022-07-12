package hu.plantae.repository;

import hu.plantae.domain.SensorReading;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class SensorReadingRepository {

    @PersistenceContext
    private EntityManager entityManager;


    public SensorReading addReading(SensorReading sensorReadingToSave) {
        entityManager.persist(sensorReadingToSave);
        return sensorReadingToSave;
    }


    public Optional<SensorReading> getLastReading(int sensorId) {
        return entityManager.createQuery("SELECT r " +
                                "FROM SensorReading r JOIN r.sensor s " +
                                "WHERE s.sensorId = (:value)" +
                                "ORDER BY r.time DESC"
                        , SensorReading.class)
                .setParameter("value", sensorId)
                .getResultList().stream().findFirst();

    }
}
