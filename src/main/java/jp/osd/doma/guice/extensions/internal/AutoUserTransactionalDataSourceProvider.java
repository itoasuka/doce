/*
 * 作成日 : 2011/04/15
 */
package jp.osd.doma.guice.extensions.internal;

import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import jp.osd.doma.extensions.jdbc.tx.AutoUserTransactionalDataSource;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * {@link AutoUserTransactionalDataSource} オブジェクトを提供する Guice プロバイダです。
 * @author asuka
 */
public class AutoUserTransactionalDataSourceProvider implements
		Provider<DataSource> {
	private final DataSource dataSource;
	private final UserTransaction tx;

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param dataSource ラップするデータソース
	 * @param tx JTA のユーザトランザクション
	 */
	@Inject
	private AutoUserTransactionalDataSourceProvider(DataSource dataSource,
			UserTransaction tx) {
		this.dataSource = dataSource;
		this.tx = tx;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource get() {
		return new AutoUserTransactionalDataSource(dataSource, tx);
	}
}
