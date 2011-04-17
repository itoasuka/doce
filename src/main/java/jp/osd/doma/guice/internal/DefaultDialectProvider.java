package jp.osd.doma.guice.internal;

import javax.inject.Provider;

import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Dialect オブジェクトを提供するプロバイダクラスです。
 * <P>
 * すでに Dialect オブジェクトがバインドされている場合はそれを提供します。
 * <P>
 * そうでない場合、接続先の URL から適切な Dialect を判断し、Dialect オブジェクトを提供します。未知の URL の場合、
 * {@link StandardDialect} のオブジェクト を提供します。提供される Dialect
 * オブジェクトはすべてデフォルトコンストラクタで生成されます。
 *
 * @author asuka
 */
public class DefaultDialectProvider implements Provider<Dialect> {
	private String jdbcUrl = null;

	private Dialect dialect = null;

	/**
	 * 新たにオブジェクトを構築します。
	 */
	public DefaultDialectProvider() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dialect get() {
		if (dialect != null) {
			return dialect;
		}
		if (jdbcUrl == null) {
			return new StandardDialect();
		}
		return JdbcUtils.getDialect(jdbcUrl);
	}

	/**
	 * Dialect を判定するための JDBC 接続 URL を設定します。
	 * @param jdbcUrl
	 *            JDBC 接続 URL
	 */
	@Inject(optional = true)
	public void setJdbcUrl(@Named("JDBC.url") String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	/**
	 * プロバイダとして提供するダイアレクトを設定します。
	 * @param dialect
	 *            ダイアレクト
	 */
	@Inject(optional = true)
	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

}
