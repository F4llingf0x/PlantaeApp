package hu.plantae.exceptionHandling;

public class SwappedLimitParametersException extends RuntimeException {
    private String parameter;

    public String getParameter() {
        return parameter;
    }

    public SwappedLimitParametersException setParameter(String parameter) {
        this.parameter = parameter;
        return this;
    }
}
