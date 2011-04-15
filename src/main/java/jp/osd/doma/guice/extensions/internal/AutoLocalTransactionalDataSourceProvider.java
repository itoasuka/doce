/*
 * 作成日 : 2011/04/15
 */
package jp.osd.doma.guice.extensions.internal;

import javax.sql.DataSource;

import jp.osd.doma.extensions.jdbc.tx.AutoLocalTransactionalDataSource;

import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * {@link AutoLocalTransactionalDataSource} オブジェクトを提供する Guice プロバイダです。
 *
 * @author asuka
 */
public class AutoLocalTransactionalDataSourceProvider implements
		Provider<DataSource> {
	private final DataSource dataSource;
	private final JdbcLogger jdbcLogger;

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param dataSource ラップするデータソース
	 * @param jdbcLogger JDBC ロガー
	 */
	@Inject
	public AutoLocalTransactionalDataSourceProvider(
			DataSource dataSource, JdbcLogger jdbcLogger) {
		this.dataSource = dataSource;
		this.jdbcLogger = jdbcLogger;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource get() {
		return new AutoLocalTransactionalDataSource(
				(LocalTransactionalDataSource) dataSource, jdbcLogger);
	}

}
