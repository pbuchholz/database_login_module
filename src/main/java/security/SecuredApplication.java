package security;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import security.DatabaseLoginModule.HashCallback;
import security.DatabaseLoginModule.UsernameCallback;

/**
 * Sample application secured with JAAS.
 * 
 * @author Philipp Buchholz
 */
public class SecuredApplication {

	private final static Logger LOGGER = LoggerFactory.getLogger(SecuredApplication.class);

	public static void main(String[] args) throws SQLException, LoginException {

		/* Instantiate LoginContext and CallbackHandler. */
		LoginContext loginContext = new LoginContext("SecuredApplication", new CallbackHandler() {

			@Override
			public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
				for (Callback cb : callbacks) {
					if (cb instanceof HashCallback) {
						try {
							((HashCallback) cb).hash("ASUIPbfpier7zt34KL");
						} catch (NoSuchAlgorithmException e) {
							throw new UnsupportedCallbackException(cb, e.getMessage());
						}
					} else if (cb instanceof UsernameCallback) {
						((UsernameCallback) cb).username("pbuchholz");
					}
				}
			}
		});

		/* Perform actual login. */
		loginContext.login();

		Subject authenicatedSubject = loginContext.getSubject();
		LOGGER.info("Authenticated Subject is {}.", authenicatedSubject);
	}

}
