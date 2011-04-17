/*
 * 作成日 : 2011/04/17
 */
package jp.osd.doma.guice.internal;

import javax.inject.Provider;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

/**
 * 名前の付けずにバインドしたデータソースを名前付きデータソースとして提供するための Guice プロバイダです。
 *
 * @author asuka
 */
public class AutoDataSourceProvider implements Provider<DataSource> {
	private DataSource dataSource;

	private Injector injector;

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param injector
	 *            インジェクタ
	 */
	@Inject
	public AutoDataSourceProvider(Injector injector) {
		this.injector = injector;
	}

	@Override
	public DataSource get() {
		if (dataSource == null) {
			// JNDI からのデータソースの作成を試みる
			if (!createJndiDataSource()) {
				// BasicDataSource の作成を試みる
				if (!createBasicDataSource()) {
					createSimpleDataSource();
				}
			}
		}
		return dataSource;
	}

	private boolean createJndiDataSource() {
		Key<String> key = Key.get(String.class, Names.named("JNDI.dataSource"));
		if (injector.getAllBindings().containsKey(key)) {
			String jndiDataSource = injector.getInstance(key);
			Context context = injector.getInstance(Context.class);

			try {
				dataSource = (DataSource) context.lookup(jndiDataSource);
			} catch (NamingException e) {
				throw new RuntimeException(e);
			}

			return true;
		}
		return false;
	}

	private boolean createBasicDataSource() {
		if (ClassUtils.isLoadable("org.apache.commons.dbcp.BasicDataSource")) {
			BasicDataSourceFactory factory = injector
					.getInstance(BasicDataSourceFactory.class);
			dataSource = factory.create();
			return true;
		}
		return false;
	}

	private boolean createSimpleDataSource() {
		SimpleDataSourceFactory factory = injector
				.getInstance(SimpleDataSourceFactory.class);
		dataSource = factory.create();
		return true;
	}
}
