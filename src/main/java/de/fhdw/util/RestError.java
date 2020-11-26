package de.fhdw.util;

public class RestError {
    public Object object;
    public String message;

    public RestError(Object object, String message) {
        this.object = object;
        this.message = message;
    }
}