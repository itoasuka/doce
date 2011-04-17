/*
 * 作成日 : 2011/04/17
 */
package jp.osd.doma.guice.internal;

import jp.osd.doma.guice.DomaGuiceException;

import org.seasar.doma.jdbc.dialect.Db2Dialect;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.H2Dialect;
import org.seasar.doma.jdbc.dialect.HsqldbDialect;
import org.seasar.doma.jdbc.dialect.Mssql2008Dialect;
import org.seasar.doma.jdbc.dialect.MysqlDialect;
import org.seasar.doma.jdbc.dialect.OracleDialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

/**
 * JDBC に関するユーティリティクラスです。
 * @author asuka
 */
public class JdbcUtils {
	/**
	 * データベースへ接続するのに必要なドライバ名を取得します。
	 *
	 * @param jdbcUrl JDBC 接続 URL
	 * @return ドライバ名
	 */
	public static String getDriverClassName(String jdbcUrl) {
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

	public static Dialect getDialect(String jdbcUrl) {
		Dialect dialect;
		if (jdbcUrl.matches("jdbc:((datadirect:)?db2|as400):.*")) {
			dialect = new Db2Dialect();
		} else if (jdbcUrl.startsWith("jdbc:h2:")) {
			dialect = new H2Dialect();
		} else if (jdbcUrl.startsWith("jdbc:hsqldb:")) {
			dialect = new HsqldbDialect();
		} else if (jdbcUrl
				.matches("jdbc:((datadirect|jtds|microsoft):)?sqlserver:.*")) {
			dialect = new Mssql2008Dialect();
		} else if (jdbcUrl.startsWith("jdbc:mysql:")) {
			dialect = new MysqlDialect();
		} else if (jdbcUrl.matches("jdbc:(datadirect:)?oracle:.*")) {
			dialect = new OracleDialect();
		} else if (jdbcUrl.startsWith("jdbc:postgresql:")) {
			dialect = new PostgresDialect();
		} else {
			dialect = new StandardDialect();
		}

		return dialect;
	}
}
