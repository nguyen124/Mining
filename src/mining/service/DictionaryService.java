package mining.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mining.dbconnection.DBConnection;
import mining.entity.Entity;

public class DictionaryService {
	public static int COUNT_ALL_WP = 3381716;
	public static int COUNT_PAGELINKS = 81835577;

	public static Entity buildEntity(String en) {
		Entity entity = new Entity(en);
		List<String> candidates = new ArrayList<String>();
		/*
		 * HashSet<Entity> disamNames = new HashSet<Entity>(); HashSet<Entity>
		 * redirectNames = new HashSet<Entity>(); HashSet<Entity> entityNames =
		 * new HashSet<Entity>();
		 */
		// HashSet<Entity> hyperLinks = new HashSet<Entity>();

		Connection conn = DBConnection.createConnection();

		try {
			// get disambiguation names
			String disamQuery = "SELECT disambiguate  FROM public.disambiguation_en where keyword like ? or keyword like ?";
			PreparedStatement ps = conn.prepareStatement(disamQuery);
			en = en.trim().replaceAll(" ", "\\\\_");
			ps.setString(1, en + "\\_%disambiguation%");
			ps.setString(2, en);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				// String key = rs.getString("keyword");
				String temp = rs.getString("disambiguate");
				// disamNames.add(temp);
				candidates.add(temp);
			}
			// entity.setDisambiguationNames(disamNames);

			// get redirect names
			String redirectsQuery = "SELECT keyword, page_name  FROM  public.redirect_en where keyword like ?";
			ps = conn.prepareStatement(redirectsQuery);
			en = en.trim().replaceAll(" ", "\\\\_");
			ps.setString(1, en);
			rs = ps.executeQuery();
			while (rs.next()) {
				// String key = rs.getString("keyword");
				String temp = rs.getString("page_name");
				// redirectNames.add(temp);
				candidates.add(temp);
			}
			// entity.setRedirectNames(redirectNames);

			// get entity page
			String entityQuery = "SELECT page_name FROM  public.wikipage_en Where page_name like ?";
			ps = conn.prepareStatement(entityQuery);
			en = en.trim().replaceAll(" ", "\\\\_");
			ps.setString(1, en);

			rs = ps.executeQuery();
			while (rs.next()) {
				// String key = rs.getString("keyword");
				String temp = rs.getString("page_name");
				// entityNames.add(temp);
				candidates.add(temp);
			}
			// entity.setEntityPages(entityNames);

			// get hyper link page
			/*
			 * String hyperLink =
			 * "SELECT pagelink FROM  public.pagelinks_en Where keyword like ?";
			 * ps = conn.prepareStatement(hyperLink); ps.setString(1, en); rs =
			 * ps.executeQuery(); while (rs.next()) { // String key =
			 * rs.getString("keyword"); Entity temp = new
			 * Entity(rs.getString("pagelink")); hyperLinks.add(temp); }
			 * entity.setHyperLinks(hyperLinks);
			 */
			entity.setCandidates(candidates);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return entity;
	}

	public static double calculateTR(Set<String> entity1Pagelinks,
			Set<String> entity2Pagelinks) {
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

	public static Set<String> getPagelinks(String can1) {
		// TODO Auto-generated method stub
		Connection conn = DBConnection.createConnection();
		String query = "Select page_name from pagelinks_en where keyword like ?";
		Set<String> pagelinks = new HashSet<String>();
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, can1);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				pagelinks.add(result.getString("page_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pagelinks;
	}
}
