package com.example.project_110;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;



public class SelectedDisplayAdapter extends RecyclerView.Adapter<SelectedDisplayAdapter.ViewHolder>{
    private List<VertexInfoStorable> selectedExhibits = Collections.emptyList();


    public void setSelectedExhibits(List<VertexInfoStorable> newSelectedExhibits){
        this.selectedExhibits.clear();
        this.selectedExhibits=newSelectedExhibits;
        notifyDataSetChanged();


    }

    @Override
    public int getItemCount() {
        return selectedExhibits.size();
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setSelectedListItem(selectedExhibits.get(position));
        //Log.d("bindmessage","position:  "+  position);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.selected_display_item, parent, false);
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView selectedListItem;

        public ViewHolder(@NonNull View itemView){

            super(itemView);
            this.selectedListItem =  itemView.findViewById(R.id.selected_list_item);






        }


        public void setSelectedListItem(VertexInfoStorable selectedItem){
            this.selectedListItem.setText(selectedItem.name);
            //Log.d("setTextMessage", "display text = " + selectedItem.name);
        }


    }

}
