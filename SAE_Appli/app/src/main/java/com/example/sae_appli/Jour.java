package com.example.sae_appli;

import android.widget.Toast;

import java.time.OffsetDateTime;

public class Jour {

    String jour;
    String jourT;

    public Jour(String j){
        this.jour = new String(j);
    }

    //convertir le string vers le format stadardisé
    public String transform(){
        String t = "T00:00:00.000+01:00";
        this.jourT = jour+t;
        return jour+t;

    }

    public String transformFinish(){
        String t = "T12:00:00.000+01:00";
        return jour+t;
    }

    public String transformFinish_2(){
        OffsetDateTime dateTime = OffsetDateTime.parse(jourT);

        dateTime.plusDays(1);
        String ans = dateTime.toString();
        return ans;
    }
    //on verifier l'état de notre String
    public boolean check(){

        boolean c;
        if (jour.charAt(4) == '-' && jour.charAt(7) == '-'){
            c = true;
        }else{
            c = false;
        }
        return c;
    }

//    public String jourProchain(){
//        j
//    }
}
