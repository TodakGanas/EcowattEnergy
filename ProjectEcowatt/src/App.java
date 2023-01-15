import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;


import java.time.Instant;
import java.util.List;

public class App {

    private Connection connexion;

    public Connection getConnexion(){
        return connexion;
    }

    //Fonction pour se connecter à la base de donnée SQLite
    public App(){
        try {
            // L'url d'accès
            String url = "jdbc:sqlite:./ecowatt.db";
            // Créer une bd ou l'ouvrir si existante
            connexion = DriverManager.getConnection(url);
            System.out.println("Connexion à SQLite établie.");    
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList searchIntoDb () {
        String sql;
        ResultSet rs;
        ArrayList<String> list = new ArrayList<>();

        sql= "select * from Infos";
        rs= null;

        try (
             PreparedStatement pstmt  = connexion.prepareStatement(sql)){
            rs  = pstmt.executeQuery();

            // Traitement du résultat
            while (rs.next()) {
                list.add(rs.getString("data"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;  
    }

    //fonction pour mettre le JSON dans un ArrayList
    public ArrayList<ArrayList<String>> jsonparser(String json){

        ArrayList<ArrayList<String>> dataset = new ArrayList<>();  //créer une liste qui contient des informations de type JSON

        JSONObject jObject = new JSONObject(json);
        JSONArray signals = jObject.getJSONArray("signals");

        //Pour chaque élément dans le String de JSON
        for(int i = 0; i < signals.length(); i++) {

            //une variable temporair qui stock des informations
            ArrayList<String> min_data = new ArrayList<>();

            JSONObject jobject = signals.getJSONObject(i);
            String dateDeGeneration = jobject.getString("GenerationFichier");
            String jour = jobject.getString("jour");
            int dvalueint = jobject.getInt("dvalue");
            String dvalue = Integer.toString(dvalueint);
            String message = jobject.getString("message");

            
            //Ajoute des informations extraits dans la liste
            min_data.add(dateDeGeneration);
            min_data.add(jour);
            min_data.add(dvalue);
            min_data.add(message);

            //l'extrait de la valeur pour chaque heure
            JSONArray value = jobject.getJSONArray("values");

            //On parcours les valeur dans le champ "values"
            for(int j=0; j<value.length(); j++){
                JSONObject object_value = value.getJSONObject(j);
                //int pas_int = object_value.getInt("pas");
                int hvalue_int = object_value.getInt("hvalue");

                //on convertit les valeurs de type "INT" au type "String"
                //String pas = Integer.toString(pas_int);
                String hvalue = Integer.toString(hvalue_int);

                //on ajoute ces informations dans l'ArrayList "min_data"
                //min_data.add(pas);
                min_data.add(hvalue);

            }

            //ajoute des datas dan dataset
            dataset.add(min_data);

        }
        //retourner le dataset
        return dataset;
    }


    public static void main(String[] args) throws Exception {

        App eco = new App();
        ArrayList<String> listData = eco.searchIntoDb();

        System.out.println(listData.get(0));

        //int = 0 tu nanti tukar sbb ada bnyk tu list dalam tostring
        for(int k=0; k<listData.size();k++) {
         ArrayList<ArrayList<String>> tostring = eco.jsonparser(listData.get(k));

         for(int i=0; i<tostring.size(); i++){
            System.out.println("=============================================================");
            System.out.println("ID = " + (i+1));
            
            String GeFi = new String(tostring.get(i).get(0));
            String day = new String(tostring.get(i).get(1));
            String vd = new String(tostring.get(i).get(2));
            int value_jour = Integer.parseInt(vd);
            String message = new String(tostring.get(i).get(3));
            
            //saja print data
            //System.out.println("data"+",gf="+GeFi+",day="+day+",valueG="+vd+",message="+message);

            //for (int j=4; j<tostring.get(i).size(); j++){
            //    System.out.print(tostring.get(i).get(j) + ',');
            //}
            //System.out.print('\n');
            
            ArrayList<String> valeur_heure = new ArrayList<>();

            System.out.println("tosring size : " + tostring.size());
            for(int y=4; y<tostring.size(); y++){
                System.out.println(tostring.size());
            }

            System.out.println(tostring.get(i));
        
        //inserer les indformations dans influxdb
            char[] token = "KeUOm0c1RHNR_qyPkBx_De4QkpGt4xKMrF3Ld0N8IaJk2eLuP5X40ktSBT0Gt4cA2OfsHBE3PZGcClh0NukQ4g==".toCharArray();
            String org = "zhar";
            String bucket = "ecowatt";

            InfluxDBClient influxDBClient = InfluxDBClientFactory.create("http://34.163.77.10:8086/", token, org, bucket);

            WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();

            //OffsetDateTime odt = OffsetDateTime.parse(day);
            //Instant instant = odt.toInstant();

            Point point = Point.measurement("ecodata")
               .addField("GenerationFichier", GeFi)
               .addField("jour", day)
               .addField("message", message)
               .addField("value_du_jour", value_jour)
               .time(Instant.parse(day).toEpochMilli(), WritePrecision.MS);
               //.time(Instant.now().toEpochMilli(), WritePrecision.MS);

            writeApi.writePoint(bucket, org, point);
            //System.out.println("sudah masuk");
        }

    }

    }
}
