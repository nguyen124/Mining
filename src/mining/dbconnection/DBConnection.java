package mining.dbconnection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
	public static Connection createConnection() {
		Connection connection = null;
		try {
			Properties props = new Properties();
			FileInputStream fis = null;
			fis = new FileInputStream("connection.properties");
			props.load(fis);
			Class.forName(props.getProperty("driver"));
			connection = DriverManager.getConnection(props.getProperty("url"),
					props.getProperty("username"),
					props.getProperty("password"));
		} catch (ClassNotFoundException e) {
			System.out.println("Need PostgreSQL JDBC Driver? ");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
}
