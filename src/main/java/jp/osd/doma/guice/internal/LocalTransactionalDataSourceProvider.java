package jp.osd.doma.guice.internal;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;

import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

/**
 * {@link LocalTransactionalDataSource} により実装されたデータソースを提供するプロバイダです。
 * @author asuka
 */
public class LocalTransactionalDataSourceProvider implements
		Provider<DataSource> {
	private final DataSource dataSource;

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param dataSource ラップ対象のデータソース
	 */
	@Inject
	public LocalTransactionalDataSourceProvider(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource get() {
		return new LocalTransactionalDataSource(dataSource);
	}

}
