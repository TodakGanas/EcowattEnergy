import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

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


            
            dataset.add(min_data);

        }
        
        return dataset;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("");

        App eco = new App();
        ArrayList<String> listData = eco.searchIntoDb();

        ArrayList<ArrayList<String>> tostring = eco.jsonparser(listData.get(0));

        for(int i=0; i<tostring.size(); i++){
            System.out.println("=============================================================");
            System.out.println("ID = " + (i+1));
            System.out.println(tostring.get(i));

        }



    
    }
}
