package security;

/**
 * Represents a user.
 * 
 * @author Philipp Buchholz
 */
public class User {

	private String username;
	private String md5PasswordHash;

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMd5PasswordHash() {
		return this.md5PasswordHash;
	}

	public void setMd5PasswordHash(String md5PasswordHash) {
		this.md5PasswordHash = md5PasswordHash;
	}

}
