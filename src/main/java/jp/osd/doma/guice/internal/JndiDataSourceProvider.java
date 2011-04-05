package jp.osd.doma.guice.internal;

import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jp.osd.doma.guice.DomaGuiceException;

import com.google.inject.Provider;

/**
 * JNDI を用いてデータソースを取得して提供する Guice プロバイダです。
 * 
 * @author asuka
 */
public class JndiDataSourceProvider implements Provider<DataSource> {
	private final Context context;

	private final String dataSourceName;

	/**
	 * 新たにオブジェクトを構築します。
	 * 
	 * @param context
	 *            データソースを JNDI でルックアップする際に使用するネーミングコンテキスト
	 * @param dataSourceName
	 *            データソースを JNDI でルックアップする際に使用するデータソース名
	 */
	@Inject
	public JndiDataSourceProvider(Context context,
			@Named("jndi.dataSource") String dataSourceName) {
		this.context = context;
		this.dataSourceName = dataSourceName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource get() {
		try {
			return (DataSource) context.lookup(dataSourceName);
		} catch (NamingException e) {
			throw new DomaGuiceException("Lookup error : " + dataSourceName, e);
		}
	}

}
