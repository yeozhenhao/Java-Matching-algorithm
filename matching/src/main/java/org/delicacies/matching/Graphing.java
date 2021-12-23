package org.delicacies.matching;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.jgrapht.alg.interfaces.*;
import org.apache.commons.collections.comparators.FixedOrderComparator;
import org.jgrapht.*;
import org.jgrapht.alg.connectivity.*;
import org.jgrapht.alg.cycle.TarjanSimpleCycles;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.alg.tour.TwoApproxMetricTSP;
import org.jgrapht.graph.*;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;

public class Graphing extends Arrange {

	public static void main(String[] args) throws FileNotFoundException, IOException, CsvValidationException {
		// TODO Auto-generated method stub
		Integer maxlimit = Integer.valueOf(0); //Set max algorithm path limit (in case of high player numbers)
		Integer length_of_paths_considered_complete = 11;
		
		String csv_path = "playerlist.csv";
		String csv_output = "Accepted Players List.csv";

		//Create mapping before reading CSV
		Map<String, String> columnMapping = new HashMap<String, String>();
	    columnMapping.put("Telegram Username", "username");
	    columnMapping.put("Name", "name");
	    columnMapping.put("Genderpref", "genderpref");
	    columnMapping.put("Gender", "gender");
	    columnMapping.put("Age", "age");
	    columnMapping.put("Maxage", "maxage");
	    columnMapping.put("Minage", "minage");
	    columnMapping.put("Interests", "interests");
	    columnMapping.put("Twotruthsonelie", "twotruthsonelie");
	    columnMapping.put("Intro", "introduction");
	    columnMapping.put("Religion", "religion");
	    columnMapping.put("Religionqn", "religionqn");
		HeaderColumnNameTranslateMappingStrategy mappingStrategy = new HeaderColumnNameTranslateMappingStrategy();
		mappingStrategy.setType(Player.class);
		mappingStrategy.setColumnMapping(columnMapping);
		
		
		
		//Recommended to use Bean Builder now
//		Logger logger = Logger.getLogger(Graphing.class.getName());
		CsvToBeanBuilder<Player> beanBuilder = new CsvToBeanBuilder(new FileReader("playerlist.csv"));
		beanBuilder.withType(Player.class);
		beanBuilder.withMappingStrategy(mappingStrategy);//setting .withMappingStrategy(setColumnMapping()) gives an error as Conversion of age to int failed. Use the standard @ (e.g. @CsvBindByName)  
		List<Player> player_list = beanBuilder.build().parse(); //NOTE: csvToBean.parse(strategy, csvReader) is deprecated
	    
		
//		System.out.println(player_list);
		  for (Player p : player_list) { // Same as player_list.forEach(System.out::println);
			  Player player = (Player) p;
			  System.out.println(player);
		  }
		
//		CSVReader reader = new CSVReader(new FileReader("playerlist.csv"));
//	    String [] nextLine;
//	    while ((nextLine = reader.readNext()) != null) {
//	        // nextLine[] is an array of values from the line
//	        System.out.println(nextLine[0] + nextLine[1] + "etc...");
//	    }
		

		
		Graph<Player, DefaultEdge> directedGraph =
	            new DefaultDirectedGraph<Player, DefaultEdge>(DefaultEdge.class);
		for (Player p : player_list) {
			directedGraph.addVertex(p);
		}
//		for (Player v : directedGraph.vertexSet()) {
//			System.out.println("vertex: " + v.getUsername());
//		}
		
		
		List<Pair> listOfPair = new ArrayList<>();
		listOfPair = get_player_edges_from_player_list(player_list);
		System.out.println("\n\nList of player edges:");
		for (Pair<Player, Player> p : listOfPair) {
		      System.out.println("(" + p.getLeft().getUsername() + ", " + p.getRight().getUsername() + ")");
		      directedGraph.addEdge(p.getLeft(), p.getRight());
		    }
		
		// computes all the strongly connected components of the directed graph //NOT USED - algorithm too limiting
		StrongConnectivityAlgorithm<Player, DefaultEdge> scAlg =
	            new KosarajuStrongConnectivityInspector<>(directedGraph); // StrongConnectivityAlgorithm has GabowStrongConnectivityInspector or KosarajuStrongConnectivityInspector. Both seem to do the same thing.
		List<Graph<Player, DefaultEdge>> stronglyConnectedSubgraphs =
	            scAlg.getStronglyConnectedComponents();
		
//		// computes all the strongly connected components of the directed graph with Tarjan's algorithm DOES NOT WORK - prints tuple edges only
//		TarjanSimpleCycles scTarjan = new TarjanSimpleCycles(directedGraph);
//		List<Graph<Player, DefaultEdge>> stronglyConnectedSubgraphs = scTarjan.findSimpleCycles();
//		System.out.println("Strongly connected components: " + stronglyConnectedSubgraphs);
		
//		 // prints the strongly connected components BEFORE removing graphs with definitely no hamilton cycle //NOT DONE as it will be too long
//        for (int i = 0; i < stronglyConnectedSubgraphs.size(); i++) {
//            System.out.println(stronglyConnectedSubgraphs.get(i));
//        }
        
		
        List<Graph<Player, DefaultEdge>> new_list_stronglyConnectedSubgraphs = new ArrayList<>();
        new_list_stronglyConnectedSubgraphs = remove_graphs_with_no_hamilton_cycle(stronglyConnectedSubgraphs);
        System.out.println("Strongly connected components: " + new_list_stronglyConnectedSubgraphs.size()); //Copy of above to show that stronglyConnectedSubgraphs has its graphs (with only one neighbour) removed
        for (int i = 0; i < new_list_stronglyConnectedSubgraphs.size(); i++) {
            System.out.println(new_list_stronglyConnectedSubgraphs.get(i));
        }
        
        
        
//        // NOT USED ANYMORE - brute-force method that fails when number of vertices > 10. The method below finds which person to start & end with to get the maximum path.
//        Map<Integer, GraphPath<Player, DefaultEdge>> longestpathMap = new HashMap<Integer, GraphPath<Player, DefaultEdge>>();
//        for (int index = 0; index < new_list_stronglyConnectedSubgraphs.size(); index++) {
//        	try {
//        		System.out.println("\nIndex " + index + " with graph: " + new_list_stronglyConnectedSubgraphs.get(index));
//            	longestpathMap = find_greatest_path_of_all_vertexes(new_list_stronglyConnectedSubgraphs.get(index), maxlimit);
//            }	catch (NoSuchElementException NSEe) {
//            	NSEe.printStackTrace();
//            	maxlimit++;
//            	longestpathMap = find_greatest_path_of_all_vertexes(new_list_stronglyConnectedSubgraphs.get(index), maxlimit);
//            }
//        }
        
//           NOT USED ANYMORE - takes only the largest sized strongly-connected component
        Map<Integer, Graph<Player, DefaultEdge>> largestVertexGraphMap = new HashMap<>();
        for (int index = 0; index < new_list_stronglyConnectedSubgraphs.size(); index++) {
 
        		System.out.println("\nIndex " + index + " with graph: " + new_list_stronglyConnectedSubgraphs.get(index));
        		Graph<Player, DefaultEdge> directedGraph_01 = new DefaultDirectedGraph<Player, DefaultEdge>(DefaultEdge.class);
        		directedGraph_01 = new_list_stronglyConnectedSubgraphs.get(index);
        		System.out.println("Graph size: " + directedGraph_01.vertexSet().size());
        		System.out.println("Graph: " + directedGraph_01);
        		largestVertexGraphMap.put(directedGraph_01.vertexSet().size(), directedGraph_01);
            }
        
		
      //UNCOMMENT PARAGRAPH		
//        Set<Player> Full_set_of_accepted_players_from_strongly_connected_components = new HashSet<>();
//        List<Player> Full_list_of_rejected_players_from_strongly_connected_components = new ArrayList<>();
//        List<Player> Full_list_of_players = new ArrayList<>();
//        List<Player> fullvertexSetCopy = List.copyOf(directedGraph.vertexSet());
//        Full_list_of_players.addAll(fullvertexSetCopy);
//        for (Graph<Player, DefaultEdge> g : new_list_stronglyConnectedSubgraphs) {
//        	 System.out.println("=======Combining strongly connected graphs=======");
//        	 List<Player> vertexSetCopy = List.copyOf(g.vertexSet());
//        	 Full_set_of_accepted_players_from_strongly_connected_components.addAll(vertexSetCopy);
//        }
//        System.out.println("\nAccepted players Set: " + Full_set_of_accepted_players_from_strongly_connected_components);
//        List<Player> Full_list_of_accepted_players_from_strongly_connected_components = List.copyOf(Full_set_of_accepted_players_from_strongly_connected_components);
//        List<Player> List_of_rejected_players_from_strongly_connected_components = return_rejected_player_list(Full_list_of_players, Full_list_of_accepted_players_from_strongly_connected_components);
//        directedGraph.removeAllVertices(List_of_rejected_players_from_strongly_connected_components);
//        System.out.println("\nNew DirectedGraph Vertex Set: "+ directedGraph.vertexSet());
        

        
        //Find Hamiltonian path 
//        System.out.println("Shortest path from i to c:");
//        TwoApproxMetricTSP<Player,DefaultEdge> TSPclass = new HamiltonianCycleAlgorithmBase()<>();
//        TSPclas
//        
//        DijkstraShortestPath<String, DefaultEdge> dijkstraAlg = new DijkstraShortestPath<>(directedGraph);
//        SingleSourcePaths<String, DefaultEdge> iPaths = dijkstraAlg.getPaths("i");
//        System.out.println(iPaths.getPath("c") + "\n");
        
        
//        directedGraph_02 NOT USED ANYMORE - use directedGraph instead
        int maxKeyValueInMap = Collections.max(largestVertexGraphMap.keySet());
        System.out.println("Max Key Value In Map: " + maxKeyValueInMap + "\n");
        Graph<Player, DefaultEdge> directedGraph_02 = new DefaultDirectedGraph<Player, DefaultEdge>(DefaultEdge.class);
        directedGraph_02 = largestVertexGraphMap.get(maxKeyValueInMap);
        System.out.println("Graph in maxKey: " + directedGraph_02 + "\n");
        
        
        List<Player> accepted_player_list = new ArrayList<>();
        accepted_player_list = dfsWithoutRecursion(directedGraph_02, length_of_paths_considered_complete);
        System.out.println("\nAccepted players list: " + accepted_player_list);
        while (accepted_player_list == null) {
        	System.out.println("\n======================Sorry, no graph path found for desired path length " + length_of_paths_considered_complete + " with randomly-generated starting node. Re-running algorithm.======================");
        	accepted_player_list = dfsWithoutRecursion(directedGraph_02, length_of_paths_considered_complete);
        } 
        System.out.println("\n=============Graph path found for desired path length " + length_of_paths_considered_complete + "! Algorithm stopped.=============");
        
        
        
        
        List<Player> rejected_player_list = return_rejected_player_list(player_list, accepted_player_list);
        try {
        writeRowsToCsv(csv_output, accepted_player_list, mappingStrategy);
        } catch (Exception e) {
        	System.out.println("Something went wrong.");
        }
        System.out.println("\n~~CSV printed!!~~");
        

        

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
	
	private static void writeRowsToCsv(String output_filename, List<Player> rows, HeaderColumnNameTranslateMappingStrategy mappingStrategy)
	        throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		Writer writer = new FileWriter(output_filename);
	    
//	    final List<String> order = List.of("username","name","genderpref","gender","age","maxage","minage","interests","twotruthsonelie","introduction","religion");
//	    final FixedOrderComparator comparator = new FixedOrderComparator(order);
	    
	    StatefulBeanToCsvBuilder<Player> builder = new StatefulBeanToCsvBuilder(writer);
	    StatefulBeanToCsv beanWriter = builder
	              .withMappingStrategy(mappingStrategy)
	              .withSeparator(',')
	              .withQuotechar('"')
	              .build();

	    beanWriter.write(rows);
	    writer.close();
	}
	
	
}


