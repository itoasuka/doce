/*
 * 作成日 : 2011/04/17
 */
package jp.osd.doma.guice.internal;

import javax.inject.Named;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.google.inject.Inject;

/**
 * @author asuka
 */
public class BasicDataSourceFactoryImpl implements BasicDataSourceFactory {
	private final BasicDataSource dataSource = new BasicDataSource();

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param url
	 *            接続先のデータベースの URL
	 * @param username
	 *            データベースへログインするためのユーザ名
	 * @param password
	 *            データベースへログインするためのパスワード
	 * @see BasicDataSource#setUrl(String)
	 * @see BasicDataSource#setUsername(String)
	 * @see BasicDataSource#setPassword(String)
	 * @see BasicDataSource#setDriverClassName(String)
	 */
	@Inject
	public BasicDataSourceFactoryImpl(@Named("JDBC.url") final String url,
			@Named("JDBC.username") final String username,
			@Named("JDBC.password") final String password) {
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setDriverClassName(JdbcUtils.getDriverClassName(url));

		// 自動コミットはさせない
		dataSource.setDefaultAutoCommit(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource create() {
		return dataSource;
	}

	/**
	 * {@link BasicDataSource} の initialSize プロパティを設定します。
	 *
	 * @param initialSize
	 *            initialSize プロパティの値
	 * @see BasicDataSource#setInitialSize(int)
	 */
	@Inject(optional = true)
	public void setInitialSize(@Named("DBCP.initialSize") int initialSize) {
		dataSource.setInitialSize(initialSize);
	}

	/**
	 * {@link BasicDataSource} の maxActive プロパティを設定します。
	 *
	 * @param maxActive
	 *            maxActive プロパティの値
	 * @see BasicDataSource#setMaxActive(int)
	 */
	@Inject(optional = true)
	public void setMaxActive(@Named("DBCP.maxActive") int maxActive) {
		dataSource.setMaxActive(maxActive);
	}

	/**
	 * {@link BasicDataSource} の maxIdle プロパティを設定します。
	 *
	 * @param maxIdle
	 *            maxIdle プロパティの値
	 * @see BasicDataSource#setMaxIdle(int)
	 */
	@Inject(optional = true)
	public void setMaxIdle(@Named("DBCP.maxIdle") int maxIdle) {
		dataSource.setMaxIdle(maxIdle);
	}

	/**
	 * {@link BasicDataSource} の maxOpenPreparedStatements プロパティを設定します。
	 *
	 * @param maxOpenPreparedStatements
	 *            maxOpenPreparedStatements プロパティの値
	 * @see BasicDataSource#setMaxOpenPreparedStatements(int)
	 */
	@Inject(optional = true)
	public void setMaxOpenPreparedStatements(
			@Named("DBCP.maxOpenPreparedStatements") int maxOpenPreparedStatements) {
		dataSource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
	}

	/**
	 * {@link BasicDataSource} の maxWait プロパティを設定します。
	 *
	 * @param maxWait
	 *            maxWait プロパティの値
	 * @see BasicDataSource#setMaxWait(long)
	 */
	@Inject(optional = true)
	public void setMaxWait(@Named("DBCP.maxWait") long maxWait) {
		dataSource.setMaxWait(maxWait);
	}

	/**
	 * {@link BasicDataSource} の minEvictableIdleTimeMillis プロパティを設定します。
	 *
	 * @param minEvictableIdleTimeMillis
	 *            minEvictableIdleTimeMillis プロパティの値
	 * @see BasicDataSource#setMinEvictableIdleTimeMillis(long)
	 */
	@Inject(optional = true)
	public void setMinEvictableIdleTimeMillis(
			@Named("DBCP.minEvictableIdleTimeMillis") long minEvictableIdleTimeMillis) {
		dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
	}

	/**
	 * {@link BasicDataSource} の minIdle プロパティを設定します。
	 *
	 * @param minIdle
	 *            minIdle プロパティの値
	 * @see BasicDataSource#setMinIdle(int)
	 */
	@Inject(optional = true)
	public void setMinIdle(@Named("DBCP.minIdle") int minIdle) {
		dataSource.setMinIdle(minIdle);
	}

	/**
	 * {@link BasicDataSource} の poolPreparedStatements プロパティを設定します。
	 *
	 * @param poolPreparedStatements
	 *            poolPreparedStatements プロパティの値
	 * @see BasicDataSource#setPoolPreparedStatements(boolean)
	 */
	@Inject(optional = true)
	public void setPoolPreparedStatements(
			@Named("DBCP.poolPreparedStatements") boolean poolPreparedStatements) {
		dataSource.setPoolPreparedStatements(poolPreparedStatements);
	}

}
