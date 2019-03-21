package com.int26.newsoft.f1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LastRaceFragment extends Fragment {


    private ArrayList<String> posizioniPiloti = new ArrayList<>();
    private ArrayList<String> numeriPiloti = new ArrayList<>();
    private ArrayList<String> nomiPiloti = new ArrayList<>();
    private ArrayList<String> scuderiePiloti = new ArrayList<>();
    private ArrayList<String> puntiPiloti = new ArrayList<>();

    private RecyclerView recyclerView;

    private ProgressBar progressBar;
    private TextView testoErroreConess;
    private TextView titoloGara;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.activity_last_race_fragment, null);
        super.onCreate(savedInstanceState);

        recyclerView = v.findViewById(R.id.recycler_risultati_piloti);

        progressBar = v.findViewById(R.id.progress_circular);
        testoErroreConess = v.findViewById(R.id.text_di_errore);
        titoloGara = v.findViewById(R.id.titolo_gara);
        prendiRisultati();

        return v;
    }


    private void prendiRisultati() {

        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));

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
                        //Prendo l'array con i risultati dei piloti che si trova nell'oggetto alla posizione i
                        //Cioè la i-esima gara della stagione
                        JSONArray arrayRisultatiPiloti = gara.getJSONArray("Results");


                        //Scorro i risultati all'interno di quest'ultimo per avere la classifica completa dei piloti
                        for (int p = 0; p < arrayRisultatiPiloti.length(); p++) {

                            JSONObject pilota = arrayRisultatiPiloti.getJSONObject(p);

                            posizioniPiloti.add(pilota.getString("position"));
                            numeriPiloti.add(pilota.getString("number"));
                            puntiPiloti.add(pilota.getString("points"));

                            JSONObject infoPilota = pilota.getJSONObject("Driver");

                            nomiPiloti.add(infoPilota.getString("familyName"));

                            JSONObject infoScuderia = pilota.getJSONObject("Constructor");

                            scuderiePiloti.add(infoScuderia.getString("name"));
                        }

                        initRecyclerView();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBar.setVisibility(View.GONE);
                testoErroreConess.setVisibility(View.VISIBLE);
            }
        });

        queue.add(request);

    }

    private void initRecyclerView() {

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), posizioniPiloti, numeriPiloti, nomiPiloti, scuderiePiloti, puntiPiloti);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        progressBar.setVisibility(View.GONE);
    }
}
