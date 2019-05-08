### ala-soils2sat-aat   [![Build Status](https://travis-ci.org/tokmakoff/ala-soils2sat.svg?branch=master)](https://travis-ci.org/tokmakoff/ala-soils2sat)

# Running with Docker

## Tools required:
  - Docker = 18.09
  - docker-compose 1.24

## App config
Docker was retro-fitted to this app. It's not a 12 factor app so there's not much you can control using env vars.
Instead, the config is stored in the database so to configure the app you must first start the stack, then login as the
admin user (default password in `grails-app/conf/Bootstrap.groovy`) and go to
http://localhost:8080/ala-soils2sat/admin/settings (change your host and port) to see/change the system settings.

## Quick steps to get started with the service

  1. create a new VM
  1. install `docker` and `docker-compose`
  1. clone this repo onto the VM
  1. copy the template script to start the docker stack

        pushd ./docker
        cp start-or-restart-stack.sh.example start-or-restart-stack.sh
        chmod +x start-or-restart-stack.sh
        popd

  1. edit `./docker/start-or-restart-stack.sh` to add the required sensitive details
  1. spin up the compose stack

        ./docker/start-or-restart-stack.sh
        # or if you need to trigger a rebuild of the app Docker image (after a git pull)
        ./docker/start-or-restart-stack.sh --build

  1. open a browser to `https://<VM IP>:443` (port `80` will redirect to HTTPS)

## About
We're running a number of docker containers.
  1. the app using `grails run-war`, which uses tomcat
  1. Postgres
  1. a container to do periodic backups of the postgres database to AWS S3
  1. an nginx instance running as a reverse proxy to terminate HTTPS connections and do some caching
  1. a LetsEncrypt container to automatically generate the SSL/TLS certificates for nginx

## Useful commands

Trigger adhoc PG backup to S3:
```bash
docker exec -it s2s-pgbackups3 /bin/sh /backup.sh
```

To start the service, or to update after making changes to `docker-compose.yml`:

```bash
./docker/start-or-restart-stack.sh
```

To stop the stack, but **leave the DB data volume alone**:

```bash
docker-compose stop
# from here you can run the `./docker/start-or-restart-stack.sh` command to start the stack again, and it'll pick up the existing DB data volume
```

You can also use `docker-compose down`, which will remove the containers but the data volumes still exist.


To completely **remove all traces, including the DB data** and other data volumes, use:

```bash
docker-compose down --volumes # the --volumes flag nukes the volumes too
```

View the last 10 lines and follow logs for a container with:

```bash
docker logs --follow --tail 10 s2s_app
```
