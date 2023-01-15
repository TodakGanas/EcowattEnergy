import java.lang.reflect.Array;
import java.time.Instant;
import java.util.ArrayList;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;

public class App {
    public static void main(String[] args) throws Exception {
        Connexion connexion = new Connexion();  //initialiser le class connexion

        ArrayList<String> listData = connexion.searchIntoDb();  //on creer une arraylist qui va contenir une liste de données
        
        //on parcourt chaque élément dans le listdata
        for(int k=0 ; k<listData.size() ; k++){
            ArrayList<ArrayList<String>> toString =  connexion.jsonparser(listData.get(k));

            for(int i=0; i<toString.size(); i++){
                Jour jour = new Jour(toString.get(i));
                jour.getDateByVal();  //une fonctione pour faire une liste de heure en format yyyy-mm-ddT23:00:00+01:00 sur chaque valeur d'heure
                //jour.afficheHeure();  //affiche l'heure


                /*
                 * On commence à insérer les données dans la base de donnée InfluxDB
                 */

                 //on specifie les inforamtions nessecaire pour se connecter au server influxDB
                char[] token = "PAd4rNjKgnkVhUU1OlAsKW7jkyb7ftwCfIHcUEvESCnr8raRcS6ZODiAlasAkdKYGpGj2QWrPtFfxETsB7m93w==".toCharArray();  //le jeton
                String org = "zhar";     //le nom d'organization
                String bucket = "ecowatt";  //le nom de bucket ou la base de données
    
                InfluxDBClient influxDBClient = InfluxDBClientFactory.create("http://34.163.151.230:8086/", token, org, bucket);  //on specifie l'url de notre serveur
    
                WriteApiBlocking writeApi_jour = influxDBClient.getWriteApiBlocking();

                //on utilise le mèthode point pour mettre les données dans influxDB

                //on insere les données des valuer du jour général
                Point point_jour = Point.measurement("EcowattDataJour")
                    .addField("GenerationFichier", jour.getGeneration())
                    .addField("message", jour.getMessage())
                    .addField("value_du_jour", Integer.parseInt(jour.getValeurJour()))
                    .time(Instant.parse(jour.getJourGeneral()).toEpochMilli(), WritePrecision.MS);
                //.time(Instant.now().toEpochMilli(), WritePrecision.MS);

                writeApi_jour.writePoint(bucket, org, point_jour);

                //on insere les données des valuer de chaque heure avec la meme mèthode <Point>
                if(jour.getValeurHeureSize() == 24){
                    for(int u=0; u<24; u++){

                        WriteApiBlocking writeApi_heure = influxDBClient.getWriteApiBlocking();
   
                        Point point_heure = Point.measurement("EcowattDataHeure")
                           .addField("GenerationFichier", jour.getGeneration()) //
                           .addField("message", jour.getMessage())
                           .addField("value_heure'", jour.getValeurHeure(u))
                           .time(Instant.parse(jour.getHeureValeur(u)).toEpochMilli(), WritePrecision.MS);
   
                         writeApi_heure.writePoint(bucket, org, point_heure);
                  }
                }
                

                influxDBClient.close();

            }
        }
        //System.out.println(toString);
    }
}
