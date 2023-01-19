package com.example.sae_appli;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.OffsetDateTime;


import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    /*
  Ajouter description
   */
    private SearchView searchBar;
    /*

     */
    private TextView date;
    /*

     */
    private TextView unknown;
    /*

     */
    private TextView message;

    private Button bouton;

    private EditText date_2;


    protected String val;
    protected String dateVal;
    private String dateVal_2;
    private String val_2;
    private String mes_2;
    protected String mes;
    private String toIntent ;

    /*
     On initialise les informations néccesaires pour se connecter au serveur
    */
    char[] token = "PAd4rNjKgnkVhUU1OlAsKW7jkyb7ftwCfIHcUEvESCnr8raRcS6ZODiAlasAkdKYGpGj2QWrPtFfxETsB7m93w==".toCharArray();  //le jeton
    String org = "zhar";     //le nom d'organization
    String bucket = "ecowatt";  //le nom de bucket ou la base de données
    String url = "http://34.163.151.230:8086/"; //l'url du serveur

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Recupere le champ search
       // searchBar = findViewById(R.id.search);

        //Recupere le champ date
        date = findViewById(R.id.date);

        //Recupere le champ unknown
        unknown = findViewById(R.id.color_j);

        //Recupere le champ message
        message = findViewById(R.id.message);

        //Recupere le champ button
        bouton = findViewById(R.id.button_1);

        //Recupere le champ search
        date_2 = findViewById(R.id.date_2);

        //mettre en listener sur le bouton GO!
        bouton.setOnClickListener(this);

        //on create un Thread pour que notre application puisse se connecter au serveur
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*
                On etabli une connexion au serveur influxDB
                 */
                Connexion connection = new Connexion(url, token, org, bucket);
                connection.createConnection();
                InfluxDBClient client = connection.getClient();


                while (true){
                    try{
                        //on crée une variable pour qu'on puisse avoir le bon format de la data d'aujourdh'ui
                        OffsetDateTime now = OffsetDateTime.now().minusDays(10);
                        OffsetDateTime end = OffsetDateTime.now().minusDays(9);

                        String start = now.toString();
                        String finish = end.toString();

                        start = start.substring(0,11) + "00:00:00.000" + start.substring(23);
                        finish = finish.substring(0,11) + "00:00:00.000" + finish.substring(23);

                        /*
                        On créer une FluxQuery pour pouvoir les données souhaitées
                         */
                        String query_jour = "from(bucket: \"ecowatt\") |> range(start: " + start + "  , stop: " + finish + ") |> filter(fn: (r) => r[\"_measurement\"] == \"EcowattDataJour\")  |> filter(fn: (r) => r[\"_field\"] == \"value_du_jour\")";
                        String query_mes  = "from(bucket: \"ecowatt\")|> range(start: " + start + " , stop: "+ finish +" ) |> filter(fn: (r) => r[\"_field\"] == \"message\")";

                        //execution de query et le garder dans le list de FluxTable
                        List<FluxTable> tables_jour= client.getQueryApi().query(query_jour, org);
                        List<FluxTable> tables_mes = client.getQueryApi().query(query_mes, org);

                        //on specifie la variable qu'on va mettre dans l'intent
                        toIntent = new String(start);

                        //Parcourir le FluxTable pour recupérer les données (Date et Valeur)
                        for (FluxTable table : tables_jour) {
                            for (FluxRecord record : table.getRecords()) {
                                dateVal = record.getValues().get("_time").toString();
                                val = record.getValues().get("_value").toString();
                            }

                        }
                        //Parcourir le FluxTable pour recupérer les données (Message du jour)
                        for (FluxTable table : tables_mes) {
                            for (FluxRecord record : table.getRecords()) {
                                mes = record.getValues().get("_value").toString();
                            }

                        }

                        //Il est important de fermer la connexion
                        connection.closeConnection();

                        /*
                        Nouveau Thread pour executer le code de changement d'interface
                         */
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //changer le champ date
                                date.setText(dateVal.substring(0,10));

                                //changer le text de champ unknown et le met au millieu
                                unknown.setText("MORE");
                                unknown.setGravity(Gravity.CENTER);

                                //met le message du jour au champ message et le met au millieu
                                message.setText(mes);
                                message.setGravity(Gravity.CENTER);

                                /*
                                On utilise le méthode Switch pour changer le couleur de champ unknown selon la valeur du jour
                                 */
                                switch (val) {
                                    case "1":
                                        // changer le couleur vers vert (valeur 1)
                                        unknown.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
                                        break;
                                    case "2":
                                        // changer le couleur vers jaune (valeur 2)
                                        unknown.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFF00")));
                                        break;
                                    case "3":
                                        // changer le couleur vers rouge (valeur 3)
                                        unknown.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                                        break;
                                    default:
                                        // la couleur par défaut (bleu)
                                        unknown.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0000FF")));
                                }
                            }
                        });

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

    //une fonction onClick sert à reagir lorsq'u le bouton est cliqué
    @Override
    public void onClick(View view) {
        //récupérer le String dans le Search Bar
        String t = date_2.getText().toString();

        //créer un nouveau class Jour
        Jour jour = new Jour(t);

        //vérifier si la date entrée est dans le bon format ou pas
        if (jour.check() == false){
            Toast.makeText(view.getContext(), "format invalide désolé" , Toast.LENGTH_SHORT).show();
        }else{
            //Créer un petit message en bas de l'ecran pour qu'on sait que le code est executé
            Toast.makeText(view.getContext(), "hooray" , Toast.LENGTH_SHORT).show();
            String newJour = jour.transform();
            String jourFinish = jour.transformFinish();

            //on specifie la variable qu'on va mettre
            toIntent = new String(newJour);

            //executer le code pour faire un changement sur l'interface MainActivity
            new Thread(new Runnable() {
                @Override
                public void run() {

                    //créer une nouvelle connexion
                    Connexion connection_2 = new Connexion(url, token, org, bucket);
                    connection_2.createConnection();
                    InfluxDBClient client_2 = connection_2.getClient();


                    while (true){
                        try{
                            //nouveaux FluxQuery
                            String query_jour_2 = "from(bucket: \"ecowatt\") |> range(start: " + newJour + "  , stop: " + jourFinish + ") |> filter(fn: (r) => r[\"_measurement\"] == \"EcowattDataJour\")  |> filter(fn: (r) => r[\"_field\"] == \"value_du_jour\")";
                            String query_mes_2  = "from(bucket: \"ecowatt\")|> range(start: " + newJour + " , stop: "+ jourFinish +" ) |> filter(fn: (r) => r[\"_field\"] == \"message\")";

                            //des listes des fluxTable
                            List<FluxTable> tables_jour_2= client_2.getQueryApi().query(query_jour_2, org);
                            List<FluxTable> tables_mes_2 = client_2.getQueryApi().query(query_mes_2, org);

                            //System.out.println(tables);

                            for (FluxTable table_2 : tables_jour_2) {
                                for (FluxRecord record_2 : table_2.getRecords()) {
                                    dateVal_2 = record_2.getValues().get("_time").toString();
                                    val_2 = record_2.getValues().get("_value").toString();

                                    //on ajout des valeurs dans un arraylist

                                }

                            }
                            for (FluxTable table_3 : tables_mes_2) {
                                for (FluxRecord record_3 : table_3.getRecords()) {
                                    mes_2 = record_3.getValues().get("_value").toString();

                                }

                            }
                            //Il est important de fermer la connexion
                            connection_2.closeConnection();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    date.setText(newJour.substring(0,10));
                                    unknown.setText(val_2);
                                    unknown.setGravity(Gravity.CENTER);
                                    message.setText(mes_2);
                                    message.setGravity(Gravity.CENTER);
                                    switch (val_2) {
                                        case "1":
                                            // changer le couleur vers vert (valeur 1)
                                            unknown.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
                                            break;
                                        case "2":
                                            // changer le couleur vers jaune (valeur 2)
                                            unknown.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFF00")));
                                            break;
                                        case "3":
                                            // changer le couleur vers rouge (valeur 3)
                                            unknown.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                                            break;
                                        default:
                                            // la couleur par défaut (bleu)
                                            unknown.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0000FF")));
                                    }
                                }
                            });

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

    //lancement de nouveau intent
    public void newIntent(View view){
        Intent action = new Intent(this, JourAct.class);
        action.putExtra("data", toIntent);
        startActivity(action);
    }
}