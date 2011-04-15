/*
 * 作成日 : 2011/04/15
 */
package jp.osd.doma.guice.extensions;

import javax.sql.DataSource;

import jp.osd.doma.extensions.jdbc.tx.AutoLocalTransactionalDataSource;
import jp.osd.doma.guice.DomaDataSourceName;
import jp.osd.doma.guice.extensions.internal.AutoLocalTransactionalDataSourceProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * {@link AutoLocalTransactionalDataSource} によるデータソースを提供する Guice モジュールです。
 *
 * @author asuka
 */
public class AutoLocalTransactionalDataSourceModule extends AbstractModule {
	private static final String DOMA_DATA_SOURCE_NAME = "AutoLocalTx";

	private final Module dataSourceModule;

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param dataSourceModule
	 *            {@link AutoLocalTransactionalDataSource} でラップするデータソースを定義している
	 *            Guice モジュール
	 */
	public AutoLocalTransactionalDataSourceModule(Module dataSourceModule) {
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
				AutoLocalTransactionalDataSourceProvider.class);
		bindConstant().annotatedWith(DomaDataSourceName.class).to(
				DOMA_DATA_SOURCE_NAME);
	}

}
