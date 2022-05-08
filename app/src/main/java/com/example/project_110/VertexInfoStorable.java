package com.example.project_110;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
@Entity(tableName = "selected_list_items")
public class VertexInfoStorable implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public long id1 =0;

    protected VertexInfoStorable(Parcel in) {
        id1 = in.readLong();
        id = in.readString();
        name = in.readString();
        tags = in.readString();
    }

    public static final Creator<VertexInfoStorable> CREATOR = new Creator<VertexInfoStorable>() {
        @Override
        public VertexInfoStorable createFromParcel(Parcel in) {
            return new VertexInfoStorable(in);
        }

        @Override
        public VertexInfoStorable[] newArray(int size) {
            return new VertexInfoStorable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id1);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(tags);
    }

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
