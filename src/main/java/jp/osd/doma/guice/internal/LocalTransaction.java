package jp.osd.doma.guice.internal;

import javax.inject.Inject;
import javax.sql.DataSource;

import jp.osd.doma.guice.Transaction;

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
	@Inject
	public LocalTransaction(DataSource dataSource,
			JdbcLogger jdbcLogger) {
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
		dataSource.getLocalTransaction(jdbcLogger).commit();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback() {
		dataSource.getLocalTransaction(jdbcLogger).rollback();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isActive() {
		return dataSource.getLocalTransaction(jdbcLogger).isActive();
	}

}
