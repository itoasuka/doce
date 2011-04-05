/**
 * 
 */
package jp.osd.doma.guice.internal;

import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.Context;
import javax.sql.DataSource;

import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

/**
 * JNDI を用いてデータソースを取得し {@link LocalTransactionalDataSource} でラップしたものを提供する Guice
 * プロバイダです。
 * 
 * @author asuka
 */
public class JndiLocalTransactionDataSourceProvider extends
		JndiDataSourceProvider {
	/**
	 * 新たにオブジェクトを構築します。
	 * 
	 * @param context
	 *            データソースを JNDI でルックアップする際に使用するネーミングコンテキスト
	 * @param dataSourceName
	 *            データソースを JNDI でルックアップする際に使用するデータソース名
	 */
	@Inject
	public JndiLocalTransactionDataSourceProvider(Context context,
			@Named("jndi.dataSource") String dataSourceName) {
		super(context, dataSourceName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource get() {
		return new LocalTransactionalDataSource(super.get());
	}

}
