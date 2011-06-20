/*
 * 作成日 : 2011/04/19
 */
package jp.osd.doce.internal.provider;

import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import jp.osd.doce.Doma;
import jp.osd.doce.Transaction;
import jp.osd.doce.TransactionBinding;
import jp.osd.doce.internal.logging.Logger;
import jp.osd.doce.internal.logging.LoggerFactory;
import jp.osd.doce.internal.logging.MessageCodes;
import jp.osd.doce.internal.tx.JtaUserTransaction;
import jp.osd.doce.internal.tx.LocalTransaction;

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
    private final TransactionBinding transactionBinding;
    private final Injector injector;
    private final DataSource dataSource;

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
        LOGGER.logConstructor(TransactionBinding.class, Injector.class,
                DataSource.class);
        this.transactionBinding = transactionBinding;
        this.injector = injector;
        this.dataSource = dataSource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction get() {
        LOGGER.debug(MessageCodes.DG002, "TransactionBinding",
                transactionBinding);
        Transaction transaction;
        switch (transactionBinding) {
        case AUTO:
            if (dataSource instanceof LocalTransactionalDataSource) {
                LOGGER.info(MessageCodes.DG005);
                transaction = injector.getInstance(LocalTransaction.class);
            } else {
                UserTransaction tx = injector.getInstance(
                        UserTransactionProvider.class).get();
                transaction = new JtaUserTransaction(tx);
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
            transaction = null;
            break;
        }
        return transaction;
    }
}
