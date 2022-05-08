package com.example.project_110;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class PathAlgorithm {
    public static List<VertexInfoStorable> shortestPath(VertexInfoStorableDao dao, Graph<String, IdentifiedWeightedEdge> g, List<VertexInfoStorable> selected) {
        Set<String> unvisitedSelectedExhibits = new HashSet(selected);
        String start = "entrance_exit_gate";
        unvisitedSelectedExhibits.remove(start);
        List<VertexInfoStorable> shortestExhibitOrder = new ArrayList<>();
        shortestExhibitOrder.add(dao.get(start));

        while (!unvisitedSelectedExhibits.isEmpty()) {
            //dijkstras to find nearest neighbor from start
            HashMap<String, Double> distances = new HashMap<>();
            for (String s : g.vertexSet())
                distances.put(s, Double.MAX_VALUE);
            distances.replace(start, 0.0);

            Set<String> unvisited = new HashSet<>(g.vertexSet());

            Set<IdentifiedWeightedEdge> edgesFromVertex = g.edgesOf(start);
            PriorityQueue<String> pq = new PriorityQueue<>((a, b) ->
                (int) (distances.get(a) - distances.get(b))
            );
            pq.add(start);

            while (!pq.isEmpty()) {
                String currVertex = pq.poll();

                if (unvisitedSelectedExhibits.contains(currVertex)) {
                    unvisitedSelectedExhibits.remove(currVertex);
                    shortestExhibitOrder.add(dao.get(currVertex));
                    start = currVertex;
                    break;
                };

                if (unvisited.contains(currVertex)) {
                    unvisited.remove(currVertex);
                    for (IdentifiedWeightedEdge e : g.edgesOf(currVertex)) {
                        double newDist = distances.get(currVertex) + g.getEdgeWeight(e);
                        if (newDist < distances.get(g.getEdgeTarget(e))) {
                            distances.replace(g.getEdgeTarget(e), newDist);
                            pq.add(g.getEdgeTarget(e));
                        }
                    }
                }
            }
        }

        return shortestExhibitOrder;
    }
}


