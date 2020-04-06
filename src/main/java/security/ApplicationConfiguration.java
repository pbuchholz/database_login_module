package security;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * Enum singleton which provides access to the configuration of the application.
 * 
 * @author Philipp Buchholz
 */
public enum ApplicationConfiguration {

	INSTANCE;

	private static final String APP_CFG_FILE = "application.properties";
	private Properties properties;

	private void ensureProperties() throws IOException {
		if (Objects.isNull(properties)) {
			properties = new Properties();

			try (InputStream is = ClassLoader.getSystemResourceAsStream(APP_CFG_FILE)) {
				properties.load(is);
			} catch (IOException e) {
				throw e;
			}
		}
	}

	/**
	 * Reads a configuration value by its key.
	 * 
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public String configurationValueByKey(String key) throws IOException {
		this.ensureProperties();
		return this.properties.getProperty(key);
	}

}
