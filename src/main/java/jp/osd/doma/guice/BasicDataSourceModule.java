package jp.osd.doma.guice;

import javax.sql.DataSource;

import jp.osd.doma.guice.internal.BasicDataSourceProvider;
import jp.osd.doma.guice.internal.LocalTransaction;
import jp.osd.doma.guice.internal.LocalTransactionalDataSourceProvider;
import jp.osd.doma.guice.internal.Plain;

import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * <A HREF="http://commons.apache.org/dbcp/" TARGET="_blank">Common DBCP</A> の
 * {@link org.apache.commons.dbcp.BasicDataSource} によるデータソースを提供する Guice モジュールです。
 * <P>
 * {@link DataSource} には {@link LocalTransactionalDataSource}
 * がバインドされます。このデータソースがラップしている元々のデータソースを取得する場合は、{@link Plain} を付けて取得してください。
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
		bind(DataSource.class).annotatedWith(Plain.class)
				.toProvider(BasicDataSourceProvider.class).in(Scopes.SINGLETON);
		bind(DataSource.class).toProvider(
				LocalTransactionalDataSourceProvider.class)
				.in(Scopes.SINGLETON);
		bind(Transaction.class).to(LocalTransaction.class);
	}
}
