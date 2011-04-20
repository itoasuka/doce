/*
 * 作成日 : 2011/04/18
 */
package jp.osd.doma.guice;

/**
 * トランザクション（{@link Transaction}）のバインディングをどのように行うのかを指定するための列挙体です。
 * <P>
 * これは、主に {@link DomaModule} が内部行っているトランザクションのバインディング方法について指定するのに用います。
 *
 * @author asuka
 * @see DomaModule.Builder#setTransactionBinding(TransactionBinding)
 */
public enum TransactionBinding {
	/**
	 *　バインドする実装クラスオブジェクトを自動で判定するように指定します。どのように判定されるかは {@link jp.osd.doma.guice.internal.AutoTransactionProvider} を参照してください。
	 * @see jp.osd.doma.guice.internal.AutoTransactionProvider
	 */
	AUTO,
	/**
	 * {@link jp.osd.doma.guice.internal.LocalTransaction} オブジェクトを実装クラスオブジェクトとしてバインディングするように指定します。
	 *
	 * @see jp.osd.doma.guice.internal.LocalTransaction
	 */
	LOCAL_TRANSACTION,
	/**
	 * {@link jp.osd.doma.guice.internal.JtaUserTransaction} オブジェクトを実装クラスオブジェクトとしてバインディングするように指定します。
	 *
	 * @see jp.osd.doma.guice.internal.JtaUserTransaction
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
