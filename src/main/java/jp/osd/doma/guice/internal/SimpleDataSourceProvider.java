package jp.osd.doma.guice.internal;

import javax.inject.Named;
import javax.inject.Provider;
import javax.sql.DataSource;

import org.seasar.doma.jdbc.SimpleDataSource;
import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

import com.google.inject.Inject;

/**
 * {@link DataSource} として {@link SimpleDataSource} を提供するプロバイダです。厳密には Doma が提供するローカルトランザクションを利用するため {@link LocalTransactionalDataSource} でラップされています。
 * <P>
 *  {@link SimpleDataSource} に対するパラメータは、Guice の定数バインドで指定します。
 *  <P>
 *  <TABLE BORDER=1>
 *  <THEAD><TR><TH>Named</TH><TH>必須</TH><TH>説明</TH></THEAD>
 *  <TBODY>
 *  <TR><TD>JDBC.url</TD><TD align="center">○</TD><TD>接続先のデータベースの URL。{@link SimpleDataSource#setUrl(String)} に使用。</TD></TR>
 *  <TR><TD>JDBC.username</TD><TD align="center">○</TD><TD>データベースへログインするためのユーザ名。{@link SimpleDataSource#setUser(String)} に使用。</TD></TR>
 *  <TR><TD>JDBC.password</TD><TD align="center">○</TD><TD>データベースへログインするためのパスワード。{@link SimpleDataSource#setPassword(String)} に使用。</TD></TR>
 *  <TR><TD>JDBC.loginTimeout</TD><TD><BR></TD><TD>データベースへのログインのタイムアウト時間。{@link SimpleDataSource#setLoginTimeout(int)} に使用。</TD></TR>
 *  </TBODY>
 *  </TABLE>
 *
 * @author asuka
 */
public class SimpleDataSourceProvider implements
		Provider<DataSource> {
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
	public LocalTransactionalDataSource get() {
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
