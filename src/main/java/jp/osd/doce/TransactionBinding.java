/*
 * 作成日 : 2011/04/18
 */
package jp.osd.doce;

/**
 * トランザクション（{@link Transaction}）のバインディングをどのように行うのかを指定するための列挙体です。
 * <P>
 * これは、主に {@link DoceModule} が内部行っているトランザクションのバインディング方法について指定するのに用います。
 *
 * @author asuka
 * @see DoceModule.Builder#setTransactionBinding(TransactionBinding)
 */
public enum TransactionBinding {
    /**
     *　バインドする実装クラスオブジェクトを自動で判定するように指定します。どのように判定されるかは {@link jp.osd.doce.internal.provider.AutoTransactionProvider} を参照してください。
     * @see jp.osd.doce.internal.provider.AutoTransactionProvider
     */
    AUTO,
    /**
     * {@link jp.osd.doce.internal.tx.LocalTransaction} オブジェクトを実装クラスオブジェクトとしてバインディングするように指定します。
     *
     * @see jp.osd.doce.internal.tx.LocalTransaction
     */
    LOCAL_TRANSACTION,
    /**
     * {@link jp.osd.doce.internal.tx.JtaUserTransaction} オブジェクトを実装クラスオブジェクトとしてバインディングするように指定します。
     *
     * @see jp.osd.doce.internal.tx.JtaUserTransaction
     */
    JTA_USER_TRANSACTION,
    /**
     * 別途名前をつけずにバインド定義しているトランザクションをバインディングするように指定します。
     */
    EXTERNAL,
    /**
     * バインディングに関して何も設定を行わないように指定します。別途 {@link Doma} アノテーションを付加してトランザクションのバインドの定義する場合に用います。
     */
    NONE;
}
