package com.int26.newsoft.f1;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    TextView titoloGara;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titoloGara = findViewById(R.id.titolo_gara);
        prendiTitoloGara();

        //loading the default fragment
        loadFragment(new LastRaceFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);

    }


    private void prendiTitoloGara() {

        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(this));

        String url = "https://ergast.com/api/f1/current/last/results.json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    //Trovo il primo oggetto all'interno del JSON
                    JSONObject primoRisultato = response.getJSONObject("MRData");
                    //Trovo il secondo oggetto all'interno del JSON
                    JSONObject secondoRisultato = primoRisultato.getJSONObject("RaceTable");
                    //Arrivo all'array che mi interessa e lo prendo dall'oggetto in cui si trova
                    JSONArray arrayRisultatiGare = secondoRisultato.getJSONArray("Races");

                    //Faccio un loop tramite il quale scorro tutti gli elementei dell'array gare
                    for (int i = 0; i < arrayRisultatiGare.length(); i++) {
                        //E prendo l'oggetto posizionato ad ogni "i", in questo caso dovrebbe esserci solo 1
                        JSONObject gara = arrayRisultatiGare.getJSONObject(i);

                        String nomeGP = gara.getString("raceName");
                        titoloGara.setText(nomeGP);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(request);

    }


    //choosing the tab fragment
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.last_race:
                fragment = new LastRaceFragment();
                prendiTitoloGara();
                break;

            case R.id.general_chart:
                fragment = new GeneralFragment();
                titoloGara.setText("Classifica Generale");
                break;

        }

        return loadFragment(fragment);
    }


    //loading the fragment
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}


