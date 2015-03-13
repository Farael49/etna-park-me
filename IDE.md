Deux principaux IDE sont utilisables pour développer en Java et sous Android : Eclipse et Android Studio

## Background ##
Eclipse fut le principal IDE utilisé pour développer sur Android, à l'aide du plugin ADT (Android Development Tools) qui était maintenu par Google et prévu pour Eclipse. Android gagnant en importance, Google a fini par développer son propre IDE, Android Studio, et vient récemment d'en sortir une première version stable.

## Choix de l'IDE ##
  * Eclipse dispose d'une communauté importante (principal IDE Java avec `NetBeans`), de nombreuses années d'expérience, et est un IDE très complet. L'ADT s'intègre très simplement, et permet le même fonctionnement que sous Android Studio.

  * Android Studio est relativement récent (une version bêta était à depuis plusieurs mois, mais la version stable officielle n'a qu'un ou deux mois), est amené à évoluer fortement, mais ne dispose pas encore d'une grosse communauté, même s'il est basé sur la même logique que d'autres IDE. C'est un choix logique dans le cadre d'une entreprise amenée à évoluer sous Android, mais n'est pas forcément le plus simple à l'heure actuelle.

Personnellement, j'utiliserais Eclipse, ayant plus d'expérience avec son fonctionnement et ses raccourcis (Android Studio ne dispose pas des mêmes keybinds...), et ayant déjà développé de nombreux projets Java et quelques projets Android avec.

Il est tout à fait possible de faire correspondre un projet Eclipse et un projet Android Studio, cependant quelques changements seront nécessaires. Une fois le projet importé (en précisant que ce n'est pas un projet Android Studio), il sera nécessaire de porter attention aux fichiers modifiés sur le svn afin de ne faire évoluer que les quelques classes impactées lors des changements, car chaque IDE crée sa propre architecture de dossiers/fichiers (Donc les changements seront à reporter à la main/un par un, et pas via un svn update du workspace entier).

## Configuration ##
Il sera nécessaire d'avoir : <br>
Un IDE à jour.<br>
Un JRE ou un JDK récent.<br>
Dans le cadre d'Eclipse, télécharger l'ADT via le menu "Help"->"Check Market Place" et chercher "Android Development Tools for Eclipse" by Google, Inc., et télécharger le SDK Android sur Internet.<br>
Le téléchargement d'Android Studio inclue en général le SDK et l'ADT est intégré à l'IDE.<br>
<br>
<h2>Android SDK Manager</h2>
L'Android SDK Manager permet de télécharger les outils et les API nécessaires pour développer sous Android et utiliser l'AVD. Il est nécessaire de disposer d'au moins une version d'Android et il est vivement conseillé d'installer l'Intel x86 Emulator Accelerator avec un proco Intel.<br>
<br>
<h2>AVD Manager</h2>
L'AVD Manager, Android Virtual Device Manager, permet de créer un ... Android Virtual Device (donc un émulateur Android). Il est possible de créer l'appareil que l'on souhaite, spécifier diverses options, dans le cadre du projet nous nous intéresserons principalement aux smartphone afin de s'assurer que l'UI soit adaptée selon les différentes tailles d'écran.