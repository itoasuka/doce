package jp.osd.doma.guice;

import javax.sql.DataSource;

import jp.osd.doma.guice.internal.BasicDataSourceProvider;
import jp.osd.doma.guice.internal.LocalTransaction;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * <A HREF="http://commons.apache.org/dbcp/" TARGET="_blank">Common DBCP</A> の
 * {@link org.apache.commons.dbcp.BasicDataSource} によるデータソースを提供する Guice モジュールです。
 * 
 * @author asuka
 */
public class BasicDataSourceModule extends AbstractModule {
	/**
	 * 新たにオブジェクトを構築します。
	 */
	public BasicDataSourceModule() {
		// 何もしない
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configure() {
		bind(DataSource.class).toProvider(BasicDataSourceProvider.class).in(
				Scopes.SINGLETON);
		bind(Transaction.class).to(LocalTransaction.class);
	}
}
