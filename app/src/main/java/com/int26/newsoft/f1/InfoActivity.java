package com.int26.newsoft.f1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    TextView nomePilota;
    TextView numeroPilota;
    TextView dataNascita;
    TextView nazionalita;
    TextView erroreConnessione;
    Button scopriButton;
    String linkDiPiu;
    ProgressBar progressBar;
    LinearLayout layoutCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_pilota);

        progressBar = findViewById(R.id.progress_info);
        layoutCard = findViewById(R.id.layout_info);
        erroreConnessione = findViewById(R.id.errore_connessione_info);
        nomePilota = findViewById(R.id.nome_pilota_info);
        numeroPilota = findViewById(R.id.numero_vettura);
        dataNascita = findViewById(R.id.data_di_nascita);
        nazionalita = findViewById(R.id.nazionalita);
        scopriButton = findViewById(R.id.scopri_button);

        getIncomingIntent();

        scopriButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //aggiungere controllo esistenza link
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(linkDiPiu));
                startActivity(i);
            }
        });


    }

    private void getIncomingIntent() {

        //Evita che l'app crashi se non c'è un extra
        if (getIntent().hasExtra("nome_pilota")) {

            String nome_pilota = getIntent().getStringExtra("nome_pilota");
            costruisciUrl(nome_pilota);
        } else {
            Toast.makeText(this, "errore nel trovare la posizione dell'elemento", Toast.LENGTH_SHORT).show();

        }

    }

    private void costruisciUrl(String nome_pilota) {

        String url = "https://ergast.com/api/f1/drivers/" + nome_pilota + ".json";
        prendiRisultati(url);
    }

    //fetch dei dati e scrittura nell'activity

    private void prendiRisultati(String url) {

        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(this));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    //Trovo il primo oggetto all'interno del JSON
                    JSONObject primoRisultato = response.getJSONObject("MRData");
                    //Trovo il secondo oggetto all'interno del JSON
                    JSONObject secondoRisultato = primoRisultato.getJSONObject("DriverTable");
                    //Arrivo all'array che mi interessa e lo prendo dall'oggetto in cui si trova
                    JSONArray arrayRisultatiGare = secondoRisultato.getJSONArray("Drivers");

                    //Faccio un loop tramite il quale scorro tutti gli elementei dell'array gare
                    for (int i = 0; i < arrayRisultatiGare.length(); i++) {
                        //E prendo l'oggetto posizionato ad ogni "i", in questo caso dovrebbe esserci solo 1

                        JSONObject pilota = arrayRisultatiGare.getJSONObject(i);
                        String nomeCompleto = pilota.getString("givenName") + " " + pilota.getString("familyName");
                        nomePilota.setText(nomeCompleto);
                        numeroPilota.setText(pilota.getString("permanentNumber"));
                        dataNascita.setText(pilota.getString("dateOfBirth"));
                        nazionalita.setText(pilota.getString("nationality"));
                        linkDiPiu = pilota.getString("url");
                    }

                    progressBar.setVisibility(View.GONE);
                    layoutCard.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBar.setVisibility(View.GONE);
                layoutCard.setVisibility(View.INVISIBLE);
                erroreConnessione.setVisibility(View.VISIBLE);
            }
        });

        queue.add(request);

    }

}
