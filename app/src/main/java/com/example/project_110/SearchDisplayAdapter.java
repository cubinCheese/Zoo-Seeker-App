package com.example.project_110;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class SearchDisplayAdapter extends RecyclerView.Adapter<SearchDisplayAdapter.ViewHolder> {

    private List<ZooData.VertexInfo> searchExhibits = Collections.emptyList();


    public void setSearchListItems(List<ZooData.VertexInfo> newSearchExhibits){
        this.searchExhibits.clear();
        this.searchExhibits=newSearchExhibits;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.search_display_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setSearchItem(searchExhibits.get(position));
    }
    //@Override
    //public long getItemId(int position){return searchExhibits.get(position).id;}

    @Override
    public int getItemCount() {
        return searchExhibits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;
        private ZooData.VertexInfo searchItem;
       //private final CheckBox checkBox;

        public ViewHolder(@NonNull View itemView){

            super(itemView);
            this.textView =  itemView.findViewById(R.id.search_list_item);

        }

        public void setSearchItem(ZooData.VertexInfo searchItem){
            this.searchItem=searchItem;
            this.textView.setText(searchItem.name);


        }


    }


}
