import java.util.ArrayList;

public class Jour {
    //c'est un variable qui a tous les valeur de chaque heure pou un jour
    //les valeur de chaque heure s'exprime en 
    // private ArrayList<String> list_heure;
    ArrayList<Integer> list_heure = new ArrayList<Integer>();

    //pour les informations importants
    private String jourGeneral;
    private String dvalue; // on met la valeur du jour somme String pour simplifier les choses
    private String message;
    private String generationFicher;
    private String[] valeurHeure = new String[24]; //liste de heure de format yyyy-mm-ddT23:00:00+01:00 pour chaque valeur d'heure

    //initialiser les attributs dans cette class
    public Jour(ArrayList<String> s){
        this.generationFicher = new String(s.get(0));  //index 0 pour la date de la creation du fichier
        this.jourGeneral = new String(s.get(1));       //index 1 pour le jour concerné
        this.dvalue = new String(s.get(2));            //index 2 pour la valeur général du jour
        this.message = new String(s.get(3));           //index 3 pour le message du jour
  
        //index 4 à 27 sont les valeur par chaque heure
        //on met des valeur pour chaque heure dans une liste
        for(int i=4; i<s.size(); i++){
            list_heure.add(Integer.parseInt(s.get(i)));
        }
    }

    //pour envoyer la date de jour concerné
    public String getJourGeneral(){
        return jourGeneral;
    }

    //pour envoyer le message du jour
    public String getMessage(){
        return message;
    }

    //pour envoyer la valeur du jour général
    public String getValeurJour(){
        return dvalue;
    }

    //retourne la date de la génération du fichier
    public String getGeneration(){
        return generationFicher;
    }

    //traduire l'heure vers date de format yyyy-mm-ddT23:00:00+01:00
    //une fonctione pour faire une liste de heure en format yyyy-mm-ddT23:00:00+01:00 sur chaque valeur d'heure
    public void getDateByVal(){
        //on initialise un array pour specifier l'heure
        String[] heure = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};

        String jour_concerne = this.jourGeneral; //on utilise jour_concerne pour simplifier les choses
        for (int i=0 ; i<valeurHeure.length; i++){      //valeureHeure est initialiser en haut avec la longuer de 24
            valeurHeure[i] = jour_concerne.substring(0,11)+heure[i]+jour_concerne.substring(13);
        }
    }

    //une fonctione simple pour afficher le liste de valeur pour chaque heure
    public void afficheHeure(){
        for(int i=0; i<valeurHeure.length; i++){
            System.out.println(valeurHeure[i]);
        }
    }

    //fonctione qui retourne l'heure du format yyyy-mm-ddT23:00:00+01:00 à partir de parametre d'entrée "o"
    public String getHeureValeur(int o){
        return valeurHeure[o];
    }

    //une fonctione qui retourne la valeur de heure specifier dans la parametre
    public int getValeurHeure(int i){
        return list_heure.get(i);
    }

    //une fonctione qui retourne la taile ou le longuer de la liste de valeur
    public int getValeurHeureSize(){
        return list_heure.size();
    }
}

