/*
 * 作成日 : 2011/04/18
 */
package jp.osd.doce.internal.provider;

import static jp.osd.doce.JndiProperties.JNDI_TRANSACTION;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jp.osd.doce.Doma;
import jp.osd.doce.DomaGuiceException;
import jp.osd.doce.TransactionBinding;
import jp.osd.doce.internal.logging.Logger;
import jp.osd.doce.internal.logging.LoggerFactory;
import jp.osd.doce.internal.logging.MessageCodes;

import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

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
 * <LI>{@link #setJndiTransactionName(String)} で JNDI
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

	private final TransactionBinding transactionBinding;

	private final String jndiDataSourceName;

	private final Context context;

	private String jndiTransactionName;

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param context
	 *            JNDI ネーミングコンテキスト
	 * @param jndiDataSourceName
	 *            JNDI でルックアップする際に使用するデータソースのオブジェクト名
	 * @param transactionBinding
	 *            トランザクションのバインド方法
	 */
	@Inject
	public JndiDataSourceProvider(@Doma Context context,
			@Named("JNDI.dataSource") String jndiDataSourceName,
			@Doma TransactionBinding transactionBinding) {
		LOGGER.logConstructor(Context.class, String.class,
				TransactionBinding.class);
		this.context = context;
		this.jndiDataSourceName = jndiDataSourceName;
		this.transactionBinding = transactionBinding;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource get() {
		// まずは JNDI でデータソースを取得
		DataSource ds;
		try {
			LOGGER.debug(MessageCodes.DG006, jndiDataSourceName);
			ds = (DataSource) context.lookup(jndiDataSourceName);
			LOGGER.debug(MessageCodes.DG008);
		} catch (NamingException e) {
			LOGGER.error(e, MessageCodes.DG007, jndiDataSourceName);
			throw new DomaGuiceException(
					"Lookup error : " + jndiDataSourceName, e);
		}

		// ローカルトランザクションを使用するならば LocalTransactionalDataSource
		// でラップ
		if (useLocalTransaction()) {
			LOGGER.debug(MessageCodes.DG009);
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
				// ためしに JNDI でトランザクションを取得してみる
				try {
					LOGGER.debug(MessageCodes.DG006, UserTransactionProvider.DEFAULT_JNDI_TRANSACTION_NAME);
					context.lookup(UserTransactionProvider.DEFAULT_JNDI_TRANSACTION_NAME);
					// 取得できるのならばローカルトランザクションは使わない
					LOGGER.debug(MessageCodes.DG008);
					result = false;
				} catch (NamingException e) {
					// 例外がでるようならローカルトランザクションを使うしかない
					LOGGER.debug(MessageCodes.DG007);
					result = true;
				}
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

	/**
	 * JNDI でトランザクション（{@link javax.transaction.UserTransaction}
	 * ）をルックアップする際に使用するオブジェクト名を設定します。
	 * <P>
	 * このメソッドで値を設定すると、その名前で実際にトランザクションオブジェクトを取得できるか否かに関わらず、{@link #get()}
	 * で返されるデータソースは {@link LocalTransactionalDataSource} でラップされません。
	 *
	 * @param jndiTransactionName
	 *            トランザクションのオブジェクト名
	 */
	@Inject(optional = true)
	public void setJndiTransactionName(
			@Named(JNDI_TRANSACTION) String jndiTransactionName) {
		LOGGER.debug(MessageCodes.DG002, JNDI_TRANSACTION,
				jndiTransactionName);
		this.jndiTransactionName = jndiTransactionName;
	}

}
