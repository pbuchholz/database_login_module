package security;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Factory which creates {@link Connection}s.
 * 
 * @author Philipp Buchholz
 */
public class DatabaseConnectionFactory {

	/**
	 * Creates a {@link Connection} and returns it.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection createConnection() throws SQLException {
		try {
			String databaseUrl = ApplicationConfiguration.INSTANCE.configurationValueByKey("database.url");
			return DriverManager.getConnection(databaseUrl);
		} catch (IOException e) {
			throw new SQLException(e);
		}
	}

}
