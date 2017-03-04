#! /bin/bash
if [ $# -le 1 -o $# -ge 3 ]; then
echo "Usage: installdb.sh dbname rolename"
exit 1
fi

echo "-- creating role, database and stored procedure."
createuser -S -D -R -P $2
createdb $1 -O $2 -E 'UTF8'
createlang plpgsql $1
psql $1 < create_storedproc.sql
psql $1 -c "ALTER FUNCTION \"AccessStat.increment\"(character varying, character varying, character varying, character varying, character varying, timestamp without time zone, timestamp without time zone, integer, timestamp without time zone, integer, timestamp without time zone, integer, integer, integer, integer) OWNER TO $2"
echo "-- installation completed."

