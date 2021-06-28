#!/usr/bin/env bash

echo 'Creating application user and db'

mongo ${MONGO_DATABASE} \
        --host ${MONGO_HOSTS}":"${MONGO_PORTS} \
        --authenticationDatabase admin \
        --eval "db.createUser({user: '${MONGO_USERNAME}', pwd: '${MONGO_PASSWORD}', roles:[{role:'dbOwner', db: '${MONGO_DATABASE}'}]});"
