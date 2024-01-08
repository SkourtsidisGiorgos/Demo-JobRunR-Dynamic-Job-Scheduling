# JobRunR Dynamic job scheduler

This is a demo project to showcase dynamic job scheduling using [JobRunR](https://www.jobrunr.io/en/).

It schedules jobs using a REST API and cron expressions.  
Jobs just print messages to the console.
Use REST calls to create, update, delete jobs.  

Jobs are persisted to Postgres DB. After a restart, the scheduler will continue existing job's execution.  

If you plan to use code from this project in production, be exremely careful as the failure handling and transaction managment needs improvement.  

## Pre-requisites

- Java 21  
- Docker  
- Docker Compose  

## Run

- Run `docker compose up` to start Postgres (or `docker-compose up` if you have older version)
- Run `mvn spring-boot:run` to start the application

## Clean-up
- `docker compose down` to stop Postgres
- `sudo rm -rf volumes/postgres` to delete the Postgres data directory

## Usage

- Using Swagger UI: `http://localhost:8080/swagger-ui/index.html`
![Screenshot from 2024-01-08 15-18-46](https://github.com/SkourtsidisGiorgos/Demo-JobRunR-Dynamic-Job-Scheduling/assets/60469956/c2fcf50c-884e-455a-b017-de694f2f4362)


- Using curl:

**Get all jobs**
```shell
curl -X 'GET' 'http://localhost:8080/jobs'
```

**Create a job**
```shell
curl -X 'POST' \
  'http://localhost:8080/jobs/create' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "job1",
  "message": "job1 message",
  "cronExpression": "* * * * *",
  "retries": 1
}'
```

**Update a job**
```shell
curl -X 'PUT' \
  'http://localhost:8080/jobs/update-by-name/job1' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "job1-new",
  "message": "updated message",
  "cronExpression": "* * * * *",
  "retries": 2
}'
```

**Delete a job**
```shell
curl -X 'DELETE' 'http://localhost:8080/jobs/delete-by-name/job1-new' 
```


- See logs to inspect job execution:
```shell
less logs/dynamic-job-scheduling.log
```
![Screenshot from 2024-01-08 15-25-18](https://github.com/SkourtsidisGiorgos/Demo-JobRunR-Dynamic-Job-Scheduling/assets/60469956/aaeaa959-8460-4ab0-9fe5-1ecb612467ac)



- Visit Web UI: `http://localhost:8000/dashboard/recurring-jobs`
![Screenshot from 2024-01-08 15-23-29](https://github.com/SkourtsidisGiorgos/Demo-JobRunR-Dynamic-Job-Scheduling/assets/60469956/5edd12d4-5016-4aa9-abb7-74a69b7e48d5)




## References
- [JobRunR](https://www.jobrunr.io/en/)
- [Quartz demo dynamic job scheduling project](https://github.com/sanjay035/Dynamic-Job-Scheduler)
