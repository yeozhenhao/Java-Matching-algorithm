package org.delicacies.matching;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.tour.TwoApproxMetricTSP;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.opencsv.CSVWriter;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;

public class Arrange {
	public static boolean is_gender_pref_respected (Player player_being_checked, Player other_player) {
		boolean gender_pref_is_respected;
		
		if (Objects.equals("no preference", player_being_checked.getGenderpref())) {
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
	
	public static boolean is_age_pref_respected (Player player_being_checked, Player other_player) {
		boolean age_pref_is_respected;
		ValueRange range_player_being_checked = java.time.temporal.ValueRange.of(player_being_checked.getMinage(), player_being_checked.getMaxage());
//		System.out.println("ValueRange " + range_player_being_checked); //TESTT
//		System.out.println("Age to check " + other_player.getAge()); //TESTT
		if (range_player_being_checked.isValidValue(other_player.getAge())) {
			age_pref_is_respected = true;
		} else {
			age_pref_is_respected = false;
		}
//		System.out.println("Age Pref is respected: " + age_pref_is_respected); //TESTT
		return age_pref_is_respected;
	}
	
	public static boolean are_age_prefs_respected (Player player_being_checked, Player other_player) {
		boolean age_prefs_are_respected;
		
		age_prefs_are_respected = is_age_pref_respected(player_being_checked, other_player)&&
				is_age_pref_respected(other_player, player_being_checked);
//		System.out.println("Age Prefs ARE respected: " + age_prefs_are_respected); //TESTT
		return age_prefs_are_respected;
	}
	
	public static boolean is_religion_pref_respected (Player player_being_checked, Player other_player) {
		boolean religion_pref_is_respected;
		String religionqn_player_being_checked = player_being_checked.getReligionqn();
//		System.out.println("Religion Qn of " + player_being_checked.getUsername() + " -> " + other_player.getUsername() + ": "  + religionqn_player_being_checked); //TESTT
		if (Objects.equals("no", religionqn_player_being_checked)) {
			religion_pref_is_respected = true;
		} else {
			religion_pref_is_respected = Objects.equals(player_being_checked.getReligion(), other_player.getReligion());
		}
//		System.out.println("Is religion pref respected: "  + religion_pref_is_respected); //TESTT
		return religion_pref_is_respected;
		}
	
	public static boolean are_religion_prefs_respected (Player player_being_checked, Player other_player) {
		boolean religion_prefs_are_respected;
		
		religion_prefs_are_respected = is_religion_pref_respected(player_being_checked, other_player)&&
				is_religion_pref_respected(other_player, player_being_checked);
//		System.out.println("Religion Prefs ARE respected: " + religion_prefs_are_respected); //TESTT
		return religion_prefs_are_respected;
	}
	
	public static boolean is_there_edge_between_players (Player angel_player, Player mortal_player) {
		boolean final_check;
//		System.out.println("Checking " + angel_player.getUsername() + " and " + mortal_player.getUsername());
		
		boolean there_is_edge_between_players;
		
		boolean gender_pref_check;
		gender_pref_check = are_gender_prefs_respected(angel_player, mortal_player);
//		System.out.println("Gender pref check: " + gender_pref_check);
		
		boolean age_pref_check;
		age_pref_check = are_age_prefs_respected(angel_player, mortal_player);
//		System.out.println("Age pref check: " + age_pref_check);
		
		boolean religion_pref_check;
		religion_pref_check = are_religion_prefs_respected(angel_player, mortal_player);
//		System.out.println("Religion pref check: " + religion_pref_check);
		
		if (gender_pref_check && age_pref_check && religion_pref_check  == true) {
//			System.out.println("Edge found btwn " + angel_player.getUsername() + " and " + mortal_player.getUsername());
			final_check = true;
			return final_check;
		}
		else {
//			System.out.println("No edge between " + angel_player.getUsername() + " and " + mortal_player.getUsername());
			final_check = false;
			return final_check;
		}
	}
	
	public static List<Pair> get_player_edges_from_player_list (List<Player> player_list) {
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
//		boolean there_is_definitely_no_hamilton_cycle = false;
		List<Graph<Player, DefaultEdge>> final_accepted_graphs_list = new ArrayList<>();
		List<Graph<Player, DefaultEdge>> graphsToRemove = new ArrayList<>();
		for (Graph<Player, DefaultEdge> g : stronglyConnectedSubgraphs) {
        	for (Player p : g.vertexSet()) {
        		if (Graphs.neighborListOf(g, p).size() <=1) {
//        			there_is_definitely_no_hamilton_cycle = true;
        			graphsToRemove.add(g);
        		}
        	}
        }
		final_accepted_graphs_list = return_accepted_graphs_list(stronglyConnectedSubgraphs, graphsToRemove);
		return final_accepted_graphs_list;
	}
	
	public static Map<Integer, GraphPath<Player, DefaultEdge>> find_greatest_path_of_all_vertexes(Graph<Player, DefaultEdge> G, Integer maxlimit) throws NoSuchElementException { //NOT USED ANYMORE - Brute force method does not work for graphs with >10 vertices
		AllDirectedPaths<Player, DefaultEdge> paths = new AllDirectedPaths<Player, DefaultEdge>(G);
		Map<Integer, GraphPath<Player, DefaultEdge>> longestpathMap = new HashMap<>();
		for (Player player : G.vertexSet()) {
			for (Player other_player : G.vertexSet()) {
				if (player.isEqual(other_player) == false) {
					if (maxlimit == 0) { //if maxlimit = 0 the algorithm will find the greatest length
						GraphPath<Player, DefaultEdge> longestPath = paths.getAllPaths(player, other_player, true, null)
						        .stream()
						        .sorted((GraphPath<Player, DefaultEdge> path1, GraphPath<Player, DefaultEdge> path2)-> Integer.valueOf(path2.getLength()).compareTo(path1.getLength()))
						        .findFirst().get();
					    //System.out.println(longestPath.getLength() + 1 +  " " + longestPath); Not used printing longest Path can be too long
					    System.out.println("Longest path from " + player.getUsername() + " to " + other_player.getUsername() + ": " + (longestPath.getLength() + 1));
					    longestpathMap.put(longestPath.getLength() + 1, longestPath);
					} else {
						GraphPath<Player, DefaultEdge> longestPath = paths.getAllPaths(player, other_player, true, maxlimit)
						        .stream()
						        .sorted((GraphPath<Player, DefaultEdge> path1, GraphPath<Player, DefaultEdge> path2)-> Integer.valueOf(path2.getLength()).compareTo(path1.getLength()))
						        .findFirst().get();
					    //System.out.println(longestPath.getLength() + 1 +  " " + longestPath); Not used printing longest Path can be too long
						System.out.println("Longest path from " + player.getUsername() + " to " + other_player.getUsername() + ": " + (longestPath.getLength() + 1));
					    longestpathMap.put(longestPath.getLength() + 1, longestPath);
					}
					
				}
			}
		}
		return longestpathMap;
	}
	
	public static List<Player> return_rejected_player_list(List<Player> player_list, List<Player> accepted_player_list) {
        List<Player> rejected_player_list = player_list.stream()
        		.filter(e -> !accepted_player_list.contains(e))
        		.collect(Collectors.toList());
        System.out.println("Rejected players list: " + rejected_player_list);
        return rejected_player_list;
	}
	public static List<Graph<Player, DefaultEdge>> return_accepted_graphs_list(List<Graph<Player, DefaultEdge>> list_to_keep, List<Graph<Player, DefaultEdge>> list_to_remove) {
		List<Graph<Player, DefaultEdge>> accepted_graph_list = new ArrayList<>();
		accepted_graph_list = list_to_keep.stream()
        		.filter(e -> !list_to_remove.contains(e))
        		.collect(Collectors.toList());
        System.out.println("\nAccepted graphs list: " + accepted_graph_list + "\n");
        return accepted_graph_list;
	}
	
	public static List<Player> dfsWithoutRecursion (Graph<Player, DefaultEdge> G, int length_of_paths_considered_complete, int stack_path_clearing_divisor_number) {
		boolean path_list_found = false;
		
		int max_possible_path_length = G.vertexSet().size();
		Random rand = new Random();
		int random_start_node_number = rand.nextInt(G.vertexSet().size() - 1);
		
		Stack<Graph<Player,DefaultEdge>> stack_graph = new Stack<>();
	    Stack<List<Player>> stack_path = new Stack<>();
	    List<Player> graph_path = new ArrayList<>();
	    List<Player> start = new ArrayList<>();
	    List<Player> listOfNodes = List.copyOf(G.vertexSet());
	    start.add(listOfNodes.get(random_start_node_number)); //Choose a randomly generated start node
//	    System.out.println("\nList of Nodes: " + listOfNodes);
//	    System.out.println("Start Node: " + start + "\n");
	    
	    stack_graph.push(G);
	    stack_path.push(start);
	    
	    
	    while (!stack_path.isEmpty()) {
//	    	System.out.println("\nRESET ==================================");
	    	Stack<List<Player>> stack_path_reset = new Stack<>();
	    	Stack<Graph<Player,DefaultEdge>> stack_graph_reset = new Stack<>();
	    	
	        Graph<Player, DefaultEdge> current_graph = new DefaultDirectedGraph<Player, DefaultEdge>(DefaultEdge.class);
	        Graphs.addGraph(current_graph, stack_graph.pop());
	        List<Player> current_path = List.copyOf(stack_path.pop()); // Makes a copy, forming an immutable List
	        Player last_player_in_path =  (Player) current_path.get(current_path.size()-1);
	        List<Player> listOfNeighbours_removed_duplicates = clearListFromDuplicateFirstName(Graphs.neighborListOf(current_graph, last_player_in_path));
//		    System.out.println("\ncurrent_path " + current_path);
//		    System.out.println("\ncurrent_graph " + current_graph);
//		    System.out.println("Neighbours list of last node in path (" + last_player_in_path + ") in G: " + listOfNeighbours_removed_duplicates + "\n");
	        for (Player neighbour : listOfNeighbours_removed_duplicates) {
//	        	System.out.println("\nNEW ROUND ==================================");
	        	List<Player> conf_p = new ArrayList<>();
	        	conf_p.addAll(current_path); //Make a copy of the immutable List into a mutable List
//	        	System.out.println("\ncurrent path " + current_path);
//	        	System.out.println("\ncurrent graph " + current_graph);
	        	conf_p.add(neighbour);
//	        	System.out.println("\nAdded " + neighbour + " to conf_p, now conf_p is " + conf_p);

	        	Graph<Player, DefaultEdge> conf_g = new DefaultDirectedGraph<Player, DefaultEdge>(DefaultEdge.class);
	        	Graphs.addGraph(conf_g, current_graph);
	        	conf_g.removeVertex(last_player_in_path); //remove the node that we just used to find neighbours for
//	        	System.out.println("\nRemoving " + last_player_in_path + " from conf_g");
	        	stack_path_reset.push(conf_p);
	        	stack_graph_reset.push(conf_g);
	        }
	        for (List<Player> p_list : stack_path_reset) {
//	        	System.out.println("\nList length: " + p_list.size());
	        	if (p_list.size() == length_of_paths_considered_complete) {
	        		path_list_found = true;
	        		return p_list;
	        	} else {
	        		int path_length = p_list.size();
	        		System.out.println("\nPath length (progress): " + path_length + " / " + max_possible_path_length);
	        	}
//        	System.out.println("\nTarget: " + length_of_paths_considered_complete);
	        if (path_list_found == false) {
	        	stack_path.addAll(stack_path_reset);
	        	stack_graph.addAll(stack_graph_reset);
	        	}
	        for (int i = 0; i < Math.floor(stack_path.size()/stack_path_clearing_divisor_number); i++) {
	        	stack_path.remove(i);
	        	stack_graph.remove(i);
	        }
	        System.out.println("\nDeleted first couple elements to save space, stack_path size is now: " + stack_path.size());
	        }
	    }
		return null;
	}

	private static List<Player> clearListFromDuplicateFirstName(List<Player> list1) {

	     Map<String, Player> cleanMap = new LinkedHashMap<String, Player>();
	     for (int i = 0; i < list1.size(); i++) {
	         cleanMap.put(list1.get(i).getUsername(), list1.get(i));
	     }
	     List<Player> list = new ArrayList<Player>(cleanMap.values());
	     return list;
	}
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