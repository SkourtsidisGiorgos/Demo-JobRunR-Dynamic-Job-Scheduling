package com.scheduling.jobrunrapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Entity(name = "job")
@Table()
public class JobEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @NonNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "cronExpression", nullable = false)
    private String cronExpression;

    @Column(name = "retries")
    private Integer retries;

    @Column(name = "scheduledId")
    private String scheduledId;


    public JobEntity(@NonNull String name, String message, String cronExpression, Integer retries, String scheduledId) {
        this.name = name;
        this.message = message;
        this.cronExpression = cronExpression;
        this.retries = retries;
        this.scheduledId = scheduledId;
    }

    public JobEntity(String name, String message, String cronExpression, Integer retries) {
        this.name = name;
        this.message = message;
        this.cronExpression = cronExpression;
        this.retries = retries;
    }

    public JobEntity(Long id, String name, String message, String cronExpression, Integer retries) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.cronExpression = cronExpression;
        this.retries = retries;
    }

    public JobEntity(JobEntity jobEntity) {
        // All fields in JobEntity (id, name, message, cronExpression, retries, scheduledId) are either primitive types or immutable objects (like String).
        // These types are inherently deep copied when assigned to a new variable
        this.id = jobEntity.id != null ? jobEntity.id : null;
        this.name = jobEntity.name != null ? new String(jobEntity.name) : null;
        this.message = jobEntity.message != null ? new String(jobEntity.message) : null;
        this.cronExpression = jobEntity.cronExpression != null ? new String(jobEntity.cronExpression) : null;
        this.retries = jobEntity.retries != null ? jobEntity.retries : null;
        this.scheduledId = jobEntity.scheduledId != null ? new String(jobEntity.scheduledId) : null;

    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        JobEntity jobEntity = (JobEntity) o;
        return getId() != null && Objects.equals(getId(), jobEntity.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
