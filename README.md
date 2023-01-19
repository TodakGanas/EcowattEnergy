# EcowattEnergy

**Quelques informations sur le fichier :**

* La répertiore *NewEcowatt* est le repertoire où il contient les fichiers et les programmes necessaires pour extraire le SQLite et l'insérer dans InfluxDB
* Le fichier *6DB6EBDC-23AC-4A6F-983A-4E3642764468.pdf* contient les diaporamas

Introduction:

Dans cette application, nous allons utiliser l'API d'Ecowatt Energy pour connecter et récupérer des données sur la consommation d'énergie en France. Ces données seront ensuite affichées de manière claire et facile à comprendre pour les utilisateurs, leur permettant de suivre et de comprendre leur consommation d'énergie en temps réel.

![sae32 drawio (1)](https://user-images.githubusercontent.com/105039681/212767475-48eca71a-cbb1-4194-9cd8-03d239c18725.png)

le figure montre notre première avis sur comment notre appli va fonctionner

**La première partie du projet :**

Sur la primière partie du projet, on a créé une programme en JAVA permettant de récupérer les donnés de SQLite et l'insérer dans la base de données InfluxDB qui se trouve dans le cloud. 
On a utilisé le ficher ecowatt.db qui se trouve le l'url "http://isis.unice.fr/~mgautero/ext/sae302/bd/ecowatt.db". Ce fichier est mis en place par Monsieur Gautero pour simplifier les choses nottament sur la création de requete pour recuperer le JSON de monecowatt API.

* Le program sur la première partie se trouve dans le répertoire NewEcowatt
* Les fichiers JAR sertent à ajouter des classes nessécaire pour qu'on puisse utiliser les classes comme InfluxDBCLient etc.

**La deuxieme partie du projet :**

Sur cette partie, on veut bien recuperer des donnees qu'on a sauvegarder dans InfluxDB. Pour arriver a cette but, on initialise la connexion avec InfluxDB et ensuite injecter le query pour recuperer des donnees.

* Le programme sur la deuxieme partie se trouve dans le repertoire rec_data

**La troiseme partie du projet :**

Dans cette derniere etape, on a pour but de construit l'interface des utilisateurs et de creer l'interaction entre cette application et InfluxDB. Pour atteindre ce but,
On fait une amelioration pour que le programme rec_data convient pour Android studio.

* La conception se fait avec Invision studio et Android studio


**Quelques informations sur le fichier :**

* La répertiore *NewEcowatt* est le repertoire où il contient les fichiers et les programmes necessaires pour extraire le SQLite et l'insérer dans InfluxDB
* Le fichier *6DB6EBDC-23AC-4A6F-983A-4E3642764468.pdf* contient les diaporamas

