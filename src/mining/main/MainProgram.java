package mining.main;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import mining.entity.Entity;
import mining.service.DictionaryService;
import mining.service.EntityService;
import mining.service.GraphService;

public class MainProgram {
	public static String tweet = "Ronaldo is such a nightmare for Real";
	public static String en1 = "Messi";
	public static String en2 = "Real";

	public static void main(String[] args) {

		List<Entity> entites = new ArrayList<Entity>();
		// Entity sun = DictionaryService.buildEntity("Sun");
		// Entity mcNealy = DictionaryService.buildEntity("McNealy");
		// Entity sun = EntityService.buildEntity("Sun");
		// Entity mcNealy = EntityService.buildEntity("McNealy");
		Entity entity1 = EntityService.buildEntity(en1);
		Entity entity2 = EntityService.buildEntity(en2);
		if (entity1.getCandidates().size() < 1
				|| entity2.getCandidates().size() < 1) {
			System.out.println("Can't recognize entity");
			return;
		}
		/*
		 * Entity bulls = DictionaryService.buildEntity("Bulls"); Entity scott =
		 * DictionaryService.buildEntity("Scott"); Entity tysonChandler =
		 * DictionaryService.buildEntity("Tyson Chandler"); Entity tonyAllen =
		 * DictionaryService.buildEntity("Tony Allen"); Entity nba =
		 * DictionaryService.buildEntity("NBA");
		 */
		// entites.add(sun);
		// entites.add(mcNealy);
		entites.add(entity1);
		entites.add(entity2);
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
