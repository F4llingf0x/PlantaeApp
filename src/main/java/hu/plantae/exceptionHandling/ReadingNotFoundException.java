package hu.plantae.exceptionHandling;

public class ReadingNotFoundException extends RuntimeException {
    private int idNotFound;

    public int getIdNotFound() {
        return idNotFound;
    }


    public ReadingNotFoundException setIdNotFound(int idNotFound) {
        this.idNotFound = idNotFound;
        return this;
    }
}
