package com.int26.newsoft.f1;

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

public class GeneralFragment extends Fragment {

    private ArrayList<String> posizioniPiloti = new ArrayList<>();
    private ArrayList<String> numeriPiloti = new ArrayList<>();
    private ArrayList<String> nomiPiloti = new ArrayList<>();
    private ArrayList<String> scuderiePiloti = new ArrayList<>();
    private ArrayList<String> puntiPiloti = new ArrayList<>();

    private RecyclerView recyclerView;

    private ProgressBar progressBar;
    private TextView testoErroreConess;
    private TextView nomeClassifica;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_general_fragment, null);
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        recyclerView = v.findViewById(R.id.recycler_class_generale);

        progressBar = v.findViewById(R.id.progress_circular_gen);
        testoErroreConess = v.findViewById(R.id.text_di_errore_gen);
        nomeClassifica = v.findViewById(R.id.nome_classifica_gen);
        prendiRisultati();

        return v;
    }

    private void prendiRisultati() {
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));

        String url = "https://ergast.com/api/f1/current/driverStandings.json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    //Trovo il primo oggetto all'interno del JSON
                    JSONObject primoRisultato = response.getJSONObject("MRData");
                    //Trovo il secondo oggetto all'interno del JSON
                    JSONObject secondoRisultato = primoRisultato.getJSONObject("StandingsTable");
                    //Arrivo all'array che mi interessa e lo prendo dall'oggetto in cui si trova
                    JSONArray arrayRisultatiGare = secondoRisultato.getJSONArray("StandingsLists");

                    for (int i = 0; i < arrayRisultatiGare.length(); i++) {
                        JSONObject singoloPilota = arrayRisultatiGare.getJSONObject(i);
                        JSONArray arraySingoloPilota = singoloPilota.getJSONArray("DriverStandings");

                        for (int x = 0; x < arraySingoloPilota.length(); x++) {
                            JSONObject trivia = arraySingoloPilota.getJSONObject(x);

                            posizioniPiloti.add(trivia.getString("position"));
                            puntiPiloti.add(trivia.getString("points"));

                            JSONObject pilota = trivia.getJSONObject("Driver");

                            String nomeCompleto = pilota.getString("givenName") + " " + pilota.getString("familyName");

                            nomiPiloti.add(nomeCompleto);
                            numeriPiloti.add(pilota.getString("permanentNumber"));

                            JSONArray costruttori = trivia.getJSONArray("Constructors");

                            for (int y = 0; y < costruttori.length(); y++) {

                                JSONObject costruttore = costruttori.getJSONObject(y);

                                scuderiePiloti.add(costruttore.getString("name"));
                            }

                        }

                        nomeClassifica.setVisibility(View.VISIBLE);
                        initRecyclerView();

                    }

                    initRecyclerView();
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