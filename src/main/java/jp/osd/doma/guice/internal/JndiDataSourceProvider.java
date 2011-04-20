/*
 * 作成日 : 2011/04/18
 */
package jp.osd.doma.guice.internal;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jp.osd.doma.guice.Doma;
import jp.osd.doma.guice.TransactionBinding;

import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

/**
 * {@link DataSource} の実装クラスオブジェクトとして JNDI から取得したデータソースを提供する Guice プロバイダです。
 * <P>
 * 以下の条件に応じて {@link LocalTransactionalDataSource} でラップします。
 * <OL>
 * <LI>コンストラクタの引数 {@code transactionBinding} に {@link TransactionBinding#LOCAL_TRANSACTION}
 * が設定されたとき。
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
	private static final String JNDI_TRANSACTION_NAME = "java:comp/UserTransaction";

	private final TransactionBinding transactionBinding;

	private final String jndiDataSourceName;

	private final Context context;

	private String jndiTransaction;

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
			ds = (DataSource) context.lookup(jndiDataSourceName);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}

		// ローカルトランザクションを使用するならば LocalTransactionalDataSource
		// でラップ
		if (useLocalTransaction()) {
			return new LocalTransactionalDataSource(ds);
		}
		return ds;
	}

	private boolean useLocalTransaction() {
		boolean result;
		switch (transactionBinding) {
		case AUTO:
			if (jndiTransaction != null) {
				// トランザクション取得用の JNDI 名があればローカルトランザクション
				// を使うということはない
				result = false;
			} else {
				// ためしに JNDI でトランザクションを取得してみる
				try {
					context.lookup(JNDI_TRANSACTION_NAME);
					// 取得できるのならばローカルトランザクションは使わない
					result = false;
				} catch (NamingException e) {
					// 例外がでるようならローカルトランザクションを使うしかない
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
	 * このメソッドで値を設定すると、その名前で実際にトランザクションオブジェクトを取得できるか否かに関わらず、{@link #get()} で返されるデータソースは
	 * {@link LocalTransactionalDataSource} でラップされません。
	 *
	 * @param jndiTransaction
	 *            トランザクションのオブジェクト名
	 */
	@Inject(optional = true)
	public void setJndiTransactionName(
			@Named("JNDI.transaction") String jndiTransaction) {
		this.jndiTransaction = jndiTransaction;
	}

}
