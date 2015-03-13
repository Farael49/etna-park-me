## CRUD ? ##
CRUD : Create / Read / Update / Delete
Le script PHP permet d'intéragir avec la DataBase et d'exécuter les requetes nécessaires a son fonctionnement.

Actuellement, 4 fonctions sont prévues :

## add\_spot() ##
(insert) Ajouter un emplacement de parking libre dans la Base de Données

## search\_spot\_in\_area() ##
(select) Retourne les emplacements libres disponibles aux alentours.

## update\_spot() ##
(update) Met à jour les données d'un emplacement de parking, permet d'indiquer la réservation d'un emplacement de parking.

## remove\_spot() ##
(delete) Supprime un emplacement de parking, s'il n'est plus proposé ou si la date est dépassée. (cron pour supprimer chaque jour date dépassée ? )