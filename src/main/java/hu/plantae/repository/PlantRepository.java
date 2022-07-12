package hu.plantae.repository;


import hu.plantae.domain.Plant;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class PlantRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Plant savePlant(Plant plantToSave) {
        entityManager.persist(plantToSave);
        return plantToSave;
    }

    public Optional<Plant> findPlantById(int plantId) {
        return Optional.ofNullable(entityManager.find(Plant.class, plantId));
    }

    public List<Plant> listPlants() {
        List<Plant> plants = entityManager.createQuery(
                "SELECT p FROM Plant p " +
                        "WHERE p.isDeleted = false",
                Plant.class).getResultList();
        return plants;
    }

    public List<Plant> listAllPlants() {
        List<Plant> plants = entityManager.createQuery("SELECT p FROM Plant p", Plant.class).getResultList();
        return plants;
    }


    public Plant modifyPlant(Plant modifiedPlant) {
        return entityManager.merge(modifiedPlant);
    }


}
