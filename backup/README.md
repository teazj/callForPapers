# Conteneur de backup de la base SQL

Effectue un backup local (pour le moment) de la base de données MySQL du conteneur "linké".
Le but à terme est de pousser le backup sur GCloud Storage.

## Build  

    $ docker build -t mysql_backup .

## Utilisation

Pour lancer un backup (remplacer CONTENEUR_MYSQL, LE_MOT_DE_PASSE, REPERTOIRE_LOCAL) : 

    $ docker run -ti --link CONTENEUR_MYSQL:db -e MYSQL_ROOT_PASSWORD="LE_MOT_DE_PASSE" -v REPERTOIRE_LOCAL:/backups mysql_backup 

