package com.example.project_110;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
@Entity(tableName = "selected_list_items")
public class VertexInfoStorable {

    @PrimaryKey(autoGenerate = true)
    public long id1 =0;

    public static enum Kind {
        // The SerializedName annotation tells GSON how to convert
        // from the strings in our JSON to this Enum.
        @SerializedName("gate") GATE,
        @SerializedName("exhibit") EXHIBIT,
        @SerializedName("intersection") INTERSECTION
    }

    public VertexInfoStorable(){

    }

    public String id;
    public ZooData.VertexInfo.Kind kind;
    public String name;
    public String tags;


    public VertexInfoStorable(ZooData.VertexInfo vertexInfo) {
        this.id = vertexInfo.id;
        this.kind = vertexInfo.kind;
        this.name = vertexInfo.name;
        tags = vertexInfo.tags.toString();
    }

    public ZooData.VertexInfo unPack(){

        return new ZooData.VertexInfo(id, kind, name, new ArrayList());

    }

}
