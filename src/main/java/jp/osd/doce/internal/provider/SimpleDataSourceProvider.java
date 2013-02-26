/*
 * 作成日 : 2011/04/18
 */
package jp.osd.doce.internal.provider;

import static jp.osd.doce.JdbcProperties.JDBC_LOGIN_TIMEOUT;
import static jp.osd.doce.JdbcProperties.JDBC_PASSWORD;
import static jp.osd.doce.JdbcProperties.JDBC_URL;
import static jp.osd.doce.JdbcProperties.JDBC_USERNAME;

import javax.sql.DataSource;

import jp.osd.doce.TransactionBinding;
import jp.osd.doce.internal.DbNamedProperties;
import jp.osd.doce.internal.logging.Logger;
import jp.osd.doce.internal.logging.LoggerFactory;
import jp.osd.doce.internal.logging.MessageCodes;

import org.seasar.doma.jdbc.SimpleDataSource;
import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

import com.google.inject.Provider;

/**
 * @author asuka
 */
public class SimpleDataSourceProvider implements Provider<DataSource> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SimpleDataSourceProvider.class);
	private final String dbName;
	private final SimpleDataSource dataSource = new SimpleDataSource();
	private final TransactionBinding transactionBinding;

	/**
	 * 新たにオブジェクトを構築します。
	 * 
	 * @param properties
	 *            データベース名付き設定プロパティ
	 */
	public SimpleDataSourceProvider(DbNamedProperties properties) {
		LOGGER.logConstructor(DbNamedProperties.class);
		dbName = properties.getDbName();
		String url = properties.getString(JDBC_URL);
		String username = properties.getString(JDBC_USERNAME);
		LOGGER.debug(MessageCodes.DG002, JDBC_URL, url);
		LOGGER.debug(MessageCodes.DG002, JDBC_USERNAME, username);
		dataSource.setUrl(url);
		dataSource.setUser(username);
		this.transactionBinding = properties.getTransactionBinding();
		
		// パスワード
		if (properties.containsKey(JDBC_PASSWORD)) {
			String password = properties.getString(JDBC_PASSWORD);
			LOGGER.debug(MessageCodes.DG002, JDBC_PASSWORD, password);
			dataSource.setPassword(password);
		}
		
		// ログインタイムアウト値
		if (properties.containsKey(JDBC_LOGIN_TIMEOUT)) {
			int loginTimeout = properties.getInt(JDBC_LOGIN_TIMEOUT, 0);
			LOGGER.debug(MessageCodes.DG002, JDBC_LOGIN_TIMEOUT,
					loginTimeout);
			dataSource.setLoginTimeout(loginTimeout);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource get() {
		LOGGER.info(MessageCodes.DG010, dbName, SimpleDataSource.class);

		// トランザクション実装を使用しない場合はそのまま返す
		if (transactionBinding == TransactionBinding.NONE) {
			return dataSource;
		}
		return new LocalTransactionalDataSource(dataSource);
	}

}
