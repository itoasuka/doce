/*
 * 作成日 : 2011/04/18
 */
package jp.osd.doma.guice.internal;

import javax.inject.Named;
import javax.sql.DataSource;

import org.seasar.doma.jdbc.SimpleDataSource;
import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author asuka
 */
public class SimpleDataSourceProvider implements Provider<DataSource> {
	private final SimpleDataSource dataSource = new SimpleDataSource();

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param url 接続先のデータベースの URL
	 * @param username データベースへログインするためのユーザ名
	 * @param password データベースへログインするためのパスワード
	 * @see SimpleDataSource#setUrl(String)
	 * @see SimpleDataSource#setUser(String)
	 * @see SimpleDataSource#setPassword(String)
	 */
	@Inject
	public SimpleDataSourceProvider(@Named("JDBC.url") final String url,
			@Named("JDBC.username") final String username,
			@Named("JDBC.password") final String password) {
		dataSource.setUrl(url);
		dataSource.setUser(username);
		dataSource.setPassword(password);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource get() {
		return new LocalTransactionalDataSource(dataSource);
	}

	/**
	 * データベースへのログインのタイムアウト時間を設定します。
	 *
	 * @param loginTimeout データベースへのログインのタイムアウト時間
	 * @see SimpleDataSource#setLoginTimeout(int)
	 */
	@Inject(optional = true)
	public void setLoginTimeout(
			@Named("JDBC.loginTimeout") final int loginTimeout) {
		dataSource.setLoginTimeout(loginTimeout);
	}

}
