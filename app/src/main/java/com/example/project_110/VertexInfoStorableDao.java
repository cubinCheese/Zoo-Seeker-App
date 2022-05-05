package com.example.project_110;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface VertexInfoStorableDao {
    @Insert
    long insert(VertexInfoStorable vertexInfo);

    @Query("SELECT * FROM 'selected_list_items' WHERE 'id'=:id")
    VertexInfoStorable get(long id);

    @Query("SELECT * FROM 'selected_list_items' ORDER BY 'order'")
    List<VertexInfoStorable> getAll();

    @Query("SELECT * FROM 'selected_list_items' ORDER BY  'order'")
    LiveData<List<VertexInfoStorable>> getAllLive();

    @Query("SELECT 'order'+ 1 FROM 'selected_list_items' ORDER BY 'order' DESC LIMIT 1")
    int getOrderForAppend();

    @Update
    int update(VertexInfoStorable vertexInfo);

    @Delete
    int delete(VertexInfoStorable vertexInfo);

    @Insert
    List<Long> insertAll(List<VertexInfoStorable> vertexInfo);
}
