#!/usr/bin/env bash
export LB_HOME=/home/ruslan/internship/liquibase
$LB_HOME/liquibase --driver=org.postgresql.Driver \
--classpath=$LB_HOME/lib \
--changeLogFile=databaseChangeLog.sql \
--url="jdbc:postgresql://localhost:5432/masterjava" \
--username=postgres \
--password=Cool61082 \
migrate