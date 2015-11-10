package mining.entity;

/*import java.io.BufferedReader;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.InputStreamReader;

 import org.springframework.core.io.ClassPathResource;
 import org.springframework.core.io.Resource;*/

import java.util.ArrayList;
import java.util.List;

import mining.service.GraphService;

public class MatrixGraph {
	private double[][] matrix;
	private List<String> nodes = new ArrayList<String>();;

	public MatrixGraph() {
		matrix = null;
	}

	// public Graph(String file, ServletContext servletContext) {
	public MatrixGraph(List<Entity> entities) {

		int lowerBound = 0;
		int upperBound = 0;
		for (Entity en : entities) {
			upperBound += en.getCandidates().size();
			for (String e : en.getCandidates()) {
				// e.setLowerBound(lowerBound);
				// e.setUpperBound(upperBound);
				nodes.add(e);
			}
			lowerBound += en.getCandidates().size();
		}
		int size = nodes.size();
		matrix = new double[size][size];
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				/*
				 * if (nodes.get(row).isSameRange(nodes.get(col))) {
				 * matrix[row][col] = 0; } else { matrix[row][col] =
				 * GraphService.countTR(nodes.get(row), nodes.get(col)); }
				 */
			}
		}
		/*
		 * // this.servletContext = servletContext; // adjLists = null; // if
		 * (servletContext == null) { //
		 * System.out.println("servet context is null"); // } try { //
		 * InputStream is = servletContext //
		 * .getResourceAsStream("knowledge.txt"); // BufferedReader br = new
		 * BufferedReader(new // InputStreamReader(is)); //Resource resource =
		 * new ClassPathResource(file); //InputStream is =
		 * resource.getInputStream(); //BufferedReader br = new
		 * BufferedReader(new InputStreamReader(is)); //adjLists = new
		 * Vertex[Integer.parseInt(br.readLine())]; for (int v = 0; v <
		 * adjLists.length; v++) { //adjLists[v] = new Vertex(br.readLine(),
		 * null);
		 * 
		 * } String line = br.readLine(); while (line != null) { String[] subs =
		 * line.split(" "); int v1 = indexForName(subs[0]); int v2 =
		 * indexForName(subs[1]); adjLists[v1].setAdjList(new Neighbor(v2,
		 * adjLists[v1] .getAdjList())); adjLists[v2].setAdjList(new
		 * Neighbor(v1, adjLists[v2] .getAdjList())); line = br.readLine(); } }
		 * catch (NumberFormatException e) { e.printStackTrace(); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
	}

	public double[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(double[][] maxtrix) {
		this.matrix = maxtrix;
	}

	public void print() {
		int size = nodes.size();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}
}
