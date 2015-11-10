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

	public static SimpleWeightedGraph<String, DefaultWeightedEdge> buildGraph(
			List<Entity> entites) {
		SimpleWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);
		Map<String, Set<String>> cachedPagelinks = new HashMap<String, Set<String>>();

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
						Set<String> pagelinks1 = cachedPagelinks.get(can1);
						Set<String> pagelinks2 = cachedPagelinks.get(can2);
						if (pagelinks1 == null) {
							pagelinks1 = DictionaryService.getPagelinks(can1);
							cachedPagelinks.put(can1, pagelinks1);
						}
						if (pagelinks2 == null) {
							pagelinks2 = DictionaryService.getPagelinks(can2);
							cachedPagelinks.put(can2, pagelinks2);
						}

						double topicalRelate = DictionaryService.calculateTR(
								pagelinks1, pagelinks2);
						if (topicalRelate != 0) {
							graph.addVertex(can1);
							graph.addVertex(can2);
							DefaultWeightedEdge edge = graph
									.addEdge(can1, can2);
							graph.setEdgeWeight(edge, topicalRelate);
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
	public static int countUnion(Entity en1, Entity en2) {
		en1.getCandidates().addAll(en2.getCandidates());
		return en1.getCandidates().size();
	}

	public static int countIntersection(Entity en1, Entity en2) {
		en1.getCandidates().retainAll(en2.getCandidates());
		return en1.getCandidates().size();
	}

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

	public static double countPriorProbability(Entity en) {
		/*
		 * double countTotal = 0; double pp = 0; for (String e :
		 * en.getCandidates()) { countTotal += e.getCandidates().size(); } pp =
		 * en.getCandidates().size() / countTotal;
		 * System.out.println("Prior Probability: " + pp);
		 */
		return 0;
	}

	public static double countCoherence(int noOfMentionedEntities,
			List<Entity> sourceOfEntities, Entity candidateEntity) {
		double totalTR = 0;
		double coh = 0;
		for (Entity e : sourceOfEntities) {
			// totalTR += countTR(e, candidateEntity);
		}
		coh = totalTR / (noOfMentionedEntities - 1);
		System.out.println("TotalTR: " + coh);
		return coh;
	}

}
