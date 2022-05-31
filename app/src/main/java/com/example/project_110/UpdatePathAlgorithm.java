package com.example.project_110;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class UpdatePathAlgorithm {
    public static List<VertexInfoStorable> shortestPath(Graph<String, IdentifiedWeightedEdge> g, List<VertexInfoStorable> selected,String startVertex) {
        Set<String> unvisitedSelectedExhibits = new HashSet();
        for (VertexInfoStorable v : selected)
            //v.getParent().id returns string id of parent of v, or itself
            unvisitedSelectedExhibits.add(v.getParent().id);
        String start = startVertex;
        String exit = "entrance_exit_gate";
        unvisitedSelectedExhibits.remove(start);
        unvisitedSelectedExhibits.remove(exit);
        List<String> shortestExhibitOrder = new ArrayList<>();
        shortestExhibitOrder.add(start);

        while (!unvisitedSelectedExhibits.isEmpty()) {
            // Dijkstra's to find nearest neighbor from start
            HashMap<String, Double> distances = new HashMap<>();
            for (String s : g.vertexSet())
                distances.put(s, Double.MAX_VALUE);
            distances.replace(start, 0.0);

            Set<String> unvisited = new HashSet<>(g.vertexSet());

            PriorityQueue<String> pq = new PriorityQueue<>((a, b) ->
                    (int) (distances.get(a) - distances.get(b))
            );
            pq.add(start);

            while (!pq.isEmpty()) {

                String currVertex = pq.poll();

                if (unvisitedSelectedExhibits.contains(currVertex)) {
                    unvisitedSelectedExhibits.remove(currVertex);
                    shortestExhibitOrder.add(currVertex);
                    start = currVertex;
                    break;
                };

                if (unvisited.contains(currVertex)) {
                    unvisited.remove(currVertex);
                    for (IdentifiedWeightedEdge e : g.edgesOf(currVertex)) {
                        double newDist = distances.get(currVertex) + g.getEdgeWeight(e);
                        if (currVertex == g.getEdgeTarget(e)) {
                            if (newDist < distances.get(g.getEdgeSource(e))) {
                                distances.replace(g.getEdgeSource(e), newDist);
                                pq.add(g.getEdgeSource(e));
                            }
                        } else {
                            if (newDist < distances.get(g.getEdgeTarget(e))) {
                                distances.replace(g.getEdgeTarget(e), newDist);
                                pq.add(g.getEdgeTarget(e));
                            }
                        }


                    }
                }
            }
        }
        shortestExhibitOrder.add(exit); // End at exit

        HashMap<String, VertexInfoStorable> idToVertex = new HashMap<>();
        HashMap<String, List<VertexInfoStorable>> parentidToVertex = new HashMap<>();
        for (VertexInfoStorable v : selected) {
            if (v.hasParent()) {
                String parent = v.getParent().id;
                if (!parentidToVertex.containsKey(parent)) {
                    ArrayList<VertexInfoStorable> arr = new ArrayList<>();
                    parentidToVertex.put(parent, arr);
                }
                parentidToVertex.get(parent).add(v);
            }
            //gets v id or parent id if available, and maps back to original v object
            idToVertex.put(v.id, v);
        }
        List<VertexInfoStorable> shortestVertexOrder = new ArrayList<>();
        for (String id : shortestExhibitOrder) {
            if (parentidToVertex.containsKey(id)) {
                for (VertexInfoStorable v : parentidToVertex.get(id))
                    shortestVertexOrder.add(v);
            } else {
                shortestVertexOrder.add(idToVertex.get(id));
            }
        }

        return shortestVertexOrder;
    }
}