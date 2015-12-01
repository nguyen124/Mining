package mining.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import mining.entity.Entity;
import mining.main.MainProgram;

public class GraphService {
	// public static final float ARTICLES = getAllWikiArticles();

	public static SimpleWeightedGraph<String, DefaultWeightedEdge> buildGraph(
			List<Entity> entities) {
		SimpleWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);
		// Map<String, List<String>> cachedPagelinks = new HashMap<String,
		// List<String>>();
		Map<String, Double> cached = new HashMap<>();
		List<String> possibleEntities = EntityService
				.interativeSubstitution(entities);
		System.out.println("The possible mapping entities are: "
				+ possibleEntities.toString());

		for (int i = 0; i < entities.size(); i++) {
			List<String> candidates1 = entities.get(i).getCandidates();
			/*
			 * for (int k = 0; k < candidates1.size(); k++) {
			 * graph.addVertex(candidates1.get(k)); }
			 */
			for (int j = 0; j < i; j++) {
				if (i != j) {
					List<String> candidates2 = entities.get(j).getCandidates();
					for (int l = 0; l < candidates1.size(); l++) {
						for (int m = 0; m < candidates2.size(); m++) {
							/*
							 * DefaultWeightedEdge edge = graph.addEdge(
							 * candidates1.get(l), candidates2.get(m));
							 */
							String can1 = candidates1.get(l);
							String can2 = candidates2.get(m);
							List<String> pagelinks1;// =
													// cachedPagelinks.get(can1);
							List<String> pagelinks2;// =
													// cachedPagelinks.get(can2);
							// if (pagelinks1 == null) {
							// pagelinks1 =
							// DictionaryService.getPagelinks(can1);
							pagelinks1 = EntityService
									.getListOfHyperLinks(can1);
							// cachedPagelinks.put(can1, pagelinks1);
							// }
							// if (pagelinks2 == null) {
							pagelinks2 = EntityService
									.getListOfHyperLinks(can2);
							// cachedPagelinks.put(can2, pagelinks2);
							// }
							// Calculate Topical Relatedness for between 2
							// candidates
							double topicalRelate = EntityService
									.calculateTopicalRelatedness(pagelinks1,
											pagelinks2);
							if (topicalRelate != 0) {
								System.out
										.println("Topical Relatedness between candidate: "
												+ can1
												+ " & candidate: "
												+ can2
												+ " is: "
												+ topicalRelate);
								graph.addVertex(can1);
								graph.addVertex(can2);
								DefaultWeightedEdge edge = graph.addEdge(can1,
										can2);
								// The weight between 2 nodes is the Topical
								// Relatedness value
								graph.setEdgeWeight(edge, topicalRelate);

								// Calculate the Prior Probability for candidate
								if (!cached.containsKey("PP:" + can1)) {
									double can1PP = EntityService
											.calculatePriorProbability(can1,
													entities.get(i));
									cached.put("PP:" + can1, can1PP);
								}
								/*
								 * if (!cached.containsKey("PP:" + can2)) {
								 * double can2PP = EntityService
								 * .calculatePriorProbability(can2,
								 * entities.get(j)); cached.put("PP:" + can2,
								 * can2PP); }
								 */
								// Calculate Context Similarity
								// String entityContext =
								// "McNealy finished he was pretty much squarely in camp of Sun";
								String entityContext = MainProgram.tweet;
								if (!cached.containsKey("CS:" + can1)) {
									double can1CS = EntityService
											.calculateContextSimilarity(can1+" "+can2,
													entityContext);
									cached.put("CS:" + can1, can1CS);
								}
								/*
								 * if (!cached.containsKey("CS:" + can2)) {
								 * double can2CS = EntityService
								 * .calculateContextSimilarity(can2,
								 * entityContext); cached.put("CS:" + can2,
								 * can2CS); }
								 */
								// Calculate the Topical Coherence for candidate

								if (!cached.containsKey("TC:" + can1)) {
									double can1TC = EntityService
											.calculateTopicalCoherence(can1, i,
													possibleEntities);
									cached.put("TC:" + can1, can1TC);
								}
								/*
								 * if (!cached.containsKey("TC:" + can2)) {
								 * double can2TC = EntityService
								 * .calculateTopicalCoherence(can2, j,
								 * possibleEntities); cached.put("TC:" + can2,
								 * can2TC); }
								 */

								// calcuate the interest score
								if (!cached.containsKey("IS:" + can1)) {
									double interestScore = EntityService.alpha
											* cached.get("PP:" + can1)
											+ EntityService.beta
											* cached.get("CS:" + can1)
											+ EntityService.gamma
											* cached.get("TC:" + can1);
									System.out.println("Interest Score of: "
											+ can1 + " is: " + interestScore);
									cached.put("IS:" + can1, interestScore);
								}
								/*
								 * if (!cached.containsKey("IS:" + can2)) {
								 * double interestScore = EntityService.alpha
								 * cached.get("PP:" + can2) + EntityService.beta
								 * cached.get("CS:" + can2) +
								 * EntityService.gamma cached.get("TC:" + can2);
								 * System.out.println("Interest Score: " + can2
								 * + " is: " + interestScore); cached.put("IS:"
								 * + can2, interestScore); }
								 */

								if (!cached.containsKey("PP:" + can2)) {
									double can2PP = EntityService
											.calculatePriorProbability(can2,
													entities.get(j));
									cached.put("PP:" + can2, can2PP);
								}

								if (!cached.containsKey("CS:" + can2)) {
									double can2CS = EntityService
											.calculateContextSimilarity(can1+ " "+ can2,
													entityContext);
									cached.put("CS:" + can2, can2CS);
								}

								if (!cached.containsKey("TC:" + can2)) {
									double can2TC = EntityService
											.calculateTopicalCoherence(can2, j,
													possibleEntities);
									cached.put("TC:" + can2, can2TC);
								}
								if (!cached.containsKey("IS:" + can2)) {
									double interestScore = EntityService.alpha
											* cached.get("PP:" + can2)
											+ EntityService.beta
											* cached.get("CS:" + can2)
											+ EntityService.gamma
											* cached.get("TC:" + can2);
									System.out.println("Interest Score: "
											+ can2 + " is: " + interestScore);
									cached.put("IS:" + can2, interestScore);
								}
							}

						}
					}

				}
			}
		}
		Map<String, Double> result = nomalizeGraph(cached, graph);

		for (int i = 0; i < entities.size(); i++) {
			double max = 0;
			String entity = "";
			for (int j = 0; j < entities.get(i).getCandidates().size(); j++) {
				if (result.containsKey(entities.get(i).getCandidates().get(j))) {
					double temp = result.get(entities.get(i).getCandidates()
							.get(j));
					if (temp > max) {
						max = temp;
						entity = entities.get(i).getCandidates().get(j);
					}
				}
			}
			System.out.println("True mapping: " + entity + " with score: "
					+ max);
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

	private static Map<String, Double> nomalizeGraph(
			Map<String, Double> cached,
			SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
		// TODO Auto-generated method stub
		Map<String, Double> result = new TreeMap<String, Double>();
		List<String> vertexes = new ArrayList<String>();
		vertexes.addAll(graph.vertexSet());
		double[] normalizedInterestScore = new double[vertexes.size()];
		double[] normalizedInterestScoreTemp = new double[vertexes.size()];
		double[][] normalizedWeight = new double[vertexes.size()][vertexes
				.size()];
		for (int i = 0; i < vertexes.size(); i++) {
			double nomalizedIS = cached.get("IS:" + vertexes.get(i));
			double totalIS = nomalizedIS;
			double totalWeight = 0;
			String target = "";
			Set<DefaultWeightedEdge> edgesOfVertex = graph.edgesOf(vertexes
					.get(i));
			for (DefaultWeightedEdge edge : edgesOfVertex) {
				target = graph.getEdgeTarget(edge);
				totalIS += cached.get("IS:" + target);
				totalWeight += graph.getEdgeWeight(edge);
			}
			nomalizedIS = nomalizedIS / totalIS;

			normalizedInterestScore[i] = nomalizedIS * EntityService.lamda;
			normalizedInterestScoreTemp[i] = nomalizedIS
					* (1 - EntityService.lamda);
			for (DefaultWeightedEdge edge : edgesOfVertex) {
				target = graph.getEdgeTarget(edge);
				double nomalizedWeight = graph.getEdgeWeight(edge)
						/ totalWeight;
				int j = vertexes.indexOf(target);
				normalizedWeight[i][j] = nomalizedWeight;
			}
		}
		for (int m = 0; m < vertexes.size(); m++) {
			for (int n = 0; n < vertexes.size(); n++) {
				normalizedInterestScoreTemp[n] += normalizedWeight[m][n]
						* normalizedInterestScoreTemp[n];
			}
			normalizedInterestScoreTemp[m] += normalizedInterestScore[m];
			result.put(vertexes.get(m), normalizedInterestScoreTemp[m]);
		}
		return result;
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

}
