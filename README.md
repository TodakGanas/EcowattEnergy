# EcowattEnergy

Introduction:

Dans cette application, nous allons utiliser l'API d'Ecowatt Energy pour connecter et récupérer des données sur la consommation d'énergie en France. Ces données seront ensuite affichées de manière claire et facile à comprendre pour les utilisateurs, leur permettant de suivre et de comprendre leur consommation d'énergie en temps réel.

![sae32 drawio (1)](https://user-images.githubusercontent.com/105039681/212767475-48eca71a-cbb1-4194-9cd8-03d239c18725.png)

le figure montre notre première avis sur comment notre appli va fonctionner

**La première partie du projet :**

Sur la primière partie du projet, on a créé une programme en JAVA permettant de récupérer les donnés de SQLite et l'insérer dans la base de données InfluxDB qui se trouve dans le cloud. 
On a utilisé le ficher ecowatt.db qui se trouve le l'url "http://isis.unice.fr/~mgautero/ext/sae302/bd/ecowatt.db". Ce fichier est mis en place par Monsieur Gautero pour simplifier les choses nottament sur la création de requete pour recuperer le JSON de monecowatt API.

* Le program sur la première partie se trouve dans le répertoire NewEcowatt
* Les fichiers JAR sertent à ajouter des classes nessécaire pour qu'on puisse utiliser les classes comme InfluxDBCLient etc.
