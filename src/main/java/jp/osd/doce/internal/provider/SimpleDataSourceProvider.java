/*
 * 作成日 : 2011/04/18
 */
package jp.osd.doce.internal.provider;

import static jp.osd.doce.JdbcProperties.JDBC_LOGIN_TIMEOUT;
import static jp.osd.doce.JdbcProperties.JDBC_PASSWORD;
import static jp.osd.doce.JdbcProperties.JDBC_URL;
import static jp.osd.doce.JdbcProperties.JDBC_USERNAME;

import javax.sql.DataSource;

import jp.osd.doce.Doma;
import jp.osd.doce.TransactionBinding;
import jp.osd.doce.internal.logging.Logger;
import jp.osd.doce.internal.logging.LoggerFactory;
import jp.osd.doce.internal.logging.MessageCodes;

import org.seasar.doma.jdbc.SimpleDataSource;
import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

/**
 * @author asuka
 */
public class SimpleDataSourceProvider implements Provider<DataSource> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SimpleDataSourceProvider.class);

	private final SimpleDataSource dataSource = new SimpleDataSource();
	private final TransactionBinding transactionBinding;

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param url
	 *            接続先のデータベースの URL
	 * @param username
	 *            データベースへログインするためのユーザ名
	 * @see SimpleDataSource#setUrl(String)
	 * @see SimpleDataSource#setUser(String)
	 */
	@Inject
	public SimpleDataSourceProvider(@Named(JDBC_URL) final String url,
			@Named(JDBC_USERNAME) final String username,
			@Doma TransactionBinding transactionBinding) {
		LOGGER.logConstructor(String.class, String.class, String.class);
		LOGGER.debug(MessageCodes.DG002, JDBC_URL, url);
		LOGGER.debug(MessageCodes.DG002, JDBC_USERNAME, username);
		dataSource.setUrl(url);
		dataSource.setUser(username);
		this.transactionBinding = transactionBinding;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource get() {
		LOGGER.info(MessageCodes.DG010, SimpleDataSource.class);

		// トランザクション実装を使用しない場合はそのまま返す
		if (transactionBinding == TransactionBinding.NONE) {
			return dataSource;
		}
		return new LocalTransactionalDataSource(dataSource);
	}

	/**
	 * データベースへログインするためのパスワードを設定します。
	 *
	 * @param password
	 *            データベースへログインするためのパスワード
	 * @see SimpleDataSource#setPassword(String)
	 */
	@Inject(optional = true)
	public void setPassword(@Named(JDBC_PASSWORD) String password) {
		LOGGER.debug(MessageCodes.DG002, JDBC_PASSWORD, password);
		dataSource.setPassword(password);
	}

	/**
	 * データベースへのログインのタイムアウト時間を設定します。
	 *
	 * @param loginTimeout
	 *            データベースへのログインのタイムアウト時間
	 * @see SimpleDataSource#setLoginTimeout(int)
	 */
	@Inject(optional = true)
	public void setLoginTimeout(
			@Named(JDBC_LOGIN_TIMEOUT) final int loginTimeout) {
		LOGGER.debug(MessageCodes.DG002, JDBC_LOGIN_TIMEOUT, loginTimeout);
		dataSource.setLoginTimeout(loginTimeout);
	}

}
