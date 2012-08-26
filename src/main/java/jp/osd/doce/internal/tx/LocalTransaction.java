package jp.osd.doce.internal.tx;

import javax.sql.DataSource;

import jp.osd.doce.Transaction;
import jp.osd.doce.internal.logging.Logger;
import jp.osd.doce.internal.logging.LoggerFactory;
import jp.osd.doce.internal.logging.MessageCodes;

import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

/**
 * Doma の {@link org.seasar.doma.jdbc.tx.LocalTransaction} を用いた
 * {@link Transaction} の実装クラスです。
 * 
 * @author asuka
 */
public class LocalTransaction implements Transaction {
	private final Logger LOGGER = LoggerFactory.getLogger(LocalTransaction.class);
	
	private final String dbName;
	
	private final LocalTransactionalDataSource dataSource;

	private final JdbcLogger jdbcLogger;

	/**
	 * 新たにオブジェクトを構築します。
	 * 
	 * @param dataSource
	 *            データソース
	 * @param jdbcLogger
	 *            JDBC ロガー
	 */
	public LocalTransaction(String dbName, DataSource dataSource, JdbcLogger jdbcLogger) {
		this.dbName = dbName;
		this.dataSource = (LocalTransactionalDataSource) dataSource;
		this.jdbcLogger = jdbcLogger;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void begin() {
		org.seasar.doma.jdbc.tx.LocalTransaction tx = getLocalTransaction();

		if (!tx.isActive()) {
			tx.begin();
			LOGGER.debug(MessageCodes.DG016, dbName);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commit() {
		org.seasar.doma.jdbc.tx.LocalTransaction tx = getLocalTransaction();

		if (tx.isActive()) {
			tx.commit();
			LOGGER.debug(MessageCodes.DG018, dbName);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback() {
		org.seasar.doma.jdbc.tx.LocalTransaction tx = getLocalTransaction();

		if (tx.isActive()) {
			tx.rollback();
			LOGGER.debug(MessageCodes.DG020, dbName);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isActive() {
		return getLocalTransaction().isActive();
	}

	private org.seasar.doma.jdbc.tx.LocalTransaction getLocalTransaction() {
		return dataSource
				.getLocalTransaction(jdbcLogger);
	}
}
