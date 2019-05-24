package com.int26.newsoft.f1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    //ArrayList vari con i dati che riempiono i posti del singoloo item e il context da fornire
    private ArrayList<String> posizioniPiloti = new ArrayList<>();
    private ArrayList <String> numeriPiloti = new ArrayList<>();
    private ArrayList <String> nomiPiloti = new ArrayList<>();
    private ArrayList <String> scuderiePiloti = new ArrayList<>();
    private ArrayList <String> puntiPiloti = new ArrayList<>();
    private Context mContext;

    //Costruttore
    public RecyclerViewAdapter(Context mContext, ArrayList<String> posizioniPiloti, ArrayList<String> numeriPiloti, ArrayList<String> nomiPiloti, ArrayList<String> scuderiePiloti, ArrayList<String> puntiPiloti) {
        this.posizioniPiloti = posizioniPiloti;
        this.numeriPiloti = numeriPiloti;
        this.nomiPiloti = nomiPiloti;
        this.scuderiePiloti = scuderiePiloti;
        this.puntiPiloti = puntiPiloti;
        this.mContext = mContext;
    }

    //inflating del layout praticamente sempre così cambia solo il nome del Layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_lista_pilota, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //Qui riempio i vari campi con gli elementi al indice ennesimo dei vari ArrayList
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.posPilota.setText(posizioniPiloti.get(i));
        viewHolder.numPilota.setText(numeriPiloti.get(i));
        viewHolder.nomePilota.setText(nomiPiloti.get(i));
        viewHolder.scuderiaPilota.setText(scuderiePiloti.get(i));
        viewHolder.puntiPilota.setText(puntiPiloti.get(i));

        //trovo il nome della scuderia
        String scuderia = viewHolder.scuderiaPilota.getText().toString().trim();

        //modifico il colore dinamicamente in base alla scuderia
        switch (scuderia) {
            case "Mercedes":
                viewHolder.coloreScuderia.setBackgroundColor(ContextCompat.getColor(mContext, R.color.mercedes));
                break;
            case "Red Bull":
                viewHolder.coloreScuderia.setBackgroundColor(ContextCompat.getColor(mContext, R.color.redbull));
                break;
            case "Ferrari":
                viewHolder.coloreScuderia.setBackgroundColor(ContextCompat.getColor(mContext, R.color.ferrari));
                break;
            case "Haas F1 Team":
                viewHolder.coloreScuderia.setBackgroundColor(ContextCompat.getColor(mContext, R.color.haas));
                break;
            case "McLaren":
                viewHolder.coloreScuderia.setBackgroundColor(ContextCompat.getColor(mContext, R.color.mcclaren));
                break;
            case "Toro Rosso":
                viewHolder.coloreScuderia.setBackgroundColor(ContextCompat.getColor(mContext, R.color.tororosso));
                break;
            case "Renault":
                viewHolder.coloreScuderia.setBackgroundColor(ContextCompat.getColor(mContext, R.color.redbull));
                break;
            case "Alfa Romeo":
                viewHolder.coloreScuderia.setBackgroundColor(ContextCompat.getColor(mContext, R.color.alfaromeo));
                break;
            case "Racing Point":
                viewHolder.coloreScuderia.setBackgroundColor(ContextCompat.getColor(mContext, R.color.racingpoint));
                break;
            case "Williams":
                viewHolder.coloreScuderia.setBackgroundColor(ContextCompat.getColor(mContext, R.color.williams));
                break;

        }

        //Listener per gli item che vengono cliccati (non obbligatorio)

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(mContext, InfoActivity.class);
                intent.putExtra("nome_pilota", nomiPiloti.get(i));
                mContext.startActivity(intent);
            }
        });

    }

    //un campo significativo per contare gli elementi che ci saranno nela recicler, se è 0 non compare nulla
    @Override
    public int getItemCount() {
        return nomiPiloti.size();
    }

    //Collego le varie View a quelle del layout fatto appositamente e dichiaro anche il parent layout
    //che non è altro che il layout principale usato nel singolo elemento della lista
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView posPilota;
        TextView numPilota;
        TextView nomePilota;
        TextView scuderiaPilota;
        TextView puntiPilota;
        LinearLayout parentLayout;
        View coloreScuderia;
        //View pallinoVeloce;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            posPilota = itemView.findViewById(R.id.pos_pilota_gara);
            numPilota = itemView.findViewById(R.id.num_pilota_gara);
            nomePilota = itemView.findViewById(R.id.nome_pilota_gara);
            scuderiaPilota = itemView.findViewById(R.id.scuderia_pilota_gara);
            puntiPilota = itemView.findViewById(R.id.punti_pilota_gara);
            parentLayout = itemView.findViewById(R.id.lista_layout);
            coloreScuderia = itemView.findViewById(R.id.bordo_colore_card);
            //pallinoVeloce = itemView.findViewById(R.id.cerchio_fast);
        }
    }
}

