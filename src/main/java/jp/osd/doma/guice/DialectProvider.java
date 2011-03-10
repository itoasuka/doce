package jp.osd.doma.guice;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

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
 * Dialect オブジェクトを提供するプロバイダクラスです。
 * <P>
 * 接続先の URL から適切な Dialect を判断し、Dialect オブジェクトを提供します。未知の URL の場合、
 * {@link StandardDialect} のオブジェクト を提供します。提供される Dialect
 * オブジェクトはすべてデフォルトコンストラクタで生成されます。
 * 
 * @author asuka
 */
public class DialectProvider implements Provider<Dialect> {
	private final String jdbcUrl;

	/**
	 * 新たにオブジェクトを構築します。
	 * 
	 * @param jdbcUrl
	 *            接続先の URL
	 */
	@Inject
	public DialectProvider(@Named("JDBC.url") String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dialect get() {
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
