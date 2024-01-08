package com.scheduling.jobrunrapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobDto {
    private String name;
    private String message;
    private String cronExpression;
    private Integer retries;

    public JobEntity toEntity() {
        return new JobEntity(name, message, cronExpression, retries);
    }
}

