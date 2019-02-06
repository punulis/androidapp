package com.example.mokytojas.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class search_pokmeon extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search_pokmeon);
//    }
    private Context context;
    private LayoutInflater inflater;


    List<Pokemon> data= Collections. emptyList() ;
    Pokemon current;
    int currentPos=0;


    // create constructor to initialize context and data sent from MainActivity
    public AdapterCars(Context context, data){
        this. context=context;
        inflater=LayoutInflater. from(context);
        this. data=data;
        for(Pokemon coolPokemon: data)
        {
            Log. e("coolPokemon", coolPokemon.getBrand()+' '+coolPokemon .getData());
        }
    }

    // Inflate the layout when ViewHolder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater. inflate(R.layout.container_car, parent,false);
        MyHolder holder = new MyHolder(view) ;
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

// Get current position of item in RecyclerView to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        Pokemon current=data.get(position);
        myHolder.textName.setText(current.getName());
        myHolder.textWeight.setText( "Weight : " + current.getWeight());
        myHolder.textCp.setText( "CP: " + current.getCp());
        myHolder.textAbilities.setText( "Abilities : " + current.getAbilities());
        myHolder.textType.setText( "Type : " + current.getType());
                Log.e( "fancyCar", current.getBrand()+ " " + current. getData());

    }

// return total item from List

    @Override
    public int getItemCount() {
        return data. Size();

    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnC1ickListener{
        TextView textName;
        TextView textWeight;
        TextView textCp;
        TextView textAbilities;
        TextView textType;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super( itemView);
            textName = (TextView) itemView.findViewById(R.id.entryName);
            textWeight = (TextView) itemView.findViewById(R.id.entryWeight);
            textCp = (TextView) itemView. findViewById(R.id.entryCp);
            textAbilities = (TextView) itemView. findViewById(R.id.entryAbilities);
            textType = (TextView) itemView. findViewById(R.id.entryType);
            itemView.setOnClickListener(this);

        }



        // Click event for all items
        @Override
        public void onC1ick(View v) {
            Toast.makeText (context, "You clicked an item", Toast.LENGTH_SHORT).show();
        }
    }
}

