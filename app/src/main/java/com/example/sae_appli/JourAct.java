package com.example.sae_appli;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

import java.util.ArrayList;
import java.util.List;

public class JourAct extends AppCompatActivity {

    //on intialise les variable ne notre interface
    private TextView color_1;
    private TextView color_2;
    private TextView color_3;
    private TextView color_4;
    private TextView color_5;
    private TextView color_6;
    private TextView color_7;
    private TextView color_8;
    private TextView color_9;
    private TextView color_10;
    private TextView color_11;
    private TextView color_12;
    private TextView color_13;
    private TextView color_14;
    private TextView color_15;
    private TextView color_16;
    private TextView color_17;
    private TextView color_18;
    private TextView color_19;
    private TextView color_20;
    private TextView color_21;
    private TextView color_22;
    private TextView color_23;
    private TextView color_24;

    protected String valeur;

    /*
    On initialise les informations néccesaires pour se connecter au serveur
    */
    char[] token = "PAd4rNjKgnkVhUU1OlAsKW7jkyb7ftwCfIHcUEvESCnr8raRcS6ZODiAlasAkdKYGpGj2QWrPtFfxETsB7m93w==".toCharArray();  //le jeton
    String org = "zhar";     //le nom d'organization
    String bucket = "ecowatt";  //le nom de bucket ou la base de données
    String url = "http://34.163.151.230:8086/"; //l'url du serveur


    //on initialise les intent, datas(information recupere depuis l'autre activité)
    private Intent intent;
    private String datas;
    private String jourFinish;
    private ArrayList<String> info = new ArrayList<String>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act);

        //recuperer les informations depuis l'autre actrivité
        intent = getIntent();
        datas = intent.getStringExtra("data");

        //on fait un petite remarque en bas de l'ecran pour dire qu'il fonctione
        Toast.makeText(this, datas , Toast.LENGTH_SHORT).show();

        /*
        On lance un nouveau Thread pour recuperer le data depuis le serveur InfluxDB
         */
            new Thread(new Runnable() {
                @Override
                public void run() {

                    //créer une nouvelle connexion
                    Connexion connection = new Connexion(url, token, org, bucket);
                    connection.createConnection();
                    InfluxDBClient client_2 = connection.getClient();


                    while (true){
                        try{

                            //met le datas dans la class Jour
                            Jour jour = new Jour(datas);
                            jourFinish = jour.transformFinish_2();

                            //nouveaux FluxQuery
                            String query_heure = "from(bucket: \"ecowatt\") |> range(start: " + datas + "  , stop: " + jourFinish + ") |> filter(fn: (r) => r[\"_measurement\"] == \"EcowattDataHeure\")  |> filter(fn: (r) => r[\"_field\"] == \"value_heure'\")";

                            //des listes des fluxTable
                            List<FluxTable> tables_heure= client_2.getQueryApi().query(query_heure, org);

                            for (FluxTable table : tables_heure) {
                                for (FluxRecord record : table.getRecords()) {
                                    //recuperer la valeur de chaque heure
                                    valeur = record.getValues().get("_value").toString();

                                    //ajouter les valeurs dans l'arraylist
                                    info.add(valeur);
                                }

                            }
                            /*
                            Thread pour fait une changement de couleur pour chaque textview selon la valeur de chaque heure
                             */
                           runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    int resId;
                                    for (int i = 1; i <= 24; i++){
                                        resId = getResources().getIdentifier("color_" + i, "id", getPackageName());
                                        TextView color = findViewById(resId);
                                        String r = info.get(i-1);
                                        switch (r) {
                                                case "1":
                                                    // changer le couleur vers vert (valeur 1)
                                                    color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
                                                    break;
                                                case "2":
                                                    // changer le couleur vers jaune (valeur 2)
                                                    color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFF00")));
                                                    break;
                                                case "3":
                                                    // changer le couleur vers rouge (valeur 3)
                                                    color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                                                    break;
                                                default:
                                                    // la couleur par défaut (bleu)
                                                    color.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0000FF")));
                                            }

                                    }
                                }
                            });
                           //il est important de fermer la connexion
                            connection.closeConnection();

                            try{
                                Thread.sleep(5000);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        }

}
