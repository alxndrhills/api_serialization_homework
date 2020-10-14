package ru.bellintegrator.data;

public class RegistrationError {

    private String error;

    public RegistrationError() {
        super();
    }

    public RegistrationError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
