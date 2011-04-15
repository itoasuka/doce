/*
 * 作成日 : 2011/04/15
 */
package jp.osd.doma.guice.internal;

import javax.sql.DataSource;

import jp.osd.doma.guice.DomaDataSourceName;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.name.Names;

/**
 * Doma で用いるデータソースを提供する Guice プロバイダです。
 * <P>
 * データソースの実装は複数あるため、このプロバイダが中間に入って Doma
 * にインジェクションを行います。デフォルトでは、名前の付いていないデータソースインスタンスの注入を行いますが、
 * {@link #setName(Key)} でキーを指定することもできます。
 *
 * @author asuka
 */
public class DomaDataSourceProvider implements Provider<DataSource> {

	private final Injector injector;

	private String name = null;

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param injector Guice インジェクタ
	 */
	@Inject
	public DomaDataSourceProvider(Injector injector) {
		this.injector = injector;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource get() {
		if (name == null) {
			return injector.getInstance(DataSource.class);
		}
		Key<DataSource> key = Key.get(DataSource.class, Names.named(name));
		return injector.getInstance(key);
	}

	/**
	 * Doma に注入するデータソースインスタンスをインジェクタから取得するための名前を設定します。
	 *
	 * @param name 名前
	 */
	@Inject(optional = true)
	public void setName(@DomaDataSourceName String name) {
		this.name = name;
	}
}
