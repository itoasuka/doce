package jp.osd.doce.internal.tx;

import javax.sql.DataSource;

import jp.osd.doce.Transaction;

import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

/**
 * Doma の {@link org.seasar.doma.jdbc.tx.LocalTransaction} を用いた
 * {@link Transaction} の実装クラスです。
 * 
 * @author asuka
 */
public class LocalTransaction implements Transaction {
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
	public LocalTransaction(DataSource dataSource, JdbcLogger jdbcLogger) {
		this.dataSource = (LocalTransactionalDataSource) dataSource;
		this.jdbcLogger = jdbcLogger;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void begin() {
		dataSource.getLocalTransaction(jdbcLogger).begin();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commit() {
		org.seasar.doma.jdbc.tx.LocalTransaction tx = dataSource
				.getLocalTransaction(jdbcLogger);

		if (tx.isActive()) {
			tx.commit();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback() {
		org.seasar.doma.jdbc.tx.LocalTransaction tx = dataSource
				.getLocalTransaction(jdbcLogger);

		if (tx.isActive()) {
			tx.rollback();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isActive() {
		return dataSource.getLocalTransaction(jdbcLogger).isActive();
	}

}
