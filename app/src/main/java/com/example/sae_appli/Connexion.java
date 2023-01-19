package com.example.sae_appli;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;

public class Connexion {

    private String url;
    private char[] token;
    private String org;
    private String bucket;

    private InfluxDBClient influxDBClient;

    //le mèthode par défaut
    public Connexion() {}

    public Connexion (String u, char[] t, String o, String b){
        //on met en place les valeurs dans chaque variable
        url = new String(u);
        this.token = t;
        org = new String(o);
        bucket = new String(b);
    }

    //le mèthode pour établir une connexion au serveur InfluxDB
    public void createConnection(){
        try{
            influxDBClient = InfluxDBClientFactory.create(this.url, this.token, this.org, this.bucket);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //retourne la variable influxDBClient
    public InfluxDBClient getClient(){
        return influxDBClient;
    }

    //le mèthode pour fermer la connexion
    public void closeConnection(){
        influxDBClient.close();
    }
}
