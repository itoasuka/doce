/*
 * 作成日 : 2011/04/15
 */
package jp.osd.doma.guice.extensions;

import javax.sql.DataSource;

import jp.osd.doma.extensions.jdbc.tx.AutoUserTransactionalDataSource;
import jp.osd.doma.guice.DomaDataSourceName;
import jp.osd.doma.guice.extensions.internal.AutoUserTransactionalDataSourceProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * @author asuka
 */
public class AutoUserTransactionalDataSourceModule extends AbstractModule {
	private static final String DOMA_DATA_SOURCE_NAME = "AutoUserTx";

	private final Module dataSourceModule;

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param dataSourceModule
	 *            {@link AutoUserTransactionalDataSource} でラップするデータソースを定義している
	 *            Guice モジュール
	 */
	public AutoUserTransactionalDataSourceModule(Module dataSourceModule) {
		this.dataSourceModule = dataSourceModule;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configure() {
		install(dataSourceModule);

		Named named = Names.named(DOMA_DATA_SOURCE_NAME);
		bind(DataSource.class).annotatedWith(named).toProvider(
				AutoUserTransactionalDataSourceProvider.class);
		bindConstant().annotatedWith(DomaDataSourceName.class).to(
				DOMA_DATA_SOURCE_NAME);
	}


}
