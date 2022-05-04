package com.example.project_110;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


public class SearchListViewModel  extends  AndroidViewModel{


    private LiveData<List<VertexInfoStorable>> searchListItems;
    private final VertexInfoStorableDao vertexInfoStorableDao;
    public SearchListViewModel(@NonNull Application application){
        super(application);
        Context context = getApplication().getApplicationContext();
        VertexDatabase db = VertexDatabase.getSingleton(context);
        vertexInfoStorableDao = db.vertexInfoDao();

    }

    public  LiveData<List<VertexInfoStorable>> getSearchListItems(){
        if (searchListItems == null){
            loadUsers();

       }
        return searchListItems;
    }

    private void loadUsers(){

        searchListItems = vertexInfoStorableDao.getAllLive();

    }

    /*
    public void toggleCompleted(ZooData.VertexInfo todoListItem){
        todoListItem.completed = !todoListItem.completed;
        vertexInfoDao.update(todoListItem);
    }*/


    /*
    public void updateText(ZooData.VertexInfo todoListItem, String newText){
        todoListItem.text=newText;
        vertexInfoDao.update((todoListItem));
    }*/

//FIND A BETTER WAY TO NOT ADD DUPLICATES MAYBE
    public void selectExhibit(ZooData.VertexInfo selectedVertex){
        //int endOfListOrder = vertexInfoDao.getOrderForAppend();
        //ZooData.VertexInfo newItem = new ZooData.VertexInfo(text, false, endOfListOrder);

        String newID = selectedVertex.id;
        Boolean exists = false;
        for (VertexInfoStorable compareVertex : vertexInfoStorableDao.getAll()){
            if (compareVertex.id.equals(newID)) exists=true;
        }
        if(!exists){
            vertexInfoStorableDao.insert(new VertexInfoStorable(selectedVertex));
            Log.d("add message", "added: " +selectedVertex.name + " to List of Selected Exhibits." );
        }
        else
        {
            Log.d("add message", selectedVertex.name + " is already in the list." );
        }




    }
    /*
    public void deleteExhibit(ZooData.VertexInfo todoListItem){

        vertexInfoDao.delete(todoListItem);

    }*/
    
}
