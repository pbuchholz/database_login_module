package security;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

/**
 * Official documentations:
 * 
 * <ul>
 * <li>JAAS developer guide:
 * https://docs.oracle.com/javase/7/docs/technotes/guides/security/jaas/JAASLMDevGuide.html</li>
 * <li></li>
 * </ul>
 * 
 * This is an example implementation of a JAAS {@link LoginModule} which uses a
 * backing database to authenticate a {@link Subject}.
 * 
 * @author Philipp Buchholz
 */
public class DatabaseLoginModule implements LoginModule {

	private CallbackHandler loginCallbackHandler;

	private DatabaseConnectionFactory databaseConnectionFactory = new DatabaseConnectionFactory();

	private boolean authenticated;

	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState,
			Map<String, ?> options) {
		this.loginCallbackHandler = callbackHandler;
	}

	/**
	 * {@link Callback} used to hash the password as MD5 hash and transfer it to the
	 * LoginModule.
	 * 
	 * @author Philipp Buchholz
	 */
	public static final class HashCallback implements Callback {

		private String hashedPassword;

		public void hash(String password) throws NoSuchAlgorithmException {
			assert Objects.nonNull(password);

			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(password.getBytes());
			byte[] md5Hash = digest.digest();
			StringBuilder sb = new StringBuilder(2 * md5Hash.length);
			for (byte b : md5Hash) {
				sb.append(String.format("%02x", b & 0xff));
			}
			hashedPassword = sb.toString();
		}
	}

	public static final class UsernameCallback implements Callback {

		private String username;

		public void username(String username) {
			assert Objects.nonNull(username);

			this.username = username;
		}

	}

	@Override
	public boolean login() throws LoginException {
		try {
			/* Receive hashed password through Callback. */
			HashCallback hashCallback = new HashCallback();
			UsernameCallback usernameCallback = new UsernameCallback();
			this.loginCallbackHandler.handle(new Callback[] { hashCallback, usernameCallback });

			try (Connection connection = this.databaseConnectionFactory.createConnection()) {
				/* Query for User... */
				UserDAO userDAO = new UserDAO(connection);
				User user = userDAO.queryForUser(usernameCallback.username);

				/* ... and compare hashes. */
				if (Objects.isNull(user) || !user.getMd5PasswordHash().equals(hashCallback.hashedPassword)) {
					authenticated = false;
				}

				authenticated = true;
			}

			return authenticated;

		} catch (IOException | UnsupportedCallbackException | SQLException e) {
			throw new LoginException(e.getMessage());
		}
	}

	@Override
	public boolean commit() throws LoginException {
		return authenticated;
	}

	@Override
	public boolean abort() throws LoginException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean logout() throws LoginException {
		// TODO Auto-generated method stub
		return false;
	}

}
