package jp.osd.doma.guice.internal;

import javax.inject.Named;
import javax.inject.Singleton;

import jp.osd.doma.guice.DomaGuiceException;

import com.google.inject.Inject;

/**
 * 設定に関するヘルパの実装クラスです。
 *
 * @author asuka
 */
@Singleton
public class SettingHelperImpl implements SettingHelper {
	@Inject(optional = true)
	@Named("DBCP.driver")
	private String dbcpDriver = "";

	private final String jdbcUrl;

	/**
	 * 新たにオブジェクトを構築します。
	 * 
	 * @param jdbcUrl 接続先のデータベースの URL
	 */
	@Inject
	public SettingHelperImpl(@Named("JDBC.url") String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDriverClassName() {
		if (0 < dbcpDriver.length()) {
			// 明示的にドライバ名が指定されている場合はそれを使用
			return dbcpDriver;
		}
		String driver;
		if (jdbcUrl.startsWith("jdbc:db2:")) {
			driver = "com.ibm.db2.jcc.DB2Driver";
		} else if (jdbcUrl.startsWith("jdbc:datadirect:db2:")) {
			driver = "com.ddtek.jdbc.db2.DB2Driver";
		} else if (jdbcUrl.startsWith("jdbc:as400:")) {
			driver = "com.ibm.as400.access.AS400JDBCDriver";
		} else if (jdbcUrl.startsWith("jdbc:h2:")) {
			driver = "org.h2.Driver";
		} else if (jdbcUrl.startsWith("jdbc:hsqldb:")) {
			driver = "org.hsqldb.jdbcDriver";
		} else if (jdbcUrl.startsWith("jdbc:datadirect:sqlserver:")) {
			driver = "com.ddtek.jdbc.sqlserver.SQLServerDriver";
		} else if (jdbcUrl.startsWith("jdbc:jtds:sqlserver:")) {
			driver = "net.sourceforge.jtds.jdbc.Driver";
		} else if (jdbcUrl.startsWith("jdbc:microsoft:sqlserver:")) {
			driver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
		} else if (jdbcUrl.startsWith("jdbc:sqlserver:")) {
			driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		} else if (jdbcUrl.startsWith("jdbc:mysql:")) {
			driver = "com.mysql.jdbc.Driver";
		} else if (jdbcUrl.startsWith("jdbc:oracle:")) {
			driver = "oracle.jdbc.OracleDriver";
		} else if (jdbcUrl.startsWith("jdbc:datadirect:oracle:")) {
			driver = "com.ddtek.jdbc.oracle.OracleDriver";
		} else if (jdbcUrl.startsWith("jdbc:postgresql:")) {
			driver = "org.postgresql.Driver";
		} else {
			throw new DomaGuiceException("Unsupported URL : " + jdbcUrl);
		}

		return driver;
	}

}
