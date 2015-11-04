package mining.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import mining.dbconnection.DBConnection;
import mining.graph.Entity;

public class DictionaryService {

	public static Entity buildEntity(String en) {
		Entity entity = new Entity(en);
		HashSet<Entity> refs = new HashSet<Entity>();

		String query = "SELECT keyword, disambiguate  FROM public.disambiguation_en where keyword like ?";
		try {
			Connection conn = DBConnection.createConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, en + "\\_%disambiguation%");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				// String key = rs.getString("keyword");
				Entity temp = new Entity(rs.getString("disambiguate"));
				refs.add(temp);
			}
			entity.setRefs(refs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entity;
	}

	public static void buildSubEntity(Entity en) {
		Set<Entity> temp = new HashSet<Entity>();
		for (Entity e : en.getRefs()) {
			temp.add(buildEntity(e.getName()));
		}
		en.setRefs(temp);
	}

	public static void buildMap(Entity[] entities) {

		// final String USER_AGENT = "Mozilla/5.0";
		for (Entity entity : entities) {

		}
	}

}
