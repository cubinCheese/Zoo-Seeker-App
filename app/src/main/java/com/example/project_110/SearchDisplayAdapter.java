package com.example.project_110;

import android.graphics.Color;
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

public class SearchDisplayAdapter extends RecyclerView.Adapter<SearchDisplayAdapter.ViewHolder> {

    private List<ZooData.VertexInfo> searchExhibits = Collections.emptyList();
    private Consumer<ZooData.VertexInfo> onSearchListItemClicked;


    public void setSearchListItems(List<VertexInfoStorable> newSearchExhibits){
        this.searchExhibits.clear();


        List<ZooData.VertexInfo> unPackedDataList = new ArrayList();


        for (VertexInfoStorable storedInfo : newSearchExhibits){
            unPackedDataList.add(storedInfo.unPack());
        }

        this.searchExhibits=unPackedDataList;
        notifyDataSetChanged();
    }


    public void setOnSearchListItemClickedHandlder(Consumer<ZooData.VertexInfo> onSearchListItemClicked){
        this.onSearchListItemClicked = onSearchListItemClicked;
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
        private final TextView searchListItem;
        private ZooData.VertexInfo searchItem;
       // private TextView selectedExhibitCount;
        //private TextView searchListItem;
       //private final CheckBox checkBox;

        public ViewHolder(@NonNull View itemView){

            super(itemView);
            this.searchListItem =  itemView.findViewById(R.id.search_list_item);
            //this.selectedExhibitCount = itemView.findViewById(R.id.selected_exhibit_count);
            
            this.searchListItem.setOnClickListener(view -> {
                if(onSearchListItemClicked == null) return;
                searchListItem.setBackgroundColor(Color.YELLOW);
                searchListItem.setAllCaps(true);
                onSearchListItemClicked.accept(searchItem);

            });



            

        }

        public void setSearchItem(ZooData.VertexInfo searchItem){
            this.searchItem=searchItem;
            this.searchListItem.setText(searchItem.name);


        }


    }


}
