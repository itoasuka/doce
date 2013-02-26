/*
 * 作成日 : 2011/04/17
 */
package jp.osd.doce.internal.provider;

import static jp.osd.doce.JdbcProperties.JDBC_DRIVER_CLASS_NAME;
import static jp.osd.doce.JdbcProperties.JDBC_PASSWORD;
import static jp.osd.doce.JdbcProperties.JDBC_URL;
import static jp.osd.doce.JdbcProperties.JDBC_USERNAME;

import javax.sql.DataSource;

import jp.osd.doce.TransactionBinding;
import jp.osd.doce.internal.DbNamedProperties;
import jp.osd.doce.internal.JdbcUtils;
import jp.osd.doce.internal.logging.Logger;
import jp.osd.doce.internal.logging.LoggerFactory;
import jp.osd.doce.internal.logging.MessageCodes;

import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

import com.google.inject.Provider;
import com.jolbox.bonecp.BoneCPDataSource;

/**
 * @author asuka
 */
public class BoneCPDataSourceProvider implements Provider<DataSource> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BoneCPDataSourceProvider.class);
	private final String dbName;
	private final BoneCPDataSource dataSource = new BoneCPDataSource();
	private final String url;
	private final TransactionBinding transactionBinding;
	private String driverClassName;

	/**
	 * 新たにオブジェクトを構築します。
	 * 
	 * @param properties
	 *            データベース名付き設定プロパティ
	 */
	public BoneCPDataSourceProvider(DbNamedProperties properties) {
		LOGGER.logConstructor(DbNamedProperties.class);
		dbName = properties.getDbName();
		url = properties.getString(JDBC_URL);
		LOGGER.debug(MessageCodes.DG002, JDBC_URL, url);
		String username = properties.getString(JDBC_USERNAME);
		LOGGER.debug(MessageCodes.DG002, JDBC_USERNAME, username);
		transactionBinding = properties.getTransactionBinding();
		dataSource.setJdbcUrl(url);
		dataSource.setUsername(username);

		// 自動コミットはさせない
		dataSource.setDefaultAutoCommit(false);

		// パスワード
		if (properties.containsKey(JDBC_PASSWORD)) {
			String password = properties.getString(JDBC_PASSWORD);
			LOGGER.debug(MessageCodes.DG002, JDBC_PASSWORD, password);
			dataSource.setPassword(password);
		}

		// JDBC ドライバのクラス名
		if (properties.containsKey(JDBC_DRIVER_CLASS_NAME)) {
			driverClassName = properties.getString(JDBC_DRIVER_CLASS_NAME);
			LOGGER.debug(MessageCodes.DG002, JDBC_DRIVER_CLASS_NAME,
					driverClassName);
			dataSource.setDriverClass(driverClassName);
		}

		// データソースのパラメータ
		if (properties.containsKey("BoneCP.defaultAutoCommit")) {
			boolean defaultAutoCommit = properties
					.getBoolean("BoneCP.defaultAutoCommit");
			LOGGER.debug(MessageCodes.DG002, "BoneCP.defaultAutoCommit",
					defaultAutoCommit);
			dataSource.setDefaultAutoCommit(defaultAutoCommit);
		}
		if (properties.containsKey("BoneCP.defaultReadOnly")) {
			boolean defaultReadOnly = properties
					.getBoolean("BoneCP.defaultReadOnly");
			LOGGER.debug(MessageCodes.DG002, "BoneCP.defaultReadOnly",
					defaultReadOnly);
			dataSource.setDefaultReadOnly(defaultReadOnly);
		}
		if (properties.containsKey("BoneCP.defaultTransactionIsolation")) {
			String defaultTransactionIsolation = properties
					.getString("BoneCP.defaultReadOnly");
			LOGGER.debug(MessageCodes.DG002,
					"BoneCP.defaultTransactionIsolation",
					defaultTransactionIsolation);
			dataSource
					.setDefaultTransactionIsolation(defaultTransactionIsolation);
		}
		if (properties.containsKey("BoneCP.defaultCatalog")) {
			String defaultCatalog = properties
					.getString("BoneCP.defaultCatalog");
			LOGGER.debug(MessageCodes.DG002, "BoneCP.defaultCatalog",
					defaultCatalog);
			dataSource.setDefaultCatalog(defaultCatalog);
		}
		if (properties.containsKey("BoneCP.partitionCount")) {
			int partitionCount = properties.getInt("BoneCP.partitionCount", 0);
			LOGGER.debug(MessageCodes.DG002, "BoneCP.partitionCount",
					partitionCount);
			dataSource.setPartitionCount(partitionCount);
		}
		if (properties.containsKey("BoneCP.maxConnectionsPerPartition")) {
			int maxConnectionsPerPartition = properties.getInt(
					"BoneCP.maxConnectionsPerPartition", 0);
			LOGGER.debug(MessageCodes.DG002,
					"BoneCP.maxConnectionsPerPartition",
					maxConnectionsPerPartition);
			dataSource
					.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
		} else {
			// 何も設定しないと警告がでるのでデフォルト値を設定
			dataSource.setMaxConnectionsPerPartition(20);
		}
		if (properties.containsKey("BoneCP.minConnectionsPerPartition")) {
			int minConnectionsPerPartition = properties.getInt(
					"BoneCP.minConnectionsPerPartition", 0);
			LOGGER.debug(MessageCodes.DG002,
					"BoneCP.minConnectionsPerPartition",
					minConnectionsPerPartition);
			dataSource
					.setMinConnectionsPerPartition(minConnectionsPerPartition);
		}
		if (properties.containsKey("BoneCP.acquireIncrement")) {
			int acquireIncrement = properties.getInt("BoneCP.acquireIncrement",
					0);
			LOGGER.debug(MessageCodes.DG002, "BoneCP.acquireIncrement",
					acquireIncrement);
			dataSource.setAcquireIncrement(acquireIncrement);
		}
		if (properties.containsKey("BoneCP.acquireRetryAttempts")) {
			int acquireRetryAttempts = properties.getInt(
					"BoneCP.acquireRetryAttempts", 0);
			LOGGER.debug(MessageCodes.DG002, "BoneCP.acquireRetryAttempts",
					acquireRetryAttempts);
			dataSource.setAcquireRetryAttempts(acquireRetryAttempts);
		}
		if (properties.containsKey("BoneCP.acquireRetryDelayInMs")) {
			int acquireRetryDelayInMs = properties.getInt(
					"BoneCP.acquireRetryDelayInMs", 0);
			LOGGER.debug(MessageCodes.DG002, "BoneCP.acquireRetryDelayInMs",
					acquireRetryDelayInMs);
			dataSource.setAcquireRetryDelayInMs(acquireRetryDelayInMs);
		}
		if (properties.containsKey("BoneCP.connectionTimeoutInMs")) {
			int connectionTimeoutInMs = properties.getInt(
					"BoneCP.connectionTimeoutInMs", 0);
			LOGGER.debug(MessageCodes.DG002, "BoneCP.connectionTimeoutInMs",
					connectionTimeoutInMs);
			dataSource.setConnectionTimeoutInMs(connectionTimeoutInMs);
		}
		if (properties.containsKey("BoneCP.idleMaxAgeInMinutes")) {
			int idleMaxAgeInMinutes = properties.getInt(
					"BoneCP.idleMaxAgeInMinutes", 0);
			LOGGER.debug(MessageCodes.DG002, "BoneCP.idleMaxAgeInMinutes",
					idleMaxAgeInMinutes);
			dataSource.setIdleMaxAgeInMinutes(idleMaxAgeInMinutes);
		}
		if (properties.containsKey("BoneCP.initSQL")) {
			String initSQL = properties.getString("BoneCP.initSQL");
			LOGGER.debug(MessageCodes.DG002, "BoneCP.initSQL", initSQL);
			dataSource.setInitSQL(initSQL);
		}
		if (properties.containsKey("BoneCP.idleConnectionTestPeriodInSeconds")) {
			int idleConnectionTestPeriodInSeconds = properties.getInt(
					"BoneCP.idleConnectionTestPeriodInSeconds", 0);
			LOGGER.debug(MessageCodes.DG002, "BoneCP.idleConnectionTestPeriodInSeconds",
					idleConnectionTestPeriodInSeconds);
			dataSource
					.setIdleConnectionTestPeriodInSeconds(idleConnectionTestPeriodInSeconds);
		}
		if (properties.containsKey("BoneCP.connectionTestStatement")) {
			String connectionTestStatement = properties
					.getString("BoneCP.connectionTestStatement");
			LOGGER.debug(MessageCodes.DG002, "BoneCP.connectionTestStatement",
					connectionTestStatement);
			dataSource.setConnectionTestStatement(connectionTestStatement);
		}
		if (properties.containsKey("BoneCP.logStatementsEnabled")) {
			boolean logStatementsEnabled = properties
					.getBoolean("BoneCP.logStatementsEnabled");
			LOGGER.debug(MessageCodes.DG002, "BoneCP.logStatementsEnabled",
					logStatementsEnabled);
			dataSource.setLogStatementsEnabled(logStatementsEnabled);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource get() {
		String dcn;
		if (driverClassName == null) {
			dcn = JdbcUtils.getDriverClassName(url);
		} else {
			dcn = driverClassName;
		}
		try {
			Class.forName(dcn);
		} catch (ClassNotFoundException e) {
			LOGGER.error(e, MessageCodes.DG023, dbName, dcn);
		}
		LOGGER.info(MessageCodes.DG010, dbName, BoneCPDataSource.class);

		// トランザクション実装を使用しない場合はそのまま返す
		if (transactionBinding == TransactionBinding.NONE) {
			return dataSource;
		}

		return new LocalTransactionalDataSource(dataSource);
	}
}
