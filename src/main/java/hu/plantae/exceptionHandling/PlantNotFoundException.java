package hu.plantae.exceptionHandling;

public class PlantNotFoundException extends RuntimeException {
    private int idNotFound;

    public int getIdNotFound() {
        return idNotFound;
    }


    public PlantNotFoundException setIdNotFound(int idNotFound) {
        this.idNotFound = idNotFound;
        return this;
    }
}
