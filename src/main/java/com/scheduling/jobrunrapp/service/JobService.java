package com.scheduling.jobrunrapp.service;

import com.scheduling.jobrunrapp.exception.AppException;
import com.scheduling.jobrunrapp.exception.AppJobNotFound;
import com.scheduling.jobrunrapp.model.JobDto;
import com.scheduling.jobrunrapp.model.JobEntity;
import com.scheduling.jobrunrapp.repository.JobRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.jobrunr.scheduling.JobScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.jobrunr.scheduling.RecurringJobBuilder.aRecurringJob;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final JobScheduler jobScheduler;
    private static final Logger LOG = LoggerFactory.getLogger(JobService.class);

    @Autowired
    public JobService(JobRepository jobRepository, JobScheduler jobScheduler) {
        this.jobRepository = jobRepository;
        this.jobScheduler = jobScheduler;
    }

    public JobEntity createJob(JobDto jobDto) {
        LOG.debug("Creating job: {}", jobDto);
        if (jobDto.getRetries() == null) {
            jobDto.setRetries(0);
        }
        JobEntity jobEntity = jobDto.toEntity();
        LOG.trace("Creating job entity: {}", jobEntity);
        String scheduledId = jobScheduler.createRecurrently(aRecurringJob()
                .withCron(jobDto.getCronExpression())
                .withAmountOfRetries(jobDto.getRetries())
                .withName(jobDto.getName())
                .withId(UUID.randomUUID().toString())
                .withDetails(() -> System.out.println("Job: name=" + jobEntity.getName() + ", message=" + jobEntity.getMessage())));
        LOG.trace("ScheduledId: {}", scheduledId);
        jobEntity.setScheduledId(scheduledId);
        LOG.trace("Saving job entity: {}", jobEntity);
        try{
            jobRepository.save(jobEntity);
        }
        catch (Exception e){
            LOG.error("Error saving job entity: {}", jobEntity);
            jobScheduler.delete(String.valueOf(jobEntity.getId()));
            throw new AppException("Error saving job entity: " + jobEntity);
        }
        return jobEntity;
    }

    public JobEntity getJobById(Long id) {
        return jobRepository.findById(id).orElseThrow(() -> new AppJobNotFound(id));
    }
    public JobEntity getJobByName(String name) {
        return jobRepository.findByName(name).orElseThrow(() -> new AppJobNotFound(name));
    }

    public List<JobEntity> getAllJobs() {
        return jobRepository.findAll();
    }

    public void deleteById(Long id) {
        LOG.debug("Deleting job with id: {}", id);
        JobEntity jobEntity = jobRepository.findById(id).orElseThrow(() -> new AppJobNotFound(id));
        jobScheduler.delete(jobEntity.getScheduledId());
        jobRepository.deleteById(id);
    }
    @Transactional
    public void deleteByName(String name) {
        LOG.debug("Deleting job with name: {}", name);
        JobEntity jobEntity = jobRepository.findByName(name).orElseThrow(() -> new AppJobNotFound(name));
        jobScheduler.delete(jobEntity.getScheduledId());
        jobRepository.deleteByName(name);
    }

    @Transactional
    public JobEntity updateJobByName(String name, JobDto updateJobDto) throws EntityNotFoundException {
        JobEntity existingJob = jobRepository.findByName(name)
                .orElseThrow(() -> new AppJobNotFound(name));
        return updateJob(existingJob, updateJobDto);
    }
    public JobEntity updateJobById(Long id, JobDto updateJobDto) throws EntityNotFoundException {
        JobEntity existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new AppJobNotFound(id));
        return updateJob(existingJob, updateJobDto);
    }
    private JobEntity updateJob(JobEntity jobEntity, JobDto updateJobDto) throws EntityNotFoundException {
        LOG.debug("Updating job entity: {}", jobEntity);
        JobEntity backupEntity = new JobEntity(jobEntity); // Deep copy
        String originalScheduledId = jobEntity.getScheduledId();

        try {
            jobEntity.setName(updateJobDto.getName());
            jobEntity.setMessage(updateJobDto.getMessage());
            jobEntity.setCronExpression(updateJobDto.getCronExpression());
            jobEntity.setRetries(updateJobDto.getRetries());
            jobScheduler.delete(originalScheduledId);

            String scheduledId = jobScheduler.createRecurrently(aRecurringJob()
                    .withCron(jobEntity.getCronExpression())
                    .withAmountOfRetries(jobEntity.getRetries())
                    .withName(jobEntity.getName())
                    .withId(UUID.randomUUID().toString())
                    .withDetails(() -> System.out.println("Job: name=" + jobEntity.getName() + ", message=" + jobEntity.getMessage())));
            jobEntity.setScheduledId(scheduledId);
            jobRepository.save(jobEntity);
        } catch (Exception e) {
            LOG.error("Error updating job entity: {}", jobEntity, e);
            revertChanges(backupEntity, originalScheduledId);
            throw new AppException("Error updating job entity: " + jobEntity);
        }
        return jobEntity;
    }

    private void revertChanges(JobEntity backupEntity, String originalScheduledId) {
        try {
            jobRepository.save(backupEntity);
            jobScheduler.delete(backupEntity.getScheduledId());
            jobScheduler.createRecurrently(aRecurringJob()
                    .withCron(backupEntity.getCronExpression())
                    .withAmountOfRetries(backupEntity.getRetries())
                    .withName(backupEntity.getName())
                    .withId(originalScheduledId)
                    .withDetails(() -> System.out.println("Job: name=" + backupEntity.getName() + ", message=" + backupEntity.getMessage())));
        } catch (Exception ex) {
            LOG.error("Error while reverting changes for job entity: {}", backupEntity, ex);
            throw new AppException("Could not update job with id: " + backupEntity.getId());
        }
    }
}