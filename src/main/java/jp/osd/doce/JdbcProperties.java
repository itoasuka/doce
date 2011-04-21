/*
 * 作成日 : 2011/04/20
 */
package jp.osd.doce;

/**
 * @author asuka
 */
public class JdbcProperties extends DomaProperties {
	private static final long serialVersionUID = 65990558787141534L;
	public static final String JDBC_URL = "JDBC.url";
	public static final String JDBC_USERNAME = "JDBC.username";
	public static final String JDBC_PASSWORD = "JDBC.password";
	public static final String JDBC_DRIVER_CLASS_NAME = "JDBC.driverClassName";
	public static final String JDBC_LOGIN_TIMEOUT = "JDBC.loginTimeout";

	public JdbcProperties(String url, String username) {
		this(url, username, null);
	}

	public JdbcProperties(String url, String username, String password) {
		setJdbcUrl(url);
		setJdbcUsername(username);
		setJdbcPassword(password);
	}

	public void setJdbcUrl(String jdbcUrl) {
		setString(JDBC_URL, jdbcUrl);
	}

	public String getJdbcUrl() {
		return getProperty(JDBC_URL);
	}

	public void setJdbcUsername(String jdbcUsername) {
		setString(JDBC_USERNAME, jdbcUsername);
	}

	public String getJdbcUsername() {
		return getProperty(JDBC_USERNAME);
	}

	public void setJdbcPassword(String password) {
		setString(JDBC_PASSWORD, password);
	}

	public String getJdbcPassword() {
		return getProperty(JDBC_PASSWORD);
	}

}
