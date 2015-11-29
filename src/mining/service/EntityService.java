package mining.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mining.entity.Entity;
import redis.clients.jedis.Jedis;

public class EntityService {
	public static double COUNT_ALL_WP = 3381716;
	public static double COUNT_ALL_CATEGORIES_RECORDS = 10021327;
	public static double alpha = 0.2;
	public static double beta = 0.6;
	public static double gamma = 0.2;
	public static double lamda = 0.4;

	// public static int COUNT_PAGELINKS = 81835577;

	public static Entity buildEntity(String en) {
		Entity entity = new Entity(en);
		List<String> candidates = new ArrayList<String>();
		Jedis jedis = new Jedis("localhost");
		try {
			// get disambiguation names
			en = en.trim();
			List<String> disambiguation = jedis.lrange("D:" + en, 0, -1);
			if (disambiguation.size() > 0) {
				candidates.addAll(disambiguation);
			}
			List<String> disambiguation2 = jedis.lrange("D:" + en
					+ "_%28disambiguation%29", 0, -1);
			if (disambiguation2.size() > 0) {
				candidates.addAll(disambiguation2);
			}
			// get redirect names
			List<String> redirect = jedis.lrange("R:" + en, 0, -1);
			if (redirect.size() > 0) {
				candidates.addAll(redirect);
			}

			// get entity page
			/*
			 * List<String> links = jedis.lrange("L:" + en, 0, -1); if (links !=
			 * null) { //candidates.add(en); candidates.addAll(links); }
			 */
			candidates.remove("");
			entity.setCandidates(candidates);
		} catch (Exception e) {

			e.printStackTrace();
		}
		jedis.close();
		return entity;
	}

	public static double calculateTopicalRelatedness(
			List<String> entity1Pagelinks, List<String> entity2Pagelinks) {
		// int minPagelinksSize;
		// int maxPagelinksSize;
		int intersectPagelinksSize;
		double topicalRelation = 0;
		Set<String> intersect = new HashSet<String>();
		entity1Pagelinks.remove("");
		entity2Pagelinks.remove("");
		intersect.addAll(entity1Pagelinks);
		// minPagelinksSize = Math.min(entity1Pagelinks.size(),
		// entity2Pagelinks.size());
		// maxPagelinksSize = Math.max(entity1Pagelinks.size(),
		// entity2Pagelinks.size());

		intersect.retainAll(entity2Pagelinks);
		intersectPagelinksSize = intersect.size();
		if (intersectPagelinksSize != 0) {
			/*
			 * topicalRelation = (Math.log(maxPagelinksSize) - Math
			 * .log(intersectPagelinksSize)) / (Math.log(COUNT_ALL_WP) -
			 * Math.log(minPagelinksSize));
			 */
			topicalRelation = Math.log(2 * intersectPagelinksSize)
					/ (Math.log(entity1Pagelinks.size()) + Math
							.log(entity2Pagelinks.size()));
		}
		return topicalRelation;
	}

	/*
	 * public static Set<String> getSetOfPagelinks(String can) { Jedis jedis =
	 * new Jedis("localhost"); // get entity page List<String> lnks =
	 * jedis.lrange("L:" + can, 0, -1); Set<String> pagelinks = new
	 * HashSet<String>(); for (String l : lnks) { pagelinks.add(l); }
	 * jedis.close(); return pagelinks; }
	 */

	public static List<String> getListOfHyperLinks(String can) {
		// TODO Auto-generated method stub
		Jedis jedis = new Jedis("localhost");
		// get entity page
		Set<String> lnks = new TreeSet<String>();
		lnks.addAll(jedis.lrange("L:" + can, 0, -1));
		lnks.addAll(jedis.lrange("R:" + can, 0, -1));
		lnks.addAll(jedis.lrange("T:" + can, 0, -1));
		lnks.addAll(jedis.lrange("D:" + can, 0, -1));
		jedis.close();
		ArrayList<String> result = new ArrayList<String>();
		result.addAll(lnks);
		return result;
	}

	/*
	 * public static int countUnion(Entity en1, Entity en2) {
	 * en1.getCandidates().addAll(en2.getCandidates()); return
	 * en1.getCandidates().size(); }
	 */

	/*
	 * public static int countIntersection(Entity en1, Entity en2) {
	 * en1.getCandidates().retainAll(en2.getCandidates()); return
	 * en1.getCandidates().size(); }
	 */

	public static double calculatePriorProbability(String child, Entity parent) {

		double pp = calculatePriorProbability(child, parent.getCandidates());
		System.out.println("Prior Probability between child: " + child
				+ " & parent: " + parent.getTitle() + " is: " + pp);
		return pp;
	}

	public static double calculatePriorProbability(String child,
			List<String> candidates) {

		double pp = 0;
		int countOfLinksToChild = countListOfLinksTo(child);
		if (countOfLinksToChild == 0) {
			return 0;
		}
		double countParentLinksToTotal = 0;
		for (String name : candidates) {
			int countOfParentLinks = countListOfLinksTo(name);
			countParentLinksToTotal += countOfParentLinks;
		}
		pp = countOfLinksToChild / countParentLinksToTotal;

		return pp;
	}

	private static int countListOfLinksTo(String targetLink) {
		Jedis jedis = new Jedis("localhost");
		// get entity page
		List<String> result = jedis.lrange("C:" + targetLink, 0, -1);
		jedis.close();
		int count = 0;
		if (result.size() > 0) {
			count = Integer.parseInt(result.get(0));
		}
		return count;
	}

	/*
	 * public static double calculateTopicalCoherence(String can, List<Entity>
	 * sources, int j) { int size = sources.size(); double sumTR = 0; if (size >
	 * 1) { for (int i = 0; i < size; i++) { if (i != j) { for (String can2 :
	 * sources.get(j).getCandidates()) { sumTR += calculateTopicalRelatedness(
	 * getListOfHyperLinks(can), getListOfHyperLinks(can2)); } } }
	 * System.out.println("Topical Coh between candidate: " + can +
	 * " & Other Mapping Entity: " + (sumTR / (size - 1))); return (sumTR /
	 * (size - 1)); } System.out.println("Topical Coh between candidate: " + can
	 * + " & Other Mapping Entity is: 0"); return 0; }
	 */
	public static String getContext(String candidateEntity) {
		String contextString = "";
		Jedis jedis = new Jedis("localhost");
		try {
			JSONObject json = new JSONObject(
					readURL("https://en.wikipedia.org/w/api.php?action=query&list=search&format=json&srsearch="
							+ URLEncoder.encode(candidateEntity, "UTF-8")
							+ "&srwhat=text&srprop=snippet&srlimit=50&generator=revisions"));
			json = json.getJSONObject("query");
			JSONArray data = json.getJSONArray("search");
			// candidateEntity = candidateEntity.replace("_", " ");
			if (data != null) {
				for (int i = 0; i < data.length(); i++) {
					json = data.getJSONObject(i);
					if (json != null) {
						String title = json.getString("title");
						if (title.equals(candidateEntity)) {
							contextString = json.getString("snippet").replace(
									"<span class=\"searchmatch\">", "");
							contextString = contextString
									.replace("</span>", "").replaceAll(
											"[^A-Za-z0-9 ]", "");
							/*
							 * for (String sub : contextString.split(" ")) { if
							 * (jedis.lrange(sub, 0, -1).size() == 0) {
							 * contextString.replaceAll(sub, ""); } }
							 */
							contextString = contextString.trim();
							// System.out.println("\n" + contextString);
							break;
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		jedis.close();
		return contextString;
	}

	public static HashMap<String, Integer> countWordsInTheBag(
			String contextString) {
		HashMap<String, Integer> wordsCount = new HashMap<String, Integer>();
		for (String str : contextString.split(" ")) {
			if (!isStopWord(str)) {
				if (wordsCount.get(str) == null) {
					wordsCount.put(str, 1);
				} else {
					wordsCount.put(str, wordsCount.get(str) + 1);
				}
			}
		}
		return wordsCount;
	}

	private static boolean isStopWord(String str) {
		// TODO Auto-generated method stub
		String csvFile = "stopwords.txt";
		BufferedReader br = null;
		List<String> stopWords = new ArrayList<String>();
		String line = "";
		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				stopWords.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return stopWords.contains(str);
	}

	public static Double calculateContextSimilarity(String candidate,
			String tweet) {
		candidate = candidate.replace("_", " ");
		HashMap<String, Integer> wordsOfMentionedEn = countWordsInTheBag(tweet);
		// findBagWordsCandidate(mentionedEntity);
		HashMap<String, Integer> wordsOfCandidate = countWordsInTheBag(getContext(candidate));
		Set<String> combineEn = new TreeSet<String>();
		combineEn.addAll(wordsOfMentionedEn.keySet());
		combineEn.addAll(wordsOfCandidate.keySet());

		// List<ArrayList> counts = new ArrayList<ArrayList>();
		ArrayList<Integer> count1 = new ArrayList<Integer>();
		ArrayList<Integer> count2 = new ArrayList<Integer>();
		for (String word : combineEn) {
			int temp1 = wordsOfMentionedEn.get(word) != null ? wordsOfMentionedEn
					.get(word) : 0;
			int temp2 = wordsOfCandidate.get(word) != null ? wordsOfCandidate
					.get(word) : 0;
			count1.add(temp1);
			count2.add(temp2);
		}
		Double magnitude1 = 0.0;
		for (Integer value : count1) {
			magnitude1 += value * value;
		}
		magnitude1 = Math.sqrt(magnitude1);
		// System.out.println("Magnitude1 : " + magnitude1);

		Double magnitude2 = 0.0;
		for (Integer value : count2) {
			magnitude2 += value * value;
		}
		magnitude2 = Math.sqrt(magnitude2);
		// System.out.println("Magnitude2 : " + magnitude2);

		Double multipleMagnitude = 0.0;
		for (int i = 0; i < count1.size(); i++) {
			multipleMagnitude += count1.get(i) * count2.get(i);
		}
		// System.out.println("MultipleMagnitude: " +
		// multipleMagnitude);
		Double cosin = multipleMagnitude / (magnitude1 * magnitude2);
		System.out
				.println("Cosin Similarity between the tweet & the context of candidate: "
						+ candidate + " is: " + cosin);
		return cosin;
	}

	public static String readURL(String urlString) throws IOException {
		BufferedReader reader = null;
		try {
			URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);

			return buffer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}

	public static List<String> interativeSubstitution(List<Entity> entities) {

		List<String> maxPriorProbs = new ArrayList<String>();
		for (int i = 0; i < entities.size(); i++) {
			double maxTemp = 0;
			String catchedCan = null;
			for (String can : entities.get(i).getCandidates()) {
				double canPriorProb = calculatePriorProbability(can,
						entities.get(i));
				if (maxTemp < canPriorProb) {
					maxTemp = canPriorProb;
					catchedCan = can;
				}
			}
			if (catchedCan != null) {
				maxPriorProbs.add(catchedCan);
			}
		}
		// int iter = 1;
		double maxLQ = 0;
		String catchedCandiate = "";
		int catchedIndex = -1;

		while (true) {
			double increLQ = 0;
			for (int i = 0; i < entities.size(); i++) {
				List<String> candidates = entities.get(i).getCandidates();
				ArrayList<String> temp = new ArrayList<String>();
				for (int k = 0; k < maxPriorProbs.size(); k++) {
					if (k != i) {
						temp.add(maxPriorProbs.get(k));
					}
				}
				for (int j = 0; j < candidates.size(); j++) {
					if (!maxPriorProbs.get(i).equals(candidates.get(j))) {
						temp.add(candidates.get(j));
						increLQ = calculateLinkingQuanlity(temp,
								entities.get(i))
								- calculateLinkingQuanlity(maxPriorProbs,
										entities.get(i));
						if (maxLQ < increLQ) {
							maxLQ = increLQ;
							catchedCandiate = candidates.get(j);
							catchedIndex = i;
						}
						temp.remove(candidates.get(j));
					}

				}

			}
			if (increLQ > 0) {
				maxPriorProbs.set(catchedIndex, catchedCandiate);
				// iter++;
			} else {
				break;
			}
		}
		return maxPriorProbs;
	}

	private static double calculateLinkingQuanlity(List<String> candidates,
			Entity e) {
		// TODO Auto-generated method stub
		double sumPriorProb = 0.0;
		double sumTR = 0.0;
		// ArrayList<String> candidates = new ArrayList<String>();
		// candidates.addAll(values);
		for (int i = 0; i < candidates.size(); i++) {
			sumPriorProb += calculatePriorProbability(candidates.get(i),
					e.getCandidates());
		}
		sumPriorProb = sumPriorProb * alpha;
		for (int i = 0; i < candidates.size(); i++) {
			for (int j = 0; j < candidates.size(); j++) {
				if (i != j) {
					sumTR += calculateTopicalRelatedness(
							getListOfHyperLinks(candidates.get(i)),
							getListOfHyperLinks(candidates.get(j)));
				}
			}
		}
		sumTR = sumTR * (1 - alpha) / (candidates.size() - 1);
		return sumPriorProb + sumTR;
	}

	public static double calculateTopicalCoherence(String can1, int i,
			List<String> possibleEntities) {
		double result = 0.0;
		for (int k = 0; k < possibleEntities.size(); k++) {
			if (k != i) {
				result += calculateTopicalRelatedness(
						getListOfHyperLinks(can1),
						getListOfHyperLinks(possibleEntities.get(k)));
			}
		}
		result = result / (possibleEntities.size() - 1);
		System.out.println("Topical Coherence of candidate: " + can1
				+ " and possible entity is: " + result);

		return result;
	}
}
