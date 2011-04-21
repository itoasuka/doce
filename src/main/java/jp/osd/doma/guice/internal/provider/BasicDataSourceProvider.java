/*
 * 作成日 : 2011/04/17
 */
package jp.osd.doma.guice.internal.provider;

import static jp.osd.doma.guice.JdbcProperties.JDBC_DRIVER_CLASS_NAME;
import static jp.osd.doma.guice.JdbcProperties.JDBC_PASSWORD;
import static jp.osd.doma.guice.JdbcProperties.JDBC_URL;
import static jp.osd.doma.guice.JdbcProperties.JDBC_USERNAME;

import javax.inject.Named;
import javax.sql.DataSource;

import jp.osd.doma.guice.internal.JdbcUtils;
import jp.osd.doma.guice.internal.logging.Logger;
import jp.osd.doma.guice.internal.logging.LoggerFactory;
import jp.osd.doma.guice.internal.logging.MessageCodes;

import org.apache.commons.dbcp.BasicDataSource;
import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author asuka
 */
public class BasicDataSourceProvider implements Provider<DataSource> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BasicDataSourceProvider.class);
	private final BasicDataSource dataSource = new BasicDataSource();
	private final String url;
	private String driverClassName;

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param url
	 *            接続先のデータベースの URL
	 * @param username
	 *            データベースへログインするためのユーザ名
	 * @see BasicDataSource#setUrl(String)
	 * @see BasicDataSource#setUsername(String)
	 */
	@Inject
	public BasicDataSourceProvider(@Named(JDBC_URL) final String url,
			@Named(JDBC_USERNAME) final String username) {
		LOGGER.logConstructor(String.class, String.class, String.class);
		LOGGER.debug(MessageCodes.DG002, JDBC_URL, url);
		LOGGER.debug(MessageCodes.DG002, JDBC_USERNAME, username);
		this.url = url;
		dataSource.setUrl(url);
		dataSource.setUsername(username);

		// 自動コミットはさせない
		dataSource.setDefaultAutoCommit(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource get() {
		if (driverClassName == null) {
			dataSource.setDriverClassName(JdbcUtils.getDriverClassName(url));
		} else {
			dataSource.setDriverClassName(driverClassName);
		}
		LOGGER.debug(MessageCodes.DG010, BasicDataSource.class);
		return new LocalTransactionalDataSource(dataSource);
	}

	/**
	 * {@link BasicDataSource} の password プロパティを設定します。
	 *
	 * @param password
	 *            password プロパティの値
	 * @see BasicDataSource#setPassword(String)
	 */
	@Inject(optional = true)
	public void setPassword(@Named(JDBC_PASSWORD) String password) {
		LOGGER.debug(MessageCodes.DG002, JDBC_PASSWORD, password);
		dataSource.setPassword(password);
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
		LOGGER.debug(MessageCodes.DG002, "DBCP.initialSize", initialSize);
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
		LOGGER.debug(MessageCodes.DG002, "DBCP.maxActive", maxActive);
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
		LOGGER.debug(MessageCodes.DG002, "DBCP.maxIdle", maxIdle);
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
		LOGGER.debug(MessageCodes.DG002, "DBCP.maxOpenPreparedStatements",
				maxOpenPreparedStatements);
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
		LOGGER.debug(MessageCodes.DG002, "DBCP.maxWait", maxWait);
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
		LOGGER.debug(MessageCodes.DG002, "DBCP.minEvictableIdleTimeMillis",
				minEvictableIdleTimeMillis);
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
		LOGGER.debug(MessageCodes.DG002, "DBCP.minIdle", minIdle);
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
		LOGGER.debug(MessageCodes.DG002, "DBCP.poolPreparedStatements",
				poolPreparedStatements);
		dataSource.setPoolPreparedStatements(poolPreparedStatements);
	}

	/**
	 * {@link BasicDataSource} の driverClassName プロパティを設定します。設定しない場合は、コンストラクタの引数
	 * {@code url} から推測して適用されます。
	 *
	 * @param driverClassName
	 *            driverClassName プロパティの値
	 */
	@Inject(optional = true)
	public void setDriverClassName(
			@Named(JDBC_DRIVER_CLASS_NAME) String driverClassName) {
		LOGGER.debug(MessageCodes.DG002, JDBC_DRIVER_CLASS_NAME,
				driverClassName);
		this.driverClassName = driverClassName;
	}

}
