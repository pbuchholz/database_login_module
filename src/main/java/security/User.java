package security;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, false);
	}

	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(o, this, false);
	}

}
