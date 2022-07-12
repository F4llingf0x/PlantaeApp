package hu.plantae.exceptionHandling;

public class SensorNotFoundException extends RuntimeException {
    private int idNotFound;

    public int getIdNotFound() {
        return idNotFound;
    }


    public SensorNotFoundException setIdNotFound(int idNotFound) {
        this.idNotFound = idNotFound;
        return this;
    }
}
