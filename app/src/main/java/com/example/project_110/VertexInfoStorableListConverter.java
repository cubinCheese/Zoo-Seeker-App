package com.example.project_110;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class VertexInfoStorableListConverter {
    @TypeConverter
    public static List<VertexInfoStorable> storedStringToMyVertexInfoStorableList(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<VertexInfoStorable>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String VertexInfoStorableListToStoredString(List<VertexInfoStorable> myObjects) {
        Gson gson = new Gson();
        return gson.toJson(myObjects);
    }
}
