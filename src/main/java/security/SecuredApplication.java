package security;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import security.DatabaseLoginModule.HashCallback;
import security.DatabaseLoginModule.UsernameCallback;

/**
 * Sample application secured with JAAS.
 * 
 * @author Philipp Buchholz
 */
public class SecuredApplication {

	public static void main(String[] args) throws SQLException, LoginException {

		/* Instantiate LoginContext and CallbackHandler. */
		LoginContext loginContext = new LoginContext("SecuredApplication", new CallbackHandler() {

			@Override
			public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
				for (Callback cb : callbacks) {
					if (cb instanceof HashCallback) {
						try {
							((HashCallback) cb).hash("SecurePassword");
						} catch (NoSuchAlgorithmException e) {
							throw new UnsupportedCallbackException(cb, e.getMessage());
						}
					} else if (cb instanceof UsernameCallback) {
						((UsernameCallback) cb).username("TestUser");
					}
				}
			}
		});

		/* Perform actual login. */
		loginContext.login();

	}

}
