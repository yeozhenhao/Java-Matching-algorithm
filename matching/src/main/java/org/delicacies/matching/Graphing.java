package org.delicacies.matching;

import java.util.List;
import java.util.ArrayList;
import org.jgrapht.alg.interfaces.*;

import org.jgrapht.*;
import org.jgrapht.alg.connectivity.*;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.*;
import org.jgrapht.graph.AsSubgraph;

public class Graphing extends Arrange {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Player player1 = new Player("yeozhenhao","Elgene","Female","Male");
		Player player2 = new Player("matt","Matthew","Female","Male");
		Player player3 = new Player("kiahui","Kia","Male","Male");
		Player player4 = new Player("ade","Ade","Male","Female");
		
		Graph<Player, DefaultEdge> directedGraph =
	            new DefaultDirectedGraph<Player, DefaultEdge>(DefaultEdge.class);
		directedGraph.addVertex(player1);
		directedGraph.addVertex(player2);
		directedGraph.addVertex(player3);
		directedGraph.addVertex(player4);
		for (Player v : directedGraph.vertexSet()) {
			System.out.println("vertex: " + v.getUsername());
		}
		
//		boolean edge_check;
//		edge_check = is_there_edge_between_players(player1, player2);
		
		ArrayList<Pair> arrayOfPair = new ArrayList<>();
		ArrayList<Player> player_list = new ArrayList<>();
		player_list.add(player1);
		player_list.add(player2);
		player_list.add(player3);
		player_list.add(player4);
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
        new_stronglyConnectedSubgraphs = remove_graphs_with_no_hamilton_cycle (stronglyConnectedSubgraphs);
       
        
        System.out.println("Strongly connected components:");
        for (int i = 0; i < stronglyConnectedSubgraphs.size(); i++) {
            System.out.println(stronglyConnectedSubgraphs.get(i));
        }

//        hamilton(new_stronglyConnectedSubgraphs);
        
//        Graph<Player, DefaultEdge> directedSubgraph =
//	            new AsSubgraph<Player, DefaultEdge>(stronglyConnectedSubgraphs.get(0));
//        System.out.println("Incoming edges: " + directedSubgraph.edgeSet());
//        System.out.println("Incoming edges: " + directedSubgraph.incomingEdgesOf(player1));
//        System.out.println("Outgoing edges: " + directedSubgraph.outgoingEdgesOf(player1));
//        String subgraph_string = stronglyConnectedSubgraphs.get(0)
//        System.out.println("TEST " + stronglyConnectedSubgraphs.toString().replaceAll("[()]", "_"));
        
	}

}
