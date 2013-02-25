/*
 * 作成日 : 2011/04/18
 */
package jp.osd.doce.internal.provider;

import static jp.osd.doce.JndiProperties.JNDI_USER_TRANSACTION;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jp.osd.doce.DoceException;
import jp.osd.doce.Doma;
import jp.osd.doce.TransactionBinding;
import jp.osd.doce.internal.DbNamedProperties;
import jp.osd.doce.internal.logging.Logger;
import jp.osd.doce.internal.logging.LoggerFactory;
import jp.osd.doce.internal.logging.MessageCodes;

import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.name.Names;

/**
 * {@link DataSource} の実装クラスオブジェクトとして JNDI から取得したデータソースを提供する Guice プロバイダです。
 * <P>
 * 以下の条件に応じて {@link LocalTransactionalDataSource} でラップします。
 * <OL>
 * <LI>コンストラクタの引数 {@code transactionBinding} に
 * {@link TransactionBinding#LOCAL_TRANSACTION} が設定されたとき。
 * <LI>コンストラクタの引数 {@code transactionBinding} に {@link TransactionBinding#AUTO}
 * が設定されたときで以下のすべての条件に当てはまるとき。
 * <UL>
 * <LI>設定プロパティの設定値 <code>JNDI.userTransaction</code> で JNDI
 * でルックアップする際に使用するトランザクションのオブジェクト名が指定されていない。
 * <LI>{@code "java:comp/UserTransaction"} というオブジェクト名で JNDI ルックアップが失敗する。
 * </UL>
 * </OL>
 * 
 * @author asuka
 */
public class JndiDataSourceProvider implements Provider<DataSource> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JndiDataSourceProvider.class);

	private final String dbName;

	private final TransactionBinding transactionBinding;

	private final String jndiDataSourceName;

	private Context context;

	private String jndiTransactionName;

	@Inject
	private Injector injector;

	/**
	 * 新たにオブジェクトを構築します。
	 * 
	 * @param properties
	 *            データベース名付き設定プロパティ
	 */
	public JndiDataSourceProvider(DbNamedProperties properties) {
		LOGGER.logConstructor(DbNamedProperties.class);
		dbName = properties.getDbName();
		jndiDataSourceName = properties.getString("JNDI.dataSource");
		transactionBinding = properties.getTransactionBinding();

		if (properties.containsKey(JNDI_USER_TRANSACTION)) {
			jndiTransactionName = properties.getString(JNDI_USER_TRANSACTION);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource get() {
		// まずは JNDI でデータソースを取得
		DataSource ds;
		try {
			LOGGER.debug(MessageCodes.DG006, dbName, jndiDataSourceName);
			ds = (DataSource) getContext().lookup(jndiDataSourceName);
			LOGGER.debug(MessageCodes.DG008, dbName, ds.getClass().getName());
		} catch (NamingException e) {
			LOGGER.error(e, MessageCodes.DG007, dbName, jndiDataSourceName);
			throw new DoceException("Lookup error : " + jndiDataSourceName, e);
		}

		// ローカルトランザクションを使用するならば LocalTransactionalDataSource
		// でラップ
		if (useLocalTransaction()) {
			LOGGER.debug(MessageCodes.DG009, dbName);
			return new LocalTransactionalDataSource(ds);
		}
		return ds;
	}

	private boolean useLocalTransaction() {
		boolean result;
		LOGGER.debug(MessageCodes.DG002, "TransactionBinding",
				transactionBinding);
		switch (transactionBinding) {
		case AUTO:
			if (jndiTransactionName != null) {
				// トランザクション取得用の JNDI 名があればローカルトランザクション
				// を使うということはない
				result = false;
			} else {
				result = true;
			}
			break;
		case LOCAL_TRANSACTION:
			result = true;
			break;
		case JTA_USER_TRANSACTION:
			result = false;
			break;
		default:
			result = false;
			break;
		}
		return result;
	}

	private <T> T getInstance(Class<T> type) {
		if (dbName == null) {
			return injector.getInstance(Key.get(type, Doma.class));
		}
		return injector.getInstance(Key.get(type, Names.named(dbName)));
	}

	private Context getContext() {
		if (context == null) {
			context = getInstance(Context.class);
		}

		return context;
	}
}
