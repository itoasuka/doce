/*
 * 作成日 : 2011/04/17
 */
package jp.osd.doce.internal.provider;

import static jp.osd.doce.JdbcProperties.JDBC_DRIVER_CLASS_NAME;
import static jp.osd.doce.JdbcProperties.JDBC_PASSWORD;
import static jp.osd.doce.JdbcProperties.JDBC_URL;
import static jp.osd.doce.JdbcProperties.JDBC_USERNAME;

import javax.sql.DataSource;

import jp.osd.doce.Doma;
import jp.osd.doce.TransactionBinding;
import jp.osd.doce.internal.JdbcUtils;
import jp.osd.doce.internal.logging.Logger;
import jp.osd.doce.internal.logging.LoggerFactory;
import jp.osd.doce.internal.logging.MessageCodes;

import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.jolbox.bonecp.BoneCPDataSource;

/**
 * @author asuka
 */
public class BoneCPDataSourceProvider implements Provider<DataSource> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BoneCPDataSourceProvider.class);
	private final BoneCPDataSource dataSource = new BoneCPDataSource();
	private final String url;
	private final TransactionBinding transactionBinding;
	private String driverClassName;

	/**
	 * 新たにオブジェクトを構築します。
	 * 
	 * @param url
	 *            接続先のデータベースの URL
	 * @param username
	 *            データベースへログインするためのユーザ名
	 * @param transactionBinding
	 *            どのようにトランザクションをバインディングするか
	 * @see BoneCPDataSource#setUrl(String)
	 * @see BoneCPDataSource#setUsername(String)
	 */
	@Inject
	public BoneCPDataSourceProvider(@Named(JDBC_URL) final String url,
			@Named(JDBC_USERNAME) final String username,
			@Doma TransactionBinding transactionBinding) {
		LOGGER.logConstructor(String.class, String.class, String.class);
		LOGGER.debug(MessageCodes.DG002, JDBC_URL, url);
		LOGGER.debug(MessageCodes.DG002, JDBC_USERNAME, username);
		this.url = url;
		this.transactionBinding = transactionBinding;
		dataSource.setJdbcUrl(url);
		dataSource.setUsername(username);

		// 自動コミットはさせない
		dataSource.setDefaultAutoCommit(false);
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
			LOGGER.error(e, MessageCodes.DG023, dcn);
		}
		LOGGER.info(MessageCodes.DG010, BoneCPDataSource.class);

		// トランザクション実装を使用しない場合はそのまま返す
		if (transactionBinding == TransactionBinding.NONE) {
			return dataSource;
		}

		return new LocalTransactionalDataSource(dataSource);
	}

	/**
	 * {@link BoneCPDataSource} の password プロパティを設定します。
	 * 
	 * @param password
	 *            password プロパティの値
	 * @see BoneCPDataSource#setPassword(String)
	 */
	@Inject(optional = true)
	public void setPassword(@Named(JDBC_PASSWORD) String password) {
		LOGGER.debug(MessageCodes.DG002, JDBC_PASSWORD, password);
		dataSource.setPassword(password);
	}

	/**
	 * {@link BoneCPDataSource} の defaultAutoCommit プロパティを設定します。
	 * 
	 * @param defaultAutoCommit
	 *            defaultAutoCommit プロパティの値
	 * @see BoneCPDataSource#setDefaultAutoCommit(boolean)
	 */
	@Inject(optional = true)
	public void setDefaultAutoCommit(
			@Named("BoneCP.defaultAutoCommit") Boolean defaultAutoCommit) {
		LOGGER.debug(MessageCodes.DG002, "BoneCP.defaultAutoCommit",
				defaultAutoCommit);
		dataSource.setDefaultAutoCommit(defaultAutoCommit);
	}

	/**
	 * {@link BoneCPDataSource} の defaultReadOnly プロパティを設定します。
	 * 
	 * @param defaultReadOnly
	 *            defaultAutoCommit プロパティの値
	 * @see BoneCPDataSource#setDefaultReadOnly(boolean)
	 */
	@Inject(optional = true)
	public void setDefaultReadOnly(
			@Named("BoneCP.defaultReadOnly") Boolean defaultReadOnly) {
		LOGGER.debug(MessageCodes.DG002, "BoneCP.defaultReadOnly",
				defaultReadOnly);
		dataSource.setDefaultReadOnly(defaultReadOnly);
	}

	/**
	 * {@link BoneCPDataSource} の defaultTransactionIsolation プロパティを設定します。
	 * 
	 * @param defaultTransactionIsolation
	 *            defaultTransactionIsolation プロパティの値
	 * @see BoneCPDataSource#setDefaultTransactionIsolation(int)
	 */
	@Inject(optional = true)
	public void setDefaultTransactionIsolation(
			@Named("BoneCP.defaultTransactionIsolation") String defaultTransactionIsolation) {
		LOGGER.debug(MessageCodes.DG002, "BoneCP.defaultTransactionIsolation",
				defaultTransactionIsolation);
		dataSource.setDefaultTransactionIsolation(defaultTransactionIsolation);
	}

	/**
	 * {@link BoneCPDataSource} の defaultCatalog プロパティを設定します。
	 * 
	 * @param defaultCatalog
	 *            defaultCatalog プロパティの値
	 * @see BoneCPDataSource#setDefaultCatalog(String)
	 */
	@Inject(optional = true)
	public void setDefaultCatalog(
			@Named("BoneCP.defaultCatalog") String defaultCatalog) {
		LOGGER.debug(MessageCodes.DG002, "BoneCP.defaultCatalog", defaultCatalog);
		dataSource.setDefaultCatalog(defaultCatalog);
	}

	/**
	 * {@link BoneCPDataSource} の partitionCount プロパティを設定します。
	 * 
	 * @param partitionCount
	 *            partitionCount プロパティの値
	 * @see BoneCPDataSource#setPartitionCount(int)
	 */
	@Inject(optional = true)
	public void setPartitionCount(@Named("BoneCP.initialSize") int partitionCount) {
		LOGGER.debug(MessageCodes.DG002, "BoneCP.initialSize", partitionCount);
		dataSource.setPartitionCount(partitionCount);
	}

	/**
	 * {@link BoneCPDataSource} の maxConnectionsPerPartition プロパティを設定します。
	 * 
	 * @param maxConnectionsPerPartition
	 *            maxConnectionsPerPartition プロパティの値
	 * @see BoneCPDataSource#setMaxIdle(int)
	 */
	@Inject(optional = true)
	public void setMaxConnectionsPerPartition(
			@Named("BoneCP.maxConnectionsPerPartition") int maxConnectionsPerPartition) {
		LOGGER.debug(MessageCodes.DG002, "BoneCP.maxConnectionsPerPartition",
				maxConnectionsPerPartition);
		dataSource.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
	}

	/**
	 * {@link BoneCPDataSource} の minConnectionsPerPartition プロパティを設定します。
	 * 
	 * @param minConnectionsPerPartition
	 *            minConnectionsPerPartition プロパティの値
	 * @see BoneCPDataSource#setMinConnectionsPerPartition(int)
	 */
	@Inject(optional = true)
	public void setMinConnectionsPerPartition(
			@Named("BoneCP.minConnectionsPerPartition") int minConnectionsPerPartition) {
		LOGGER.debug(MessageCodes.DG002, "BoneCP.minConnectionsPerPartition",
				minConnectionsPerPartition);
		dataSource.setMinConnectionsPerPartition(minConnectionsPerPartition);
	}

	/**
	 * {@link BoneCPDataSource} の acquireIncrement プロパティを設定します。
	 * 
	 * @param acquireIncrement
	 *            acquireIncrement プロパティの値
	 * @see BoneCPDataSource#setAcquireIncrement(int)
	 */
	@Inject(optional = true)
	public void setAcquireIncrement(
			@Named("BoneCP.acquireIncrement") int acquireIncrement) {
		LOGGER.debug(MessageCodes.DG002, "BoneCP.acquireIncrement",
				acquireIncrement);
		dataSource.setAcquireIncrement(acquireIncrement);
	}

	/**
	 * {@link BoneCPDataSource} の minEvictableIdleTimeMillis プロパティを設定します。
	 * 
	 * @param acquireRetryAttempts
	 *            acquireRetryAttempts プロパティの値
	 * @see BoneCPDataSource#setAcquireRetryAttempts(int)
	 */
	@Inject(optional = true)
	public void setAcquireRetryAttempts(
			@Named("BoneCP.acquireRetryAttempts") int acquireRetryAttempts) {
		LOGGER.debug(MessageCodes.DG002, "BoneCP.acquireRetryAttempts",
				acquireRetryAttempts);
		dataSource.setAcquireRetryAttempts(acquireRetryAttempts);
	}

	/**
	 * {@link BoneCPDataSource} の acquireRetryDelayInMs プロパティを設定します。
	 * 
	 * @param acquireRetryDelayInMs
	 *            acquireRetryDelayInMs プロパティの値
	 * @see BoneCPDataSource#setAcquireRetryDelayInMs(long)
	 */
	@Inject(optional = true)
	public void setAcquireRetryDelayInMs(
			@Named("BoneCP.acquireRetryDelayInMs") long acquireRetryDelayInMs) {
		LOGGER.debug(MessageCodes.DG002, "BoneCP.acquireRetryDelayInMs",
				acquireRetryDelayInMs);
		dataSource.setAcquireRetryDelayInMs(acquireRetryDelayInMs);
	}

	/**
	 * {@link BoneCPDataSource} の connectionTimeoutInMs プロパティを設定します。
	 * 
	 * @param connectionTimeoutInMs
	 *            connectionTimeoutInMs プロパティの値
	 * @see BoneCPDataSource#setConnectionTimeoutInMs(long)
	 */
	@Inject(optional = true)
	public void setConnectionTimeoutInMs(
			@Named("BoneCP.connectionTimeoutInMs") long connectionTimeoutInMs) {
		LOGGER.debug(MessageCodes.DG002, "BoneCP.connectionTimeoutInMs",
				connectionTimeoutInMs);
		dataSource.setConnectionTimeoutInMs(connectionTimeoutInMs);
	}

	/**
	 * {@link BoneCPDataSource} の idleMaxAgeInMinutes プロパティを設定します。
	 * 
	 * @param idleMaxAgeInMinutes
	 *            idleMaxAgeInMinutes プロパティの値
	 * @see BoneCPDataSource#setIdleMaxAgeInMinutes(long)
	 */
	@Inject(optional = true)
	public void setIdleMaxAgeInMinutes(
			@Named("BoneCP.connectionTimeoutInMs") long idleMaxAgeInMinutes) {
		LOGGER.debug(MessageCodes.DG002, "BoneCP.idleMaxAgeInMinutes",
				idleMaxAgeInMinutes);
		dataSource.setIdleMaxAgeInMinutes(idleMaxAgeInMinutes);
	}

	/**
	 * {@link BoneCPDataSource} の initSQL プロパティを設定します。
	 * 
	 * @param initSQL
	 *            initSQL プロパティの値
	 * @see BoneCPDataSource#setInitSQL(String)
	 */
	@Inject(optional = true)
	public void setInitSQL(@Named("BoneCP.initSQL") String initSQL) {
		LOGGER.debug(MessageCodes.DG002, "BoneCP.initSQL", initSQL);
		dataSource.setInitSQL(initSQL);
	}

	/**
	 * {@link BoneCPDataSource} の logStatementsEnabled プロパティを設定します。
	 * 
	 * @param logStatementsEnabled
	 *            logStatementsEnabled プロパティの値
	 * @see BoneCPDataSource#setLogStatementsEnabled(boolean)
	 */
	@Inject(optional = true)
	public void setInitSQL(
			@Named("BoneCP.logStatementsEnabled") boolean logStatementsEnabled) {
		LOGGER.debug(MessageCodes.DG002, "BoneCP.logStatementsEnabled",
				logStatementsEnabled);
		dataSource.setLogStatementsEnabled(logStatementsEnabled);
	}

	/**
	 * {@link BoneCPDataSource} の driverClassName
	 * プロパティを設定します。設定しない場合は、コンストラクタの引数 {@code url} から推測して適用されます。
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
