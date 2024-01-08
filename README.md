# JobRunR Dynamic job scheduler

This is a demo project to showcase dynamic job scheduling using [JobRunR](https://www.jobrunr.io/en/).

It schedules jobs using a REST API and cron expressions. 
Jobs just print messages to the console. Messages are defined by the user.
Use REST calls to create, update, delete jobs. 
Don't use code from this project in production as is, as the failure handling and transaction managment is not perfect.


Jobs are persisted Postgres and after a restart, the scheduler will continue existing job execution.

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


- See the logs to see the job execution
```shell
less logs/dynamic-job-scheduling.log
```

- Visit Web UI to see the job execution `http://localhost:8000/dashboard/recurring-jobs`



## References
- [JobRunR](https://www.jobrunr.io/en/)
- [Quartz demo dynamic job scheduling project](https://github.com/sanjay035/Dynamic-Job-Scheduler)