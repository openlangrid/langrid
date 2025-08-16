#!/bin/bash

docker compose down
mvn package -DskipTests=true
docker compose up -d
