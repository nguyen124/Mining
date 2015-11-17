package mining.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mining.entity.Entity;
import redis.clients.jedis.Jedis;

public class EntityService {
	public static int COUNT_ALL_WP = 3381716;

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
		int minPagelinksSize;
		int maxPagelinksSize;
		int intersectPagelinksSize;
		double topicalRelation = 0;
		Set<String> intersect = new HashSet<String>();
		intersect.addAll(entity1Pagelinks);
		minPagelinksSize = Math.min(entity1Pagelinks.size(),
				entity2Pagelinks.size());
		maxPagelinksSize = Math.max(entity1Pagelinks.size(),
				entity2Pagelinks.size());

		intersect.retainAll(entity2Pagelinks);
		intersectPagelinksSize = intersect.size();
		if (intersectPagelinksSize != 0) {
			topicalRelation = 1
					- (Math.log(maxPagelinksSize) - Math
							.log(intersectPagelinksSize))
					/ (Math.log(COUNT_ALL_WP) - Math.log(minPagelinksSize));
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
		List<String> lnks = jedis.lrange("L:" + can, 0, -1);
		jedis.close();
		return lnks;
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

		double pp = 0;
		int countOfLinksToChild = countListOfLinksTo(child);
		if (countOfLinksToChild == 0) {
			return 0;
		}
		double countParentLinksToTotal = 0;
		for (String name : parent.getCandidates()) {
			int countOfParentLinks = countListOfLinksTo(name);
			countParentLinksToTotal += countOfParentLinks;
		}
		pp = countOfLinksToChild / countParentLinksToTotal;
		System.out.println("Prior Probability between child: " + child
				+ " & parent: " + parent.getTitle() + " is: " + pp);
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

	public static double calculateTopicalCoherence(String can,
			List<Entity> sources, int j) {
		int size = sources.size();
		double sumTR = 0;
		if (size > 1) {
			for (int i = 0; i < size; i++) {
				if (i != j) {
					for (String can2 : sources.get(j).getCandidates()) {
						sumTR += calculateTopicalRelatedness(
								getListOfHyperLinks(can),
								getListOfHyperLinks(can2));
					}
				}
			}
			System.out.println("Topical Coh between candidate: " + can
					+ " & Other Mapping Entity: " + (sumTR / (size - 1)));
			return (sumTR / (size - 1));
		}
		System.out.println("Topical Coh between candidate: " + can
				+ " & Other Mapping Entity is: 0");
		return 0;
	}
}
