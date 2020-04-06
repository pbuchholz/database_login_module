package security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Simple database access object which allows to query for user information.
 *
 * @author Philipp Buchholz
 *
 */
public class UserDAO {

	public enum UserQueries {
		BY_USERNAME("SELECT username, md5passwordhash FROM jaas.users where username = ?");

		private String statement;

		UserQueries(String statement) {
			this.statement = statement;
		}

	}

	private Connection connection;

	public UserDAO(Connection connection) {
		this.connection = connection;
	}

	public User queryForUser(String username) throws SQLException {
		try (PreparedStatement preparedStatement = this.connection
				.prepareStatement(UserQueries.BY_USERNAME.statement)) {
			preparedStatement.setString(1, username);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.first()) {
					User user = new User();
					user.setUsername(resultSet.getString("username"));
					user.setMd5PasswordHash(resultSet.getString("md5passwordhash"));
					return user;
				}
			}
		}
		return null;
	}

}
