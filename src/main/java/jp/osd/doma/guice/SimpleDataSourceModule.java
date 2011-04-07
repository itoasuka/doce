package jp.osd.doma.guice;

import javax.sql.DataSource;

import org.seasar.doma.jdbc.SimpleDataSource;

import jp.osd.doma.guice.internal.LocalTransaction;
import jp.osd.doma.guice.internal.SimpleDataSourceProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * データソースとして {@link SimpleDataSource} をベースとした実装を提供する Guice
 * モジュールです。
 * <P>
 * 提供されるデータソースは、ローカルトランザクションと連動させるため
 * {@link org.seasar.doma.jdbc.tx.LocalTransactionalDataSource} でラップしています。
 * <P>
 * {@link SimpleDataSource} に対するパラメータは、Guice の定数バインドで指定します。
 * <P>
 * <TABLE BORDER=1>
 * <THEAD>
 * <TR>
 * <TH>Named</TH>
 * <TH>必須</TH>
 * <TH>説明</TH></THEAD> <TBODY>
 * <TR>
 * <TD>JDBC.url</TD>
 * <TD align="center">○</TD>
 * <TD>接続先のデータベースの URL。{@link SimpleDataSource#setUrl(String)} に使用。</TD>
 * </TR>
 * <TR>
 * <TD>JDBC.username</TD>
 * <TD align="center">○</TD>
 * <TD>データベースへログインするためのユーザ名。{@link SimpleDataSource#setUser(String)} に使用。</TD>
 * </TR>
 * <TR>
 * <TD>JDBC.password</TD>
 * <TD align="center">○</TD>
 * <TD>データベースへログインするためのパスワード。{@link SimpleDataSource#setPassword(String)} に使用。</TD>
 * </TR>
 * <TR>
 * <TD>JDBC.loginTimeout</TD>
 * <TD><BR>
 * </TD>
 * <TD>データベースへのログインのタイムアウト時間。{@link SimpleDataSource#setLoginTimeout(int)} に使用。</TD>
 * </TR>
 * </TBODY>
 * </TABLE>
 * 
 * @author asuka
 * @see SimpleDataSourceProvider
 */
public class SimpleDataSourceModule extends AbstractModule {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configure() {
		bind(DataSource.class).toProvider(SimpleDataSourceProvider.class).in(
				Scopes.SINGLETON);
		bind(Transaction.class).to(LocalTransaction.class);
	}

}
