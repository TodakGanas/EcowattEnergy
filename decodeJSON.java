package com.example.demojson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText input;
    private TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input= findViewById(R.id.inputString);
        output= findViewById(R.id.output);
    }

    public void decodeJSON (View v) {
        String s;
        JSONObject jobj;
        JSONArray tobj;
        int vi;
        StringBuilder r;


        s = input.getText().toString();
        r = new StringBuilder("");

        try {
            jobj = new JSONObject(s);
            tobj = jobj.getJSONArray("questions");
            for (int i = 0; i < tobj.length(); i++) {
                vi = tobj.getInt(i);
                r.append(vi);
                r.append("-");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        output.setText(r.toString());
    }

}
