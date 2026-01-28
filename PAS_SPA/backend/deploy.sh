#!/bin/bash
mvn clean package
asadmin redeploy --name PAS_SPA target/PAS_SPA.war

