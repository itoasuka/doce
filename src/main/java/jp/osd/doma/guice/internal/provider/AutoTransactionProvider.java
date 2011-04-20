/*
 * 作成日 : 2011/04/19
 */
package jp.osd.doma.guice.internal.provider;

import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import jp.osd.doma.guice.Doma;
import jp.osd.doma.guice.Transaction;
import jp.osd.doma.guice.TransactionBinding;
import jp.osd.doma.guice.internal.tx.JtaUserTransaction;
import jp.osd.doma.guice.internal.tx.LocalTransaction;

import org.seasar.doma.jdbc.tx.LocalTransactionalDataSource;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

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
 * <LI>{@link #setTransaction(Transaction)} で設定されたトランザクションがあればそれを提供します。
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
 * {@link JtaUserTransaction} オブジェクトを生成して提供します。このとき、ひもづく
 * {@link UserTransaction} の取得に {@link UserTransactionProvider} が使用されます。
 *
 * <H4>上記以外の場合</H4>
 *
 * {@link #setTransaction(Transaction)} で設定されたトランザクションを提供します。
 *
 * @author asuka
 * @see UserTransactionProvider
 */
public class AutoTransactionProvider implements Provider<Transaction> {
	private final TransactionBinding transactionBinding;
	private final Injector injector;
	private final DataSource dataSource;
	private Transaction transaction;

	/**
	 * 新たにオブジェクトを構築します。
	 *
	 * @param transactionBinding
	 *            トランザクションのバインド方法
	 * @param injector
	 *            Guice インジェクタ
	 * @param dataSource
	 *            このプロバイダが提供するトランザクションにひもづくデータソース
	 */
	@Inject
	public AutoTransactionProvider(@Doma TransactionBinding transactionBinding,
			Injector injector, @Doma DataSource dataSource) {
		this.transactionBinding = transactionBinding;
		this.injector = injector;
		this.dataSource = dataSource;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Transaction get() {
		switch (transactionBinding) {
		case AUTO:
			if (transaction == null) {
				if (dataSource instanceof LocalTransactionalDataSource) {
					transaction = injector.getInstance(LocalTransaction.class);
				} else {
					UserTransaction tx = injector.getInstance(
							UserTransactionProvider.class).get();
					transaction = new JtaUserTransaction(tx);
				}
			}
			break;
		case LOCAL_TRANSACTION:
			transaction = injector.getInstance(LocalTransaction.class);
			break;
		case JTA_USER_TRANSACTION:
			UserTransaction tx = injector.getInstance(
					UserTransactionProvider.class).get();
			transaction = new JtaUserTransaction(tx);
			break;
		default:
			break;
		}
		return transaction;
	}

	/**
	 * {@link #get()} で返すトランザクションを設定します。このメソッドで設定した値は、すべに優先して {@link #get()}
	 * の戻り値として採用されます。
	 *
	 * @param transaction
	 *            トランザクション
	 */
	@Inject(optional = true)
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
}
