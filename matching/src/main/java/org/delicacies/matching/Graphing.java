package org.delicacies.matching;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
		//Start the timer
		double startTime = System.currentTimeMillis(); //or nanoTime()
		
		final Integer maxlimit = Integer.valueOf(0); //Set max algorithm path limit (in case of high player numbers)
		final Integer length_of_paths_considered_complete = 309;
		final Integer stack_path_clearing_divisor_number = 500; // A Java server with 2GB RAM stack_path size can handle up to 400,000 before crashing (OutOfMemory Error) 
		
		// Find the current date and time in your system; to be used in naming the csv outputs
		LocalDateTime date_now_utc = LocalDateTime.now();
		String date_now_string = date_now_utc.format(DateTimeFormatter.ofPattern("dd-MM-yy HH-mm-ss"));
		System.out.println("Date now is " + date_now_string);
		
		String csv_path = "playerlist.csv";
		String accepted_players_csv_output = "Accepted Players" + date_now_string + ".csv";
		String rejected_players_csv_output = "Rej Players" + date_now_string + ".csv";

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

		
		//Recommended to use Bean Builder now instead of Bean constructor
//		Logger logger = Logger.getLogger(Graphing.class.getName());
		CsvToBeanBuilder<Player> beanBuilder = new CsvToBeanBuilder(new FileReader("playerlist.csv"));
		List<Player> player_list = beanBuilder.withType(Player.class).withMappingStrategy(mappingStrategy).build().parse(); //NOTE: csvToBean.parse(strategy, csvReader) is deprecated; //setting .withMappingStrategy(setColumnMapping()) gives an error as Conversion of age to int failed. Use the standard @ (e.g. @CsvBindByName)  
		
		
//		System.out.println(player_list);
//		  for (Player p : player_list) { // Same as player_list.forEach(System.out::println);
//			  Player player = (Player) p;
//			  System.out.println(player);
//		  }		
		
		Graph<Player, DefaultEdge> directedGraph =
	            new DefaultDirectedGraph<Player, DefaultEdge>(DefaultEdge.class);
		for (Player p : player_list) {
			directedGraph.addVertex(p);
		}
		
		
		 
		List<Pair> listOfPair = get_player_edges_from_player_list(player_list);
		System.out.println("\n\nList of player edges:");
		for (Pair<Player, Player> p : listOfPair) {
		      System.out.println("(" + p.getLeft().getUsername() + ", " + p.getRight().getUsername() + ")");
		      directedGraph.addEdge(p.getLeft(), p.getRight());
		    }
		
		// computes all the strongly connected components of the directed graph
		StrongConnectivityAlgorithm<Player, DefaultEdge> scAlg =
	            new KosarajuStrongConnectivityInspector<>(directedGraph); // StrongConnectivityAlgorithm Interface has GabowStrongConnectivityInspector or KosarajuStrongConnectivityInspector. Both seem to do the same thing.
		List<Graph<Player, DefaultEdge>> stronglyConnectedSubgraphs =
	            scAlg.getStronglyConnectedComponents();
		
        
		
        
		List<Graph<Player, DefaultEdge>> new_list_stronglyConnectedSubgraphs = remove_graphs_with_no_hamilton_cycle(stronglyConnectedSubgraphs);
        System.out.println("Strongly connected components: " + new_list_stronglyConnectedSubgraphs.size());
//        for (int i = 0; i < new_list_stronglyConnectedSubgraphs.size(); i++) {
//            System.out.println(new_list_stronglyConnectedSubgraphs.get(i));
//        }
        
        
        
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
        
        //Find the largest number of vertices of the largest sized strongly-connected component
        Map<Integer, Graph<Player, DefaultEdge>> largestVertexGraphMap = new HashMap<>();
        for (int index = 0; index < new_list_stronglyConnectedSubgraphs.size(); index++) {
//        		System.out.println("\nIndex " + index + " with graph: " + new_list_stronglyConnectedSubgraphs.get(index));
//        		Graph<Player, DefaultEdge> directedGraph_01 = new DefaultDirectedGraph<Player, DefaultEdge>(DefaultEdge.class);
//        		directedGraph_01 = new_list_stronglyConnectedSubgraphs.get(index);
//        		System.out.println("Graph size: " + directedGraph_01.vertexSet().size());
//        		System.out.println("Graph: " + directedGraph_01);
        		largestVertexGraphMap.put(new_list_stronglyConnectedSubgraphs.get(index).vertexSet().size(), new_list_stronglyConnectedSubgraphs.get(index));
            }
        int maxKeyValueInMap = Collections.max(largestVertexGraphMap.keySet());
        System.out.println("Max Key Value In Map: " + maxKeyValueInMap + "\n");
        
        
        
        List<Graph<Player, DefaultEdge>> list_of_largest_stronglyConnectedSubgraphs = new ArrayList<Graph<Player, DefaultEdge>>();
        for (Graph<Player, DefaultEdge> e : new_list_stronglyConnectedSubgraphs) {
        	if (e.vertexSet().size() == maxKeyValueInMap) {
        		list_of_largest_stronglyConnectedSubgraphs.add(e);
        	}
        }
        System.out.println("Number of SCC graphs with largest number of vertexes: " + list_of_largest_stronglyConnectedSubgraphs.size() + "\n");
        
        
        
        Graph<Player, DefaultEdge> directedGraph_largest = new DefaultDirectedGraph<Player, DefaultEdge>(DefaultEdge.class);
        Random rand = new Random();
        int random_index = rand.nextInt(list_of_largest_stronglyConnectedSubgraphs.size()); // The nextInt(int n) is used to get a random number between 0(inclusive) and the number passed in this argument(n), exclusive.
        directedGraph_largest = list_of_largest_stronglyConnectedSubgraphs.get(random_index);
        System.out.println("Graph maximum number of vertexes to be processed by DFS: " + directedGraph_largest + "\n");
        
        
        
        List<Player> accepted_player_list = dfsWithoutRecursion(directedGraph_largest, length_of_paths_considered_complete, stack_path_clearing_divisor_number);
        System.out.println("\nAccepted players list: " + accepted_player_list);
        while (accepted_player_list == null) {
        	System.out.println("\n======================Sorry, no graph path found for desired path length " + length_of_paths_considered_complete + " with randomly-generated starting node. Re-running algorithm.======================");
        	accepted_player_list = dfsWithoutRecursion(directedGraph_largest, length_of_paths_considered_complete, stack_path_clearing_divisor_number);
        } 
        System.out.println("\n=============Graph path found for desired path length " + length_of_paths_considered_complete + "! Algorithm stopped.=============");
        
        //Stop the timer
        double endTime = System.currentTimeMillis(); // NOTE: Integer division will give only an integer result. See https://stackoverflow.com/questions/33273809/how-to-get-java-to-produce-decimal-points-while-dividing
		double duration = (endTime - startTime)/1000;  //if nanoTime() is used, divide by 1000000 to get milliseconds.
		System.out.println("\nstartTime = **"+ startTime + "** milliseconds");
		System.out.println("\nendTime = **"+ endTime + "** milliseconds");
		System.out.println("\nThe script took **"+ duration + "** seconds to find a solution!");
		
        List<Player> rejected_player_list = return_rejected_player_list(player_list, accepted_player_list);
        try {
        writeRowsToCsv(accepted_players_csv_output, accepted_player_list, mappingStrategy);
        System.out.println("\n~~Accepted Player List CSV exported!!~~");
        if (rejected_player_list.size() != 0) {
        	writeRowsToCsv(rejected_players_csv_output, rejected_player_list, mappingStrategy);
            System.out.println("\n~~Rejected Player List CSV exported!!~~");	
        } else {
        	System.out.println("\n~~No rejected players~~");	
        }
        } catch (Exception e) {
        	System.out.println("Something went wrong.");
        }


	}
	
	private static void writeRowsToCsv(String output_filename, List<Player> rows, HeaderColumnNameTranslateMappingStrategy mappingStrategy)
	        throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		Writer writer = new FileWriter(output_filename);
	    
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


