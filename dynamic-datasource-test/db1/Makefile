.PHONY: all
all: help

SHELL := /bin/bash

DB=plutus

MIGRATE_IMG=migrate/migrate

MYSQL_HOST=10.192.33.86
MYSQL_USER=plutus_user
MYSQL_PWD=cAubtNi4joKgU7Z1VasM

.PHONY: help
help:
	################################################################
	# [AVAILABLE COMMANDS]
	# conn           - connect mysql
	# docker-migrate - make migration by using docker
	# migrate        - make migration
	################################################################
	@echo "Enjoy!"

.PHONY: conn
conn:
	mysql -A -h${MYSQL_HOST} -u${MYSQL_USER} -p${MYSQL_PWD} ${DB}

.PHONY: docker-migrate
docker-migrate:
	@if [ "$(v)" == "" ]; then \
		docker run -it --rm -v ${PWD}/migrations:/migrations "${MIGRATE_IMG}" -path /migrations -database "mysql://${MYSQL_USER}:${MYSQL_PWD}@tcp(${MYSQL_HOST}:3306)/${DB}" up; \
	elif [ "$(v)" == "down" ]; then \
		docker run -it --rm -v ${PWD}/migrations:/migrations "${MIGRATE_IMG}" -path /migrations -database "mysql://${MYSQL_USER}:${MYSQL_PWD}@tcp(${MYSQL_HOST}:3306)/${DB}" down; \
	else \
		docker run -it --rm -v ${PWD}/migrations:/migrations "${MIGRATE_IMG}" -path /migrations -database "mysql://${MYSQL_USER}:${MYSQL_PWD}@tcp(${MYSQL_HOST}:3306)/${DB}" goto $(v); \
	fi;

.PHONY: migrate
migrate:
	@if [ "$(v)" == "" ]; then \
		migrate -path ./migrations -database "mysql://${MYSQL_USER}:${MYSQL_PWD}@tcp(${MYSQL_HOST}:3306)/${DB}" up; \
	elif [ "$(v)" == "down" ]; then \
		migrate -path ./migrations -database "mysql://${MYSQL_USER}:${MYSQL_PWD}@tcp(${MYSQL_HOST}:3306)/${DB}" down; \
	else \
		migrate -path ./migrations -database "mysql://${MYSQL_USER}:${MYSQL_PWD}@tcp(${MYSQL_HOST}:3306)/${DB}" goto $(v); \
	fi;
