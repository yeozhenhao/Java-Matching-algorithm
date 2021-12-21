package org.delicacies.matching;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class Arrange {
	public static boolean is_gender_pref_respected (Player player_being_checked, Player other_player) {
		boolean gender_pref_is_respected;
		
		if (player_being_checked.getGenderpref() == "no preference") {
			gender_pref_is_respected = true;
		} else {
			gender_pref_is_respected = Objects.equals(player_being_checked.getGenderpref(), other_player.getGender());
		}
		return gender_pref_is_respected;
	}
	
	public static boolean are_gender_prefs_respected (Player player_being_checked, Player other_player) {
		boolean gender_prefs_are_respected;
		
		gender_prefs_are_respected = is_gender_pref_respected(player_being_checked, other_player)&&
				is_gender_pref_respected(other_player, player_being_checked);
		return gender_prefs_are_respected;
	}
	
	public static boolean is_there_edge_between_players (Player angel_player, Player mortal_player) {
		boolean final_check;
		System.out.println("Checking " + angel_player.getUsername() + " and " + mortal_player.getUsername());
		
		boolean there_is_edge_between_players;
		
		boolean gender_pref_check;
		gender_pref_check = are_gender_prefs_respected(angel_player, mortal_player);
		System.out.println("Gender pref check: " + gender_pref_check);
		
		if (gender_pref_check == true) {
			System.out.println("Edge found btwn " + angel_player.getUsername() + " and " + mortal_player.getUsername());
			final_check = true;
			return final_check;
		}
		else {
			System.out.println("No edge between " + angel_player.getUsername() + " and " + mortal_player.getUsername());
			final_check = false;
			return final_check;
		}
	}
	
	public static ArrayList<Pair> get_player_edges_from_player_list (ArrayList<Player> player_list) {
		ArrayList<Pair> player_edges = new ArrayList<>();
		for (Player player : player_list) {
			for (Player other_player : player_list) {
				if (player.isEqual(other_player) == false) {
					if (is_there_edge_between_players(player, other_player) == true) {
						Pair<Player,Player> pair_edges = new Pair<> (player, other_player);
						player_edges.add(pair_edges);
						} 
					}
				}
			}
		return player_edges;
		}
	
	public static List<Graph<Player, DefaultEdge>> remove_graphs_with_no_hamilton_cycle (List<Graph<Player, DefaultEdge>> stronglyConnectedSubgraphs) {
		boolean there_is_definitely_no_hamilton_cycle = false;
		for (Graph<Player, DefaultEdge> g : stronglyConnectedSubgraphs) {
        	for (Player p : g.vertexSet()) {
        		if (Graphs.neighborListOf(g, p).size() <=1) {
        			there_is_definitely_no_hamilton_cycle = true;
        		}
        	}
			if (there_is_definitely_no_hamilton_cycle == true) {
				stronglyConnectedSubgraphs.remove(g);
			}
        }
		return stronglyConnectedSubgraphs;
	}
	
	public static void hamilton(Graph<Player, DefaultEdge> G) {
//		F = [(G,[list(G.nodes())[0]])]
		AllDirectedPaths<Player, DefaultEdge> paths = new AllDirectedPaths<Player, DefaultEdge>(G);
	    GraphPath<Player, DefaultEdge> longestPath = paths.getAllPaths(source, target, true, null)
	        .stream()
	        .sorted((GraphPath<String, DefaultEdge> path1, GraphPath<String, DefaultEdge> path2)-> new Integer(path2.getLength()).compareTo(path1.getLength()))
	        .findFirst().get();
	    System.out.println(longestPath.getLength() +  " " + longestPath);
	}
	
//	public static calcDepths(g) {    
//
//	    Map<Player, Integer> vertexToDepthMap = new HashMap<>();
//	    Iterator<Player> iterator = new TopologicalOrderIterator<Player, DefaultEdge>(g);
//	    iterator.forEachRemaining( 
//	    		v -> {
//	    			Set<Player> predecessors = Graphs.predecessorListOf(g, v).toSet();
//	    			int maxPredecessorDepth = -1;
//	    			predecessors.forEach(predecessor -> 
//	    				maxPredecessorDepth = Math.max(maxPredecessorDepth, vertexToDepthMap.get(predecessor))
//	    			);
//            vertexToDepthMap.put(v, maxPredecessorDepth + 1);
//            }
//	    		);
//	    return vertexToDepthMap;
//	}
}

class Pair<L, R> {

	  private final L left;
	  private final R right;

	  public Pair(L left, R right) {
	    assert left != null;
	    assert right != null;

	    this.left = left;
	    this.right = right;
	  }

	  public L getLeft() { return left; }
	  public R getRight() { return right; }

	  @Override
	  public int hashCode() { return left.hashCode() ^ right.hashCode(); }

	  @Override
	  public boolean equals(Object o) {
	    if (!(o instanceof Pair)) return false;
	    Pair pairo = (Pair) o;
	    return this.left.equals(pairo.getLeft()) &&
	           this.right.equals(pairo.getRight());
	  }

	}