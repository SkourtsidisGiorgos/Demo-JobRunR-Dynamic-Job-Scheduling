package com.scheduling.jobrunrapp.repository;

import com.scheduling.jobrunrapp.model.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<JobEntity, Long> {
    Optional<JobEntity> findByName(String name);

    void deleteByName(String name);
}
