/*
 * 作成日 : 2011/04/19
 */
package jp.osd.doce.internal.provider;

import static jp.osd.doce.JndiProperties.JNDI_USER_TRANSACTION;

import javax.naming.Context;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import jp.osd.doce.Doma;
import jp.osd.doce.Transaction;
import jp.osd.doce.TransactionBinding;
import jp.osd.doce.internal.DbNamedProperties;
import jp.osd.doce.internal.logging.Logger;
import jp.osd.doce.internal.logging.LoggerFactory;
import jp.osd.doce.internal.logging.MessageCodes;
import jp.osd.doce.internal.tx.JtaUserTransaction;
import jp.osd.doce.internal.tx.LocalTransaction;

import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.name.Names;

/**
 * {@link Transaction} の実装クラスオブジェクトを提供する Guice プロバイダです。
 * <P>
 * このプロバイダは、コンストラクタ引数 {@code transactionBinding} の値によって提供するオブジェクトが変わります。
 * 
 * <H4>{@link TransactionBinding#AUTO} の場合</H4>
 * 
 * 提供される実装クラスオブジェクトは以下のように決定されます。
 * <P>
 * <OL>
 * <LI>コンストラクタの引数 {@code dataSource} に設定されたデータソースが
 * {@link LocalTransactionalDataSource} 型ならば {@link LocalTransaction} オブジェクトを
 * Guice インジェクタから取得して提供します。
 * <LI>それ以外のとき {@link JtaUserTransaction} オブジェクトを生成して提供します。このとき、ひもづく
 * {@link UserTransaction} の取得に {@link UserTransactionProvider} が使用されます。
 * </OL>
 * 
 * <H4>{@link TransactionBinding#LOCAL_TRANSACTION} の場合</H4>
 * 
 * {@link LocalTransaction} オブジェクトを Guice インジェクタから取得して提供します。
 * 
 * <H4>{@link TransactionBinding#JTA_USER_TRANSACTION} の場合</H4>
 * 
 * {@link JtaUserTransaction} オブジェクトを生成して提供します。このとき、ひもづく {@link UserTransaction}
 * の取得に {@link UserTransactionProvider} が使用されます。
 * 
 * <H4>上記以外の場合</H4>
 * 
 * <code>null</code> を返します（Guice としてはエラーになります）。
 * 
 * @author asuka
 * @see UserTransactionProvider
 */
public class AutoTransactionProvider implements Provider<Transaction> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AutoTransactionProvider.class);
	private final DbNamedProperties properties;
	@Inject
	private Injector injector;

	/**
	 * 新たにオブジェクトを構築します。
	 * 
	 * @param properties
	 *            データベース名付き設定プロパティ
	 */
	public AutoTransactionProvider(DbNamedProperties properties) {
		LOGGER.logConstructor(DbNamedProperties.class);
		this.properties = properties;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Transaction get() {
		TransactionBinding transactionBinding = properties
				.getTransactionBinding();
		LOGGER.debug(MessageCodes.DG002, "TransactionBinding",
				transactionBinding);
		DataSource dataSource = getInstance(DataSource.class);
		Transaction transaction;
		switch (transactionBinding) {
		case AUTO:
			if (dataSource instanceof LocalTransactionalDataSource) {
				LOGGER.info(MessageCodes.DG005, properties.getDbName());
				transaction = getLocalTransaction(dataSource);
			} else {
				transaction = getJtaUserTransaction(dataSource);
			}
			break;
		case LOCAL_TRANSACTION:
			transaction = getLocalTransaction(dataSource);
			break;
		case JTA_USER_TRANSACTION:
			transaction = getJtaUserTransaction(dataSource);
			break;
		default:
			transaction = null;
			break;
		}
		return transaction;
	}

	private <T> T getInstance(Class<T> type) {
		if (injector == null) {
			return null;
		}
		String dbName = properties.getDbName();
		if (dbName == null) {
			return injector.getInstance(Key.get(type, Doma.class));
		}
		return injector.getInstance(Key.get(type, Names.named(dbName)));
	}

	private Transaction getLocalTransaction(DataSource dataSource) {
		JdbcLogger jdbcLogger = getInstance(JdbcLogger.class);
		return new LocalTransaction(properties.getDbName(), dataSource,
				jdbcLogger);
	}

	private Transaction getJtaUserTransaction(DataSource dataSource) {
		Context context = getInstance(Context.class);
		UserTransactionProvider provider = new UserTransactionProvider(
				properties.getDbName(), context);
		if (properties.containsKey(JNDI_USER_TRANSACTION)) {
			provider.setDefaultJndiTransactionName(properties
					.getString(JNDI_USER_TRANSACTION));
		}
		// TODO 検討
		//return new JtaUserTransaction(properties.getDbName(), provider.get());
		return getLocalTransaction(dataSource);
	}
}
