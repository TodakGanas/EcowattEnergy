import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class App {

    
    private Connection connexion;

    public Connection getConnexion () {
        
        return connexion;
    }

    public void initDb () {
        Statement s;
        String sql;
        
        try {
            s= connexion.createStatement();
            sql= "CREATE TABLE IF NOT EXISTS Infos ( " + 
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nom VARCHAR( 150 ), " +
                "annee INTEGER);";
            s.execute(sql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void insertIntoDb (String nom, int an) {
        String sql;

        nom= "dupont";
        an= 1980;

        sql= "insert into Infos ('nom', 'annee') Values (?, ?);";
        try (
        PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setString(1, nom);
            pstmt.setInt(2, an);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void searchIntoDb () {
        String sql;
        ResultSet rs;

        sql= "select * from Infos";
        rs= null;

        try (
             PreparedStatement pstmt  = connexion.prepareStatement(sql)){
            rs  = pstmt.executeQuery();

            // Traitement du résultat
            while (rs.next()) {
                System.out.println(
                    rs.getInt("id") +  "\t" + 
                    rs.getString("nom") + "\t" +
                    rs.getInt("annee")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }  
    }

    public App () {
        try {
            // L'url d'accès
            String url = "jdbc:sqlite:./demo.db";
            // Créer une bd ou l'ouvrir si existante
            connexion = DriverManager.getConnection(url);
            System.out.println("Connexion à SQLite établie.");    
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        App p;

        p= new App();
        p.initDb();
        p.insertIntoDb("Dupont", 1980);
        p.searchIntoDb();
    }
}
