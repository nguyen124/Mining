package mining.main;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.jgrapht.WeightedGraph;
import org.jgrapht.generate.WeightedGraphGenerator;

import com.sun.javafx.geom.Edge;

import mining.dbconnection.DBConnection;

import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import mining.entity.Entity;
import mining.service.DictionaryService;
import mining.service.GraphService;

public class MainProgram {

	public static void main(String[] args) {

		List<Entity> entites = new ArrayList<Entity>();
		Entity sun = DictionaryService.buildEntity("Sun");
		Entity mcNealy = DictionaryService.buildEntity("McNealy");

		/*
		 * Entity bulls = DictionaryService.buildEntity("Bulls"); Entity scott =
		 * DictionaryService.buildEntity("Scott"); Entity tysonChandler =
		 * DictionaryService.buildEntity("Tyson Chandler"); Entity tonyAllen =
		 * DictionaryService.buildEntity("Tony Allen"); Entity nba =
		 * DictionaryService.buildEntity("NBA");
		 */
		entites.add(sun);
		entites.add(mcNealy);
		/*
		 * entites.add(bulls); entites.add(scott); entites.add(tysonChandler);
		 * entites.add(tonyAllen); entites.add(nba);
		 */
		SimpleWeightedGraph<String, DefaultWeightedEdge> graph = GraphService
				.buildGraph(entites);
		int a = 0;
		/*
		 * SimpleWeightedGraph<String, DefaultWeightedEdge> graph = new
		 * SimpleWeightedGraph<String, DefaultWeightedEdge>(
		 * DefaultWeightedEdge.class);
		 * 
		 * for (int a = 0; a < sun.getCandidates().size(); a++) {
		 * graph.addVertex(sun.getCandidates().get(a)); for (int b = 0; b <
		 * mcNealy.getCandidates().size(); b++) {
		 * graph.addVertex(mcNealy.getCandidates().get(b)); DefaultWeightedEdge
		 * e1 = graph.addEdge( sun.getCandidates().get(a),
		 * mcNealy.getCandidates() .get(b)); graph.setEdgeWeight(e1, 1);
		 * 
		 * for (int c = 0; c < bulls.getCandidates().size(); c++) {
		 * graph.addVertex(bulls.getCandidates().get(c)); for (int d = 0; d <
		 * scott.getCandidates().size(); d++) {
		 * graph.addVertex(scott.getCandidates().get(d)); for (int e = 0; e <
		 * tysonChandler.getCandidates() .size(); e++) {
		 * graph.addVertex(tysonChandler.getCandidates() .get(e)); for (int f =
		 * 0; f < tonyAllen.getCandidates() .size(); f++) {
		 * graph.addVertex(tonyAllen.getCandidates() .get(f)); for (int g = 0; g
		 * < nba.getCandidates().size(); g++) {
		 * graph.addVertex(nba.getCandidates().get(g)); } } } } }
		 * 
		 * }}
		 */

	}
}
