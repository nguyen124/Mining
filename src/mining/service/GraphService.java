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
import java.util.List;
import java.util.Map;
import mining.graph.Entity;

public class GraphService {
	public static final float ARTICLES = getAllWikiArticles();

	public static void buildGraph(String file) {
		// Graph gr = new Graph(file);
		// gr.print();
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
	 * entities.get(0).getRefs().size(); i++) { //
	 * bw.write(entities.get(0).getRefs().get(i).getName() + ",");
	 * System.out.print(entities.get(0).getRefs().get(i).getName() + ","); for
	 * (int j = 0; j < entities.get(1).getRefs().size(); j++) { //
	 * bw.write(entities.get(1).getRefs().get(j).getName() + // ",");
	 * System.out.print(entities.get(1).getRefs().get(j).getName() + ","); } //
	 * bw.newLine(); System.out.println(); } } catch (FileNotFoundException e) {
	 * // TODO Auto-generated catch block e.printStackTrace(); } catch
	 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace();
	 * }
	 * 
	 * }
	 */
	public static int countUnion(Entity en1, Entity en2) {
		en1.getRefs().addAll(en2.getRefs());
		return en1.getRefs().size();
	}

	public static int countIntersection(Entity en1, Entity en2) {
		en1.getRefs().retainAll(en2.getRefs());
		return en1.getRefs().size();
	}

	public static double countTR(Entity en1, Entity en2) {
		int sizeU1 = en1.getRefs().size();
		int sizeU2 = en2.getRefs().size();
		// int sizeUnion = countUnion(en1, en2);
		int sizeIntersect = countIntersection(en1, en2);

		double result;
		result = 1
				- (Math.log10(Math.max(sizeU1, sizeU2)
						- Math.log10(sizeIntersect)))
				/ (Math.log10(ARTICLES) - Math.log10(Math.min(sizeU1, sizeU2)));

		System.out.println("MaxSize(U1,U2):" + Math.max(sizeU1, sizeU2)
				+ " - IntersectSize" + sizeIntersect + " - TotalArticles"
				+ ARTICLES + " - MinSize(U1,U2)" + Math.min(sizeU1, sizeU2));
		return result;
	}

	public static double countPriorProbability(Entity en) {
		double countTotal = 0;
		double pp = 0;
		for (Entity e : en.getRefs()) {
			countTotal += e.getRefs().size();
		}
		pp = en.getRefs().size() / countTotal;
		System.out.println("Prior Probability: " + pp);
		return pp;
	}

	public static double countCoherence(int noOfMentionedEntities,
			List<Entity> sourceOfEntities, Entity candidateEntity) {
		double totalTR = 0;
		double coh = 0;
		for (Entity e : sourceOfEntities) {
			totalTR += countTR(e, candidateEntity);
		}
		coh = totalTR / (noOfMentionedEntities - 1);
		System.out.println("TotalTR: " + coh);
		return coh;
	}

	private static int getAllWikiArticles() {
		return 0;
	}
}
