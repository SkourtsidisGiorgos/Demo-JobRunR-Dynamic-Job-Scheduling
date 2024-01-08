package com.scheduling.jobrunrapp.controller;

import com.scheduling.jobrunrapp.model.JobDto;
import com.scheduling.jobrunrapp.model.JobEntity;
import com.scheduling.jobrunrapp.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/create")
    public ResponseEntity<JobEntity> createJob(@RequestBody JobDto job) {
        return ResponseEntity.ok(jobService.createJob(job));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<JobEntity> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @GetMapping("/get-by-name/{name}")
    public ResponseEntity<JobEntity> getJobByName(@PathVariable String name) {
        return ResponseEntity.ok(jobService.getJobByName(name));
    }

    @GetMapping
    public List<JobEntity> getAllJobs() {
        return jobService.getAllJobs();
    }

    @PutMapping("/update-by-id/{id}")
    public ResponseEntity<JobEntity> updateJob(@PathVariable Long id, @RequestBody JobDto job) {
        return ResponseEntity.ok(jobService.updateJobById(id, job));
    }

    @PutMapping("/update-by-name/{name}")
    public ResponseEntity<JobEntity> updateJob(@PathVariable String name, @RequestBody JobDto job) {
        return ResponseEntity.ok(jobService.updateJobByName(name, job));

    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-by-name/{name}")
    public ResponseEntity<Void> deleteJob(@PathVariable String name) {
        jobService.deleteByName(name);
        return ResponseEntity.ok().build();
    }
}
