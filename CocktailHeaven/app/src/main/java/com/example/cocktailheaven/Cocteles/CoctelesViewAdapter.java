package com.example.cocktailheaven.Cocteles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocktailheaven.R;

import java.util.List;

public class CoctelesViewAdapter extends RecyclerView.Adapter<CoctelesViewHolder> {

    private List<CoctelesData> cocteles;

    private Activity activity;

    public CoctelesViewAdapter(List<CoctelesData> dataSet, Activity activity){
        this.cocteles=dataSet;
        this.activity=activity;
    }


    @NonNull
    @Override

    public CoctelesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View coctelView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cocteles_recycler_cell,parent,false);
        return new CoctelesViewHolder(coctelView);
    }
    @Override
    public void onBindViewHolder(@NonNull CoctelesViewHolder holder, int position) {
        CoctelesData dataForThisCell = cocteles.get(position);
        holder.bind(dataForThisCell);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, CoctelDetail.class);

                intent.putExtra("idDrink", dataForThisCell.getId());
                intent.putExtra("strDrink", dataForThisCell.getNombreCoctel());
                intent.putExtra("strCategory", dataForThisCell.getCategoria());
                intent.putExtra("strDrinkThumb", dataForThisCell.getImageUrl());
                intent.putExtra("strGlass", dataForThisCell.getVaso());
                intent.putExtra("strAlcoholic", dataForThisCell.getAlcohol());
                intent.putExtra("strInstructions", dataForThisCell.getInstrucciones());

                context.startActivity(intent);
            }
            });
            }



    @Override
    public int getItemCount() {
        return cocteles.size();
    }


}
