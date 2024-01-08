package com.scheduling.jobrunrapp.exception;

public class AppJobNotFound extends AppException {

    public AppJobNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public AppJobNotFound(Long id) {
        super("Job with id " + id + " not found");
    }

    public AppJobNotFound(String name) {
        super("Job with name " + name + " not found");
    }
}
