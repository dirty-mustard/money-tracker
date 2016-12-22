#!/bin/bash
#set -e

createuser $POSTGRES_USER
createdb $POSTGRES_DB -O $POSTGRES_USER

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" $POSTGRES_DB < /maven/init.sql
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" $POSTGRES_DB < /maven/data.sql
