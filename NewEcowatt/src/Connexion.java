import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Connexion {
    //le chemin d'ecowatt.db par défaut
    private String url = new String("jdbc:sqlite:./ecowatt.db");

    //initialiser la variable connection en utilisant le class Connection
    private Connection connexion;

    //la méthode getConnexion pour connexion
    public Connection getConnexion(){
        return connexion;
    }

    //le methode principal
    public Connexion(){
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

    //le methode pour chercher les informations dans la base de SQLite
    public ArrayList searchIntoDb () {
        String sql;
        ResultSet rs;
        ArrayList<String> list = new ArrayList<>();

        sql= "select * from Infos";  //la table Infos qui contient les informations souhaitées
        rs= null;

        try (
             PreparedStatement pstmt  = connexion.prepareStatement(sql)){
            rs  = pstmt.executeQuery();

            // Traitement du résultat
            while (rs.next()) {
                list.add(rs.getString("data")); // la colonne data qui contient le String JSON
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage()); //affiher le message dans le cas d'un échec
        }
        return list; //le resultat donnée dans l'arraylist 
    }

    public ArrayList<ArrayList<String>> jsonparser(String json){

        ArrayList<ArrayList<String>> dataset = new ArrayList<>();  //créer une liste de liste qui contient des informations de type JSON

        JSONObject jObject = new JSONObject(json);
        JSONArray signals = jObject.getJSONArray("signals");

        //Pour chaque élément dans le String de JSON
        for(int i = 0; i < signals.length(); i++) {

            //une variable temporaire qui stock des informations
            ArrayList<String> min_data = new ArrayList<>();

            JSONObject jobject = signals.getJSONObject(i);                        //initialiser l'objet signals
            String dateDeGeneration = jobject.getString("GenerationFichier"); //prendre le variable GenerationFichier du type String
            String jour = jobject.getString("jour");                          //prendre la variable jour du type Sring
            int dvalueint = jobject.getInt("dvalue");                         //prendre la variable dvalue de type interger
            String dvalue = Integer.toString(dvalueint);                          //transformer la variable dvalue au type string
            String message = jobject.getString("message");                    ////prendre la variable message de type interger

            
            //Ajoute des informations extraits dans la liste (une variable temporaire)
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

    
}
