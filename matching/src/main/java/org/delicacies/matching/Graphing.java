package org.delicacies.matching;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.jgrapht.alg.interfaces.*;

import org.jgrapht.*;
import org.jgrapht.alg.connectivity.*;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.*;

public class Graphing extends Arrange {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Integer maxlimit = Integer.valueOf(1); //Set max algorithm path limit (in case of high player numbers) 
		
		Player player1 = new Player("yeozhenhao","Elgene","Female","Male");
		Player player2 = new Player("matt","Matthew","Female","Male");
		Player player3 = new Player("kiahui","Kia","Male","Male");
		Player player4 = new Player("ade","Ade","Male","Female");
		
		ArrayList<Player> player_list = new ArrayList<>();
		player_list.add(player1);
		player_list.add(player2);
		player_list.add(player3);
		player_list.add(player4);
		
		Graph<Player, DefaultEdge> directedGraph =
	            new DefaultDirectedGraph<Player, DefaultEdge>(DefaultEdge.class);
		for (Player p : player_list) {
			directedGraph.addVertex(p);
		}
		for (Player v : directedGraph.vertexSet()) {
			System.out.println("vertex: " + v.getUsername());
		}
		
//		boolean edge_check;
//		edge_check = is_there_edge_between_players(player1, player2);
		
		ArrayList<Pair> arrayOfPair = new ArrayList<>();
		arrayOfPair = get_player_edges_from_player_list(player_list);
		System.out.println("\n\nList of player edges:");
		for (Pair<Player, Player> p : arrayOfPair) {
		      System.out.println("(" + p.getLeft().getUsername() + ", " + p.getRight().getUsername() + ")");
		      directedGraph.addEdge(p.getLeft(), p.getRight());
		    }
		
		// computes all the strongly connected components of the directed graph
		StrongConnectivityAlgorithm<Player, DefaultEdge> scAlg =
	            new KosarajuStrongConnectivityInspector<>(directedGraph);
		List<Graph<Player, DefaultEdge>> stronglyConnectedSubgraphs =
	            scAlg.getStronglyConnectedComponents();
		
		 // prints the strongly connected components
        System.out.println("Strongly connected components:");
        for (int i = 0; i < stronglyConnectedSubgraphs.size(); i++) {
            System.out.println(stronglyConnectedSubgraphs.get(i));
        }
        
        List<Graph<Player, DefaultEdge>> new_stronglyConnectedSubgraphs = new
        		ArrayList<>();
        remove_graphs_with_no_hamilton_cycle (stronglyConnectedSubgraphs);
       
        
        System.out.println("Strongly connected components:");
        for (int i = 0; i < stronglyConnectedSubgraphs.size(); i++) {
            System.out.println(stronglyConnectedSubgraphs.get(i));
        }
        
        Map<Integer, GraphPath<Player, DefaultEdge>> longestpathMap = new HashMap<Integer, GraphPath<Player, DefaultEdge>>();
        try {
        	longestpathMap = find_greatest_path_of_all_vertexes(stronglyConnectedSubgraphs.get(0), maxlimit);
        }	catch (NoSuchElementException NSEe) {
        	NSEe.printStackTrace();
        	maxlimit++;
        	longestpathMap = find_greatest_path_of_all_vertexes(stronglyConnectedSubgraphs.get(0), maxlimit);
        }
        int maxValueInMap = Collections.max(longestpathMap.keySet());
        System.out.println("Max Value In Map: " + longestpathMap + "\n");
        
        System.out.println("Max Value In Map: " + maxValueInMap);
        System.out.println("Longest Path: " + longestpathMap.get(maxValueInMap));
        
//        GraphPath<Player, DefaultEdge> longestPath = new GraphPath<>();
//        longestPath = longestpathMap.get(maxValueInMap);
//        longestPath.getVertexList();
//        System.out.println(longestPath.getVertexList());
//        
        

//        AllDirectedPaths<Player, DefaultEdge> paths = new AllDirectedPaths<Player, DefaultEdge>(new_stronglyConnectedSubgraphs.get(0));
//	    GraphPath<Player, DefaultEdge> longestPath = paths.getAllPaths(player1, player2, true, null)
//	        .stream()
//	        .sorted((GraphPath<Player, DefaultEdge> path1, GraphPath<Player, DefaultEdge> path2)-> new Integer(path2.getLength()).compareTo(path1.getLength()))
//	        .findFirst().get();
//	    System.out.println(longestPath.getLength() + 1 +  " " + longestPath);
        
//        Graph<Player, DefaultEdge> directedSubgraph =
//	            new AsSubgraph<Player, DefaultEdge>(stronglyConnectedSubgraphs.get(0));
//        System.out.println("Incoming edges: " + directedSubgraph.edgeSet());
//        System.out.println("Incoming edges: " + directedSubgraph.incomingEdgesOf(player1));
//        System.out.println("Outgoing edges: " + directedSubgraph.outgoingEdgesOf(player1));
//        String subgraph_string = stronglyConnectedSubgraphs.get(0)
//        System.out.println("TEST " + stronglyConnectedSubgraphs.toString().replaceAll("[()]", "_"));
        
	}

}
