# Introduction #

Nous allons voir comment déployer le webservice et lancer l'application.


# `WebService` #

Le `WebService` nécessite un environnement LAMP/WAMP/MAMP accessible en ligne. Pour rendre votre serveur local accessible, il suffit de configurer votre routeur afin de rediriger le port 80 vers l'ip de la machine faisant tourner votre serveur. La vidéo http://youtu.be/-rKDhZJignU?t=2m46s explique si besoin comment faire, la partie sur DynDNS est inutile car vous vous servirez simplement de l'ip de votre routeur dans le cadre des tests.
Une fois votre serveur accessible en ligne (http://ip_de_votre_routeur/) :
  1. Importez la DataBase (via PhpMyAdmin par exemple) récupérée dans /trunk/DataBase/
  1. Mettez le dossier script\_php (/trunk/Code/script\_php) dans votre www directory
  1. Modifiez le fichier config.php pour refléter la configuration de votre database
  1. C'est tout... Pour le moment.

# Android Application #

Le projet actuel nécessite une modification afin d'aller contacter votre serveur.
  1. Importez le projet Android (récupéré dans /trunk/Code/android\_project) via votre IDE (quelque chose comme "File"->"Import"->"Existing Android Code Into Workspace" sur Eclipse)
  1. Modifiez ServerRequests.java (dans ParkMe/src/classes/), en remplaçant l'url ligne 29 par l'adresse du script sur votre serveur : "http://107.170.145.214/android_connect/php_script/crud.php" devient "http://ip_de_votre_routeur/dossiers_jusquau_script/crud.php"
  1. Ctrl+S pour sauvegarder votre modification (oui bon voilà...)
  1. Lancez l'application sur votre Android Device

Une fois lancée, en supposant qu'aucune erreur n'ait été rencontrée et que le serveur soit bien démarré, l'application devrait ouvrir une page demandant un username (email) et un password. En appuyant sur Submit, l'application contacte votre serveur, et tente d'authentifier l'utilisateur. L'inscription n'étant pas encore disponible, afin de s’authentifier il sera nécessaire de créer au moins 1 utilisateur à la main dans la table user ;)