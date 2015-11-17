package mining.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import mining.entity.Entity;

public class GraphService {
	// public static final float ARTICLES = getAllWikiArticles();

	public static SimpleWeightedGraph<String, DefaultWeightedEdge> buildGraph(List<Entity> entites) {
		SimpleWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);
		// Map<String, List<String>> cachedPagelinks = new HashMap<String,
		// List<String>>();
		Map<String, Double> cached = new HashMap<>();
		for (int i = 0; i < entites.size(); i++) {
			List<String> candidates1 = entites.get(i).getCandidates();
			/*
			 * for (int k = 0; k < candidates1.size(); k++) {
			 * graph.addVertex(candidates1.get(k)); }
			 */
			for (int j = 0; j < i; j++) {
				List<String> candidates2 = entites.get(j).getCandidates();
				for (int l = 0; l < candidates1.size(); l++) {
					for (int m = 0; m < candidates2.size(); m++) {
						/*
						 * DefaultWeightedEdge edge = graph.addEdge(
						 * candidates1.get(l), candidates2.get(m));
						 */
						String can1 = candidates1.get(l);
						String can2 = candidates2.get(m);
						List<String> pagelinks1;// = cachedPagelinks.get(can1);
						List<String> pagelinks2;// = cachedPagelinks.get(can2);
						// if (pagelinks1 == null) {
						// pagelinks1 =
						// DictionaryService.getPagelinks(can1);
						pagelinks1 = EntityService.getListOfHyperLinks(can1);
						// cachedPagelinks.put(can1, pagelinks1);
						// }
						// if (pagelinks2 == null) {
						pagelinks2 = EntityService.getListOfHyperLinks(can2);
						// cachedPagelinks.put(can2, pagelinks2);
						// }
						// Calculate Topical Relatedness for between 2
						// candidates
						double topicalRelate = EntityService.calculateTopicalRelatedness(pagelinks1, pagelinks2);

						if (topicalRelate != 0) {
							graph.addVertex(can1);
							graph.addVertex(can2);
							DefaultWeightedEdge edge = graph.addEdge(can1, can2);
							// The weight between 2 nodes is the Topical
							// Relatedness value
							graph.setEdgeWeight(edge, topicalRelate);
							// Calculate the Prior Probability for candidate
							if (!cached.containsKey("PP:" + can1)) {
								double can1PP = EntityService.calculatePriorProbability(can1, entites.get(i));
								cached.put("PP:" + can1, can1PP);
							}
							if (!cached.containsKey("PP:" + can2)) {
								double can2PP = EntityService.calculatePriorProbability(can2, entites.get(j));
								cached.put("PP:" + can2, can2PP);
							}
							// Calculate the Topical Coherence for candidate
							if (!cached.containsKey("TC:" + can1)) {
								double can1TC = EntityService.calculateTopicalCoherence(can1, entites, j);
								cached.put("TC:" + can1, can1TC);
							}
							if (!cached.containsKey("TC:" + can2)) {
								double can2TC = EntityService.calculateTopicalCoherence(can2, entites, i);
								cached.put("TC:" + can2, can2TC);
							}

						}

					}
				}

			}
		}
		return graph;
	}

	/*
	 * public static void saveToFile(List<Entity> entities) { // TODO
	 * Auto-generated method stub Resource resource = new
	 * ClassPathResource("knowledge2.txt"); try { FileOutputStream fos = new
	 * FileOutputStream(resource.getFile()); BufferedWriter bw = new
	 * BufferedWriter(new OutputStreamWriter(fos)); int size = entities.size();
	 * // bw.write(size); System.out.println(size); // bw.newLine(); for (Entity
	 * en : entities) { // bw.write(en.getName());
	 * System.out.println(en.getName()); // bw.newLine(); } for (int i = 0; i <
	 * entities.get(0).getDisambiguationNames().size(); i++) { //
	 * bw.write(entities.get(0).getDisambiguationNames().get(i).getName() +
	 * ",");
	 * System.out.print(entities.get(0).getDisambiguationNames().get(i).getName
	 * () + ","); for (int j = 0; j <
	 * entities.get(1).getDisambiguationNames().size(); j++) { //
	 * bw.write(entities.get(1).getDisambiguationNames().get(j).getName() + //
	 * ",");
	 * System.out.print(entities.get(1).getDisambiguationNames().get(j).getName
	 * () + ","); } // bw.newLine(); System.out.println(); } } catch
	 * (FileNotFoundException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); }
	 * 
	 * }
	 */

	/*
	 * public static double countTR(Entity en1, Entity en2) { int sizeU1 =
	 * en1.getCandidates().size(); int sizeU2 = en2.getCandidates().size(); //
	 * int sizeUnion = countUnion(en1, en2); int sizeIntersect =
	 * countIntersection(en1, en2);
	 * 
	 * double result; result = 1 - (Math.log10(Math.max(sizeU1, sizeU2) -
	 * Math.log10(sizeIntersect))) / (Math.log10(ARTICLES) -
	 * Math.log10(Math.min(sizeU1, sizeU2)));
	 * 
	 * System.out.println("MaxSize(U1,U2):" + Math.max(sizeU1, sizeU2) +
	 * " - IntersectSize" + sizeIntersect + " - TotalArticles" + ARTICLES +
	 * " - MinSize(U1,U2)" + Math.min(sizeU1, sizeU2)); return result; }
	 */

}
