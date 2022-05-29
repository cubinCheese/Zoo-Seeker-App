package com.example.project_110;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.nio.json.JSONImporter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ZooData {
    @Entity(tableName = "selected_list_items")
    public static class VertexInfo {
        //This is angry, figure out how to make keys if we need them
      // @PrimaryKey(autoGenerate = true)


        public static enum Kind {
            // The SerializedName annotation tells GSON how to convert
            // from the strings in our JSON to this Enum.
            @SerializedName("gate") GATE,
            @SerializedName("exhibit") EXHIBIT,
            @SerializedName("intersection") INTERSECTION,
            @SerializedName("exhibit_group") EXHIBIT_GROUP
        }

        @SerializedName("id")
        @NonNull
        public String id;

        @SerializedName("kind")
        @NonNull
        public Kind kind;

        @SerializedName("name")
        @NonNull
        public String name;

        @SerializedName("tags")
        @NonNull
        public List<String> tags;

        @SerializedName("parent_id")
        @Nullable
        public String parent_id;

        @SerializedName("lat")
        @Nullable
        public Double lat;

        @SerializedName("lng")
        @Nullable
        public Double lng;

        public boolean isExhibit() {
            return kind.equals(Kind.EXHIBIT);
        }

        public boolean isIntersection() {
            return kind.equals(Kind.INTERSECTION);
        }

        public boolean isGroup() {
            return kind.equals(Kind.EXHIBIT_GROUP);
        }

        public boolean hasGroup() {
            return parent_id != null;
        }


        public VertexInfo(@NonNull String id,
                          @Nullable String parent_id,
                          @NonNull Kind kind,
                          @NonNull String name,
                          @NonNull List<String> tags,
                          @Nullable Double lat,
                          @Nullable Double lng) {
            this.id = id;
            this.kind = kind;
            this.name = name;
            this.tags = tags;
            this.parent_id = parent_id;
            this.lat = lat;
            this.lng = lng;

            if (!this.hasGroup() && (lat == null || lng == null)) {
                throw new RuntimeException("Nodes must have a lat/long unless they are grouped.");
            }
        }


    }

    public static class EdgeInfo {
        public String id;
        public String street;
    }

    public static Map<String, VertexInfo> loadVertexInfoJSON(Context context, String path) {

        try {
            InputStream inputStream = context.getAssets().open(path);
            Reader reader = new InputStreamReader(inputStream);
            Gson gson = new Gson();
            Type type = new TypeToken<List<VertexInfo>>(){}.getType();
            List<ZooData.VertexInfo> zooData = gson.fromJson(reader, type);
            Map<String, ZooData.VertexInfo> indexedZooData = zooData
                    .stream()
                    .collect(Collectors.toMap(v -> v.id, datum -> datum));

            return indexedZooData;
        } catch (IOException e) {
            e.printStackTrace();
            return (Map<String, VertexInfo>) Collections.emptyList();
        }


    }

    public static Map<String, ZooData.EdgeInfo> loadEdgeInfoJSON(Context context, String path) {
        try{
            InputStream inputStream = context.getAssets().open(path);
            Reader reader = new InputStreamReader(inputStream);

            Gson gson = new Gson();
            Type type = new TypeToken<List<ZooData.EdgeInfo>>(){}.getType();
            List<ZooData.EdgeInfo> zooData = gson.fromJson(reader, type);

            Map<String, ZooData.EdgeInfo> indexedZooData = zooData
                    .stream()
                    .collect(Collectors.toMap(v -> v.id, datum -> datum));
            return indexedZooData;
        } catch (IOException e) {
            e.printStackTrace();
            return (Map<String, ZooData.EdgeInfo>) Collections.emptyList();
        }

    }

    public static Graph<String, IdentifiedWeightedEdge> loadZooGraphJSON(Context context, String path) {
        // Create an empty graph to populate.
        Graph<String, IdentifiedWeightedEdge> g = new DefaultUndirectedWeightedGraph<>(IdentifiedWeightedEdge.class);

        // Create an importer that can be used to populate our empty graph.
        JSONImporter<String, IdentifiedWeightedEdge> importer = new JSONImporter<>();

        // We don't need to convert the vertices in the graph, so we return them as is.
        importer.setVertexFactory(v -> v);

        // We need to make sure we set the IDs on our edges from the 'id' attribute.
        // While this is automatic for vertices, it isn't for edges. We keep the
        // definition of this in the IdentifiedWeightedEdge class for convenience.
        importer.addEdgeAttributeConsumer(IdentifiedWeightedEdge::attributeConsumer);

        // On Android, you would use context.getAssets().open(path) here like in Lab 5.
        try{
            InputStream inputStream = context.getAssets().open(path);
            Reader reader = new InputStreamReader(inputStream);
            // And now we just import it!
            importer.importGraph(g, reader);
            return g;
        } catch (IOException e) {
            e.printStackTrace();
            return new DefaultUndirectedWeightedGraph<>(IdentifiedWeightedEdge.class);
        }
    }
}
