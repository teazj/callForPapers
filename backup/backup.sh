#!/bin/bash

readonly HERE="$(readlink -f $(dirname "$0"))"

mkdir -p "$HERE/backups"
BACKUP_FILE="$HERE/backups/database-$(date +"%Y%m%d-%H%M%S").mysqldump.gz"

/bin/sh -c 'exec mysqldump  -h db -uroot -p"$MYSQL_ROOT_PASSWORD" --all-databases' | gzip > "$BACKUP_FILE"
